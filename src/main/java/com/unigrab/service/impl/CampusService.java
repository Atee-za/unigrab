package com.unigrab.service.impl;

import com.unigrab.model.entity.Campus;
import com.unigrab.repository.CampusRepository;
import com.unigrab.service.ICampusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampusService implements ICampusService {

    private final CampusRepository campusRepository;

    @Override
    public Campus save(Campus campus) {
        log.info("Saving campus with id: {}", campus.getCampusId());
        return campusRepository.save(campus);
    }

    @Override
    public Campus find(String id) {
        log.info("Finding campus by id: {}", id);
        return campusRepository.findByCampusId(id).orElse(null);
    }

    @Override
    public List<Campus> findAll() {
        log.info("Finding all campus");
        return campusRepository.findAll();
    }

    @Override
    public Campus update(Campus campus) {
        log.info("Updating campus with id: {}", campus.getCampusId());
        return campusRepository.save(campus);
    }

    @Override
    public boolean delete(String id) {
        Campus delete = this.find(id);
        if(delete != null){
            log.info("Deleting campus with id: {}", delete.getCampusId());
            campusRepository.delete(delete);
            return true;
        }
        log.info("Failed to deleting campus with id: {}", id);
        return false;
    }

    @Override
    public String getUniqueCampusId() {
        String uniqueId;
        boolean isUnique;
        do{
            uniqueId = UUID.randomUUID().toString();
            isUnique = campusRepository.existsByCampusId(uniqueId);
        } while (isUnique);
        return uniqueId.replace("-", "");
    }

}
