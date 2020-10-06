package com.denyskozii.meetingcounter.services.impl;

import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.exception.EntityNotFoundException;
import com.denyskozii.meetingcounter.model.FriendRequest;
import com.denyskozii.meetingcounter.model.User;
import com.denyskozii.meetingcounter.repository.FriendRequestRepository;
import com.denyskozii.meetingcounter.repository.UserRepository;
import com.denyskozii.meetingcounter.services.FriendRequestService;
import com.denyskozii.meetingcounter.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FriendRequestServiceImpl implements FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository, UserRepository userRepository, UserService userService) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }



    @Override
    public boolean inviteByEmail(String userEmail, String friendEmail) {
        Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findByInvitorEmailAndAcceptorEmail(userEmail,friendEmail);

        if(friendRequestOptional.isEmpty()){
            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setInvitorEmail(userEmail);
            friendRequest.setAcceptorEmail(friendEmail);
            friendRequest.setStatus(false);
            friendRequestRepository.save(friendRequest);
            return true;
        }
        return false;
    }

    @Override
    public boolean acceptByEmail(String userEmail, String friendEmail) {
        FriendRequest friendRequest = friendRequestRepository.findByInvitorEmailAndAcceptorEmail(userEmail,friendEmail)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Friend request by user email %s and friend email %s doesn't exists!",userEmail,friendEmail)));
        if(friendRequest.getStatus())
            return false;
        friendRequest.setStatus(true);
        friendRequestRepository.save(friendRequest);
        return true;
    }

    @Override
    public List<UserDto> getListForAcceptByEmail(String email) {
        List<FriendRequest> friendRequests = friendRequestRepository.findAllByAcceptorEmail(email);
        return friendRequests.stream().map(o->userService.getUserByEmail(o.getAcceptorEmail())).collect(Collectors.toList());
    }

    @Override
    public boolean removeByEmail(String userEmail, String friendEmail) {
        FriendRequest friendRequest = friendRequestRepository.findByInvitorEmailAndAcceptorEmail(userEmail,friendEmail)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Friend request by user email %s and friend email %s doesn't exists!",userEmail,friendEmail)));
        friendRequestRepository.delete(friendRequest);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + userEmail + " doesn't exists!"));
        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + friendEmail + " doesn't exists!"));

        user.getFriends().remove(friend);

        userRepository.save(user);
        return true;
    }
}
