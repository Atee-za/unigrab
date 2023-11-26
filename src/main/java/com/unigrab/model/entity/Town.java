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
public class Town {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String townName;
    private String suburb;
    private String townId;
}