package sii.internship.lipinski.dao.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LectureDto {
    private Long id;
    @JsonFormat(pattern = "HH:mm dd-MM-yyyy")
    private LocalDateTime startingDateTime;
    @JsonFormat(pattern = "HH:mm dd-MM-yyyy")
    private LocalDateTime endingDateTime;
    private String subject;
    private Integer numberOfFreeSeats;
}
