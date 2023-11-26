package com.unigrab.service.impl;

import com.unigrab.model.entity.Image;
import com.unigrab.repository.ImageRepository;
import com.unigrab.service.IImageService;
import com.unigrab.service.s3.S3ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final S3ClientService s3ClientService;

    @Override
    public Image save(Image image) {
        log.info("Saving image with id: {}", image.getImageId());
        return imageRepository.save(image);
    }

    @Override
    public Image find(String id) {
        log.info("Finding image by id: {}", id);
        return imageRepository.findByImageId(id).orElse(null);
    }

    @Override
    public List<Image> findAll() {
        log.info("Finding all image");
        return imageRepository.findAll();
    }

    @Override
    public Image update(Image image) {
        log.info("Updating image with id: {}", image.getImageId());
        return imageRepository.save(image);
    }

    @Override
    public boolean delete(String id) {
        Image delete = this.find(id);
        if(delete != null){
            log.info("Deleting image with id: {}", delete.getImageId());
            imageRepository.delete(delete);
            return true;
        }
        log.info("Failed to deleting image with id: {}", id);
        return false;
    }

    @Override
    public byte[] findImageByUrl(String url) {
        return StringUtils.isBlank(url) ? new byte[0] : s3ClientService.downloadImage(url);
    }

    @Override
    public String getUniqueImageId() {
        String uniqueId;
        boolean isUnique;
        do{
            uniqueId = UUID.randomUUID().toString();
            isUnique = imageRepository.existsByImageId(uniqueId);
        } while (isUnique);
        return uniqueId.replace("-", "");
    }

}
