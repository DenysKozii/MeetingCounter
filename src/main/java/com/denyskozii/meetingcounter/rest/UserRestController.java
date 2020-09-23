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

    @PostMapping("/add/{meetingId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseStatus addToMeeting(@PathVariable long meetingId,
                                       @RequestParam Double longitude,
                                       @RequestParam Double latitude,
                                       HttpServletRequest request) {
        log.info("Add user to meeting " + meetingId);
        String userFullName = request.getUserPrincipal().getName();
        Long userId = userService.getUserIdByName(userFullName);
        boolean added = userService.addUserToMeeting(userId, longitude, latitude, meetingId);
        return new ResponseStatus(HttpStatus.OK.value(), added ? "user successfully added" : "user distance is incorrect");
    }

    @PostMapping("/checkAdd/{meetingId}")
    @PreAuthorize("hasAuthority('USER')")
    public boolean checkAddToMeeting(@PathVariable long meetingId,
                                       @RequestParam Double longitude,
                                       @RequestParam Double latitude,
                                       HttpServletRequest request) {
        log.info("Check if user added to meeting " + meetingId);
        String userFullName = request.getUserPrincipal().getName();
        Long userId = userService.getUserIdByName(userFullName);
//        return new ResponseStatus(HttpStatus.OK.value(), added ? "user already added" : "user is not here");
        return userService.checkUserAdded(userId, longitude, latitude, meetingId);
    }

    @GetMapping("/getUser")
    @PreAuthorize("hasAuthority('USER')")
    public UserDto getUser(HttpServletRequest request) {
        Long userIdByName = userService.getUserIdByName(request.getUserPrincipal().getName());
        log.info("Get user by id " + userIdByName);
        return userService.getUserById(userIdByName);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
