package com.unigrab.model.dto;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class RegisterDto {
    private String email;
    private String password;
    private String confirmPassword;
    private boolean isStudent;
}
