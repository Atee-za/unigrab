package com.unigrab.service;

import com.unigrab.model.entity.Town;

public interface ITownService extends IService<Town, Long> {
    String getUniqueTownId();
}
