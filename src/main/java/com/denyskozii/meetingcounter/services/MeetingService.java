package com.denyskozii.meetingcounter.services;

import com.denyskozii.meetingcounter.dto.MeetingDto;
import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.exception.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
public interface MeetingService {
    MeetingDto createOrUpdateMeeting(MeetingDto meetingDto);
//    boolean createOrUpdateMeetingByTitle(String title);

    List<MeetingDto> getAll();

    MeetingDto getMeetingById(Long id) throws EntityNotFoundException;

//    void deleteMeetingById(Long id);
//
//    boolean update(MeetingDto meetingDto);
//
//    Long getHereAmountByMeeting(Long id);

    MeetingDto getMeetingByTitle(String title);

    List<MeetingDto> getGenerateMeetingsList(Long id);

    List<MeetingDto> getMeetingsByUserId(Long id);

}
