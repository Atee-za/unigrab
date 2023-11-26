package com.unigrab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {
    private String email;
    private String password;
    private String confirmPassword;
    private String contact;
    private String townName;
    private String suburb;
    private String campusName;
    private String schoolName;
    private boolean isStudent;
}

 /* private Long id;
    private String email;
    private String password;
    private String contact;
    private boolean isStudent; // IF TRUE LOCATION = CAMPUS OTHERWISE LOCATION = TOWN
    private long locationId;
    @Enumerated
    private Role role;
  */