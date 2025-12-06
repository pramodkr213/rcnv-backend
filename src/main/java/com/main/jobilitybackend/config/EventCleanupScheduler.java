package com.main.jobilitybackend.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.main.jobilitybackend.service.EventService;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class EventCleanupScheduler {

    @Autowired
    private EventService eventService;

    // Runs every day at 00:01 AM
    @Scheduled(cron = "0 1 0 * * ?")
    public void deleteTodaysEvents() {
        eventService.deleteTodayEvents();
    }
}
