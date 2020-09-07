package org.example.meetingcounter.services;

import org.example.meetingcounter.dto.MeetingDto;
import org.example.meetingcounter.exception.EntityNotFoundException;
import org.example.meetingcounter.model.Meeting;

import java.util.List;

public interface MeetingService {
    MeetingDto createOrUpdateMeeting(MeetingDto meetingDto);
//    boolean createOrUpdateMeetingByTitle(String title);

    List<MeetingDto> getAll();

    MeetingDto getMeetingById(Long id) throws EntityNotFoundException;

    void deleteMeetingById(Long id);

    public boolean update(MeetingDto meetingDto);

    Long getHereAmountByMeeting(Long id);
}
