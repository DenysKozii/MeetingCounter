package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.services.FriendRequestService;
import com.denyskozii.meetingcounter.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for actions with friendship between users
 * <p>
 * Date: 06.10.2020
 *
 * @author Denys Kozii
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user/friends")
public class FriendsRestController {

    private final UserService userService;
    private final FriendRequestService friendRequestService;

    @Autowired
    public FriendsRestController(UserService userService, FriendRequestService friendRequestService) {
        this.userService = userService;
        this.friendRequestService = friendRequestService;
    }

    /**
     * return list of all user's friends
     *
     * @param user
     * @return List<UserDto>
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('USER')")
    public List<UserDto> getFriends(@AuthenticationPrincipal UserDto user) {
        log.info("Get friends by user " + user);
        return userService.getFriendsByUserId(user.getId());
    }

    /**
     * return list of user's friends who chose meeting with the same id
     *
     * @param meetingId
     * @param user
     * @return List<UserDto>
     */
    @GetMapping("/fromMeeting/{meetingId}")
    @PreAuthorize("hasAuthority('USER')")
    public List<UserDto> getFriendsFromMeeting(@PathVariable Long meetingId,
                                               @AuthenticationPrincipal UserDto user) {
        log.info(String.format("Get %s friends from meeting with id %s ", user, meetingId));
        return userService.getFriendsByUserIdAndMeetingId(user.getId(), meetingId);
    }

    /**
     * invite new friend
     *
     * @param email
     * @param user
     * @return boolean
     */
    @PostMapping("/invite")
    @PreAuthorize("hasAuthority('USER')")
    public boolean inviteFriend(@RequestParam String email,
                                @AuthenticationPrincipal UserDto user) {
        log.info(String.format("User %s invited new friend with email: %s", user, email));
        return friendRequestService.inviteByEmail(user.getEmail(), email);
    }

    /**
     * accept friend invitation from another user
     *
     * @param email
     * @param user
     * @return boolean
     */
    @PutMapping("/accept")
    @PreAuthorize("hasAuthority('USER')")
    public boolean acceptFriend(@RequestParam String email,
                                @AuthenticationPrincipal UserDto user) {
        log.info(String.format("User with email: %s accepted invitation from user: %s", email, user));
        return friendRequestService.acceptByEmail(user.getEmail(), email);
    }

    /**
     * return all invitations for current user
     *
     * @param user
     * @return List<UserDto>
     */
    @GetMapping("/accept/list")
    @PreAuthorize("hasAuthority('USER')")
    public List<UserDto> listForAcceptFriend(@AuthenticationPrincipal UserDto user) {
        log.info(String.format("Get all invitations for user: %s", user));
        return friendRequestService.getListForAcceptByEmail(user.getEmail());
    }

    /**
     * remove another user from friends list
     *
     * @param email
     * @param user
     * @return boolean
     */
    @DeleteMapping("/remove")
    @PreAuthorize("hasAuthority('USER')")
    public boolean removeFriend(@RequestParam String email, @AuthenticationPrincipal UserDto user) {
        log.info(String.format("User %s removed friend with email: %s", user, email));
        return friendRequestService.removeByEmail(user.getEmail(), email);
    }

}
