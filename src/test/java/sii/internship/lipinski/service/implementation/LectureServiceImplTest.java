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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LectureServiceImplTest {

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
    void whenGivenCorrectUserLogin_thenItReturnsAllLecturesBoundWithThatUser() {
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

    @Test
    @DisplayName("when called it returns participation percentage per lecture as Map")
    void whenCalled_thenItReturnsParticipationPercentagePerLecture() {
        //given
        Lecture expectedLecture1 = new Lecture();
        expectedLecture1.setSubject("subject 1");
        expectedLecture1.setEndingDateTime(LocalDateTime.parse("11:45 01-06-2022", formatter));
        expectedLecture1.setStartingDateTime(LocalDateTime.parse("10:00 01-06-2022", formatter));
        expectedLecture1.setNumberOfFreeSeats(3);
        expectedLecture1.setMaxNumberOfSeats(5);
        Lecture expectedLecture2 = new Lecture();
        expectedLecture2.setSubject("subject 2");
        expectedLecture2.setEndingDateTime(LocalDateTime.parse("11:45 01-06-2022", formatter));
        expectedLecture2.setStartingDateTime(LocalDateTime.parse("10:00 01-06-2022", formatter));
        expectedLecture2.setNumberOfFreeSeats(4);
        expectedLecture2.setMaxNumberOfSeats(5);
        Lecture expectedLecture3 = new Lecture();
        expectedLecture3.setSubject("subject 3");
        expectedLecture3.setEndingDateTime(LocalDateTime.parse("11:45 01-06-2022", formatter));
        expectedLecture3.setStartingDateTime(LocalDateTime.parse("10:00 01-06-2022", formatter));
        expectedLecture3.setNumberOfFreeSeats(1);
        expectedLecture3.setMaxNumberOfSeats(5);
        List<Lecture> expectedLectures = new ArrayList<>(Arrays.asList(expectedLecture1,
                expectedLecture2, expectedLecture3));
        Map<LectureDto, Double> expectedPercentageMap = new LinkedHashMap<>();
        Double expectedPercentageLecture1 = 100 * (double) (expectedLecture1.getMaxNumberOfSeats() - expectedLecture1.getNumberOfFreeSeats()) / (expectedLecture1.getMaxNumberOfSeats());
        Double expectedPercentageLecture2 = 100 * (double) (expectedLecture2.getMaxNumberOfSeats() - expectedLecture2.getNumberOfFreeSeats()) / (expectedLecture2.getMaxNumberOfSeats());
        Double expectedPercentageLecture3 = 100 * (double) (expectedLecture3.getMaxNumberOfSeats() - expectedLecture3.getNumberOfFreeSeats()) / (expectedLecture3.getMaxNumberOfSeats());
        expectedPercentageMap.put(modelMapper.map(expectedLecture3, LectureDto.class), expectedPercentageLecture3);
        expectedPercentageMap.put(modelMapper.map(expectedLecture1, LectureDto.class), expectedPercentageLecture1);
        expectedPercentageMap.put(modelMapper.map(expectedLecture2, LectureDto.class), expectedPercentageLecture2);
        //when
        when(lectureRepository.findAll()).thenReturn(expectedLectures);
        //then
        Map<LectureDto, Double> actualPercentageMap = lectureService.getParticipationPercentagePerLecture();
        assertEquals(expectedPercentageMap, actualPercentageMap);
    }

    @Test
    @DisplayName("when called then it returns participation percentage per lecture subject")
    void whenCalled_thenItReturnsParticipationPercentagePerSubject() {
        //given
        Lecture expectedLecture1 = new Lecture();
        expectedLecture1.setSubject("subject 1");
        expectedLecture1.setEndingDateTime(LocalDateTime.parse("11:45 01-06-2022", formatter));
        expectedLecture1.setStartingDateTime(LocalDateTime.parse("10:00 01-06-2022", formatter));
        expectedLecture1.setNumberOfFreeSeats(3);
        expectedLecture1.setMaxNumberOfSeats(5);
        Lecture expectedLecture2 = new Lecture();
        expectedLecture2.setSubject("subject 2");
        expectedLecture2.setEndingDateTime(LocalDateTime.parse("11:45 01-06-2022", formatter));
        expectedLecture2.setStartingDateTime(LocalDateTime.parse("10:00 01-06-2022", formatter));
        expectedLecture2.setNumberOfFreeSeats(4);
        expectedLecture2.setMaxNumberOfSeats(5);
        Lecture expectedLecture3 = new Lecture();
        expectedLecture3.setSubject("subject 3");
        expectedLecture3.setEndingDateTime(LocalDateTime.parse("11:45 01-06-2022", formatter));
        expectedLecture3.setStartingDateTime(LocalDateTime.parse("10:00 01-06-2022", formatter));
        expectedLecture3.setNumberOfFreeSeats(5);
        expectedLecture3.setMaxNumberOfSeats(5);
        Lecture expectedLecture4 = new Lecture();
        expectedLecture4.setSubject("subject 1");
        expectedLecture4.setEndingDateTime(LocalDateTime.parse("11:45 01-06-2022", formatter));
        expectedLecture4.setStartingDateTime(LocalDateTime.parse("10:00 01-06-2022", formatter));
        expectedLecture4.setNumberOfFreeSeats(2);
        expectedLecture4.setMaxNumberOfSeats(5);
        List<Lecture> expectedLectures = new ArrayList<>(Arrays.asList(expectedLecture1,
                expectedLecture2, expectedLecture3, expectedLecture4));
        Map<String, Double> expectedPercentageMap = new LinkedHashMap<>();
        Double expectedPercentageSubject1 = 100 * (double) (expectedLecture1.getMaxNumberOfSeats() - expectedLecture1.getNumberOfFreeSeats() + expectedLecture4.getMaxNumberOfSeats() - expectedLecture4.getNumberOfFreeSeats()) / (expectedLecture1.getMaxNumberOfSeats() + expectedLecture4.getMaxNumberOfSeats());
        Double expectedPercentageSubject2 = 100 * (double) (expectedLecture2.getMaxNumberOfSeats() - expectedLecture2.getNumberOfFreeSeats()) / (expectedLecture2.getMaxNumberOfSeats());
        Double expectedPercentageSubject3 = 100 * (double) (expectedLecture3.getMaxNumberOfSeats() - expectedLecture3.getNumberOfFreeSeats()) / (expectedLecture3.getMaxNumberOfSeats());
        expectedPercentageMap.put(expectedLecture1.getSubject(), expectedPercentageSubject1);
        expectedPercentageMap.put(expectedLecture2.getSubject(), expectedPercentageSubject2);
        expectedPercentageMap.put(expectedLecture3.getSubject(), expectedPercentageSubject3);
        //when
        when(lectureRepository.findAll()).thenReturn(expectedLectures);
        //then
        Map<String, Double> actualPercentageMap = lectureService.getParticipationPercentagePerSubject();
        assertEquals(expectedPercentageMap, actualPercentageMap);
    }


}
