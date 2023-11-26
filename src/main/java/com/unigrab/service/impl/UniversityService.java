package com.unigrab.service.impl;

import com.unigrab.model.entity.University;
import com.unigrab.repository.UniversityRepository;
import com.unigrab.service.IUniversityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniversityService implements IUniversityService {

    private final UniversityRepository universityRepository;

    @Override
    public University save(University university) {
        log.info("Saving university with id: {}", university.getUniversityId());
        return universityRepository.save(university);
    }

    @Override
    public University find(String  id) {
        log.info("Finding university by id: {}", id);
        return universityRepository.findByUniversityId(id).orElse(null);
    }

    @Override
    public List<University> findAll() {
        log.info("Finding all university");
        return universityRepository.findAll();
    }

    @Override
    public University update(University university) {
        log.info("Updating university with id: {}", university.getUniversityId());
        return universityRepository.save(university);
    }

    @Override
    public boolean delete(String  id) {
        University delete = this.find(id);
        if(delete != null){
            log.info("Deleting university with id: {}", delete.getUniversityId());
            universityRepository.delete(delete);
            return true;
        }
        log.info("Failed to deleting university with id: {}", id);
        return false;
    }

}
