package com.unigrab.service;

import com.unigrab.model.entity.Image;

import java.util.Set;

public interface IImageService extends IService<Image, Long> {
    byte[] findImageByUrl(String url);
    String getUniqueImageId();
}
