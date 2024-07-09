package com.example.onefit.service;

import com.example.onefit.entity.SportTypeEntity;

import java.util.List;

public interface SportTypeService {
    SportTypeEntity createSportType(SportTypeEntity sportType);
    SportTypeEntity updateSportType(Long id, SportTypeEntity sportType);
    List<SportTypeEntity> getAllSportTypes();
    SportTypeEntity getSportTypeById(Long id);
}
