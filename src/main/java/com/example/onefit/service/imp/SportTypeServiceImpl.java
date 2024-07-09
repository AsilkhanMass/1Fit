package com.example.onefit.service.imp;

import com.example.onefit.entity.SportTypeEntity;
import com.example.onefit.repository.SportTypeRepository;
import com.example.onefit.service.SportTypeService;

import java.util.List;
import java.util.Optional;

public class SportTypeServiceImpl implements SportTypeService {

    private final SportTypeRepository repository;

    public SportTypeServiceImpl(SportTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public SportTypeEntity createSportType(SportTypeEntity sportType) {
        return repository.save(sportType);
    }

    @Override
    public SportTypeEntity updateSportType(Long id, SportTypeEntity sportType) {
        Optional<SportTypeEntity> sportTypeOptional = repository.findById(id);
        if (sportTypeOptional.isPresent()) {

        }
        return repository.save(sportType);
    }

    @Override
    public List<SportTypeEntity> getAllSportTypes() {
        return repository.findAll();
    }

    @Override
    public SportTypeEntity getSportTypeById(Long id) {
        return repository.findById(id).get();
    }
}
