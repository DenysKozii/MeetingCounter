package com.denyskozii.meetingcounter.model;


import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;
/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
public enum Role implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}

