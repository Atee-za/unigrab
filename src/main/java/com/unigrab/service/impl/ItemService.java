package com.unigrab.service.impl;

import com.unigrab.model.constant.AvailabilityStatus;
import com.unigrab.model.dto.ItemDto;
import com.unigrab.model.dto.NewItemDto;
import com.unigrab.model.dto.SearchDto;
import com.unigrab.model.dto.ToPage;
import com.unigrab.model.entity.*;
import com.unigrab.repository.ItemRepository;
import com.unigrab.repository.UserRepository;
import com.unigrab.security.JwtService;
import com.unigrab.service.ICampusService;
import com.unigrab.service.IImageService;
import com.unigrab.service.IItemService;
import com.unigrab.service.ITownService;
import com.unigrab.service.s3.S3ClientService;
import com.unigrab.util.Converter;
import com.unigrab.util.SearchCriteria;
import com.unigrab.util.Validate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.unigrab.util.Common.*;
import static com.unigrab.util.HandleExceptions.*;

@Slf4j
@Service
@Transactional()
@RequiredArgsConstructor
public class ItemService implements IItemService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final IImageService imageService;
    private final S3ClientService s3ClientService;
    private final ITownService townService;
    private final ICampusService campusService;
    private final ItemRepository itemRepository;
    private final SearchCriteria searchCriteria;

    @Override
    public Item save(Item item) {
        log.info("Saving item with id: {}", item.getItemId());
        return itemRepository.save(item);
        //0 = available,
        //1 = sold,
        //-1 = deleted,
        //2 = pending <- still busy with upload <- rarely occurs when busy uploading images but itemId needed by images.
    }

    @Override
    public Item find(String id) {
        log.info("Finding item by id: {}", id);
        return itemRepository.findByItemId(id).orElse(null);
    }

    @Override
    public List<Item> findAll() {
        log.info("Finding all item");
        return itemRepository.findAll();
    }

    @Override
    public Item update(Item item) {
        log.info("Updating item with id: {}", item.getItemId());
        return itemRepository.save(item);
    }

    @Override
    public boolean delete(String id) {
        Item delete = this.find(id);
        if(delete != null){
            log.info("Deleting item with id: {}", delete.getItemId());
            delete.setStatus(AvailabilityStatus.DELETED);
            this.update(delete);
            return true;
        }
        log.info("Failed to deleting item with id: {}", id);
        return false;
    }

    @Override
    public List<Item> findAllByUserIdAndStatusEquals(String userId, AvailabilityStatus status) {
        return itemRepository.findAllByOwnerIdAndStatusEquals(userId, status);
    }

    @Override
    public ItemDto saveNewItemDto(NewItemDto dto) {
        Validate.newItemDto(dto);
        int savedImagesCounter = 0;

        Town town = null;
        Campus campus = null;
        EndUser endUser = userRepository.findByEmail(jwtService.getCurrentUserEmail()).orElse(null);

        if(endUser == null){
            throwUnauthorizedException("0112", "User not recognized");
        }

        if(endUser.isStudent()){
            campus = campusService.find(endUser.getLocationId());
        } else {
            town = townService.find(endUser.getLocationId());
        }

        if(campus == null && town == null){
            throwNotFoundException("0124", "User location details required");
        }

        Item item = Item.builder()
                .name(dto.getName())
                .type(dto.getType())
                .price(Double.parseDouble(dto.getPrice()))
                .description(dto.getDescription())
                .status(AvailabilityStatus.PROCESSING)
                .ownerId(endUser.getUserId())
                .suburb(endUser.isStudent() ? campus.getSuburb() : town.getSuburb())
                .town(endUser.isStudent() ? campus.getSchoolName() : town.getTownName())
                .images(new HashSet<>())
                .itemId(getUniqueItemId())
                .build();

        item = this.save(item);

        Set<Image> images = new HashSet<>();
        DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSS");

        for(MultipartFile imageFile : dto.getImages()){
            String filename = timeStampPattern.format(java.time.LocalDateTime.now())
                    + getCombinedString(item.getItemId().substring(4, 8), endUser.getUserId().substring(0, 4))
                    + "." + StringUtils.getFilenameExtension(imageFile.getOriginalFilename());

            boolean uploaded = s3ClientService.uploadImage(filename, imageFile);

            if(uploaded){
                Image image = Image.builder()
                        .url(filename)
                        .itemId(item.getItemId())
                        .imageId(imageService.getUniqueImageId())
                        .build();
                savedImagesCounter ++;
                images.add(imageService.save(image));
                log.info("Added New Image: {}", filename);
            }
        }

        if(savedImagesCounter == 0){
            throwNotFoundException("X1C1", "Could not save image(s), try again");
        }

        item.setImages(images);
        item.setStatus(AvailabilityStatus.AVAILABLE);
        this.update(item);

        log.info("Added New Item: {}", item);
        return Converter.toItemDto(item);
    }

    @Override
    public ItemDto findItemDto(String id){
        if(!isValidString(id)){
            throwBadRequestException("1170", String.format("Invalid id: %s provided", id));
        }

        Item item = this.find(id);
        if(item == null){
            throwNotFoundException("1171", String.format("Item with id: %s not found", id));
        }

        return Converter.toItemDto(item);
    }

    @Override
    public ItemDto updateItemDto(ItemDto dto) {
        Validate.itemDto(dto);
        Item originalItem = itemRepository.findByStatusEqualsAndItemId(AvailabilityStatus.AVAILABLE, dto.getId());

        if(originalItem == null){
            throwNotFoundException("1172", String.format("Item with id: %s not found", dto.getId()));
        }

        Item item = Converter.toItem(dto);
        item.setId(originalItem.getId());
        item.setOwnerId(originalItem.getOwnerId());

        Map<String, Image> originalImages = originalItem.getImages()
                .stream()
                .collect(Collectors.toMap(Image::getImageId, Function.identity()));

        item.getImages().forEach(i -> {
            Image originalImage = originalImages.get(i.getImageId());
            if (originalImage != null) {
                i.setId(originalImage.getId());
            }
        });

        return Converter.toItemDto(this.update(item));
    }

    @Override
    public boolean deleteItemDto(String id) {
        if(!isValidLong(id)){
            throwBadRequestException("1173", String.format("Invalid id: %s provided", id));
        }

        EndUser endUser = userRepository.findByEmail(jwtService.getCurrentUserEmail()).orElse(null);

        if(endUser == null){
            throwUnauthorizedException("1175", "Invalid user");
        }

        return this.delete(id, endUser.getUserId());
    }

    @Override
    public ToPage<ItemDto> findAllAvailableItemDto(int offset, int pageSize, SearchDto searchDto){
        Validate.searchData(searchDto);
        Page<Item> items = searchCriteria.findItems(searchDto, offset, pageSize);

        return ToPage.<ItemDto>builder()
                .content(Converter.toItemDto(items.getContent()))
                .pageSize(items.getSize())
                .currentPage(items.getNumber())
                .totalElements((int)items.getTotalElements())
                .build();
    }

    private boolean delete(String id, String userId) {
        Item delete = this.find(id);
        if(delete != null && userId.equals(delete.getOwnerId())){
            log.info("Deleting item with id: {}", delete.getItemId());
            delete.setStatus(AvailabilityStatus.DELETED);
            this.update(delete);
            return true;
        }
        log.info("Failed to deleting item with id: {}", id);
        return false;
    }

    private String getUniqueItemId() {
        String uniqueId;
        boolean isUnique;
        do{
            uniqueId = UUID.randomUUID().toString();
            isUnique = itemRepository.existsByItemId(uniqueId);
        } while (isUnique);
        return uniqueId.replace("-", "");
    }

    private String getCombinedString(String itemId, String userId) {
        StringBuilder combinedString = new StringBuilder();
        for(int i = 0; i < itemId.length(); i++){
            combinedString.append(itemId.charAt(i));
            combinedString.append(userId.charAt(i));
        }
        return combinedString.toString();
    }

}
