package com.example.onefit.controller;

import com.example.onefit.entity.SportTypeEntity;
import com.example.onefit.service.SportTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sport-types")
public class SportTypeController {

    private final SportTypeService service;

    public SportTypeController(SportTypeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SportTypeEntity> createSportType(@RequestBody SportTypeEntity sportType) {
        SportTypeEntity createdSportType = service.createSportType(sportType);
        return new ResponseEntity<>(createdSportType, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SportTypeEntity> updateSportType(@PathVariable Long id, @RequestBody SportTypeEntity sportType) {
        SportTypeEntity updatedSportType = service.updateSportType(id, sportType);
        return new ResponseEntity<>(updatedSportType, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SportTypeEntity>> getAllSportTypes() {
        List<SportTypeEntity> sportTypes = service.getAllSportTypes();
        return new ResponseEntity<>(sportTypes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SportTypeEntity> getSportTypeById(@PathVariable Long id) {
        SportTypeEntity sportType = service.getSportTypeById(id);
        return new ResponseEntity<>(sportType, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSportType(@PathVariable Long id) {
        service.deleteSportType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

