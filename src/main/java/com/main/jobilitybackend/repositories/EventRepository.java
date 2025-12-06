package com.main.jobilitybackend.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.jobilitybackend.entities.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


public interface EventRepository extends JpaRepository<Event, Long> {
    // Changed from After to GreaterThanEqual
    Page<Event> findByDateGreaterThanEqual(LocalDate date, Pageable pageable);
    
    // Changed from After to GreaterThanEqual
    List<Event> findByDateGreaterThanEqual(LocalDate date);
    
    List<Event> findByDate(LocalDate date);
}