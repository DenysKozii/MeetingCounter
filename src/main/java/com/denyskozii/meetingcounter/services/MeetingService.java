package com.denyskozii.meetingcounter.services;

import com.denyskozii.meetingcounter.dto.MeetingDto;
import com.denyskozii.meetingcounter.exception.EntityNotFoundException;

import java.util.List;

public interface MeetingService {
    MeetingDto createOrUpdateMeeting(MeetingDto meetingDto);
//    boolean createOrUpdateMeetingByTitle(String title);

    List<MeetingDto> getAll();

    MeetingDto getMeetingById(Long id) throws EntityNotFoundException;

    void deleteMeetingById(Long id);

    public boolean update(MeetingDto meetingDto);

    Long getHereAmountByMeeting(Long id);

    MeetingDto getMeetingByTitle(String title);

    List<MeetingDto> getGenerateMeetingsList(Long id);
}
