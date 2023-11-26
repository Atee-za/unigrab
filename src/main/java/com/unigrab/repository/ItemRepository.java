package com.unigrab.repository;

import com.unigrab.model.constant.AvailabilityStatus;
import com.unigrab.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    Optional<Item> findByItemId(String itemId);
    Item findByStatusEqualsAndItemId(AvailabilityStatus status, String itemId);
    List<Item> findAllByOwnerIdAndStatusEquals(String ownerId, AvailabilityStatus status);
    boolean existsByItemId(String itemId);
}