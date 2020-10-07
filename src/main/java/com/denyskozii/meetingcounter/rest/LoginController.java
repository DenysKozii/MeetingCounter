package com.denyskozii.meetingcounter.rest;


import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.jwt.JwtProvider;
import com.denyskozii.meetingcounter.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * login and google login for user
 *
 * Date: 12.09.2020
 *
 * @author Denys Kozii
 */
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


    /**
     * login user by Dto
     *
     * @param userLoginDto
     * @return ResponseEntity
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginPost(@RequestBody UserDto userLoginDto) {
        log.info("login " + userLoginDto);
        if (userService.login(userLoginDto.getEmail(), userLoginDto.getPassword())){
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(jwtProvider.generateToken(userLoginDto.getEmail()));
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }


    /**
     * login user from Google auth by token.
     * @param token
     * @return ResponseEntity
     * @throws IOException
     */
    @PostMapping("/googleLogin")
    public ResponseEntity<String> googleLoginPost(@RequestParam String token) throws IOException {
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
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(jwtProvider.generateToken(email));
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
    }
}
