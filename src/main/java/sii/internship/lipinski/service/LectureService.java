package sii.internship.lipinski.service;

import sii.internship.lipinski.dao.dto.LectureDto;

import java.util.Map;


public interface LectureService {
    Iterable<LectureDto> getAll();

    Iterable<LectureDto> getAllByUserLogin(String login);

    Map<LectureDto, Double> getParticipationPercentagePerLecture();

    Map<String, Double> getParticipationPercentagePerSubject();
}
