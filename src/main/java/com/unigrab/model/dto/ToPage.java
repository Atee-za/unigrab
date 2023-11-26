package com.unigrab.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ToPage<T> {
    private List<T> content;
    private int pageSize;
    private int currentPage;
    private int totalElements;
}
