package sii.internship.lipinski.service;

import sii.internship.lipinski.dao.dto.UserDto;
import sii.internship.lipinski.dao.entity.LectureRegistration;
import sii.internship.lipinski.util.exception.*;

public interface LectureRegistrationService {
    Iterable<LectureRegistration> getAllLectureRegistrationsByUserLogin(String login);

    void createNewRegistration(UserDto userDto, Long lectureId) throws LoginTakenException, LectureNotFoundException, NoFreeSeatsAvailableException, LectureSchedulesCollideException;

    void cancelRegistration(String userLogin, Long lectureId) throws LectureRegistrationNotFoundException, LectureNotFoundException;
}
