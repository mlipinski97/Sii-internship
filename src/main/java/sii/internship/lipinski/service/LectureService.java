package sii.internship.lipinski.service;

import sii.internship.lipinski.dao.dto.LectureDto;


public interface LectureService {
    Iterable<LectureDto> getAll();

    Iterable<LectureDto> getAllByUserLogin(String login);
}
