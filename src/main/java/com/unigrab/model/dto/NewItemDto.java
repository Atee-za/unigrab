package com.unigrab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewItemDto {
    private String name;
    private String type;
    private String price;
    private String description;
    private Set<MultipartFile> images;
}
