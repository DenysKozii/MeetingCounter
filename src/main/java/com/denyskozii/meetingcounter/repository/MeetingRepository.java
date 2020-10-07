package com.denyskozii.meetingcounter.repository;

import com.denyskozii.meetingcounter.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    @Transactional(readOnly = true)
    List<Meeting> findAllByTitleContainingOrderByStartDate(String title);

//    @Query(value = "SELECT * FROM meetings " +
//            "ORDER BY ID DESC " +
//            "LIMIT :id+20 OFFSET :id", nativeQuery = true)
//    List<Meeting> getGenerateMeetingsList(Long id);

    @Override
    List<Meeting> findAll();
}
