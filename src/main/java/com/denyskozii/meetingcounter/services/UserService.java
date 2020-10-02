package com.denyskozii.meetingcounter.services;

import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.model.User;

import java.util.List;

/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
public interface UserService {

    UserDto getUserById(Long id);

    Long getUserIdByName(String userFullName);

    List<UserDto> getFriendsByUserId(Long id);

    boolean addUserToMeeting(Long userId, Double longitude, Double latitude, Long meetingId);

    boolean checkUserAdded(Long userId, Double longitude, Double latitude, Long meetingId);

    boolean isUserInMeeting(Long userId, Long meetingId);

    boolean login(String email, String password);

    boolean login(String email, String firstName, String lastName);

    boolean register(UserDto userDto);

    boolean register(String email, String firstName, String lastName);

}
