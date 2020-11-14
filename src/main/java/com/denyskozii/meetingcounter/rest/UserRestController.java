package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Controller for user information and adding to meeting
 * <p>
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }


    /**
     * Calls if user wants to click "I'm here" and add his account to a meeting by id
     *
     * @param meetingId
     * @param coordinates
     * @param user
     * @return ResponseEntity
     */
    @PostMapping("/add/{meetingId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> addToMeeting(@PathVariable long meetingId,
                                          @RequestBody HashMap<String, Double> coordinates,
                                          @AuthenticationPrincipal UserDto user) {
        log.info(String.format("Add user %s to meeting by id %s", user, meetingId));
        boolean added = userService.addUserToMeeting(user.getId(), coordinates.get("longitude"), coordinates.get("latitude"), meetingId);
        return added ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /**
     * Calls to check if user is in meeting radius
     *
     * @param meetingId
     * @param coordinates
     * @param user
     * @return boolean
     */
    @GetMapping("/isInRadius")
    @PreAuthorize("hasAuthority('USER')")
    public boolean isUserInMeetingRadius(@RequestParam Long meetingId,
                                         @RequestBody HashMap<String, Double> coordinates,
                                         @AuthenticationPrincipal UserDto user) {
        log.info(String.format("Check if user %s added to meeting by id %s", user, meetingId));
        return userService.checkUserAdded(user.getId(), coordinates.get("longitude"), coordinates.get("latitude"), meetingId);
    }

    /**
     * Calls to check if user is already in meeting
     *
     * @param user
     * @param meetingId
     * @return boolean
     */
    @GetMapping("/isSubscribed")
    @PreAuthorize("hasAuthority('USER')")
    public boolean isUserSubscribedToMeeting(@AuthenticationPrincipal UserDto user,
                                             @RequestParam Long meetingId) {
        log.info(String.format("Check if user %s subscribed to meeting by id %s", user, meetingId));
        return userService.isUserSubscribedToMeeting(user.getId(), meetingId);
    }

    /**
     * return user information
     *
     * @param user
     * @return UserDto
     */
    @GetMapping("/current")
    @PreAuthorize("hasAuthority('USER')")
    public UserDto getUser(@AuthenticationPrincipal UserDto user) {
        log.info("Get user " + user);
        return user;
    }


    /**
     * test connection.
     */
    @GetMapping("/version")
    public String version() {
        return "1.0";
    }

    /**
     * test connection.
     */
    @GetMapping("/version/login")
    @PreAuthorize("hasAuthority('USER')")
    public String versionLogin() {
        return "1.0";
    }
}
