package com.example.onefit.service.imp;

import com.example.onefit.entity.SportTypeEntity;
import com.example.onefit.exceptions.SportTypeAlreadyExistsException;
import com.example.onefit.exceptions.SportTypeNotFoundException;
import com.example.onefit.repository.SportTypeRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


class SportTypeServiceImplTest {

    @Mock
    private SportTypeRepository repository;

    @InjectMocks
    private SportTypeServiceImpl sportTypeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    public void testCreateSportType_Success() {
        // Given
        SportTypeEntity sportType = new SportTypeEntity();
        sportType.setName("Basketball");
        sportType.setDescription("A team sport");
        sportType.setPrice(BigDecimal.valueOf(50.00));
        sportType.setLocation("Court");
        sportType.setLimit((short) 10);

        when(repository.findByName(sportType.getName())).thenReturn(Optional.empty());
        when(repository.save(sportType)).thenReturn(sportType);

        // When
        SportTypeEntity createdSportType = sportTypeService.createSportType(sportType);

        // Then
        assertNotNull(createdSportType);
        assertEquals("Basketball", createdSportType.getName());
        verify(repository, times(1)).findByName(sportType.getName());
        verify(repository, times(1)).save(sportType);
    }

    @Test
    @Transactional
    public void testCreateSportType_AlreadyExists() {
        // Given
        SportTypeEntity sportType = new SportTypeEntity();
        sportType.setName("Basketball");

        when(repository.findByName(sportType.getName())).thenReturn(Optional.of(sportType));

        // When / Then
        SportTypeAlreadyExistsException exception = assertThrows(SportTypeAlreadyExistsException.class, () -> {
            sportTypeService.createSportType(sportType);
        });

        assertEquals("SportType with name Basketball already exists", exception.getMessage());
        verify(repository, times(1)).findByName(sportType.getName());
        verify(repository, times(0)).save(sportType);
    }

    @Test
    @Transactional
    public void testUpdateSportType_Success() {
        // Given
        SportTypeEntity existingSportType = new SportTypeEntity();
        existingSportType.setId(1L);
        existingSportType.setName("Basketball");
        existingSportType.setDescription("A team sport");
        existingSportType.setPrice(BigDecimal.valueOf(50.00));
        existingSportType.setLocation("Court");
        existingSportType.setLimit((short) 10);

        SportTypeEntity updatedSportType = new SportTypeEntity();
        updatedSportType.setId(1L);
        updatedSportType.setName("Basketball Updated");
        updatedSportType.setDescription("A team sport updated");
        updatedSportType.setPrice(BigDecimal.valueOf(60.00));
        updatedSportType.setLocation("New Court");
        updatedSportType.setLimit((short) 12);

        when(repository.findById(existingSportType.getId())).thenReturn(Optional.of(existingSportType));
        when(repository.save(any(SportTypeEntity.class))).thenReturn(updatedSportType);

        // When
        SportTypeEntity result = sportTypeService.updateSportType(updatedSportType);

        // Then
        assertNotNull(result);
        assertEquals("Basketball Updated", result.getName());
        assertEquals("A team sport updated", result.getDescription());
        assertEquals(BigDecimal.valueOf(60.00), result.getPrice());
        assertEquals("New Court", result.getLocation());
        assertEquals((short) 12, result.getLimit());

        verify(repository, times(1)).findById(existingSportType.getId());
        verify(repository, times(1)).save(any(SportTypeEntity.class));
    }

    @Test
    @Transactional
    public void testUpdateSportType_NotFound() {
        // Given
        SportTypeEntity sportType = new SportTypeEntity();
        sportType.setId(1L);
        sportType.setName("Basketball");

        when(repository.findById(sportType.getId())).thenReturn(Optional.empty());

        // When / Then
        SportTypeNotFoundException exception = assertThrows(SportTypeNotFoundException.class, () -> {
            sportTypeService.updateSportType(sportType);
        });

        assertEquals("SportType not found", exception.getMessage());
        verify(repository, times(1)).findById(sportType.getId());
        verify(repository, times(0)).save(any(SportTypeEntity.class));
    }

    @Test
    public void testGetAllSportTypes() {
        // Given
        SportTypeEntity sportType1 = new SportTypeEntity();
        sportType1.setId(1L);
        sportType1.setName("Basketball");
        sportType1.setDescription("A team sport");
        sportType1.setPrice(BigDecimal.valueOf(50.00));
        sportType1.setLocation("Court");
        sportType1.setLimit((short) 10);

        SportTypeEntity sportType2 = new SportTypeEntity();
        sportType2.setId(2L);
        sportType2.setName("Soccer");
        sportType2.setDescription("A field sport");
        sportType2.setPrice(BigDecimal.valueOf(40.00));
        sportType2.setLocation("Field");
        sportType2.setLimit((short) 22);

        List<SportTypeEntity> sportTypes = Arrays.asList(sportType1, sportType2);

        when(repository.findAll()).thenReturn(sportTypes);

        // When
        List<SportTypeEntity> result = sportTypeService.getAllSportTypes();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Basketball", result.get(0).getName());
        assertEquals("Soccer", result.get(1).getName());

        verify(repository, times(1)).findAll();
    }

    @Test
    public void testGetSportTypeById_Success() {
        // Given
        SportTypeEntity sportType = new SportTypeEntity();
        sportType.setId(1L);
        sportType.setName("Basketball");
        sportType.setDescription("A team sport");
        sportType.setPrice(BigDecimal.valueOf(50.00));
        sportType.setLocation("Court");
        sportType.setLimit((short) 10);

        when(repository.findById(1L)).thenReturn(Optional.of(sportType));

        // When
        SportTypeEntity result = sportTypeService.getSportTypeById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Basketball", result.getName());
        assertEquals("A team sport", result.getDescription());
        assertEquals(BigDecimal.valueOf(50.00), result.getPrice());
        assertEquals("Court", result.getLocation());
        assertEquals((short) 10, result.getLimit());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    public void testGetSportTypeById_NotFound() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        SportTypeNotFoundException exception = assertThrows(SportTypeNotFoundException.class, () -> {
            sportTypeService.getSportTypeById(1L);
        });

        assertEquals("SportType not found", exception.getMessage());
        verify(repository, times(1)).findById(1L);
    }


    @Test
    @Transactional
    public void testDeleteSportType_Success() {
        // Given
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        // When
        sportTypeService.deleteSportType(id);

        // Then
        verify(repository, times(1)).existsById(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @Transactional
    public void testDeleteSportType_NotFound() {
        // Given
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(false);

        // When / Then
        SportTypeNotFoundException exception = assertThrows(SportTypeNotFoundException.class, () -> {
            sportTypeService.deleteSportType(id);
        });

        assertEquals("SportType not found", exception.getMessage());
        verify(repository, times(1)).existsById(id);
        verify(repository, times(0)).deleteById(id);
    }
}