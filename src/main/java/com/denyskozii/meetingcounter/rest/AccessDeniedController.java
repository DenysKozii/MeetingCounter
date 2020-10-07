package com.denyskozii.meetingcounter.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
@RestController
@CrossOrigin(origins = "*")
public class AccessDeniedController {

    /**
     * return 403 Access forbidden!
     *
     * @return ResponseEntity
     */
    @RequestMapping(path = "/access-denied", method = RequestMethod.GET)
    public  ResponseEntity<?> accessPage() {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
