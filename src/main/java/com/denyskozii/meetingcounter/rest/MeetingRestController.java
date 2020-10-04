package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.dto.GenerateMeetingDto;
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


//    /**
//     * return all meeting from concrete user.
//     */
//    @GetMapping("/get/current")
//    @PreAuthorize("hasAuthority('USER')")
//    public List<MeetingDto> getMeetingsByUser(@AuthenticationPrincipal UserDto user) {
//        log.info("Get meetings by user " + user);
//        return meetingService.getMeetingsByUserId(user.getId()).stream()
//                .filter(o -> o.getFinishDate().isAfter(LocalDate.now()))
//                .filter(o -> o.getStartDate().isBefore(LocalDate.now()))
//                .collect(Collectors.toList());
//    }


//    // my current, my future,  my created, current, future
//    // current+future
//
//    /**
//     * return all meeting from concrete user.
//     */
//    @GetMapping("/get/future")
//    @PreAuthorize("hasAuthority('USER')")
//    public List<MeetingDto> getFutureMeetingsByUser(@AuthenticationPrincipal UserDto user) {
//
//        log.info("Get meetings by user " + user);
//
//
//        return meetingService.getMeetingsByUserId(user.getId()).stream()
//                .filter(o -> o.getFinishDate()
//                        .isAfter(LocalDate.now()))
//                .collect(Collectors.toList());
//    }


//    /**
//     * return all meeting from concrete user.
//     */
//    @GetMapping("/myMeetings")
//    @PreAuthorize("hasAuthority('USER')")
//    public List<MeetingDto> getMyMeetings(@AuthenticationPrincipal UserDto user) {
//        log.info("Get meetings by user " + user);
//        return meetingService.getMeetingsByAuthorId(user.getId());
//    }


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

        return meetingService.getAllMeetings(startId);
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
