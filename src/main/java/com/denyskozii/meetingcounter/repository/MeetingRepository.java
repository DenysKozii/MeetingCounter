package com.denyskozii.meetingcounter.repository;

import com.denyskozii.meetingcounter.model.Meeting;
import com.denyskozii.meetingcounter.model.User;
import net.bytebuddy.TypeCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    @Transactional(readOnly = true)
    Meeting findAllByTitleContainingOrderByStartDate(String title);

    Meeting findAllByTitleIsContaining(String title);

    @Transactional
    @Modifying
    @Query(value = "UPDATE meetings  " +
            " SET here_amount = here_amount + 1 " +
            " where id = :id", nativeQuery = true)
    void increaseHereAmountByMeetingId(Long id);


    @Query(value = "SELECT * FROM meetings " +
            "ORDER BY ID DESC " +
            "LIMIT :id+20 OFFSET :id", nativeQuery = true)
    List<Meeting> getGenerateMeetingsList(Long id);

    @Override
    List<Meeting> findAll();
}
