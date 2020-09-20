package com.denyskozii.meetingcounter.services;

import com.denyskozii.meetingcounter.dto.UserDto;

import java.util.List;

public interface UserService  {
    List<UserDto> getAll();

    UserDto getUserById(Long id);

    UserDto getUserByEmail(String email);

    Long getUserIdByName(String userFullName);

    List<UserDto> getAllByRole(String role);

    boolean addUserToMeeting(Long userId, Double longitude, Double latitude, Long meetingId);

    List<UserDto> getAllByMeetingId(Long meetingId);

    void delete(Long userId);

    boolean removeFromMeeting(Long userId, Long meetingId);

    boolean login(String email, String password);

    boolean login(String email, String firstName,String lastName);

    boolean register(UserDto userDto);

    boolean register(String email, String firstName,String lastName);

}
