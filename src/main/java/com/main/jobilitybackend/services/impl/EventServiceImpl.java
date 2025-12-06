package com.main.jobilitybackend.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.main.jobilitybackend.dto.requestDTO.UpdateEventRequest;
import com.main.jobilitybackend.entities.Event;
import com.main.jobilitybackend.repositories.EventRepository;
import com.main.jobilitybackend.service.EventService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

   
    public void deleteTodayEvents() {
        LocalDate today = LocalDate.now();
        List<Event> todaysEvents = eventRepository.findByDate(today);
        eventRepository.deleteAll(todaysEvents);
    }
    
    
    public ResponseEntity<?> deleteEventById(Long id) {
    	
    Optional<Event>	e = eventRepository.findById(id);
    
    if(e.isEmpty()) {
    	return ResponseEntity.badRequest().body("Event Not Found");
    }
        eventRepository.deleteById(id);
		return ResponseEntity.ok("Event Deleted..");
    }
    
    
    public Event updateEvent(Long id, UpdateEventRequest request) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (!optionalEvent.isPresent()) {
            throw new RuntimeException("Event not found with ID: " + id);
        }

        Event event = optionalEvent.get();
        event.setTitle(request.getTitle());
        event.setDate(request.getDate());

        return eventRepository.save(event);
    }

	@Override
	public Event updateEvent(Long id, Event eventDetails) {
	    Event event = eventRepository.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
	    
	    // Update only the fields you want to allow updating
	    event.setTitle(eventDetails.getTitle());
	    event.setDescription(eventDetails.getDescription());
	    event.setDate(eventDetails.getDate());
	    
	    return eventRepository.save(event);
	}
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND)  // Removed the accidental backtick
	public class ResourceNotFoundException extends RuntimeException {
	    public ResourceNotFoundException(String message) {
	        super(message);
	    }
	}

	@Override
	public Page<Event> getUpcomingEvents(Integer page, Integer size) {
	    LocalDate today = LocalDate.now(ZoneId.of("UTC")); // Explicit timezone
	    Pageable pageable = PageRequest.of(page, size, Sort.by("date").ascending());
	    return eventRepository.findByDateGreaterThanEqual(today, pageable);
	}

	@Override
	public List<Event> getAllUpcomingEvents() {
	    LocalDate today = LocalDate.now(ZoneId.of("UTC")); // Explicit timezone
	    return eventRepository.findByDateGreaterThanEqual(today);
	}


	@Override
	public ResponseEntity<?> findById(Long id) {
		// TODO Auto-generated method stub
		
	Optional<Event> e =	eventRepository.findById(id);
		
	if(e.isEmpty()) {
		return ResponseEntity.notFound().build();
	}
	
		return ResponseEntity.ok(e);
	}

	

}

