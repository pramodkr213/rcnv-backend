package com.main.jobilitybackend.dto.requestDTO.rcnv;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GalleryRequest {
    private String title;
    private String detail;
    private Long catid;
}
