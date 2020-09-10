package com.denyskozii.meetingcounter.rest;


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
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/user")
public class UserRestController {

    private final UserService userService;
    private final MeetingService meetingService;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;

    @Autowired
    public UserRestController(UserService userService,
                              MeetingService meetingService,
                              UserRepository userRepository,
                              MeetingRepository meetingRepository) {
        this.userService = userService;
        this.meetingService = meetingService;
        this.userRepository = userRepository;
        this.meetingRepository = meetingRepository;
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

    @PostMapping("/createMeeting")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseStatus createMeeting(@RequestParam String title,
                                        @RequestParam String description,
                                        @RequestParam Double longitude,
                                        @RequestParam Double latitude,
                                        @RequestParam Double availableDistance,
                                        HttpServletRequest request) {
        log.info("Create meeting");
        boolean added = false;
        String userFullName = request.getUserPrincipal().getName();
        Long userId = userService.getUserIdByName(userFullName);

        MeetingDto meetingDto = new MeetingDto(title,description,0L,longitude,latitude,availableDistance);
        MeetingDto createdMeeting = meetingService.createOrUpdateMeeting(meetingDto);

//        if(!createdMeeting.equals(meetingDto))
//            added = userService.addUserToMeeting(userId, createdMeeting.getId());
        return new ResponseStatus(HttpStatus.OK.value(),added ? "":"");
    }
    @GetMapping("/generateMeetings/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public List<MeetingDto> generateMeetings(@PathVariable long id) {
        log.info("Generate meetings");
        return meetingService.getGenerateMeetingsList(id);
    }





    @GetMapping("/{meetingId}/here")
    @PreAuthorize("hasAuthority('USER')")
    public Long createMeeting(@PathVariable Long meetingId) {
        log.info("Getting here amount by meeting id " + meetingId);
        return meetingRepository.getOne(meetingId).getHereAmount();
    }
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
