package com.unigrab.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchDto {
    private String name;
    private String price;
    private String location;
    private String[] type;
}
