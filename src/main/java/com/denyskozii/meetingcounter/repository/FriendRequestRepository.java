package com.denyskozii.meetingcounter.repository;

import com.denyskozii.meetingcounter.model.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
        Optional<FriendRequest> findByInvitorEmailAndAcceptorEmail(String invitorEmail, String acceptorEmail);

        List<FriendRequest> findAllByAcceptorEmail(String email);
}
