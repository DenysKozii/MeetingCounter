package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.dto.GenerateMeetingDto;
import com.denyskozii.meetingcounter.dto.MeetingDto;
import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.model.User;
import com.denyskozii.meetingcounter.services.MeetingService;
import com.denyskozii.meetingcounter.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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
     * return 20 meetings from id for main list on the website.
     */
    @GetMapping("/getList")
    @PreAuthorize("hasAuthority('USER')")
    public List<MeetingDto> generateMeetings(@RequestParam Long startId,
                                             @RequestParam String title,
                                             @RequestParam GenerateMeetingDto generateMeetingDto,
                                             @AuthenticationPrincipal UserDto user) {

        log.info("Generate meetings from " + startId);

        MeetingDto meetingDto = meetingService.getMeetingByTitle(title);

        if (meetingDto!=null)
            return Collections.singletonList(meetingDto);
        if (generateMeetingDto.getMyCurrent())
            return meetingService.getCurrentMeetingsByUserId(user.getId(), startId);
        if (generateMeetingDto.getMyCreated())
            return meetingService.getMeetingsByAuthorId(user.getId(), startId);
        if (generateMeetingDto.getMyFuture())
            return meetingService.getFutureMeetingsByUserId(user.getId(), startId);
        if (generateMeetingDto.getCurrent())
            return meetingService.getCurrentMeetings(startId);
        if (generateMeetingDto.getFuture())
            return meetingService.getFutureMeetings(startId);
        if(generateMeetingDto.getFriendsCreated())
            return meetingService.getMeetingsFromFriendsByUserId(user.getId(), startId);

        return meetingService.getAllMeetings(startId);
    }


    /**
     * create new meeting by Dto.
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> createMeeting(@RequestBody MeetingDto meetingDto, @AuthenticationPrincipal UserDto user) {
        meetingDto.setAuthor(user);
        log.info("Create meeting " + meetingDto);
        boolean updated = meetingService.updateMeeting(meetingDto);
        return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> deleteMeeting(@RequestBody MeetingDto meetingDto, @AuthenticationPrincipal UserDto user) {
        meetingDto.setAuthor(user);
        log.info("Create meeting " + meetingDto);
        boolean deleted = meetingService.deleteMeeting(meetingDto.getId());
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
