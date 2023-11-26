package com.unigrab.service.impl;

import com.unigrab.model.entity.Collage;
import com.unigrab.repository.CollageRepository;
import com.unigrab.service.ICollageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollageService implements ICollageService {

    private final CollageRepository collageRepository;

    @Override
    public Collage save(Collage collage) {
        log.info("Saving collage with id: {}", collage.getCollageId());
        return collageRepository.save(collage);
    }

    @Override
    public Collage find(String id) {
        log.info("Finding collage by id: {}", id);
        return collageRepository.findByCollageId(id).orElse(null);
    }

    @Override
    public List<Collage> findAll() {
        log.info("Finding all collage");
        return collageRepository.findAll();
    }

    @Override
    public Collage update(Collage collage) {
        log.info("Updating collage with id: {}", collage.getCollageId());
        return collageRepository.save(collage);
    }

    @Override
    public boolean delete(String  id) {
        Collage delete = this.find(id);
        if(delete != null){
            log.info("Deleting collage with id: {}", delete.getCollageId());
            collageRepository.delete(delete);
            return true;
        }
        log.info("Failed to deleting collage with id: {}", id);
        return false;
    }

}
