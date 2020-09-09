package com.denyskozii.meetingcounter.rest;

import com.denyskozii.meetingcounter.dto.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccessDeniedController {

    @RequestMapping(path = "/access-denied", method = RequestMethod.GET)
    public ResponseStatus accessPage() {
        return new ResponseStatus(HttpStatus.FORBIDDEN.value(), "Access forbidden!");
    }
}
