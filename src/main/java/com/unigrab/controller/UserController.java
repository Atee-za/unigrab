package com.unigrab.controller;

import com.unigrab.model.dto.*;
import com.unigrab.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {

    private final IUserService userService;

    @GetMapping("/info")
    public UserInfoDto getUserInfo() {
        return userService.findUserInfo();
    }

    @PostMapping("/town")
    public TownDto updateAddress(@RequestBody TownDto dto){
        log.info("Updating Town dto: {}", dto);
        return userService.updateAddressTown(dto);
    }

    @PostMapping("/campus")
    public CampusDto updateCampus(@RequestBody CampusDto dto){
        log.info("Updating Campus dto: {}", dto);
        return userService.updateAddressCampus(dto);
    }

    @PostMapping("/password")
    public boolean updatePassword(@Valid @RequestBody PasswordUpdate dto){
        log.info("Updating Password");
        return userService.updatePassword(dto);
    }

    @GetMapping("/refresh")
    public Token refreshToken() {
        return userService.refreshToken();
    }

}
