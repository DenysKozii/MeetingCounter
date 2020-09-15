//package com.denyskozii.meetingcounter.services;
//
//import com.denyskozii.meetingcounter.dto.UserDto;
//import com.denyskozii.meetingcounter.dto.UserGoogleDto;
//
//import java.util.List;
//
//public interface UserGoogleService  {
//    List<UserGoogleDto> getAll();
//
//    UserGoogleDto getUserById(Long id);
//
//    Long getUserIdByName(String userFullName);
//
//    List<UserGoogleDto> getAllByRole(String role);
//
//    boolean addUserToMeeting(Long userId, Double longitude, Double latitude, Long meetingId);
//
//    List<UserGoogleDto> getAllByMeetingId(Long meetingId);
//
//    boolean removeFromMeeting(Long userId, Long meetingId);
//
//    boolean login(String firstName, String lastName);
//
//    boolean register(UserGoogleDto userGoogleDto);
//
//}
