package com.denyskozii.meetingcounter.services;

import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.model.Role;
import com.denyskozii.meetingcounter.model.User;

import java.util.List;
import java.util.function.Function;

/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
public interface UserService {

    UserDto getUserById(Long id);

    Long getUserIdByName(String userFullName);

    List<UserDto> getFriendsByUserId(Long id);

    List<UserDto> getFriendsByUserIdAndMeetingId(Long userId, Long meetingId);

    boolean addUserToMeeting(Long userId, Double longitude, Double latitude, Long meetingId);

    boolean checkUserAdded(Long userId, Double longitude, Double latitude, Long meetingId);

    boolean isUserInMeeting(Long userId, Long meetingId);

    boolean login(String email, String password);

    boolean login(String email, String firstName, String lastName);

    boolean register(UserDto userDto);

    boolean register(String email, String firstName, String lastName);

    Function<UserDto, User> mapToUser = (userDto -> {
        User user = new User();
        user.setPassword(userDto.getPassword());
        user.setConfirmPassword(userDto.getConfirmPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setRole(Role.USER);
        return user;
    });


    Function<User, UserDto> mapToUserDto = (user -> UserDto.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .password(user.getPassword())
            .build());

}
