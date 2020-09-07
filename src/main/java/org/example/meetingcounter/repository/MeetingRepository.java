package org.example.meetingcounter.repository;

import org.example.meetingcounter.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {
    Meeting findByTitle(String title);
//    Meeting findById(Long id);
    @Query(value = "SELECT M.hereAmount FROM MEETING " +
            " WHERE M.ID = :id", nativeQuery = true)
    Long findHereAmountByMeetingId(Long id);

    @Transactional
    @Modifying
    @Query(value ="UPDATE meetings  " +
            " SET here_amount = here_amount + 1 " +
            " where id = :id", nativeQuery = true)
    void increaseHereAmountByMeetingId(Long id);
}
