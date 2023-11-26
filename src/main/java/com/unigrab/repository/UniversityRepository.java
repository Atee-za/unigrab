package com.unigrab.repository;

import com.unigrab.model.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniversityRepository extends JpaRepository<University, Long> {
    Optional<University> findByUniversityId(String universityId);
}
