package org.example.meetingcounter.rest;


import org.example.meetingcounter.dto.ResponseStatus;
import org.example.meetingcounter.dto.TokenDto;
import org.example.meetingcounter.jwt.JwtProvider;
import org.example.meetingcounter.services.UserService;
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
