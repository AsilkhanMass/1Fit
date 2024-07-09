package com.example.onefit.repository;

import com.example.onefit.entity.SportTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SportTypeRepository extends JpaRepository<SportTypeEntity, Long> {
  Optional<SportTypeEntity> findById(Long id);
  Optional<SportTypeEntity> findByName(String name);
  List<SportTypeEntity> findAll();
}