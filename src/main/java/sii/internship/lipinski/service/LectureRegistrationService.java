package sii.internship.lipinski.service;

import sii.internship.lipinski.dao.entity.LectureRegistration;

public interface LectureRegistrationService {
    Iterable<LectureRegistration> getAllLectureRegistrationsByUserLogin(String login);
}
