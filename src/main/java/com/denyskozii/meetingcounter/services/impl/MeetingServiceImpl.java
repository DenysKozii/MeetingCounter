package com.denyskozii.meetingcounter.services.impl;

import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.model.User;
import com.denyskozii.meetingcounter.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import com.denyskozii.meetingcounter.dto.MeetingDto;
import com.denyskozii.meetingcounter.model.Meeting;
import com.denyskozii.meetingcounter.repository.MeetingRepository;
import com.denyskozii.meetingcounter.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.Validator;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
@Service
@Slf4j
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;



    @Autowired
    private Validator validator;


    @Autowired
    public MeetingServiceImpl(MeetingRepository meetingRepository,UserRepository userRepository) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
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
                .orElseThrow(() -> new EntityNotFoundException("Meeting with id " + id + " doesn't exists!")));
    }

    @Override
    public MeetingDto createOrUpdateMeeting(MeetingDto meetingDto) throws EntityNotFoundException{
        Meeting meeting = meetingRepository.findById(meetingDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Meeting with id " + meetingDto.getId() + " doesn't exists!"));

        if (meeting == null) {
            meeting = new Meeting(meetingDto.getTitle(),
                    meetingDto.getDescription(),
                    meetingDto.getHereAmount(),
                    meetingDto.getLongitude(),
                    meetingDto.getLatitude(),
                    meetingDto.getAvailableDistance(),
                    meetingDto.getZoom());
            if (validator.validate(meeting).size() == 0) {
                meetingRepository.save(meeting);
                return mapToMeetingDto.apply(meeting);
            }
        }
        meetingRepository.save(meeting);
        return mapToMeetingDto.apply(meeting);
    }

    @Override
    public MeetingDto getMeetingByTitle(String title) {
        Meeting meeting = meetingRepository.findAllByTitleContainingOrderByStartDate(title);
        return meeting == null ? null : mapToMeetingDto.apply(meeting);
    }

    @Override
    public List<MeetingDto> getAllMeetings(Long startId) {
//        return meetingRepository
//                .getGenerateMeetingsList(id).stream()
//                .map(mapToMeetingDto)
//                .collect(Collectors.toList());
        List<MeetingDto> meetingDtos = meetingRepository.findAll().stream()
                .limit(startId+20)
                .map(mapToMeetingDto)
                .collect(Collectors.toList());
        Collections.reverse(meetingDtos);
        return meetingDtos.subList(0,20);
    }

//    @Override
//    public List<MeetingDto> getMeetingsByUserId(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " doesn't exists!"));
//        return user.getMeetings().stream()
//                .map(mapToMeetingDto)
//                .collect(Collectors.toList());
//    }

    @Override
    public List<MeetingDto> getMeetingsByAuthorId(Long userId, Long startId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " doesn't exists!"));
        List<MeetingDto> meetingDtos = user.getMyMeetings().stream()
                .limit(startId+20)
                .map(mapToMeetingDto)
                .collect(Collectors.toList());
        Collections.reverse(meetingDtos);
        return meetingDtos.subList(0,20);
    }

    @Override
    public List<MeetingDto> getCurrentMeetingsByUserId(Long userId, Long startId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " doesn't exists!"));
        List<MeetingDto> meetingDtos = user.getMeetings().stream()
                .limit(startId+20)
                .filter(o->o.getStartDate().isBefore(LocalDate.now()))
                .filter(o->o.getFinishDate().isAfter(LocalDate.now()))
                .map(mapToMeetingDto)
                .collect(Collectors.toList());
        Collections.reverse(meetingDtos);
        return meetingDtos.subList(0,20);
    }

    @Override
    public List<MeetingDto> getFutureMeetingsByUserId(Long userId, Long startId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " doesn't exists!"));
        List<MeetingDto> meetingDtos = user.getMeetings().stream()
                .limit(startId+20)
                .filter(o->o.getStartDate().isAfter(LocalDate.now()))
                .map(mapToMeetingDto)
                .collect(Collectors.toList());
        Collections.reverse(meetingDtos);
        return meetingDtos.subList(0,20);
    }

    @Override
    public List<MeetingDto> getCurrentMeetings(Long startId) {
        List<MeetingDto> meetingDtos = meetingRepository.findAll().stream()
                .limit(startId+20)
                .filter(o->o.getStartDate().isBefore(LocalDate.now()))
                .filter(o->o.getFinishDate().isAfter(LocalDate.now()))
                .map(mapToMeetingDto)
                .collect(Collectors.toList());
        Collections.reverse(meetingDtos);
        return meetingDtos.subList(0,20);
    }

    @Override
    public List<MeetingDto> getFutureMeetings(Long startId) {
        List<MeetingDto> meetingDtos = meetingRepository.findAll().stream()
                .limit(startId+20)
                .filter(o->o.getStartDate().isAfter(LocalDate.now()))
                .map(mapToMeetingDto)
                .collect(Collectors.toList());
        Collections.reverse(meetingDtos);
        return meetingDtos.subList(0,20);
    }


}

