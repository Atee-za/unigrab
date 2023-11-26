package com.unigrab.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String url;
    private String itemId;
    private String imageId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return  Objects.equals(url, image.url) &&
                Objects.equals(itemId, image.itemId) &&
                Objects.equals(imageId, image.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, itemId, imageId);
    }

}