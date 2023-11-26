package com.unigrab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TownDto { //TODO: note to self, IF NOT IS STUDENT WE SEARCH ADDRESS INFO HERE else CAMPUS
    private String id;
    private String townName;
    private String suburb;
}
