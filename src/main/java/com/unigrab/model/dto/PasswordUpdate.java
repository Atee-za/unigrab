package com.unigrab.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class PasswordUpdate {
    @NotBlank(message = "New password is required")
    @Length(min = 6, message = "New Password length is too short. Minimum of 6 characters required")
    private String newPassword;
    @NotBlank(message = "Current password is required")
    @Length(min = 6, message = "Current Password length is too short. Minimum of 6 characters required")
    private String currentPassword;
    @NotBlank(message = "Confirm password is required")
    @Length(min = 6, message = "Confirm Password length is too short. Minimum of 6 characters required")
    private String confirmPassword;
}