package com.unigrab.repository;

import com.unigrab.model.entity.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<EndUser, Long> {
    boolean existsByEmail(String email);
    boolean existsByUserId(String userId);
    Optional<EndUser> findByUserId(String userId);
    Optional<EndUser> findByEmail(String email);
}
