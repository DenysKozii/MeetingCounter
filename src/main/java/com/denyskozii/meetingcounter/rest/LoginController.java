package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.dto.UserLoginDto;
import com.denyskozii.meetingcounter.services.UserService;
import com.denyskozii.meetingcounter.dto.ResponseStatus;
import com.denyskozii.meetingcounter.dto.TokenDto;
import com.denyskozii.meetingcounter.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    JwtProvider jwtProvider;

    private String firstName;
    private String lastName;
    private String email;

    @GetMapping("/form-login")
    public ResponseStatus login() {
        return new ResponseStatus(200, "login complete");
    }

    @PostMapping("/login")
    public ResponseStatus loginPost(@RequestBody UserLoginDto userLoginDto) {
        log.info("login " + userLoginDto);
        if (userService.login(userLoginDto.getEmail(), userLoginDto.getPassword()))
            return new ResponseStatus(200, new TokenDto(jwtProvider.generateToken(userLoginDto.getEmail())).toString());
        return new ResponseStatus(409, "some problems in login");
    }

    @PostMapping("/google-login")
    public ResponseStatus googleLoginPost(@RequestParam String token) throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            var request = new HttpGet("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + token);
            HttpResponse response = client.execute(request);
            var bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            String line;
            while ((line = bufReader.readLine()) != null) {
                if (line.contains("given_name"))
                    firstName = line.substring(17, line.length() - 2);
                if (line.contains("family_name"))
                    lastName = line.substring(18, line.length() - 2);
                if (line.contains("email") && email == null)
                    email = line.substring(12, line.length() - 2);
            }
            log.info("Google login for " + email);
            if (!userService.login(email, firstName, lastName))
                userService.register(email, firstName, lastName);
            return new ResponseStatus(200,new TokenDto(jwtProvider.generateToken(email)).toString());
        }
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
