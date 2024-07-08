package com.example.onefit.controller;

import com.example.onefit.entity.SportEntity;
import com.example.onefit.service.SportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sports")
public class SportController {

    @Autowired
    private SportService sportService;

    @GetMapping
    public List<SportEntity> getAllSports() {
        return sportService.getAllSports();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SportEntity> getSportById(@PathVariable Long id) {
        SportEntity sport = sportService.getSportById(id);
        if (sport != null) {
            return ResponseEntity.ok(sport);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public SportEntity createSport(@RequestBody SportEntity sport) {
        return sportService.createSport(sport);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SportEntity> updateSport(@PathVariable Long id, @RequestBody SportEntity sport) {
        SportEntity updatedSport = sportService.updateSport(id, sport);
        if (updatedSport != null) {
            return ResponseEntity.ok(updatedSport);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSport(@PathVariable Long id) {
        sportService.deleteSport(id);
        return ResponseEntity.noContent().build();
    }
}
