package com.unigrab.service;

import com.unigrab.model.dto.*;
import com.unigrab.model.entity.EndUser;

public interface IUserService extends IService<EndUser, Long> {
    EndUser findByEmail(String email);
    UserInfoDto findUserInfo();
    TownDto updateAddressTown(TownDto dto);
    CampusDto updateAddressCampus(CampusDto dto);
    boolean updatePassword(PasswordUpdate dto);
    boolean existsByEmail(String email);
    String getUniqueUserId();
    Token refreshToken();
}
