package com.denyskozii.meetingcounter.services;

import com.denyskozii.meetingcounter.dto.UserDto;

import java.util.List;
public interface FriendRequestService {
    boolean inviteByEmail(String userEmail, String friendEmail);

    boolean acceptByEmail(String userEmail, String friendEmail);

    List<UserDto> getListForAcceptByEmail(String email);

    boolean removeByEmail(String userEmail, String friendEmail);
}
