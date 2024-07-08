package com.example.onefit.service.imp;

import com.example.onefit.entity.SportEntity;
import com.example.onefit.repository.SportRepository;
import com.example.onefit.service.SportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SportServiceImpl implements SportService {

    @Autowired
    private SportRepository sportRepository;

    @Override
    public List<SportEntity> getAllSports() {
        return sportRepository.findAll();
    }

    @Override
    public SportEntity getSportById(Long id) {
        Optional<SportEntity> sport = sportRepository.findById(id);
        return sport.orElse(null);
    }

    @Override
    public SportEntity createSport(SportEntity sport) {
        return sportRepository.save(sport);
    }

    @Override
    public SportEntity updateSport(Long id, SportEntity sport) {
        if (sportRepository.existsById(id)) {
            sport.setId(id);
            return sportRepository.save(sport);
        }
        return null;
    }

    @Override
    public void deleteSport(Long id) {
        sportRepository.deleteById(id);
    }
}
