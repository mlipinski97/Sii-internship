package sii.internship.lipinski.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sii.internship.lipinski.dao.entity.LectureRegistration;

@Repository
public interface LectureRegistrationRepository extends JpaRepository<LectureRegistration, Long> {
    Iterable<LectureRegistration> findAllByUserLogin(String login);

}
