package com.unigrab.model.entity;

import com.unigrab.model.constant.AvailabilityStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;

import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String type;
    private double price;
    private String description;
    private String ownerId;
    private String town;
    private String suburb;
    @Enumerated(value = EnumType.STRING)
    private AvailabilityStatus status;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId", referencedColumnName = "itemId")
    private Set<Image> images;
    private String itemId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return // Objects.equals(id, item.id) &&
                Objects.equals(itemId, item.itemId) &&
                Objects.equals(ownerId, item.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, ownerId);
    }

}
