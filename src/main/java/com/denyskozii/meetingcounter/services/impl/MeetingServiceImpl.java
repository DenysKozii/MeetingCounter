package com.denyskozii.meetingcounter.services.impl;

import lombok.extern.slf4j.Slf4j;
import com.denyskozii.meetingcounter.dto.MeetingDto;
import com.denyskozii.meetingcounter.model.Meeting;
import com.denyskozii.meetingcounter.repository.MeetingRepository;
import com.denyskozii.meetingcounter.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MeetingServiceImpl implements MeetingService {

    private MeetingRepository meetingRepository;

    @Autowired
    private Validator validator;


    @Autowired
    public MeetingServiceImpl(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    @Override
    public List<MeetingDto> getAll() {
        return meetingRepository.findAll()
                .stream()
                .map(mapToMeetingDto)
                .collect(Collectors.toList());
    }

    @Override
    public MeetingDto getMeetingById(Long id) {
        return mapToMeetingDto.apply(meetingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Marathon with id " + id + " doesn't exists!")));
    }

    @Override
    public MeetingDto createOrUpdateMeeting(MeetingDto meetingDto) {
        Meeting meeting = meetingRepository.findByTitle(meetingDto.getTitle());


        if (meeting == null) {
            meeting = new Meeting(meetingDto.getTitle(),
                    meetingDto.getDescription(),
                    meetingDto.getHereAmount(),
                    meetingDto.getLongitude(),
                    meetingDto.getLatitude(),
                    meetingDto.getAvailableDistance());
            if (validator.validate(meeting).size() == 0) {
//                return mapToMeetingDto.apply(meetingRepository.save(meeting));
                meetingRepository.save(meeting);
                return mapToMeetingDto.apply(meeting);
            }
        }
        meetingRepository.save(meeting);
        return mapToMeetingDto.apply(meeting);
    }

//    @Override
//    public boolean createOrUpdateMeetingByTitle(String title) {
//        Meeting meeting = meetingRepository.findByTitle(meetingDto.getTitle());
//        if (meeting == null) {
//            meeting = new Meeting(meetingDto.getTitle(), meetingDto.getHereAmount());
//            if (validator.validate(meeting).size() == 0) {
//                return mapToMeetingDto.apply(meetingRepository.save(meeting));
//            }
//        }
//        return mapToMeetingDto.apply(meetingRepository.save(meeting));
//    }

    @Override
    public boolean update(MeetingDto meetingDto) {
        Meeting meeting = meetingRepository.findById(meetingDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Meeting with id "
                        + meetingDto.getId() + " doesn't exists!"));
        if (meeting != null && !meeting.getTitle().equals(meetingDto.getTitle()) && meetingDto.getTitle().trim().length() != 0) {
            meeting.setTitle(meetingDto.getTitle());
            meetingRepository.save(meeting);
            return true;
        }
        return false;
    }

    @Override
    public void deleteMeetingById(Long id) {
        meetingRepository.delete(meetingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meeting with id " + id + " doesn't exists!")));
    }
    @Override
    public Long getHereAmountByMeeting(Long id) {
        return meetingRepository.findHereAmountByMeetingId(id);
    }

    @Override
    public MeetingDto getMeetingByTitle(String title) {
        return mapToMeetingDto.apply(meetingRepository.findByTitle(title));
    }

    @Override
    public List<MeetingDto> getGenerateMeetingsList(Long id) {
        return meetingRepository
                .getGenerateMeetingsList(id).stream()
                .map(mapToMeetingDto)
                .collect(Collectors.toList());
    }

    Function<Meeting, MeetingDto> mapToMeetingDto = (meeting -> MeetingDto.builder()
            .id(meeting.getId())
            .title(meeting.getTitle())
            .description(meeting.getDescription())
            .hereAmount(meeting.getHereAmount())
            .longitude(meeting.getLongitude())
            .latitude(meeting.getLatitude())
            .availableDistance(meeting.getAvailableDistance())
            .build());

}

