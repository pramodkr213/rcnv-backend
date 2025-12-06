package com.main.jobilitybackend.dto.responseDTO;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import com.main.jobilitybackend.entities.Media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DataResponse {
  
	private boolean success;
    private HttpStatus status;
    private int statusCode;
    private String message;
    private long totalCount;
    private Instant timestamp;
    private Object data;

    private List<Map<String, Object>> studentData; 
    private List<Map<String, String>> employeeData; 
    private Map<String, Object> meta; 
    public DataResponse(boolean success2, String string, int size, Object message2, Object object, Object timestamp2,
			Optional<Media> media, List<Media> mediaList, Map<String, Object> pagination) {
		// TODO Auto-generated constructor stub
	}
}