package com.unigrab.service;

import com.unigrab.model.dto.RegisterDto;
import com.unigrab.model.dto.SignInDto;
import com.unigrab.model.dto.Token;

public interface IAuthService {
    Token register(RegisterDto dto);
    Token signIn(SignInDto dto);
}
