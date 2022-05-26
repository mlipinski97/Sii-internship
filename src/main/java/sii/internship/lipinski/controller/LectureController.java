package sii.internship.lipinski.controller;

import org.springframework.web.bind.annotation.*;
import sii.internship.lipinski.dao.dto.LectureDto;
import sii.internship.lipinski.service.LectureService;

@RestController
@RequestMapping("/api/lecture")
public class LectureController {
    private final LectureService lectureService;


    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping
    public Iterable<LectureDto> getConventionPlan() {
        return lectureService.getAll();
    }

    @GetMapping("/getRegisteredLectures/{login}")
    public Iterable<LectureDto> getAllRegisteredLecturesByUserLogin(@PathVariable String login) {
        return lectureService.getAllByUserLogin(login);
    }
}
