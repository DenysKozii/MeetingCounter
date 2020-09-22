package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.dto.ResponseStatus;
import com.denyskozii.meetingcounter.services.MeetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping(value = "/meeting")
@CrossOrigin(origins = "*")
public class MeetingRestController {

    private final MeetingService meetingService;

    @Autowired
    public MeetingRestController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @DeleteMapping("delete/{meetingId}")
    public ResponseStatus deleteMeeting(@PathVariable long meetingId) {
        log.info("Delete meeting");
        meetingService.deleteMeetingById(meetingId);
        return new ResponseStatus(HttpStatus.OK.value(), "Meeting successfully deleted");
    }
}
