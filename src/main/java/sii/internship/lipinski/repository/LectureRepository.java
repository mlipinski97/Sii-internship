package sii.internship.lipinski.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sii.internship.lipinski.dao.entity.Lecture;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
