package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import com.denyskozii.meetingcounter.dto.MeetingDto;
import com.denyskozii.meetingcounter.dto.ResponseStatus;
import com.denyskozii.meetingcounter.repository.MeetingRepository;
import com.denyskozii.meetingcounter.repository.UserRepository;
import com.denyskozii.meetingcounter.services.MeetingService;
import com.denyskozii.meetingcounter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/user")
@CrossOrigin(origins = "*")
public class UserRestController {

    private final UserService userService;
    private final MeetingService meetingService;

    @Autowired
    public UserRestController(UserService userService,
                              MeetingService meetingService) {
        this.userService = userService;
        this.meetingService = meetingService;
    }

    @PostMapping("/add/{meetingId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseStatus addToMeeting(@PathVariable long meetingId,
                                       @RequestParam Double longitude,
                                       @RequestParam Double latitude,
                                       HttpServletRequest request) {
        log.info("Adding user to meeting");
        String userFullName = request.getUserPrincipal().getName();
        Long userId = userService.getUserIdByName(userFullName);
        boolean added = userService.addUserToMeeting(userId, longitude, latitude, meetingId);
        return new ResponseStatus(HttpStatus.OK.value(), added ? "user successfully added" : "user already added");
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
