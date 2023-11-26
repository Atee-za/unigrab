package com.unigrab.repository;

import com.unigrab.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByImageId(String imageId);
    boolean existsByImageId(String imageId);
}
