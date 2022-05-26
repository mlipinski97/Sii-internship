package sii.internship.lipinski.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sii.internship.lipinski.dao.entity.LectureRegistration;
import sii.internship.lipinski.repository.LectureRegistrationRepository;
import sii.internship.lipinski.service.LectureRegistrationService;

@Service
public class LectureRegistrationServiceImpl implements LectureRegistrationService {

    LectureRegistrationRepository lectureRegistrationRepository;
    ModelMapper modelMapper;

    public LectureRegistrationServiceImpl(LectureRegistrationRepository lectureRegistrationRepository) {
        this.lectureRegistrationRepository = lectureRegistrationRepository;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public Iterable<LectureRegistration> getAllLectureRegistrationsByUserLogin(String login) {
        return lectureRegistrationRepository.findAllByUserLogin(login);
    }
}
