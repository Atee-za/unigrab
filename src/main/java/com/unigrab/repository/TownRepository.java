package com.unigrab.repository;

import com.unigrab.model.entity.Town;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TownRepository extends JpaRepository<Town, Long> {
    Optional<Town> findByTownId(String townId);
    boolean existsByTownId(String townId);
}
