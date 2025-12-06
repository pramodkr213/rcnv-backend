package com.main.jobilitybackend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.main.jobilitybackend.dto.requestDTO.UpdateEventRequest;
import com.main.jobilitybackend.entities.Event;

public interface EventService {

	Object saveEvent(Event event);

	Page<Event> getUpcomingEvents(Integer page, Integer size);

	void deleteTodayEvents();

	ResponseEntity<?> deleteEventById(Long id);

	Event updateEvent(Long id, UpdateEventRequest request);

	Event updateEvent(Long id, Event eventDetails);

	List<Event> getAllUpcomingEvents();

	ResponseEntity<?> findById(Long id);


	
	
	

}
