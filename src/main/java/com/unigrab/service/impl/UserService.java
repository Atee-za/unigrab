package com.unigrab.service.impl;

import com.unigrab.model.constant.AvailabilityStatus;
import com.unigrab.model.dto.*;
import com.unigrab.model.entity.Campus;
import com.unigrab.model.entity.EndUser;
import com.unigrab.model.entity.Town;
import com.unigrab.repository.UserRepository;
import com.unigrab.security.JwtService;
import com.unigrab.service.ICampusService;
import com.unigrab.service.IItemService;
import com.unigrab.service.ITownService;
import com.unigrab.service.IUserService;
import com.unigrab.util.Converter;
import com.unigrab.util.Validate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import static com.unigrab.util.HandleExceptions.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final JwtService jwtService;
    private final ITownService townService;
    private final IItemService itemService;
    private final ICampusService campusService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public EndUser save(EndUser user) {
        log.info("Saving user with id: {}", user.getUserId());
        return userRepository.save(user);
    }

    @Override
    public EndUser find(String id) {
        log.info("Finding user by id: {}", id);
        return userRepository.findByUserId(id).get();
    }

    @Override
    public List<EndUser> findAll() {
        log.info("Finding all user");
        return userRepository.findAll();
    }

    @Override
    public EndUser update(EndUser user) {
        log.info("Updating user with id: {}", user.getUserId());
        return userRepository.save(user);
    }

    @Override
    public boolean delete(String id) {
        EndUser delete = this.find(id);
        if(delete != null){
            log.info("Deleting user with id: {}", delete.getUserId());
            userRepository.delete(delete);
            return true;
        }
        log.info("Failed to deleting user with id: {}", id);
        return false;
    }

    @Override
    public EndUser findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public UserInfoDto findUserInfo() {
        EndUser endUser = checkCurrentUser();

        UserInfoDto user = UserInfoDto.builder()
                .email(endUser.getEmail())
                .contact(endUser.getContact())
                .locationId(endUser.getLocationId())
                .isStudent(endUser.isStudent())
                .build();

        if(endUser.isStudent()){
            Campus campus = campusService.find(endUser.getLocationId());
            if(campus != null){
                user.setSuburb(campus.getSuburb());
                user.setCampusName(campus.getCampusName());
                user.setSchoolName(campus.getSchoolName());
            }
        } else {
            Town town = townService.find(endUser.getLocationId());
            if(town != null){
                user.setSuburb(town.getSuburb());
                user.setTownName(town.getTownName());
            }
        }

        Set<ItemDto> items = itemService.findAllByUserIdAndStatusEquals(endUser.getUserId(), AvailabilityStatus.AVAILABLE)
                .stream()
                .map(Converter::toItemDto)
                .collect(Collectors.toSet());

        user.setItems(items);

        return user;
    }

    @Override
    public TownDto updateAddressTown(TownDto dto) {
        Validate.townDto(dto);
        EndUser user = checkCurrentUser();

        if(StringUtils.isEmpty(dto.getId())) {
            dto.setId(townService.getUniqueTownId());
        }

        Town town = townService.update(Converter.toTown(dto));

        itemService.findAllByUserIdAndStatusEquals(user.getUserId(), AvailabilityStatus.AVAILABLE)
                .forEach(item -> {
                    item.setSuburb(town.getSuburb());
                    item.setTown(town.getTownName());
                    itemService.save(item);
                });

        user.setStudent(false);
        user.setLocationId(town.getTownId());
        update(user);

        return Converter.toTownDto(town);
    }

    @Override
    public CampusDto updateAddressCampus(CampusDto dto) {
        Validate.campusDto(dto);
        EndUser user = checkCurrentUser();

        if(StringUtils.isEmpty(dto.getId())){
            dto.setId(campusService.getUniqueCampusId());
        }

        Campus campus = campusService.update(Converter.toCampus(dto));

        itemService.findAllByUserIdAndStatusEquals(user.getUserId(), AvailabilityStatus.AVAILABLE)
                .forEach(item -> {
                    item.setSuburb(campus.getSuburb());
                    item.setTown(campus.getSchoolName());
                    itemService.save(item);
                });

        user.setStudent(true);
        user.setLocationId(campus.getCampusId());
        update(user);

        return Converter.toCampusDto(campus);
    }

    @Override
    public boolean updatePassword(PasswordUpdate dto) {
        Validate.passwordUpdateDto(dto);

        EndUser user = checkCurrentUser();

        boolean isValidPassword = passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword());

        if(isValidPassword) {
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            this.update(user);
        }

        log.trace("Updated Password: {} for: {}", dto, user.getUserId());
        return isValidPassword;
    }

    @Override
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    @Override
    public String getUniqueUserId() {
        String uniqueId;
        boolean isUnique;
        do{
            uniqueId = UUID.randomUUID().toString();
            isUnique = userRepository.existsByUserId(uniqueId);
        } while (isUnique);
        return uniqueId.replace("-", "");
    }

    private EndUser checkCurrentUser() {
        EndUser user = findByEmail(jwtService.getCurrentUserEmail());
        if(user == null) {
            throwUnauthorizedException("9889", "Unauthorized user");
        }
        return user;
    }

}
