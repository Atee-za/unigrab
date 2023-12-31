package com.unigrab.repository;

import com.unigrab.model.entity.Campus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CampusRepository extends JpaRepository<Campus, Long> {
    Optional<Campus> findByCampusId(String campusId);
    boolean existsByCampusId(String campusId);
}
