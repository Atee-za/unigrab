package com.unigrab.util;

import com.unigrab.model.constant.*;
import com.unigrab.model.dto.*;
import com.unigrab.model.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Converter {

    public static Item toItem(ItemDto dto){
        log.info("Converting ItemDto with id: {} to Item.", dto.getId());
        return Item.builder()
                .itemId(dto.getId())
                .name(dto.getName())
                .type(dto.getType())
                .price(Double.parseDouble(dto.getPrice()))
                .description(dto.getDescription())
                .town(dto.getTown())
                .status(AvailabilityStatus.AVAILABLE)
                .suburb(dto.getSuburb())
                .images(dto.getImages().stream().map(Converter::toImage).collect(Collectors.toSet()))
                .build();
    }

    public static ItemDto toItemDto(Item i){
        log.info("Converting Item with id: {} to ItemDto.", i.getItemId());
        return ItemDto.builder()
                .id(i.getItemId())
                .name(i.getName())
                .type(i.getType())
                .price(""+i.getPrice())
                .description(i.getDescription())
                .town(i.getTown())
                .suburb(i.getSuburb())
                .images(i.getImages().stream().map(Converter::toImageDto).collect(Collectors.toSet()))
                .build();
    }

    public static List<ItemDto> toItemDto(List<Item> i){
        log.info("Converting Items: {} to ItemDtos.", i);
        return i.stream().map(Converter::toItemDto).collect(Collectors.toList());
    }

    public static Campus toCampus(CampusDto dto){
        log.info("Converting CampusDto with id: {} to Campus.", dto.getId());
        return Campus.builder()
                .campusId(dto.getId())
                .campusName(dto.getCampusName())
                .schoolName(dto.getSchoolName())
                .suburb(dto.getSuburb())
                .build();
    }

    public static CampusDto toCampusDto(Campus c){
        log.info("Converting Campus with id: {} to CampusDto.", c.getCampusId());
        return CampusDto.builder()
                .id(c.getCampusId())
                .campusName(c.getCampusName())
                .schoolName(c.getSchoolName())
                .suburb(c.getSuburb())
                .build();
    }

    public static Collage toCollage(CollageDto dto){
        log.info("Converting CollageDto with id: {} to Collage.", dto.getId());
        return Collage.builder()
                .collageId(dto.getId())
                .name(dto.getName())
                .build();
    }

    public static CollageDto toCollageDto(Collage c){
        log.info("Converting Collage with id: {} to CollageDto.", c.getCollageId());
        return CollageDto.builder()
                .id(c.getCollageId())
                .name(c.getName())
                .build();
    }

    public static Image toImage(ImageDto dto){
        log.info("Converting ImageDto with id: {} to Image.", dto.getId());
        return Image.builder()
                .imageId(dto.getId())
                .url(dto.getUrl())
                .itemId(dto.getItemId())
                .build();
    }

    public static ImageDto toImageDto(Image i){
        log.info("Converting Image with id: {} to ImageDto.", i.getImageId());
        return ImageDto.builder()
                .id(i.getImageId())
                .url(i.getUrl())
                .itemId(""+i.getItemId())
                .build();
    }

    public static Town toTown(TownDto dto){
        log.info("Converting TownDto with id: {} to Town.", dto.getId());
        return Town.builder()
                .townId(dto.getId())
                .townName(dto.getTownName())
                .suburb(dto.getSuburb())
                .build();
    }

    public static TownDto toTownDto(Town t){
        log.info("Converting Town with id: {} to TownDto.", t.getTownId());
        return TownDto.builder()
                .id(t.getTownId())
                .townName(t.getTownName())
                .suburb(t.getSuburb())
                .build();
    }

    public static University toUniversity(UniversityDto dto){
        log.info("Converting UniversityDto with id: {} to University.", dto.getId());
        return University.builder()
                .universityId(dto.getId())
                .name(dto.getName())
                .build();
    }

    public static UniversityDto toUniversityDto(University u){
        log.info("Converting University with id: {} to UniversityDto.", u.getUniversityId());
        return UniversityDto.builder()
                .id(u.getUniversityId())
                .name(u.getName())
                .build();
    }

    public static EndUser toEndUser(UserRegistrationDto dto){
        log.info("Converting UserInfoDto with id: {} to EndUser.", dto);
        return EndUser.builder()
                .userId("0")
                .email(dto.getEmail())
                .password(dto.getPassword())
                .contact(dto.getContact())
                .isStudent(dto.isStudent())
                .locationId("0")
                .role(Role.USER)
                .build();
    }

  /*  public static UserInfoDto toUserDto(UserInfo u){
        log.info("Converting UserInfo with id: {} to UserInfoDto.", u.getId());
        return UserInfoDto.builder()
                .id(u.getId().toString())
                .isStudent(u.isStudent())
                .locationId(""+u.getLocationId())
                .items(u.getItems().stream().map(Converter::toItemDto).collect(Collectors.toSet()))
                .build();
    }*/

}