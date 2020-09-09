package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.services.UserService;
import com.denyskozii.meetingcounter.dto.ResponseStatus;
import com.denyskozii.meetingcounter.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    Logger logger = LoggerFactory.getLogger(RegistrationController.class);


    @Autowired
    UserService userService;

    @PostMapping("/registration")
    public ResponseStatus registrationPost(@RequestBody UserDto userDto) {
        logger.info("Registration of " + userDto);
        boolean isRegistered = userService.register(userDto);
        return isRegistered ? new ResponseStatus(HttpStatus.OK.value(),"successfully registered") : new ResponseStatus(409,"some problems");
    }
}
