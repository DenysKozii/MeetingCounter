package com.denyskozii.meetingcounter.services;

import com.denyskozii.meetingcounter.dto.MeetingDto;
import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.exception.EntityNotFoundException;
import com.denyskozii.meetingcounter.model.Meeting;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
public interface MeetingService {

    MeetingDto createOrUpdateMeeting(MeetingDto meetingDto);

    List<MeetingDto> getAll();

    MeetingDto getMeetingById(Long id) throws EntityNotFoundException;

    MeetingDto getMeetingByTitle(String title);

    List<MeetingDto> getAllMeetings(Long startId);

//    List<MeetingDto> getMeetingsByUserId(Long userId);

    List<MeetingDto> getMeetingsByAuthorId(Long userId, Long startId);

    List<MeetingDto> getCurrentMeetingsByUserId(Long userId, Long startId);

    List<MeetingDto> getFutureMeetingsByUserId(Long userId, Long startId);

    List<MeetingDto> getCurrentMeetings( Long startId);

    List<MeetingDto> getFutureMeetings( Long startId);


    Function<Meeting, MeetingDto> mapToMeetingDto = (meeting -> MeetingDto.builder()
            .id(meeting.getId())
            .title(meeting.getTitle())
            .description(meeting.getDescription())
            .hereAmount(meeting.getHereAmount())
            .longitude(meeting.getLongitude())
            .latitude(meeting.getLatitude())
            .availableDistance(meeting.getAvailableDistance())
            .zoom(meeting.getZoom())
            .startDate(meeting.getStartDate())
            .finishDate(meeting.getFinishDate())
            .build());
}
