package com.denyskozii.meetingcounter.services;

import com.denyskozii.meetingcounter.dto.UserDto;

import java.util.List;

public interface UserService  {
    List<UserDto> getAll();

    UserDto getUserById(Long id);

    Long getUserIdByName(String userFullName);

    List<UserDto> getAllByRole(String role);

    boolean addUserToMeeting(Long userId, Double longitude, Double latitude, Long meetingId);

    List<UserDto> getAllByMeetingId(Long meetingId);

    void delete(Long userId);

    boolean removeFromMeeting(Long userId, Long meetingId);

    boolean login(String login, String password);

    boolean register(UserDto userDto);

}