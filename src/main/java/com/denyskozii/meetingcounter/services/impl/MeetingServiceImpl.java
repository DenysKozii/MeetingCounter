package com.denyskozii.meetingcounter.services.impl;

import com.denyskozii.meetingcounter.dto.GenerateMeetingDto;
import com.denyskozii.meetingcounter.dto.MeetingDto;
import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.model.Meeting;
import com.denyskozii.meetingcounter.model.User;
import com.denyskozii.meetingcounter.repository.MeetingRepository;
import com.denyskozii.meetingcounter.repository.UserRepository;
import com.denyskozii.meetingcounter.services.MeetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    public MeetingDto createMeeting(MeetingDto meetingDto) throws EntityNotFoundException{
        Optional<Meeting> meetingOptional = meetingRepository.findById(meetingDto.getId());
        if (meetingOptional.isEmpty()) {
            Meeting meeting = new Meeting(meetingDto.getTitle(),
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
        return meetingDto;
    }

    @Override
    public boolean updateMeeting(MeetingDto meetingDto) {
        Meeting meeting = meetingRepository.findById(meetingDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Meeting with id " + meetingDto.getId() + " doesn't exists!"));
        meeting.setTitle(meetingDto.getTitle());
        meeting.setAvailableDistance(meetingDto.getAvailableDistance());
        meeting.setDescription(meetingDto.getDescription());
        meeting.setFinishDate(meetingDto.getFinishDate());
        meeting.setHereAmount(meetingDto.getHereAmount());
        meeting.setLatitude(meetingDto.getLatitude());
        meeting.setLongitude(meetingDto.getLongitude());
        meeting.setStartDate(meetingDto.getStartDate());
        if (validator.validate(meeting).size() == 0) {
            meetingRepository.save(meeting);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteMeeting(Long meetingId) {
        meetingRepository.deleteById(meetingId);
        return true;
    }

    @Override
    public List<MeetingDto> getMeetingByTitle(String title, Long startId) {
        List<MeetingDto> meetingDtos = meetingRepository
                .findAllByTitleContainingOrderByStartDate(title).stream()
                .limit(startId+20)
                .map(mapToMeetingDto)
                .collect(Collectors.toList());;

        return meetingDtos.subList(0, Math.min(meetingDtos.size(), 20));
    }

    @Override
    public List<MeetingDto> getAllMeetings(Long startId) {
        List<MeetingDto> meetingDtos = meetingRepository
                .findAll().stream()
                .limit(startId+20)
                .map(mapToMeetingDto)
                .collect(Collectors.toList());
        Collections.reverse(meetingDtos);
        return meetingDtos.subList(0, Math.min(meetingDtos.size(), 20));
    }


    @Override
    public List<MeetingDto> getMeetingsByAuthorId(Long userId, Long startId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " doesn't exists!"));
        List<MeetingDto> meetingDtos = user
                .getMyMeetings().stream()
                .limit(startId+20)
                .map(mapToMeetingDto)
                .collect(Collectors.toList());
        Collections.reverse(meetingDtos);
        return meetingDtos.subList(0,Math.min(meetingDtos.size(), 20));
    }

    @Override
    public List<MeetingDto> getCurrentMeetingsByUserId(Long userId, Long startId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " doesn't exists!"));
        List<MeetingDto> meetingDtos = user
                .getMeetings().stream()
                .limit(startId+20)
                .filter(o->o.getStartDate().isBefore(LocalDate.now()))
                .filter(o->o.getFinishDate().isAfter(LocalDate.now()))
                .map(mapToMeetingDto)
                .collect(Collectors.toList());
        Collections.reverse(meetingDtos);
        return meetingDtos.subList(0,Math.min(meetingDtos.size(), 20));
    }

    @Override
    public List<MeetingDto> getFutureMeetingsByUserId(Long userId, Long startId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " doesn't exists!"));
        List<MeetingDto> meetingDtos = user
                .getMeetings().stream()
                .limit(startId+20)
                .filter(o->o.getStartDate().isAfter(LocalDate.now()))
                .map(mapToMeetingDto)
                .collect(Collectors.toList());
        Collections.reverse(meetingDtos);
        return meetingDtos.subList(0,Math.min(meetingDtos.size(), 20));
    }

    @Override
    public List<MeetingDto> getCurrentMeetings(Long startId) {
        List<MeetingDto> meetingDtos = meetingRepository
                .findAll().stream()
                .limit(startId+20)
                .filter(o->o.getStartDate().isBefore(LocalDate.now()))
                .filter(o->o.getFinishDate().isAfter(LocalDate.now()))
                .map(mapToMeetingDto)
                .collect(Collectors.toList());
        Collections.reverse(meetingDtos);
        return meetingDtos.subList(0,Math.min(meetingDtos.size(), 20));
    }

    @Override
    public List<MeetingDto> getFutureMeetings(Long startId) {
        List<MeetingDto> meetingDtos = meetingRepository
                .findAll().stream()
                .limit(startId+20)
                .filter(o->o.getStartDate().isAfter(LocalDate.now()))
                .map(mapToMeetingDto)
                .collect(Collectors.toList());
        Collections.reverse(meetingDtos);
        return meetingDtos.subList(0,Math.min(meetingDtos.size(), 20));
    }

    @Override
    public List<MeetingDto> getMeetingsFromFriendsByUserId(Long userId, Long startId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " doesn't exists!"));
        return user.getFriends().stream()
                .flatMap(o->o.getMyMeetings().stream()
                        .map(mapToMeetingDto))
                .collect(Collectors.toList());
    }

    @Override
    public List<MeetingDto> generateMeetings(GenerateMeetingDto generateMeetingDto, Long startId, UserDto user) {
        boolean myCurrent = generateMeetingDto.getMyCurrent();
        boolean myCreated = generateMeetingDto.getMyCreated();
        boolean myFuture = generateMeetingDto.getMyFuture();
        boolean current = generateMeetingDto.getCurrent();
        boolean future = generateMeetingDto.getFuture();
        boolean friendCreated = generateMeetingDto.getFriendsCreated();

        if(!myCurrent && !myCreated && !myFuture && !current && !future && !friendCreated)
            return getAllMeetings(startId);

        List<MeetingDto> result = new ArrayList<>();

        if (myCurrent)
            result.addAll(getCurrentMeetingsByUserId(user.getId(), startId));
        if (myCreated)
            result.addAll(getMeetingsByAuthorId(user.getId(), startId));
        if (myFuture)
            result.addAll(getFutureMeetingsByUserId(user.getId(), startId));
        if (current)
            result.addAll(getCurrentMeetings(startId));
        if (future)
            result.addAll(getFutureMeetings(startId));
        if  (friendCreated)
            result.addAll(getMeetingsFromFriendsByUserId(user.getId(), startId));
        return result;
    }


}

