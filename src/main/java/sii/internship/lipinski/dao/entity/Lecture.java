package sii.internship.lipinski.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "lectures")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "HH:mm dd-MM-yyyy")
    @Column(nullable = false, name = "starting_date")
    private LocalDateTime startingDateTime;

    @JsonFormat(pattern = "HH:mm dd-MM-yyyy")
    @Column(nullable = false, name = "ending_time")
    private LocalDateTime endingDateTime;

    @Column(nullable = false, name = "subject")
    private String subject;

    @Column(name = "number_of_free_seats")
    private Integer numberOfFreeSeats;

    @Column(name = "max_number_of_seats")
    private Integer maxNumberOfSeats;
}
