//package com.denyskozii.meetingcounter.repository;
//
//import com.denyskozii.meetingcounter.model.Role;
//import com.denyskozii.meetingcounter.model.User;
//import com.denyskozii.meetingcounter.model.UserGoogle;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import javax.validation.constraints.Email;
//import java.util.List;
//
//public interface UserGoogleRepository extends JpaRepository<UserGoogle, Long> {
//    @Query(value =
//            "SELECT * FROM usergoogle u " +
//                    "WHERE u.id IN( " +
//                    "   SELECT me.user_id " +
//                    "   FROM meeting_user me" +
//                    "   WHERE me.meeting_id =:meetingId" +
//                    ")", nativeQuery = true)
//    List<UserGoogle> getAllByMeetingId(@Param("meetingId") Long meetingId);
//
//    UserGoogle findByEmail(@Email(message = "Wrong email format") String email);
//
//    List<UserGoogle> getAllByRole(Role role);
//
//    UserGoogle findByFirstNameAndLastName(String firstName, String lastName);
//
//}
