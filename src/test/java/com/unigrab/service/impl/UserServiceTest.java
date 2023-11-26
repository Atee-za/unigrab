package com.unigrab.service.impl;

import com.unigrab.model.constant.AvailabilityStatus;
import com.unigrab.model.constant.Role;
import com.unigrab.model.dto.CampusDto;
import com.unigrab.model.dto.PasswordUpdate;
import com.unigrab.model.dto.TownDto;
import com.unigrab.model.dto.UserInfoDto;
import com.unigrab.model.entity.*;
import com.unigrab.model.exception.BadRequestException;
import com.unigrab.model.exception.UnAuthorizedException;
import com.unigrab.repository.UserRepository;
import com.unigrab.security.JwtService;
import com.unigrab.util.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private JwtService jwtService;
    @Mock
    private TownService townService;
    @Mock
    private ItemService itemService;
    @Mock
    private CampusService campusService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private EndUser endUser;

    @BeforeEach
    public void setUp(){
        endUser = EndUser.builder().userId("1").userId("1").email("abc.com").contact("0227191304").isStudent(true)
                .locationId("25").password("989898").role(Role.USER).build();
    }

    @Test
    void findUserInfo_ValidStudentUser() {
        Campus campus = Campus.builder().campusId("1").campusName("District 6").schoolName("CPUT").suburb("CPT").build();

        UserInfoDto userInfoDto = UserInfoDto.builder().email(endUser.getEmail())
                .contact(endUser.getContact())
                .locationId(endUser.getLocationId())
                .isStudent(endUser.isStudent())
                .suburb(campus.getSuburb())
                .campusName(campus.getCampusName())
                .schoolName(campus.getSchoolName())
                .items(getItems().stream().map(Converter::toItemDto).collect(Collectors.toSet()))
                .build();


        when(userRepository.findByEmail(endUser.getEmail())).thenReturn(Optional.ofNullable(endUser));
        when(jwtService.getCurrentUserEmail()).thenReturn(endUser.getEmail());
        when(campusService.find(endUser.getLocationId())).thenReturn(campus);
        when(itemService.findAllByUserIdAndStatusEquals(endUser.getUserId(), AvailabilityStatus.AVAILABLE)).thenReturn(getItems());

        UserInfoDto actualResponse = userService.findUserInfo();

        assertEquals(userInfoDto, actualResponse);
        verify(townService, never()).find(endUser.getLocationId());
    }

    @Test
    void findUserInfo_ValidNonStudentUser() {
        Town town = Town.builder().townId("1").townName("District 6").suburb("CPT").build();

        endUser.setStudent(false);

        UserInfoDto userInfoDto = UserInfoDto.builder().email(endUser.getEmail())
                .contact(endUser.getContact())
                .locationId(endUser.getLocationId())
                .isStudent(endUser.isStudent())
                .suburb(town.getSuburb())
                .townName(town.getTownName())
                .items(getItems().stream().map(Converter::toItemDto).collect(Collectors.toSet()))
                .build();


        when(userRepository.findByEmail(endUser.getEmail())).thenReturn(Optional.ofNullable(endUser));
        when(jwtService.getCurrentUserEmail()).thenReturn(endUser.getEmail());
        when(townService.find(endUser.getLocationId())).thenReturn(town);
        when(itemService.findAllByUserIdAndStatusEquals(endUser.getUserId(), AvailabilityStatus.AVAILABLE)).thenReturn(getItems());

        UserInfoDto actualResponse = userService.findUserInfo();

        verify(campusService, never()).find("25");
        assertEquals(userInfoDto, actualResponse);
    }

    @Test
    void findUserInfo_InValidUser() {
        endUser = null;

        when(userRepository.findByEmail("abc.com")).thenReturn(Optional.ofNullable(endUser));
        when(jwtService.getCurrentUserEmail()).thenReturn("abc.com");

        assertThrowsExactly(UnAuthorizedException.class, () -> userService.findUserInfo());
        verifyNoMoreInteractions(jwtService, campusService, townService, itemService, userRepository);
    }

    @Test
    void updateAddressTown_Valid() {
        TownDto townDto = getTownDto();
        Town town = Town.builder().townId(townDto.getId()).townName(townDto.getTownName())
                .suburb(townDto.getSuburb()).build();

        when(userRepository.findByEmail(endUser.getEmail())).thenReturn(Optional.ofNullable(endUser));
        when(jwtService.getCurrentUserEmail()).thenReturn(endUser.getEmail());
        when(townService.update(town)).thenReturn(town);
        when(userRepository.save(any(EndUser.class))).thenReturn(endUser);

        TownDto actualTownDto = userService.updateAddressTown(townDto);

        verify(townService, times(1)).update(town);
        verify(userRepository, times(1)).save(endUser);

        assertFalse(endUser.isStudent());
        assertEquals(townDto, actualTownDto);
        assertEquals(town.getTownId(), endUser.getLocationId());
    }

    @Test
    void updateAddressTown_InvalidUser() {
        TownDto townDto = getTownDto();
        endUser = null;

        when(userRepository.findByEmail("abc.com")).thenReturn(Optional.ofNullable(endUser));
        when(jwtService.getCurrentUserEmail()).thenReturn("abc.com");

        assertThrows(UnAuthorizedException.class, () -> userService.updateAddressTown(townDto));
        verifyNoMoreInteractions(townService, userRepository, jwtService);
    }

    @Test
    void updateAddressTown_InvalidDto() {
        TownDto townDto = getTownDto();
        townDto.setSuburb("");

        assertThrows(BadRequestException.class, () -> userService.updateAddressTown(townDto));
        verifyNoInteractions(jwtService, townService, userRepository);
    }

    @Test
    void updateAddressCampus_Valid() {
        CampusDto campusDto = getCampusDto();
        Campus campus = Campus.builder().campusId(campusDto.getId()).campusName(campusDto.getCampusName())
                .schoolName(campusDto.getSchoolName()).suburb(campusDto.getSuburb()).build();

        when(userRepository.findByEmail(endUser.getEmail())).thenReturn(Optional.ofNullable(endUser));
        when(jwtService.getCurrentUserEmail()).thenReturn(endUser.getEmail());
        when(campusService.update(campus)).thenReturn(campus);
        when(userRepository.save(any(EndUser.class))).thenReturn(endUser);

        CampusDto actualCampusDto = userService.updateAddressCampus(campusDto);

        verify(campusService, times(1)).update(campus);
        verify(userRepository, times(1)).save(endUser);

        assertTrue(endUser.isStudent());
        assertEquals(campusDto, actualCampusDto);
        assertEquals(campus.getCampusId(), endUser.getLocationId());
    }

    @Test
    void updateAddressCampus_InvalidUser() {
        CampusDto campusDto = getCampusDto();
        endUser = null;

        when(userRepository.findByEmail("abc.com")).thenReturn(Optional.ofNullable(endUser));
        when(jwtService.getCurrentUserEmail()).thenReturn("abc.com");

        assertThrows(UnAuthorizedException.class, () -> userService.updateAddressCampus(campusDto));
        verifyNoMoreInteractions(campusService, userRepository);
    }

    @Test
    void updateAddressCampus_InvalidDto() {
        CampusDto campusDto = getCampusDto();
        campusDto.setCampusName("");

        assertThrows(BadRequestException.class, () -> userService.updateAddressCampus(campusDto));
        verifyNoInteractions(jwtService, campusService, userRepository);
    }

    @Test
    void updatePassword_Valid() {
        PasswordUpdate passwordUpdateDto = PasswordUpdate.builder().currentPassword("989898")
                .newPassword("858585").confirmPassword("858585").build();

        when(userRepository.findByEmail(endUser.getEmail())).thenReturn(Optional.ofNullable(endUser));
        when(jwtService.getCurrentUserEmail()).thenReturn(endUser.getEmail());
        when(passwordEncoder.matches(passwordUpdateDto.getCurrentPassword(), endUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(passwordUpdateDto.getNewPassword())).thenReturn("858585");
        when(userRepository.save(endUser)).thenReturn(endUser);

        boolean actualResponse = userService.updatePassword(passwordUpdateDto);

        assertTrue(actualResponse);
        verify(userRepository, times(1)).save(endUser);
        verify(passwordEncoder, times(1)).encode(passwordUpdateDto.getNewPassword());
        assertEquals(endUser.getPassword(), passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
    }

    @Test
    void updatePassword_InValidCurrentPassword() {
        PasswordUpdate passwordUpdateDto = PasswordUpdate.builder().currentPassword("747474")
                .newPassword("858585").confirmPassword("858585").build();

        when(userRepository.findByEmail(endUser.getEmail())).thenReturn(Optional.ofNullable(endUser));
        when(jwtService.getCurrentUserEmail()).thenReturn(endUser.getEmail());
        when(passwordEncoder.matches(passwordUpdateDto.getCurrentPassword(), endUser.getPassword())).thenReturn(false);

        boolean actualResponse = userService.updatePassword(passwordUpdateDto);

        assertFalse(actualResponse);
        verify(userRepository, never()).save(endUser);
        verify(passwordEncoder, never()).encode(passwordUpdateDto.getNewPassword());
    }

    @Test
    void updatePassword_InValidShortPassword() {
        PasswordUpdate passwordUpdateDto = PasswordUpdate.builder().currentPassword(endUser.getPassword())
                .newPassword("8585").confirmPassword("8585").build();

        assertThrows(BadRequestException.class, () -> userService.updatePassword(passwordUpdateDto));
        verifyNoInteractions(jwtService, passwordEncoder, userRepository);
    }

    @Test
    void updatePassword_InValidConfirmPassword() {
        PasswordUpdate passwordUpdateDto = PasswordUpdate.builder().currentPassword("989898")
                .newPassword("858585").confirmPassword("758585").build();

        assertThrows(BadRequestException.class, () -> userService.updatePassword(passwordUpdateDto));
        verifyNoInteractions(jwtService, passwordEncoder, userRepository);
    }

    @Test
    void updatePassword_InValidUser() {
        PasswordUpdate passwordUpdateDto = PasswordUpdate.builder().currentPassword(endUser.getPassword())
                .newPassword("858585").confirmPassword("858585").build();

        endUser = null;

        when(userRepository.findByEmail("abc.com")).thenReturn(Optional.ofNullable(endUser));
        when(jwtService.getCurrentUserEmail()).thenReturn("abc.com");

        assertThrows(UnAuthorizedException.class, () -> userService.updatePassword(passwordUpdateDto));

        verify(jwtService, times(1)).getCurrentUserEmail();
        verify(userRepository, times(1)).findByEmail("abc.com");
        verifyNoMoreInteractions(jwtService, passwordEncoder, userRepository);
    }

    private CampusDto getCampusDto(){
        return CampusDto.builder().id("1").campusName("District 6").schoolName("North Link").suburb("CPT").build();
    }
    private TownDto getTownDto(){
        return TownDto.builder().id("778").townName("Florida").suburb("CPT").build();
    }
    private List<Item> getItems(){
        List<Item> items = new ArrayList<>();
        items.add(Item.builder().itemId("1").ownerId("1").name("Dell").type("Laptop").description("5th Gen Core")
                .status(AvailabilityStatus.AVAILABLE).town("CPT").suburb("D6").images(getImages("1")).build());

        items.add(Item.builder().itemId("2").ownerId("1").name("Samsung").type("Phone").description("Slim sides")
                .status(AvailabilityStatus.AVAILABLE).town("CPT").suburb("Gardens").images(getImages("2")).build());

        return items;
    }
    private Set<Image> getImages(String  itemId){
        Set<Image> images = new HashSet<>();
        images.add(Image.builder().imageId("1").itemId("1").url("www.img1.com").build());
        images.add(Image.builder().imageId("2").itemId("2").url("www.img2.com").build());
        images.add(Image.builder().imageId("3").itemId("1").url("www.img3.com").build());

        return images.stream().filter(i -> i.getItemId().equals(itemId)).collect(Collectors.toSet());
    }

}