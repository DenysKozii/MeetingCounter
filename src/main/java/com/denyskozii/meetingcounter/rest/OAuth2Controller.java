package com.denyskozii.meetingcounter.rest;
import com.denyskozii.meetingcounter.dto.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2Controller {

    @GetMapping("/")
    public String helloWorld() {
        return "you don't need to be logged in";
    }

    @GetMapping("/not-restricted")
    public String notRestricted() {
        return "you don't need to be logged in";
    }

    @GetMapping("/restricted")
    public ResponseStatus restricted() {
        return new ResponseStatus(200,"login complete");
    }
}