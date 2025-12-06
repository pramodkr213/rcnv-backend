package com.main.jobilitybackend.dto.responses.internshipResponses;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface InternshipResponse {
    String getId();
    String getTitle();
    String getDuration();
    boolean getIsPaid();
    String getMinStipend();
    String getMaxStipend();
    String getMode();
    String getLocation();
    String getInternshipType();
    int getNumberOfOpenings();
    boolean getIsImmediate();
    LocalDate getJoinFrom();
    LocalDate getJoinTo();
    LocalDate getApplicationDeadline();
    String getPostedBy();
    boolean getIsActive();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    LocalDateTime getDeleteAt();
}
