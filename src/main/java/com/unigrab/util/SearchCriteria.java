package com.unigrab.util;

import com.unigrab.model.constant.AvailabilityStatus;
import com.unigrab.model.dto.SearchDto;
import com.unigrab.model.entity.Item;
import com.unigrab.model.entity.Item_;
import com.unigrab.repository.ItemRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchCriteria {

    private final ItemRepository itemRepository;

    public Page<Item> findItems(SearchDto request, int offset, int pageSize) {
        return itemRepository.findAll((root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!ObjectUtils.isEmpty(request.getName())) {
                predicates.add(withName(request.getName()).toPredicate(root, criteriaQuery, cb));
            }
            if (!ObjectUtils.isEmpty(request.getPrice())) {
                predicates.add(withPriceLower(Integer.parseInt(request.getPrice())).toPredicate(root, criteriaQuery, cb));
            }
            if (!ObjectUtils.isEmpty(request.getLocation())) {
                predicates.add(withLocation(request.getLocation()).toPredicate(root, criteriaQuery, cb));
            }
            if (!ObjectUtils.isEmpty(request.getType())) {
                predicates.add(withType(request.getType()).toPredicate(root, criteriaQuery, cb));
            }

            predicates.add(withAvailabilityTrue().toPredicate(root, criteriaQuery, cb));
            criteriaQuery.distinct(true);

            return cb.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(offset, pageSize));
    }

    private Specification<Item> withAvailabilityTrue() {
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder.equal(root.get(Item_.status), AvailabilityStatus.AVAILABLE)));
    }

    private Specification<Item> withName(String name) {
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(root.get(Item_.name)),
                        "%" + name.toLowerCase() + "%")));
    }

    private Specification<Item> withPriceLower(double price) {
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get(Item_.price), price)));
    }

    private Specification<Item> withLocation(String location) {
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get(Item_.suburb)),
                                "%" + location.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get(Item_.town)),
                                "%" + location.toLowerCase() + "%")
                ));
    }

    private Specification<Item> withType(String[] types) {
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.and(root.get(Item_.type).in(Arrays.stream(types).map(String::toLowerCase)
                        .collect(Collectors.toList()))));
    }

}
