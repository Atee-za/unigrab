package com.unigrab.service.impl;

import com.unigrab.model.constant.AvailabilityStatus;
import com.unigrab.model.constant.Role;
import com.unigrab.model.dto.ItemDto;
import com.unigrab.model.dto.NewItemDto;
import com.unigrab.model.entity.*;
import com.unigrab.repository.ItemRepository;
import com.unigrab.repository.UserRepository;
import com.unigrab.security.JwtService;
import com.unigrab.service.s3.S3ClientService;
import com.unigrab.util.SearchCriteria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private TownService townService;
    @Mock
    private CampusService campusService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private S3ClientService s3ClientService;
    @Mock
    private SearchCriteria searchCriteria;

    @Test
    void saveNewItemDto_Valid() {
        EndUser endUser = EndUser.builder().userId("test-user-101").email("abc.com").contact("0227191304").isStudent(true)
                .locationId("25").password("989898").role(Role.USER).build();

        Campus campus = Campus.builder().campusId("1").campusName("District 6").schoolName("CPUT").suburb("CPT").build();

        Set<MultipartFile> images = new HashSet<>();
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                "test1.jpg",
                "image/jpeg",
                "test1 image".getBytes()
        );

        images.add(mockMultipartFile);

        NewItemDto newItemDto = NewItemDto.builder()
                .name("Acer")
                .type("Laptop")
                .price("2500")
                .description("i11, 16GB Memory, 500GB Storage")
                .images(images)
                .build();

        Item item = Item.builder().itemId("abcdefghijklmnopqestuvyxyz").description(newItemDto.getDescription()).name(newItemDto.getName())
                .ownerId(endUser.getUserId()).suburb(campus.getSuburb()).town(campus.getSchoolName())
                .status(AvailabilityStatus.PROCESSING).build();

        Image image = Image.builder().imageId("0").url("image1.xxx").itemId(item.getItemId()).build();

        when(userRepository.findByEmail(endUser.getEmail())).thenReturn(Optional.of(endUser));
        when(jwtService.getCurrentUserEmail()).thenReturn(endUser.getEmail());
        when(campusService.find(endUser.getLocationId())).thenReturn(campus);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(s3ClientService.uploadImage(anyString(), any(MultipartFile.class))).thenReturn(true);
        when(imageService.save(any(Image.class))).thenReturn(image);

     //   doNothing().when(mockMultipartFile).transferTo(any(File.class));

        ItemDto result = itemService.saveNewItemDto(newItemDto);

        verify(townService, never()).find(anyString());
        verify(jwtService, times(1)).getCurrentUserEmail();
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(userRepository, times(1)).findByEmail(endUser.getEmail());

        assertNotNull(result);
    }

    @Test
    void findItemDto() {
    }

    @Test
    void updateItemDto() {
    }

    @Test
    void deleteItemDto() {
    }

    @Test
    void findAllAvailableItemDto() {
    }

}