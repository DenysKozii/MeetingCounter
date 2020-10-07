package com.denyskozii.meetingcounter.repository;

import com.denyskozii.meetingcounter.model.Role;
import com.denyskozii.meetingcounter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;

/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
public interface UserRepository extends JpaRepository<User, Long> {
//    @Query(value =
//            "SELECT * FROM user u " +
//                    "WHERE u.id IN( " +
//                    "   SELECT me.user_id " +
//                    "   FROM meeting_user me" +
//                    "   WHERE me.meeting_id =:meetingId" +
//                    ")", nativeQuery = true)
//    List<User> getAllByMeetingId(@Param("meetingId") Long meetingId);

    Optional<User> findByEmail(@Email(message = "Wrong email format") String email);

    List<User> getAllByRole(Role role);

    Optional<User> findByEmailAndFirstNameAndLastName(String email, String firstName, String lastName);

    @Transactional
    @Query(value = "SELECT COUNT(*) FROM meeting_user " +
            "WHERE user_id = :userId AND  meeting_id = :meetingId", nativeQuery = true)
    boolean isUserSubscribedToMeeting(Long userId, Long meetingId);

}
