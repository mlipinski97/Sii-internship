package sii.internship.lipinski.service;

import sii.internship.lipinski.dao.dto.LectureDto;
import sii.internship.lipinski.dao.dto.UserDto;
import sii.internship.lipinski.dao.entity.LectureRegistration;
import sii.internship.lipinski.util.exception.LectureNotFoundException;
import sii.internship.lipinski.util.exception.LoginTakenException;
import sii.internship.lipinski.util.exception.NoFreeSeatsAvailableException;

public interface LectureRegistrationService {
    Iterable<LectureRegistration> getAllLectureRegistrationsByUserLogin(String login);
    void createNewRegistration(UserDto userDto, LectureDto lectureDto) throws LoginTakenException, LectureNotFoundException, NoFreeSeatsAvailableException;
}
