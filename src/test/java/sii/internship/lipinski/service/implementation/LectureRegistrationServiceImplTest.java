package sii.internship.lipinski.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sii.internship.lipinski.dao.entity.Lecture;
import sii.internship.lipinski.dao.entity.LectureRegistration;
import sii.internship.lipinski.dao.entity.User;
import sii.internship.lipinski.repository.LectureRegistrationRepository;
import sii.internship.lipinski.service.LectureRegistrationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LectureRegistrationServiceImplTest {

    @Mock
    LectureRegistrationRepository lectureRegistrationRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    //object under tests
    LectureRegistrationService lectureRegistrationService;

    @BeforeEach
    void setUp() {
        lectureRegistrationService = new LectureRegistrationServiceImpl(lectureRegistrationRepository);
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

}
