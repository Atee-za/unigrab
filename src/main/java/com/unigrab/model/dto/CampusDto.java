package com.unigrab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CampusDto { //TODO: note to self, IF IS STUDENT WE SEARCH ADDRESS INFO HERE else TOWN
    private String id;
    private String campusName;
    private String schoolName;
    private String suburb;
}
