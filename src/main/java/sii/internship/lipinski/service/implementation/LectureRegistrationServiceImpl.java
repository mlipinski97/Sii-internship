package sii.internship.lipinski.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sii.internship.lipinski.dao.dto.LectureDto;
import sii.internship.lipinski.dao.dto.UserDto;
import sii.internship.lipinski.dao.entity.Lecture;
import sii.internship.lipinski.dao.entity.LectureRegistration;
import sii.internship.lipinski.dao.entity.User;
import sii.internship.lipinski.repository.LectureRegistrationRepository;
import sii.internship.lipinski.repository.LectureRepository;
import sii.internship.lipinski.repository.UserRepository;
import sii.internship.lipinski.service.LectureRegistrationService;
import sii.internship.lipinski.service.LectureService;
import sii.internship.lipinski.service.UserService;
import sii.internship.lipinski.util.enums.ErrorCode;
import sii.internship.lipinski.util.enums.ErrorMessage;
import sii.internship.lipinski.util.exception.LectureNotFoundException;
import sii.internship.lipinski.util.exception.LoginTakenException;
import sii.internship.lipinski.util.exception.NoFreeSeatsAvailableException;

import java.util.Optional;

import static sii.internship.lipinski.util.enums.ErrorCode.LECTURE_NOT_FOUND;
import static sii.internship.lipinski.util.enums.ErrorMessage.*;
import static sii.internship.lipinski.util.enums.ErrorCode.*;

@Service
public class LectureRegistrationServiceImpl implements LectureRegistrationService {

    UserService userService;
    LectureRepository lectureRepository;
    LectureRegistrationRepository lectureRegistrationRepository;
    UserRepository userRepository;
    ModelMapper modelMapper;

    public LectureRegistrationServiceImpl(LectureRegistrationRepository lectureRegistrationRepository,
                                          UserService userService,
                                          LectureRepository lectureRepository,
                                          UserRepository userRepository) {
        this.lectureRegistrationRepository = lectureRegistrationRepository;
        this.modelMapper = new ModelMapper();
        this.lectureRepository = lectureRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public Iterable<LectureRegistration> getAllLectureRegistrationsByUserLogin(String login) {
        return lectureRegistrationRepository.findAllByUserLogin(login);
    }

    @Override
    public void createNewRegistration(UserDto userDto, LectureDto lectureDto) throws LoginTakenException, LectureNotFoundException, NoFreeSeatsAvailableException {
        userService.register(userDto);
        User user = userRepository.findByLogin(userDto.getLogin()).get();
        Lecture lecture = lectureRepository.findById(lectureDto.getId()).orElseThrow(
                () -> new LectureNotFoundException(ErrorMessage.LECTURE_NOT_FOUND.getMessage(),
                        ErrorCode.LOGIN_ALREADY_TAKEN.getValue())
        );
        if(lecture.getNumberOfFreeSeats() < 1){
            throw new NoFreeSeatsAvailableException(ErrorMessage.NO_SEATS_AVAILABLE.getMessage(),
                    ErrorCode.NO_SEATS_AVAILABLE.getValue());
        }
        //sprawdzenie czy uzytkownik jest na innej prelekcji o tej godzinie
        //stworzenie prelekcji
        //"wyslanie" maila - utworzenie pliku txt
    }
}
