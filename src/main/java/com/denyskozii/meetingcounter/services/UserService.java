package com.denyskozii.meetingcounter.services;

import com.denyskozii.meetingcounter.dto.UserDto;

import java.util.List;
/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
public interface UserService  {

    UserDto getUserById(Long id);

//    UserDto getUserByEmail(String email);

    Long getUserIdByName(String userFullName);

//    List<UserDto> getAllByRole(String role);

    boolean addUserToMeeting(Long userId, Double longitude, Double latitude, Long meetingId);

    boolean checkUserAdded(Long userId, Double longitude, Double latitude, Long meetingId);

//    List<UserDto> getAllByMeetingId(Long meetingId);
//
//    void delete(Long userId);
//
//    boolean removeFromMeeting(Long userId, Long meetingId);

    boolean login(String email, String password);

    boolean login(String email, String firstName,String lastName);

    boolean register(UserDto userDto);

    boolean register(String email, String firstName,String lastName);

}
