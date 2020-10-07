package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.dto.GenerateMeetingDto;
import com.denyskozii.meetingcounter.dto.MeetingDto;
import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.services.MeetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing meetings
 *
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

    @Autowired
    public MeetingRestController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }


    /**
     * return 20 meetings from id by title searcher or type for main list on the website.
     *
     * @param startId
     * @param title
     * @param generateMeetingDto
     * @param user
     * @return List<MeetingDto>
     */
    @GetMapping("/getList")
    @PreAuthorize("hasAuthority('USER')")
    public List<MeetingDto> generateMeetings(@RequestParam Long startId,
                                             @RequestParam String title,
                                             @RequestParam GenerateMeetingDto generateMeetingDto,
                                             @AuthenticationPrincipal UserDto user) {
        List<MeetingDto> meetingsByTitle = meetingService.getMeetingByTitle(title,startId);

        if (meetingsByTitle.size()!=0){
            log.info("Find meeting by title " + title);
            return meetingsByTitle;
        }
        log.info("Generate meetings from " + startId);
        return meetingService.generateMeetings(generateMeetingDto, startId, user);
    }


    /**
     * create new meeting by Dto
     *
     * @param meetingDto
     * @param user
     * @return ResponseEntity
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> createMeeting(@RequestBody MeetingDto meetingDto, @AuthenticationPrincipal UserDto user) {
        meetingDto.setAuthor(user);
        log.info("Create meeting " + meetingDto);
        boolean updated = meetingService.updateMeeting(meetingDto);
        return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /**
     * delete meeting by id
     *
     * @param meetingId
     * @return ResponseEntity
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> deleteMeeting(@RequestParam Long meetingId) {
        log.info("Delete meeting with id " + meetingId);
        boolean deleted = meetingService.deleteMeeting(meetingId);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
