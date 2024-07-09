package com.example.onefit.repository;

import com.example.onefit.entity.SportTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SportTypeRepository extends JpaRepository<SportTypeEntity, Long> {
  Optional<SportTypeEntity> findById(Long id);
  Optional<SportTypeEntity> findByName(String name);
  List<SportTypeEntity> findAll();
}