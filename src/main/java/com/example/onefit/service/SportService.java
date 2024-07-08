package com.example.onefit.service;

import com.example.onefit.entity.SportEntity;

import java.util.List;

public interface SportService {
    List<SportEntity> getAllSports();
    SportEntity getSportById(Long id);
    SportEntity createSport(SportEntity sport);
    SportEntity updateSport(Long id, SportEntity sport);
    void deleteSport(Long id);
}
