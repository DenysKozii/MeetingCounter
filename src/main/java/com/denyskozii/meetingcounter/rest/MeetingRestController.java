package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.dto.MeetingDto;
import com.denyskozii.meetingcounter.dto.ResponseStatus;
import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.services.MeetingService;
import com.denyskozii.meetingcounter.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('USER')")
    public List<MeetingDto> getMeetingsByUser(@AuthenticationPrincipal UserDto user) {
        log.info("Get meetings by user " + user);
        return meetingService.getMeetingsByUserId(user.getId());
    }

    /**
     * return all meeting from concrete user.
     */
    @GetMapping("/get/future")
    @PreAuthorize("hasAuthority('USER')")
    public List<MeetingDto> getFutureMeetingsByUser(@AuthenticationPrincipal UserDto user) {
        log.info("Get meetings by user " + user);
        return meetingService.getMeetingsByUserId(user.getId()).stream()
                .filter(o->o.getFinishDate()
                        .isAfter(LocalDate.now()))
                .collect(Collectors.toList());
    }

    /**
     * return all meeting from concrete user.
     */
    @GetMapping("/myMeetings")
    @PreAuthorize("hasAuthority('USER')")
    public List<MeetingDto> getMyMeetings(@AuthenticationPrincipal UserDto user) {
        log.info("Get meetings by user " + user);
        return meetingService.getMeetingsByAuthorId(user.getId());
    }


    /**
     * return 20 meetings from id for main list on the website.
     */
    @GetMapping("/generate")
    @PreAuthorize("hasAuthority('USER')")
    public List<MeetingDto> generateMeetings(@RequestParam long id,
                                             @RequestParam String title) {
        log.info("Generate meetings from " + id);
        MeetingDto meetingDto = meetingService.getMeetingByTitle(title);
        return meetingDto == null ? meetingService.getGenerateMeetingsList(id) : Collections.singletonList(meetingDto);
    }

    /**
     * create new meeting by Dto.
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseStatus createMeeting(@RequestBody MeetingDto meetingDto, @AuthenticationPrincipal UserDto user) {
        meetingDto.setAuthor(userService.mapToUser.apply(user));
        log.info("Create meeting " + meetingDto);
        meetingService.createOrUpdateMeeting(meetingDto);
        return new ResponseStatus(HttpStatus.OK.value());
    }

}
