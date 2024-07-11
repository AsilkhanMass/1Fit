package com.example.onefit.service.imp;

import com.example.onefit.entity.SportTypeEntity;
import com.example.onefit.entity.Subscription;
import com.example.onefit.entity.UserEntity;
import com.example.onefit.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServiceImplTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSubscription() {
        // Create a sample UserEntity and SportTypeEntity for testing
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        SportTypeEntity sportTypeEntity = new SportTypeEntity();
        sportTypeEntity.setId(1L);

        // Create a Subscription instance to save
        Subscription subscriptionToSave = new Subscription(1L,userEntity, sportTypeEntity, (short) 10, true);
        subscriptionToSave.setId(1L);

        // Mock behavior of subscriptionRepository.save
        when(subscriptionRepository.save(subscriptionToSave)).thenReturn(subscriptionToSave);

        // Call the service method
        Subscription result = subscriptionService.createSubscription(subscriptionToSave);

        // Assertions
        assertNotNull(result);
        assertEquals(subscriptionToSave.getId(), result.getId());
        assertEquals(subscriptionToSave.getUserId(), result.getUserId());
        assertEquals(subscriptionToSave.getSportId(), result.getSportId());
        assertEquals(subscriptionToSave.getNumberOfVisits(), result.getNumberOfVisits());
        assertEquals(subscriptionToSave.getIsActive(), result.getIsActive());

        // Verify that subscriptionRepository.save was called exactly once with subscriptionToSave
        verify(subscriptionRepository, times(1)).save(subscriptionToSave);
    }
    @Test
    public void testUpdateSubscription() {
        // Create a sample Subscription
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        SportTypeEntity sportTypeEntity = new SportTypeEntity();
        sportTypeEntity.setId(1L);

        Subscription existingSubscription = new Subscription(1L,userEntity, sportTypeEntity, (short) 10, true);
        existingSubscription.setId(1L);

        // Mock behavior of subscriptionRepository.findById
        when(subscriptionRepository.findById(existingSubscription.getId()))
                .thenReturn(Optional.of(existingSubscription));

        // Create an updated Subscription
        UserEntity updatedUserEntity = new UserEntity();
        updatedUserEntity.setId(2L);

        SportTypeEntity updatedSportTypeEntity = new SportTypeEntity();
        updatedSportTypeEntity.setId(2L);

        Subscription updatedSubscription = new Subscription(2L, updatedUserEntity, updatedSportTypeEntity, (short) 15, false);
        updatedSubscription.setId(existingSubscription.getId());

        // Mock behavior of subscriptionRepository.save
        when(subscriptionRepository.save(existingSubscription)).thenReturn(updatedSubscription);

        // Call the service method
        Optional<Subscription> result = subscriptionService.updateSubscription(existingSubscription);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(updatedSubscription.getId(), result.get().getId());
        assertEquals(updatedSubscription.getUserId(), result.get().getUserId());
        assertEquals(updatedSubscription.getSportId(), result.get().getSportId());
        assertEquals(updatedSubscription.getNumberOfVisits(), result.get().getNumberOfVisits());
        assertEquals(updatedSubscription.getIsActive(), result.get().getIsActive());

        // Verify that subscriptionRepository.findById was called exactly once with existingSubscription.getId()
        verify(subscriptionRepository, times(1)).findById(existingSubscription.getId());

        // Verify that subscriptionRepository.save was called exactly once with existingSubscription
        verify(subscriptionRepository, times(1)).save(existingSubscription);
    }

    @Test
    public void testUpdateSubscriptionNotFound() {
        // Create a sample Subscription with non-existent ID
        Subscription subscription = new Subscription();
        subscription.setId(999L);

        // Mock behavior of subscriptionRepository.findById
        when(subscriptionRepository.findById(subscription.getId())).thenReturn(Optional.empty());

        // Call the service method
        Optional<Subscription> result = subscriptionService.updateSubscription(subscription);

        // Assertions
        assertFalse(result.isPresent());

        // Verify that subscriptionRepository.findById was called exactly once with subscription.getId()
        verify(subscriptionRepository, times(1)).findById(subscription.getId());

        // Verify that subscriptionRepository.save was never called
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    public void testDeleteSubscription() {
        // Mock behavior of subscriptionRepository.deleteById
        Long subscriptionId = 1L;
        doNothing().when(subscriptionRepository).deleteById(subscriptionId);

        // Call the service method
        subscriptionService.deleteSubscription(subscriptionId);

        // Verify that subscriptionRepository.deleteById was called exactly once with subscriptionId
        verify(subscriptionRepository, times(1)).deleteById(subscriptionId);
    }

    @Test
    public void testGetAllSubscriptions() {
        // Create sample subscriptions
        Subscription subscription1 = new Subscription(3L,new UserEntity(), new SportTypeEntity(), (short) 10, true);
        Subscription subscription2 = new Subscription(4L, new UserEntity(), new SportTypeEntity(), (short) 5, false);
        List<Subscription> subscriptions = Arrays.asList(subscription1, subscription2);

        // Mock behavior of subscriptionRepository.findAll
        when(subscriptionRepository.findAll()).thenReturn(subscriptions);

        // Call the service method
        List<Subscription> result = subscriptionService.getAllSubscriptions();

        // Verify the result
        assertEquals(2, result.size());
        assertEquals(subscription1.getId(), result.get(0).getId());
        assertEquals(subscription2.getId(), result.get(1).getId());

        // Verify that subscriptionRepository.findAll was called exactly once
        verify(subscriptionRepository, times(1)).findAll();
    }

    @Test
    public void testGetSubscriptionById() {
        // Sample subscription
        Long subscriptionId = 1L;
        Subscription subscription = new Subscription(5L,new UserEntity(), new SportTypeEntity(), (short) 10, true);
        subscription.setId(subscriptionId);

        // Mock behavior of subscriptionRepository.findById
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));

        // Call the service method
        Optional<Subscription> result = subscriptionService.getSubscriptionById(subscriptionId);

        // Verify the result
        assertTrue(result.isPresent());
        assertEquals(subscriptionId, result.get().getId());

        // Verify that subscriptionRepository.findById was called exactly once with subscriptionId
        verify(subscriptionRepository, times(1)).findById(subscriptionId);
    }

    @Test
    public void testGetSubscriptionById_NotFound() {
        // Mock behavior of subscriptionRepository.findById for non-existent subscription
        Long nonExistentId = 999L;
        when(subscriptionRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Call the service method
        Optional<Subscription> result = subscriptionService.getSubscriptionById(nonExistentId);

        // Verify the result
        assertFalse(result.isPresent());

        // Verify that subscriptionRepository.findById was called exactly once with nonExistentId
        verify(subscriptionRepository, times(1)).findById(nonExistentId);
    }
}