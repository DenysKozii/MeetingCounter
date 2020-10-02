package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import com.denyskozii.meetingcounter.dto.ResponseStatus;
import com.denyskozii.meetingcounter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
//                                       @RequestParam Double longitude,
//                                       @RequestParam Double latitude,
                                       @RequestBody HashMap<String, Double> coordinates,
                                       HttpServletRequest request) {
        log.info("Add user to meeting " + meetingId);
        Long userId = Long.valueOf(request.getUserPrincipal().getName());
        boolean added = userService.addUserToMeeting(userId, coordinates.get("longitude"), coordinates.get("latitude"), meetingId);
        return new ResponseStatus(HttpStatus.OK.value(), added ? "user successfully added" : "user distance is incorrect");
    }


    /**
     * Calls to check if user already in concrete meeting.
     */
    @PostMapping("/checkAdd/{meetingId}")
    @PreAuthorize("hasAuthority('USER')")
    public boolean checkAddToMeeting(@PathVariable long meetingId,
//                                       @RequestParam Double longitude,
//                                       @RequestParam Double latitude,
                                     @RequestBody HashMap<String, Double> coordinates,
                                     HttpServletRequest request) {
        log.info("Check if user added to meeting " + meetingId);
        Long userId = Long.valueOf(request.getUserPrincipal().getName());
//        return new ResponseStatus(HttpStatus.OK.value(), added ? "user already added" : "user is not here");
        return userService.checkUserAdded(userId, coordinates.get("longitude"), coordinates.get("latitude"), meetingId);
    }

    @GetMapping("/isInMeeting")
    @PreAuthorize("hasAuthority('USER')")
    public boolean isInMeeting(HttpServletRequest request,
                                   @RequestParam Long meetingId) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());
        log.info("Check is user in meeting by id " + userId);
        return userService.isUserInMeeting(userId,meetingId);
    }

    /**
     * return user information.
     */
    @GetMapping("/getUser")
    @PreAuthorize("hasAuthority('USER')")
    public UserDto getUser(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());
        log.info("Get user by id " + userId);
        return userService.getUserById(userId);
    }

    /**
     * return user information.
     */
    @GetMapping("/friends")
    @PreAuthorize("hasAuthority('USER')")
    public List<UserDto> getFriends(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());
        log.info("Get friends by user id " + userId);
        return userService.getFriendsByUserId(userId);
    }

    /**
     * test connection.
     */
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
