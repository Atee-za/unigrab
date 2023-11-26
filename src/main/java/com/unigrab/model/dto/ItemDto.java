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
public class ItemDto {
    private String id;
    private String name;
    private String type;
    private String price;
    private String description;
    private String town;
    private String suburb;
    private Set<ImageDto> images;
}
