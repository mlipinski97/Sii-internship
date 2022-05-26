package sii.internship.lipinski.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "lecture_registrations")
public class LectureRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Lecture lecture;
}
