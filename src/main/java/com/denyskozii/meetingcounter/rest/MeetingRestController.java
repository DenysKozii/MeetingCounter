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
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping(value = "/meeting")
public class MeetingRestController {

    private final MeetingService meetingService;
    private final UserService userService;

    @Autowired
    public MeetingRestController(MeetingService meetingService, UserService userService) {
        this.meetingService = meetingService;
        this.userService = userService;
    }

    /**
     * return all meeting from concrete user.
     */
    @GetMapping("/getByUser")
    @PreAuthorize("hasAuthority('USER')")
    public List<Long> getMeetingsByUser(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());
        log.info("Get meetings by user id " + userId);
        return meetingService.getMeetingsByUserId(userId)
                .stream()
                .map(MeetingDto::getId)
                .collect(Collectors.toList());
    }



    /**
     * return 20 meetings from id for main list on the website.
     */
    @GetMapping("/generate/{id}")
    public List<MeetingDto> generateMeetings(@PathVariable long id,
                                             @RequestParam String title) {
        log.info("Generate meetings from " + id);
        MeetingDto meetingDto = meetingService.getMeetingByTitle(title);
        return meetingDto == null ? meetingService.getGenerateMeetingsList(id) : Collections.singletonList(meetingDto);
    }


    /**
     * return 20 meetings from id for main list on the website.
     */
//    @GetMapping("/upload/{time}/{limit}")
//    public List<MeetingDto> uploadNewMeetings(@PathVariable LocalDate time,
//                                              @PathVariable long limit,
//                                              @RequestParam String title) {
//        log.info("Upload meetings from " + time);
//        MeetingDto meetingDto = meetingService.getMeetingByTitle(title);
//        return meetingDto == null ? meetingService.uploadMeetingsList(time, limit) : Collections.singletonList(meetingDto);
//    }

    /**
     * create new meeting by Dto.
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseStatus createMeeting(@RequestBody MeetingDto meetingDto) {
        log.info("Create meeting " + meetingDto);
        meetingService.createOrUpdateMeeting(meetingDto);
        return new ResponseStatus(HttpStatus.OK.value(),"");
    }

}
