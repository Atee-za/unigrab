package com.unigrab.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Campus {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String campusName;
    private String schoolName;
    private String suburb;
    private String campusId;
}
