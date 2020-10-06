package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.services.UserService;
import com.denyskozii.meetingcounter.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
@RestController
@CrossOrigin(origins = "*")
public class RegistrationController {

    Logger logger = LoggerFactory.getLogger(RegistrationController.class);


    @Autowired
    UserService userService;
    /**
     * register new user by form
     */
    @PostMapping("/registration")
    public ResponseEntity<?> registrationPost(@RequestBody UserDto userDto) {
        logger.info("Registration of " + userDto);
        boolean isRegistered = userService.register(userDto);
        return isRegistered ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
