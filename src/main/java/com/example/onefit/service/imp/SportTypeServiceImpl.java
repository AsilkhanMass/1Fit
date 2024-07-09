package com.example.onefit.service.imp;

import com.example.onefit.entity.SportTypeEntity;
import com.example.onefit.exceptions.SportTypeAlreadyExistsException;
import com.example.onefit.exceptions.SportTypeNotFoundException;
import com.example.onefit.repository.SportTypeRepository;
import com.example.onefit.service.SportTypeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SportTypeServiceImpl implements SportTypeService {

    private final SportTypeRepository repository;

    public SportTypeServiceImpl(SportTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public SportTypeEntity createSportType(SportTypeEntity sportType) {
        if(repository.findByName(sportType.getName()).isPresent()) {
            throw new SportTypeAlreadyExistsException("SportType with name " + sportType.getName() + " already exists");
        }
        return repository.save(sportType);
    }

    @Override
    @Transactional
    public SportTypeEntity updateSportType(Long id, SportTypeEntity sportType) {
        Optional<SportTypeEntity> sportTypeOptional = repository.findById(id);
        if (sportTypeOptional.isEmpty()) {
            throw new SportTypeNotFoundException("SportType not found");
        }
        SportTypeEntity existingSportType = sportTypeOptional.get();
        existingSportType.setName(sportType.getName());
        existingSportType.setDescription(sportType.getDescription());
        existingSportType.setPrice(sportType.getPrice());
        existingSportType.setLocation(sportType.getLocation());
        existingSportType.setLimitation(sportType.getLimitation());
        return repository.save(existingSportType);
    }

    @Override
    public List<SportTypeEntity> getAllSportTypes() {
        return repository.findAll();
    }

    @Override
    public SportTypeEntity getSportTypeById(Long id) {
        return repository.findById(id).orElseThrow(() -> new SportTypeNotFoundException("SportType not found"));
    }

    @Override
    @Transactional
    public void deleteSportType(Long id) {
        if (!repository.existsById(id)) {
            throw new SportTypeNotFoundException("SportType not found");
        }
        repository.deleteById(id);
    }
}
