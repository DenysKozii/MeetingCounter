package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.dto.MeetingDto;
import com.denyskozii.meetingcounter.dto.ResponseStatus;
import com.denyskozii.meetingcounter.services.MeetingService;
import com.denyskozii.meetingcounter.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/meeting")
@CrossOrigin(origins = "*")
public class MeetingRestController {

    private final MeetingService meetingService;
    private final UserService userService;

    @Autowired
    public MeetingRestController(MeetingService meetingService, UserService userService) {
        this.meetingService = meetingService;
        this.userService = userService;
    }

    @GetMapping("/getByUser")
    @PreAuthorize("hasAuthority('USER')")
    public List<MeetingDto> getMeetingsByUser(HttpServletRequest request) {
        Long userIdByName = userService.getUserIdByName(request.getUserPrincipal().getName());
        log.info("Getting meetings by user id " + userIdByName);
        return meetingService.getMeetingsByUserId(userIdByName);
    }

    @GetMapping("/generate/{id}")
    public List<MeetingDto> generateMeetings(@PathVariable long id,
                                             @RequestParam String title) {
        log.info("Generate meetings from " + id);
        MeetingDto meetingDto = meetingService.getMeetingByTitle(title);
        return meetingDto == null ? meetingService.getGenerateMeetingsList(id) : Collections.singletonList(meetingDto);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseStatus createMeeting(@RequestBody MeetingDto meetingDto) {
        log.info("Create meeting " + meetingDto);
        meetingService.createOrUpdateMeeting(meetingDto);
        return new ResponseStatus(HttpStatus.OK.value(),"");
    }

}
