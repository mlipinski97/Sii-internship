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
import sii.internship.lipinski.service.UserService;
import sii.internship.lipinski.util.enums.ErrorCode;
import sii.internship.lipinski.util.enums.ErrorMessage;
import sii.internship.lipinski.util.exception.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void createNewRegistration(UserDto userDto, Long lectureId) throws LoginTakenException, LectureNotFoundException, NoFreeSeatsAvailableException, LectureSchedulesCollideException {
        userService.register(userDto);
        User user = userRepository.findByLogin(userDto.getLogin()).get();
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(
                () -> new LectureNotFoundException(ErrorMessage.LECTURE_NOT_FOUND.getMessage(),
                        ErrorCode.LECTURE_NOT_FOUND.getValue())
        );
        if (lecture.getNumberOfFreeSeats() < 1) {
            throw new NoFreeSeatsAvailableException(ErrorMessage.NO_SEATS_AVAILABLE.getMessage(),
                    ErrorCode.NO_SEATS_AVAILABLE.getValue());
        }
        if (isCollidingWithOtherRegisteredLecture(userDto.getLogin(), lecture.getStartingDateTime())) {
            throw new LectureSchedulesCollideException(ErrorMessage.LECTURE_SCHEDULES_COLLIDE.getMessage(),
                    ErrorCode.LECTURE_SCHEDULES_COLLIDE.getValue());
        }
        LectureRegistration newLectureRegistration = new LectureRegistration();
        newLectureRegistration.setUser(user);
        newLectureRegistration.setLecture(lecture);
        lectureRegistrationRepository.save(newLectureRegistration);
        lecture.setNumberOfFreeSeats(lecture.getNumberOfFreeSeats() - 1);
        lectureRepository.save(lecture);
        createEmailFile(user.getEmail(), lecture);
    }

    @Override
    public void cancelRegistration(String userLogin, Long lectureId) throws LectureRegistrationNotFoundException, LectureNotFoundException {
        LectureRegistration lectureRegistration = lectureRegistrationRepository
                .findByUserLoginAndLectureId(userLogin, lectureId)
                .orElseThrow(() -> new LectureRegistrationNotFoundException(ErrorMessage.LECTURE_REGISTRATION_NOT_FOUND.getMessage(),
                        ErrorCode.LECTURE_REGISTRATION_NOT_FOUND.getValue()));
        Lecture lectureToUpdate = lectureRepository.findById(lectureId).orElseThrow(() -> new LectureNotFoundException(
                ErrorMessage.LECTURE_NOT_FOUND.getMessage(), ErrorCode.LECTURE_NOT_FOUND.getValue()
        ));
        lectureRegistrationRepository.delete(lectureRegistration);
        lectureToUpdate.setNumberOfFreeSeats(lectureToUpdate.getNumberOfFreeSeats() + 1);
        lectureRepository.save(lectureToUpdate);
    }

    @Override
    public Map<LectureDto, Double> getParticipationPercentagePerLectureByAllParticipants() {
        List<LectureRegistration> lectureRegistrations = lectureRegistrationRepository.findAll();
        Map<Lecture, Long> frequencyMap = lectureRegistrations.stream()
                .collect(Collectors.groupingBy(LectureRegistration::getLecture, Collectors.counting()));
        return frequencyMap.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> modelMapper.map(e.getKey(), LectureDto.class), e -> (e.getValue().doubleValue() / lectureRegistrations.size()) * 100));
    }


    private boolean isCollidingWithOtherRegisteredLecture(String userLogin, LocalDateTime lectureTime) {
        Iterable<LectureRegistration> registeredLectures = lectureRegistrationRepository.findAllByUserLogin(userLogin);
        for (LectureRegistration lr : registeredLectures) {
            if (lectureTime.isEqual(lr.getLecture().getStartingDateTime())) {
                return true;
            }
        }
        return false;
    }

    private void createEmailFile(String userEmail, Lecture lecture) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        Path path = Paths.get("./src/main/resources/powiadomienia.txt");
        String message = "Do: " + userEmail + "\n" +
                "Zapisano Cie na prelekcje numer: " + lecture.getId() +
                " o tematyce: " + lecture.getSubject() +
                "\n" + "Prelekcja odbedzie sie w terminie: " +
                lecture.getStartingDateTime().format(formatter) +
                " - " + lecture.getEndingDateTime().format(formatter);
        byte[] strToBytes = message.getBytes();

        try {
            Files.write(path, strToBytes);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
