package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.services.FriendRequestService;
import com.denyskozii.meetingcounter.services.MeetingService;
import com.denyskozii.meetingcounter.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Date: 06.10.2020
 *
 * @author Denys Kozii
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user/friends")
public class FriendsRestController {

    private final MeetingService meetingService;
    private final UserService userService;
    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    public FriendsRestController(MeetingService meetingService, UserService userService) {
        this.meetingService = meetingService;
        this.userService = userService;
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('USER')")
    public List<UserDto> getFriends(@AuthenticationPrincipal UserDto user) {
        log.info("Get friends by user " + user);
        return userService.getFriendsByUserId(user.getId());
    }

    @GetMapping("/fromMeeting/{meetingId}")
    @PreAuthorize("hasAuthority('USER')")
    public List<UserDto> getFriendsFromMeeting(@PathVariable Long meetingId,
                                               @AuthenticationPrincipal UserDto user) {
        log.info(String.format("Get %s friends from meeting with id %s ", user, meetingId));
        return userService.getFriendsByUserIdAndMeetingId(user.getId(), meetingId);
    }

    @PostMapping("/invite")
    @PreAuthorize("hasAuthority('USER')")
    public boolean inviteFriend(@RequestParam String email,
                                      @AuthenticationPrincipal UserDto user) {
        return friendRequestService.inviteByEmail(user.getEmail(), email);
    }

    @PutMapping("/accept")
    @PreAuthorize("hasAuthority('USER')")
    public boolean acceptFriend(@RequestParam String email,
                                @AuthenticationPrincipal UserDto user) {
        return friendRequestService.acceptByEmail(user.getEmail(), email);
    }
    @GetMapping("/accept/list")
    @PreAuthorize("hasAuthority('USER')")
    public List<UserDto> listForAcceptFriend(@AuthenticationPrincipal UserDto user) {
        return friendRequestService.getListForAcceptByEmail(user.getEmail());
    }
    @DeleteMapping("/remove")
    @PreAuthorize("hasAuthority('USER')")
    public boolean removeFriend(@RequestParam String email, @AuthenticationPrincipal UserDto user) {
        return friendRequestService.removeByEmail(user.getEmail(), email);
    }

}
