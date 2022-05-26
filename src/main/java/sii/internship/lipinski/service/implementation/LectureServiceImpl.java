package sii.internship.lipinski.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sii.internship.lipinski.dao.dto.LectureDto;
import sii.internship.lipinski.repository.LectureRepository;
import sii.internship.lipinski.service.LectureRegistrationService;
import sii.internship.lipinski.service.LectureService;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final ModelMapper modelMapper;
    private final LectureRegistrationService lectureRegistrationService;

    public LectureServiceImpl(LectureRepository lectureRepository, LectureRegistrationService lectureRegistrationService) {
        this.lectureRepository = lectureRepository;
        this.lectureRegistrationService = lectureRegistrationService;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public Iterable<LectureDto> getAll() {
        return lectureRepository.findAll().stream()
                .map(lecture -> modelMapper.map(lecture, LectureDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<LectureDto> getAllByUserLogin(String login) {
        return StreamSupport
                .stream(lectureRegistrationService.getAllLectureRegistrationsByUserLogin(login).spliterator(), false)
                .map(lectureRegistration -> modelMapper.map(lectureRegistration.getLecture(), LectureDto.class))
                .collect(Collectors.toList());
    }


}
