package com.denyskozii.meetingcounter.repository;

import com.denyskozii.meetingcounter.model.Meeting;
import com.denyskozii.meetingcounter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Meeting findByTitle(String title);
//    Meeting findByTitle(String title);

    //    Meeting findById(Long id);
    @Query(value = "SELECT M.hereAmount FROM meetings " +
            " WHERE M.ID = :id", nativeQuery = true)
    Long findHereAmountByMeetingId(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE meetings  " +
            " SET here_amount = here_amount + 1 " +
            " where id = :id", nativeQuery = true)
    void increaseHereAmountByMeetingId(Long id);


    @Query(value = "SELECT * FROM meetings " +
            "            WHERE ID BETWEEN :id and :id+20", nativeQuery = true)
    List<Meeting> getGenerateMeetingsList(Long id);

    List<Meeting> getMeetingByUsers(User user);
}
