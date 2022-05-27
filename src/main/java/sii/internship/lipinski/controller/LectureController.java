package sii.internship.lipinski.controller;

import org.springframework.web.bind.annotation.*;
import sii.internship.lipinski.dao.dto.LectureDto;
import sii.internship.lipinski.dao.dto.UserDto;
import sii.internship.lipinski.service.LectureRegistrationService;
import sii.internship.lipinski.service.LectureService;
import sii.internship.lipinski.util.exception.*;

import java.util.Map;

@RestController
@RequestMapping("/api/lecture")
public class LectureController {
    private final LectureService lectureService;
    private final LectureRegistrationService lectureRegistrationService;


    public LectureController(LectureService lectureService, LectureRegistrationService lectureRegistrationService) {
        this.lectureService = lectureService;
        this.lectureRegistrationService = lectureRegistrationService;
    }

    @GetMapping
    public Iterable<LectureDto> getConventionPlan() {
        return lectureService.getAll();
    }

    @GetMapping("/getRegisteredLectures/{login}")
    public Iterable<LectureDto> getAllRegisteredLecturesByUserLogin(@PathVariable String login) {
        return lectureService.getAllByUserLogin(login);
    }

    @PostMapping("/{lectureId}")
    public void registerForLecture(@PathVariable Long lectureId, @RequestBody UserDto userDto) throws LectureNotFoundException, LectureSchedulesCollideException, NoFreeSeatsAvailableException, LoginTakenException {
        lectureRegistrationService.createNewRegistration(userDto, lectureId);
    }

    @DeleteMapping("/{userLogin}/{lectureId}")
    public void cancelLectureRegistration(@PathVariable String userLogin, @PathVariable Long lectureId) throws LectureRegistrationNotFoundException, LectureNotFoundException {
        lectureRegistrationService.cancelRegistration(userLogin, lectureId);
    }

    @GetMapping("/getParticipationPercentagePerLectureByAllParticipants")
    public Map<LectureDto, Double> getConventionSummary(){
       return lectureRegistrationService.getParticipationPercentagePerLectureByAllParticipants();
    }

    @GetMapping("/getParticipationPercentagePerLecture")
    Map<LectureDto, Double> getParticipationPercentagePerLecture(){
        return lectureService.getParticipationPercentagePerLecture();
    }

    @GetMapping("/getParticipationPercentagePerSubject")
    Map<String, Double> getParticipationPercentagePerSubject(){
        return lectureService.getParticipationPercentagePerSubject();
    }

}
