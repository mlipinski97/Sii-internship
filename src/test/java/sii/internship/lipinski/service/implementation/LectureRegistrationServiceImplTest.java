package sii.internship.lipinski.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import sii.internship.lipinski.dao.dto.UserDto;
import sii.internship.lipinski.dao.entity.Lecture;
import sii.internship.lipinski.dao.entity.LectureRegistration;
import sii.internship.lipinski.dao.entity.User;
import sii.internship.lipinski.repository.LectureRegistrationRepository;
import sii.internship.lipinski.repository.LectureRepository;
import sii.internship.lipinski.repository.UserRepository;
import sii.internship.lipinski.service.LectureRegistrationService;
import sii.internship.lipinski.service.UserService;
import sii.internship.lipinski.util.exception.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureRegistrationServiceImplTest {

    @Mock
    LectureRegistrationRepository lectureRegistrationRepository;
    @Mock
    UserService userService;
    @Mock
    LectureRepository lectureRepository;
    @Mock
    UserRepository userRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    ModelMapper modelMapper = new ModelMapper();
    private static final Logger logger = Logger.getLogger(UserServiceImplTest.class.getName());

    //object under tests
    LectureRegistrationService lectureRegistrationService;

    @BeforeEach
    void setUp() {
        lectureRegistrationService = new LectureRegistrationServiceImpl(userService, lectureRepository, lectureRegistrationRepository, userRepository);
    }

    @Test
    @DisplayName("when given correct user login then it returns all lecture registrations bound to that user")
    void whenGivenCorrectUserLogin_thenItReturnsAllLectureRegistrationsWithThatUser() {
        //given
        String testLogin = "login";
        User testUser = new User();
        testUser.setLogin(testLogin);
        testUser.setEmail("testEmail@email.com");
        Lecture testLecture = new Lecture();
        testLecture.setSubject("subject");
        testLecture.setEndingDateTime(LocalDateTime.parse("11:45 01-06-2022", formatter));
        testLecture.setStartingDateTime(LocalDateTime.parse("10:00 01-06-2022", formatter));
        testLecture.setNumberOfFreeSeats(5);
        LectureRegistration testLectureRegistration = new LectureRegistration();
        testLectureRegistration.setLecture(testLecture);
        testLectureRegistration.setUser(testUser);
        List<LectureRegistration> expectedLectureRegistrations = new ArrayList<>(Collections.singletonList(testLectureRegistration));
        //when
        when(lectureRegistrationRepository.findAllByUserLogin(testLogin)).thenReturn(expectedLectureRegistrations);
        //then
        Iterable<LectureRegistration> actualLectureRegistrations = lectureRegistrationService
                .getAllLectureRegistrationsByUserLogin(testLogin);
        verify(lectureRegistrationRepository).findAllByUserLogin(testLogin);
        assertEquals(expectedLectureRegistrations, actualLectureRegistrations);
    }

    @Test
    @DisplayName("when given correct user credentials and lecture id it creates new lecture registration and creates mock email file")
    void whenGivenCorrectUserCredentialsAndLectureId_thenItCreatesLectureRegistrationAndMockEmailFile() {
        //given
        Path path = Paths.get("./src/main/resources/powiadomienia.txt");
        UserDto testUserDto = new UserDto();
        testUserDto.setEmail("test@email.com");
        testUserDto.setLogin("testlogin");
        testUserDto.setId(1L);
        User testUser = modelMapper.map(testUserDto, User.class);
        Lecture testLecture = new Lecture();
        testLecture.setSubject("subject");
        testLecture.setEndingDateTime(LocalDateTime.parse("11:45 01-06-2022", formatter));
        testLecture.setStartingDateTime(LocalDateTime.parse("10:00 01-06-2022", formatter));
        testLecture.setNumberOfFreeSeats(5);
        testLecture.setId(1L);
        LectureRegistration expectedLectureRegistration = new LectureRegistration();
        expectedLectureRegistration.setUser(testUser);
        expectedLectureRegistration.setLecture(testLecture);
        //when
        when(userRepository.findByLogin(testUser.getLogin())).thenReturn(Optional.of(testUser));
        when(lectureRepository.findById(testLecture.getId())).thenReturn(Optional.of(testLecture));
        //then
        try {
            lectureRegistrationService.createNewRegistration(testUserDto, testLecture.getId());
        } catch (NoFreeSeatsAvailableException | LectureSchedulesCollideException | LectureNotFoundException | LoginTakenException e) {
            logger.info("Problem occurred while testing creating new lecture reservation"
                    + "\n see more: " + Arrays.asList(e.getStackTrace()));
        }
        verify(lectureRegistrationRepository).save(expectedLectureRegistration);
        assertTrue(Files.isReadable(path));
    }

    @Test
    @DisplayName("when given correct user credentials and lecture id but overlapping lecture hours it throws LectureSchedulesCollideException")
    void whenGivenCorrectUserCredentialsAndLectureIdButOverlappingLectureHours_thenItThrowsLectureSchedulesCollideException() {
        //given
        UserDto testUserDto = new UserDto();
        testUserDto.setEmail("test@email.com");
        testUserDto.setLogin("testlogin");
        testUserDto.setId(1L);
        User testUser = modelMapper.map(testUserDto, User.class);
        Lecture testLecture = new Lecture();
        testLecture.setSubject("subject");
        testLecture.setEndingDateTime(LocalDateTime.parse("11:45 01-06-2022", formatter));
        testLecture.setStartingDateTime(LocalDateTime.parse("10:00 01-06-2022", formatter));
        testLecture.setNumberOfFreeSeats(5);
        testLecture.setId(1L);
        LectureRegistration expectedLectureRegistration = new LectureRegistration();
        expectedLectureRegistration.setUser(testUser);
        expectedLectureRegistration.setLecture(testLecture);
        List<LectureRegistration> testLectureRegistrations = new ArrayList<>(Collections.singleton(expectedLectureRegistration));
        //when
        when(userRepository.findByLogin(testUser.getLogin())).thenReturn(Optional.of(testUser));
        when(lectureRepository.findById(testLecture.getId())).thenReturn(Optional.of(testLecture));
        when(lectureRegistrationRepository.findAllByUserLogin(testUser.getLogin())).thenReturn(testLectureRegistrations);
        //then
        assertThrows(LectureSchedulesCollideException.class, () -> lectureRegistrationService.createNewRegistration(testUserDto, testLecture.getId()));
        verify(lectureRegistrationRepository, never()).save(any());
    }

    @Test
    @DisplayName("when given taken user credentials and correct lecture id it throws LoginTakenException")
    void whenGivenTakenUserCredentialsAndCorrectLectureId_thenItThrowsLoginTakenException() {
        //given
        UserDto testUserDto = new UserDto();
        testUserDto.setEmail("test@email.com");
        testUserDto.setLogin("testlogin");
        testUserDto.setId(1L);
        UserDto takenTestUserDto = new UserDto();
        takenTestUserDto.setEmail("anotherTest@email.com");
        takenTestUserDto.setLogin("testlogin");
        Lecture testLecture = new Lecture();
        testLecture.setSubject("subject");
        testLecture.setEndingDateTime(LocalDateTime.parse("11:45 01-06-2022", formatter));
        testLecture.setStartingDateTime(LocalDateTime.parse("10:00 01-06-2022", formatter));
        testLecture.setNumberOfFreeSeats(5);
        testLecture.setId(1L);
        //when
        try {
            when(userService.register(takenTestUserDto)).thenThrow(LoginTakenException.class);
        } catch (LoginTakenException e) {
            logger.info("Problem occurred while testing creating new lecture reservation"
                    + "\n see more: " + Arrays.asList(e.getStackTrace()));
        }
        //then
        assertThrows(LoginTakenException.class, () -> lectureRegistrationService.createNewRegistration(takenTestUserDto, testLecture.getId()));
        verify(lectureRegistrationRepository, never()).save(any());
    }

    @Test
    @DisplayName("when given user credentials and correct lecture id then it throws NoFreeSeatsAvailableException")
    void whenGivenUserCredentialsAndLectureIdWithoutFreeSeats_thenItThrowsNoFreeSeatsAvailableException() {
        //given
        UserDto testUserDto = new UserDto();
        testUserDto.setEmail("test@email.com");
        testUserDto.setLogin("testlogin");
        testUserDto.setId(1L);
        User testUser = modelMapper.map(testUserDto, User.class);
        Lecture testLecture = new Lecture();
        testLecture.setSubject("subject");
        testLecture.setEndingDateTime(LocalDateTime.parse("11:45 01-06-2022", formatter));
        testLecture.setStartingDateTime(LocalDateTime.parse("10:00 01-06-2022", formatter));
        testLecture.setNumberOfFreeSeats(0);
        testLecture.setId(1L);
        //when
        when(userRepository.findByLogin(testUserDto.getLogin())).thenReturn(Optional.of(testUser));
        when(lectureRepository.findById(testLecture.getId())).thenReturn(Optional.of(testLecture));
        //then
        assertThrows(NoFreeSeatsAvailableException.class, () -> lectureRegistrationService.createNewRegistration(testUserDto, testLecture.getId()));
        verify(lectureRegistrationRepository, never()).save(any());
    }

    @Test
    @DisplayName("when given user credentials and incorrect lecture id then it throws LectureNotFoundException")
    void whenGivenUserCredentialsAndIncorrectLectureId_thenItThrowsLectureNotFoundException() {
        //given
        UserDto testUserDto = new UserDto();
        testUserDto.setEmail("test@email.com");
        testUserDto.setLogin("testlogin");
        testUserDto.setId(1L);
        User testUser = modelMapper.map(testUserDto, User.class);
        Long lectureId = 0L;
        //when
        when(userRepository.findByLogin(testUserDto.getLogin())).thenReturn(Optional.of(testUser));
        //then
        assertThrows(LectureNotFoundException.class, () -> lectureRegistrationService.createNewRegistration(testUserDto, lectureId));
        verify(lectureRegistrationRepository, never()).save(any());
    }

    @Test
    @DisplayName("when given user login and lecture id then it removes lecture registration")
    void whenGivenUserLoginAndLectureId_thenItRemovesLectureRegistration() {
        //given
        User testUser = new User();
        testUser.setLogin("testLogin");
        testUser.setEmail("test@email.com");
        Lecture testLecture = new Lecture();
        testLecture.setNumberOfFreeSeats(4);
        testLecture.setId(1L);
        LectureRegistration expectedLectureRegistration = new LectureRegistration();
        expectedLectureRegistration.setLecture(testLecture);
        expectedLectureRegistration.setUser(testUser);
        //when
        when(lectureRegistrationRepository.findByUserLoginAndLectureId(testUser.getLogin(), testLecture.getId()))
                .thenReturn(Optional.of(expectedLectureRegistration));
        when(lectureRepository.findById(testLecture.getId())).thenReturn(Optional.of(testLecture));
        //then
        try {
            lectureRegistrationService.cancelRegistration(testUser.getLogin(), testLecture.getId());
        } catch (LectureRegistrationNotFoundException | LectureNotFoundException e) {
            logger.info("Problem occurred while testing canceling lecture reservation"
                    + "\n see more: " + Arrays.asList(e.getStackTrace()));
        }
        verify(lectureRegistrationRepository).delete(expectedLectureRegistration);
        assertEquals(5, testLecture.getNumberOfFreeSeats());
    }

    @Test
    @DisplayName("when given wrong user login or wrong lecture id then it throws LectureRegistrationNotFoundException")
    void whenGivenWrongUserLoginOrWrongLectureId_thenItThrowsLectureRegistrationNotFoundException() {
        //given
        User testUser = new User();
        testUser.setLogin("testLogin");
        testUser.setEmail("test@email.com");
        Lecture testLecture = new Lecture();
        testLecture.setNumberOfFreeSeats(4);
        testLecture.setId(1L);
        LectureRegistration expectedLectureRegistration = new LectureRegistration();
        expectedLectureRegistration.setLecture(testLecture);
        expectedLectureRegistration.setUser(testUser);
        //when
        //then
        assertThrows(LectureRegistrationNotFoundException.class, () -> lectureRegistrationService.cancelRegistration(testUser.getLogin(), testLecture.getId()));
        verify(lectureRegistrationRepository, never()).delete(any());
        assertEquals(4, testLecture.getNumberOfFreeSeats());
    }
}
