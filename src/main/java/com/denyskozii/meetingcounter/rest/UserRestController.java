package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.model.User;
import lombok.extern.slf4j.Slf4j;
import com.denyskozii.meetingcounter.dto.ResponseStatus;
import com.denyskozii.meetingcounter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }


    /**
     * Calls if user wants to click "I'm here" and add his account to a meeting by id.
     */
    @PostMapping("/add/{meetingId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseStatus addToMeeting(@PathVariable long meetingId,
                                       @RequestBody HashMap<String, Double> coordinates,
                                       @AuthenticationPrincipal UserDto user) {
        log.info("Add user to meeting " + meetingId);
        boolean added = userService.addUserToMeeting(user.getId(), coordinates.get("longitude"), coordinates.get("latitude"), meetingId);
        return new ResponseStatus(HttpStatus.OK.value(), added ? "user successfully added" : "user distance is incorrect");
    }


    /**
     * Calls to check if user already in concrete meeting.
     */
    @PostMapping("/isInRadius")
    @PreAuthorize("hasAuthority('USER')")
    public boolean isUserInMeetingRadius(@RequestParam Long meetingId,
                                         @RequestBody HashMap<String, Double> coordinates,
                                         @AuthenticationPrincipal UserDto user) {
        log.info("Check if user added to meeting " + meetingId);
//        return new ResponseStatus(HttpStatus.OK.value(), added ? "user already added" : "user is not here");
        return userService.checkUserAdded(user.getId(), coordinates.get("longitude"), coordinates.get("latitude"), meetingId);
    }

    @GetMapping("/isSubscribed")
    @PreAuthorize("hasAuthority('USER')")
    public boolean isUserSubscribedToMeeting(@AuthenticationPrincipal UserDto user,
                                   @RequestParam Long meetingId) {
        log.info("Check is user in meeting " + user);
        return userService.isUserSubscribedToMeeting(user.getId(),meetingId);
    }

    /**
     * return user information.
     */
//    @GetMapping("/get")
//    @PreAuthorize("hasAuthority('USER')")
//    public UserDto getUser(HttpServletRequest request) {
//        Long userId = Long.valueOf(request.getUserPrincipal().getName());
//        log.info("Get user by id " + userId);
//        return userService.getUserById(userId);
//    }

    @GetMapping("/current")
    @PreAuthorize("hasAuthority('USER')")
    public UserDto getUser(@AuthenticationPrincipal UserDto user) {
        log.info("Get user " + user);
        return user;
    }

    /**
     * return user information.
     */
    @GetMapping("/friends")
    @PreAuthorize("hasAuthority('USER')")
    public List<UserDto> getFriends(@AuthenticationPrincipal UserDto user) {
        log.info("Get friends by user " + user);
        return userService.getFriendsByUserId(user.getId());
    }


    // add to friends
//    @GetMapping("/friends")
//    @PreAuthorize("hasAuthority('USER')")
//    public List<UserDto> getFriends(@AuthenticationPrincipal UserDto user) {
//        log.info("Get friends by user " + user);
//        return userService.getFriendsByUserId(user.getId());
//    }
    /**
     * return user information.
     */
    @GetMapping("/friends/subscribedTo")
    @PreAuthorize("hasAuthority('USER')")
    public List<UserDto> getFriends(@RequestParam Long meetingId,
                                    @AuthenticationPrincipal UserDto user) {
        log.info("Get friends by user " + user);
        return userService.getFriendsByUserIdAndMeetingId(user.getId(), meetingId);
    }

    /**
     * test connection.
     */
    @GetMapping("/version")
    public String version() {
        return "1.0";
    }
}
