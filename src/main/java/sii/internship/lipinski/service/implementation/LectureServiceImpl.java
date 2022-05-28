package sii.internship.lipinski.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sii.internship.lipinski.dao.dto.LectureDto;
import sii.internship.lipinski.dao.entity.Lecture;
import sii.internship.lipinski.repository.LectureRepository;
import sii.internship.lipinski.service.LectureRegistrationService;
import sii.internship.lipinski.service.LectureService;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public Map<LectureDto, Double> getParticipationPercentagePerLecture() {
        Map<LectureDto, Double> sortedPercentageMap = new LinkedHashMap<>();
        lectureRepository.findAll()
                .stream()
                .collect(Collectors.toMap(lecture -> modelMapper.map(lecture, LectureDto.class),
                        lecture -> 100 * ((lecture.getMaxNumberOfSeats() - (double) lecture.getNumberOfFreeSeats()) / lecture.getMaxNumberOfSeats())))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(entry -> sortedPercentageMap.put(entry.getKey(), entry.getValue()));
        return sortedPercentageMap;
    }

    @Override
    public Map<String, Double> getParticipationPercentagePerSubject() {
        Map<String, Double> sortedPercentageMap = new LinkedHashMap<>();
        List<Lecture> allLectures = lectureRepository.findAll();
        Map<String, Long> subjectMaxSeatsMap = allLectures.stream()
                .collect(Collectors.groupingBy(Lecture::getSubject, Collectors.summingLong(Lecture::getMaxNumberOfSeats)));
        allLectures.stream()
                .collect(Collectors.toMap(Lecture::getSubject, lecture -> (double) (lecture.getMaxNumberOfSeats() - lecture.getNumberOfFreeSeats()), Double::sum))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(entry -> sortedPercentageMap.put(entry.getKey(),
                        100 * entry.getValue() / subjectMaxSeatsMap.get(entry.getKey())));
        return sortedPercentageMap;
    }


}
