package com.denyskozii.meetingcounter.services;

import com.denyskozii.meetingcounter.dto.MeetingDto;
import com.denyskozii.meetingcounter.exception.EntityNotFoundException;
import com.denyskozii.meetingcounter.model.Meeting;

import java.util.List;
import java.util.function.Function;

/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
public interface MeetingService {

    MeetingDto createMeeting(MeetingDto meetingDto);

    boolean updateMeeting(MeetingDto meetingDto);

    boolean deleteMeeting(Long meetingId);

    List<MeetingDto> getAll();

    MeetingDto getMeetingById(Long id) throws EntityNotFoundException;

    List<MeetingDto> getMeetingByTitle(String title);

    List<MeetingDto> getAllMeetings(Long startId);

    List<MeetingDto> getMeetingsByAuthorId(Long userId, Long startId);

    List<MeetingDto> getCurrentMeetingsByUserId(Long userId, Long startId);

    List<MeetingDto> getFutureMeetingsByUserId(Long userId, Long startId);

    List<MeetingDto> getCurrentMeetings(Long startId);

    List<MeetingDto> getFutureMeetings(Long startId);

    List<MeetingDto> getMeetingsFromFriendsByUserId(Long userId, Long startId);

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
