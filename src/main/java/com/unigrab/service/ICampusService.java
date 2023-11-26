package com.unigrab.service;

import com.unigrab.model.entity.Campus;

public interface ICampusService extends IService<Campus, Long> {
    String getUniqueCampusId();
}
