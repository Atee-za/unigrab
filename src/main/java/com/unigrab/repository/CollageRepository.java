package com.unigrab.repository;

import com.unigrab.model.entity.Collage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollageRepository extends JpaRepository<Collage, Long> {
    Optional<Collage> findByCollageId(String collageId);
}
