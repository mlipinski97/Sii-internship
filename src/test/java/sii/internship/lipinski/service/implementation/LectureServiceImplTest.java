package sii.internship.lipinski.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import sii.internship.lipinski.dao.dto.LectureDto;
import sii.internship.lipinski.dao.entity.Lecture;
import sii.internship.lipinski.dao.entity.LectureRegistration;
import sii.internship.lipinski.dao.entity.User;
import sii.internship.lipinski.repository.LectureRepository;
import sii.internship.lipinski.service.LectureRegistrationService;
import sii.internship.lipinski.service.LectureService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LectureServiceImplTest {

    private static final Logger logger = Logger.getLogger(UserServiceImplTest.class.getName());

    @Mock
    LectureRepository lectureRepository;
    @Mock
    LectureRegistrationService lectureRegistrationService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    ModelMapper modelMapper = new ModelMapper();
    //object under tests
    LectureService lectureService;

    @BeforeEach
    void setUp() {
        lectureService = new LectureServiceImpl(lectureRepository, lectureRegistrationService);
    }

    @Test
    @DisplayName("when called it returns all lectures")
    void whenCalled_thenItReturnsAllLectures() {
        //given
        Lecture testLecture = new Lecture();
        testLecture.setSubject("subject");
        testLecture.setEndingDateTime(LocalDateTime.parse("11:45 01-06-2022", formatter));
        testLecture.setStartingDateTime(LocalDateTime.parse("10:00 01-06-2022", formatter));
        testLecture.setNumberOfFreeSeats(5);
        Lecture testLecture2 = new Lecture();
        testLecture.setSubject("another subject");
        testLecture.setEndingDateTime(LocalDateTime.parse("11:45 01-06-2022", formatter));
        testLecture.setStartingDateTime(LocalDateTime.parse("14:00 01-06-2022", formatter));
        testLecture.setNumberOfFreeSeats(5);
        List<Lecture> expectedLectures = new ArrayList<>(Arrays.asList(testLecture, testLecture2));
        //when
        when(lectureRepository.findAll()).thenReturn(expectedLectures);
        //then
        List<Lecture> actualLectures = StreamSupport.stream(lectureService.getAll().spliterator(), false)
                .map(lectureDto -> modelMapper.map(lectureDto, Lecture.class))
                .collect(Collectors.toList());
        verify(lectureRepository).findAll();
        assertEquals(expectedLectures, actualLectures);
    }

    @Test
    @DisplayName("when given correct user login it returns all lecture that user is bound with")
    void whenGivenCorrectUserLogin_thenItReturnsAllLecturesBoundWithThatUser(){
        //given
        String testLogin = "testLogin";
        User expectedUser = new User();
        expectedUser.setLogin(testLogin);
        expectedUser.setEmail("testEmail@email.com");
        Lecture expectedLecture = new Lecture();
        expectedLecture.setSubject("subject");
        expectedLecture.setEndingDateTime(LocalDateTime.parse("11:45 01-06-2022", formatter));
        expectedLecture.setStartingDateTime(LocalDateTime.parse("10:00 01-06-2022", formatter));
        expectedLecture.setNumberOfFreeSeats(5);
        LectureRegistration testLectureRegistration = new LectureRegistration();
        testLectureRegistration.setLecture(expectedLecture);
        testLectureRegistration.setUser(expectedUser);
        List<LectureRegistration> expectedLectureRegistrations = new ArrayList<>(Collections.singletonList(testLectureRegistration));
        List<LectureDto> expectedLectures = new ArrayList<>(Collections.singletonList(modelMapper.map(expectedLecture, LectureDto.class)));
        //when
        when(lectureRegistrationService.getAllLectureRegistrationsByUserLogin(testLogin))
                .thenReturn(expectedLectureRegistrations);
        //then
        Iterable<LectureDto> actualLectures = lectureService.getAllByUserLogin(testLogin);
        assertEquals(expectedLectures, actualLectures);
    }

}
