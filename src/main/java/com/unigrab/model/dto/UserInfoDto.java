package com.unigrab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private String email;
    private String contact;
    private String locationId;
    private boolean isStudent;
    private String townName;
    private String suburb;
    private String campusName;
    private String schoolName;
    private Set<ItemDto> items;
}
