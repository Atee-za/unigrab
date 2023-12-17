package com.unigrab.service.impl;

import com.unigrab.model.constant.Role;
import com.unigrab.model.dto.Token;
import com.unigrab.model.dto.RegisterDto;
import com.unigrab.model.dto.SignInDto;
import com.unigrab.model.entity.EndUser;
import com.unigrab.security.JwtService;
import com.unigrab.service.IAuthService;
import com.unigrab.service.IUserService;
import com.unigrab.util.Validate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static com.unigrab.util.HandleExceptions.throwBadRequestException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public Token register(RegisterDto dto) {
        Validate.registerDto(dto);

        if(userService.existsByEmail(dto.getEmail())) {
            throwBadRequestException("0124", "Email provided already exist");
        }

        var user = EndUser.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .contact("")
                .isStudent(dto.isStudent())
                .role(Role.USER)
                .userId(userService.getUniqueUserId())
                .build();

        userService.save(user);
        return new Token(jwtService.generateToken(user));
    }

    @Override
    public Token signIn(SignInDto dto){
        Validate.signInDto(dto);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        } catch (Exception e) {
            throwBadRequestException("0112", "Invalid credentials provided");
        }

        var user = userService.findByEmail(dto.getEmail());
        return new Token(jwtService.generateToken(user));
    }

}
