package sii.internship.lipinski.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sii.internship.lipinski.dao.dto.LectureDto;
import sii.internship.lipinski.dao.dto.UserDto;
import sii.internship.lipinski.service.LectureRegistrationService;
import sii.internship.lipinski.service.LectureService;
import sii.internship.lipinski.util.exception.*;

import java.util.Map;

@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
public class LectureController {
    private final LectureService lectureService;
    private final LectureRegistrationService lectureRegistrationService;


    @GetMapping(value = {"", "/{pageNumber}/{pageSize}"})
    public Iterable<LectureDto> getConventionPlan(@PathVariable(required = false) Integer pageNumber, @PathVariable(required = false) Integer pageSize) {
        return lectureService.getAll(pageNumber, pageSize);
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

    @GetMapping("/getParticipationPercentagePerLecture")
    Map<LectureDto, Double> getParticipationPercentagePerLecture() {
        return lectureService.getParticipationPercentagePerLecture();
    }

    @GetMapping("/getParticipationPercentagePerSubject")
    Map<String, Double> getParticipationPercentagePerSubject() {
        return lectureService.getParticipationPercentagePerSubject();
    }

}
