package com.denyskozii.meetingcounter.repository;

import com.denyskozii.meetingcounter.model.Role;
import com.denyskozii.meetingcounter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.Email;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value =
            "SELECT * FROM user u " +
                    "WHERE u.id IN( " +
                    "   SELECT me.user_id " +
                    "   FROM meeting_user me" +
                    "   WHERE me.meeting_id =:meetingId" +
                    ")", nativeQuery = true)
    List<User> getAllByMeetingId(@Param("meetingId") Long meetingId);

    User findByEmail(@Email(message = "Wrong email format") String email);

    User findByEmailAndPassword(String email, String password);

    List<User> getAllByRole(Role role);

    User findByFirstNameAndLastName(String firstName, String lastName);

}
