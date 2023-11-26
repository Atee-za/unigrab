package com.unigrab.service.impl;

import com.unigrab.model.entity.Town;
import com.unigrab.repository.TownRepository;
import com.unigrab.service.ITownService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TownService implements ITownService {

    private final TownRepository townRepository;

    @Override
    public Town save(Town town) {
        log.info("Saving town with id: {}", town.getTownId());
        return townRepository.save(town);
    }

    @Override
    public Town find(String id) {
        log.info("Finding town by id: {}", id);
        return townRepository.findByTownId(id).orElse(null);
    }

    @Override
    public List<Town> findAll() {
        log.info("Finding all town");
        return townRepository.findAll();
    }

    @Override
    public Town update(Town town) {
        log.info("Updating town with id: {}", town.getTownId());
        return townRepository.save(town);
    }

    @Override
    public boolean delete(String id) {
        Town delete = this.find(id);
        if(delete != null){
            log.info("Deleting town with id: {}", delete.getTownId());
            townRepository.delete(delete);
            return true;
        }
        log.info("Failed to deleting town with id: {}", id);
        return false;
    }

    @Override
    public String getUniqueTownId() {
        String uniqueId;
        boolean isUnique;
        do{
            uniqueId = UUID.randomUUID().toString();
            isUnique = townRepository.existsByTownId(uniqueId);
        } while (isUnique);
        return uniqueId.replace("-", "");
    }

}
