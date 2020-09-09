package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.services.UserService;
import com.denyskozii.meetingcounter.dto.ResponseStatus;
import com.denyskozii.meetingcounter.dto.TokenDto;
import com.denyskozii.meetingcounter.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    JwtProvider jwtProvider;

    @GetMapping("/form-login")
    public ResponseStatus login() {
        return new ResponseStatus(200,"login complete");
    }

    @PostMapping("/login")
    public TokenDto loginPost(@RequestParam String email, @RequestParam String password) {
        userService.login(email, password);
        return new TokenDto(jwtProvider.generateToken(email));
    }
}
