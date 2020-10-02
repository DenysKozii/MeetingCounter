package com.denyskozii.meetingcounter.services.impl;

//import com.denyskozii.meetingcounter.model.PasswordType;
import com.denyskozii.meetingcounter.services.UserService;
import lombok.extern.slf4j.Slf4j;
import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.exception.EntityNotFoundException;
import com.denyskozii.meetingcounter.model.Meeting;
import com.denyskozii.meetingcounter.model.Role;
import com.denyskozii.meetingcounter.model.User;
import com.denyskozii.meetingcounter.repository.MeetingRepository;
import com.denyskozii.meetingcounter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final MeetingRepository meetingRepository;

//    private RedisTemplate<Long, Long> redisTemplate;

//    private HashOperations hashOperations;

    @Autowired
    private Validator validator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final static String DELIMITER = " ";

    @Autowired
    public UserServiceImpl(UserRepository userRepository, MeetingRepository meetingRepository/*, RedisTemplate<Long, Long> redisTemplate*/) {
        this.userRepository = userRepository;
        this.meetingRepository = meetingRepository;
//        this.redisTemplate = redisTemplate;
    }


    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException("User not found");
        return user;
    }

    @Override
    public UserDto getUserById(Long id) {
        return mapToUserDto.apply(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " doesn't exists!")));
    }

    @Override
    public Long getUserIdByName(String userFullName) {
        return userRepository
                .findByFirstNameAndLastName(
                        userFullName.split(DELIMITER)[0],
                        userFullName.split(DELIMITER)[1])
                .getId();
    }

    @Override
    public List<UserDto> getFriendsByUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " doesn't exists!"));

        return user.getFriends().stream()
                .map(mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getFriendsByUserIdAndMeetingId(Long userId, Long meetingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " doesn't exists!"));
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + meetingId + " doesn't exists!"));;
        return user.getFriends().stream()
                .filter(o->meeting.getUsers()
                        .contains(o))
                .map(mapToUserDto)
                .collect(Collectors.toList());

    }
    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * <p>
     * latitudeUser, longitudeUser Start point latitudeMeeting, longitudeMeeting End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @returns Distance in Meters
     * @author Den
     */
    private boolean distance(double latitudeUser, double longitudeUser, double latitudeMeeting,
                             double longitudeMeeting, double meetingAvailableDistance) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(latitudeMeeting - latitudeUser);
        double lonDistance = Math.toRadians(longitudeMeeting - longitudeUser);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latitudeUser)) * Math.cos(Math.toRadians(latitudeMeeting))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

//        double height = el1 - el2;
//        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        return distance <= meetingAvailableDistance;
//        return Math.sqrt(distance);
    }

    @Override
    public boolean addUserToMeeting(Long userId, Double longitude, Double latitude, Long meetingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("User with id " + userId + " not found"));
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(()->new EntityNotFoundException("Meeting with id " + meetingId + " not found"));

        if (!user.getMeetings().contains(meeting)) {
            boolean availableDistance = distance(
                    latitude,
                    longitude,
                    meeting.getLatitude(),
                    meeting.getLongitude(),
                    meeting.getAvailableDistance());
            if (availableDistance) {
                user.getMeetings().add(meeting);
                userRepository.save(user);
                meetingRepository.increaseHereAmountByMeetingId(meetingId);
//                increaseHereAmountByMeetingId(meetingId);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkUserAdded(Long userId, Double longitude, Double latitude, Long meetingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("User with id " + userId + " not found"));
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(()->new EntityNotFoundException("Meeting with id " + meetingId + " not found"));
        return user.getMeetings().contains(meeting);
    }

    @Override
    public boolean isUserInMeeting(Long userId, Long meetingId) {
        return userRepository.isUserInMeeting(userId,meetingId);
    }

    /**
     * Using Redis for increment amount of people on the meeting
     */

//    private void increaseHereAmountByMeetingId(Long meetingId) {
//        Long currentAmount = (Long) hashOperations.get("MEETING", meetingId);
//        hashOperations.put("MEETING", meetingId, currentAmount + 1);
//    }


    @Override
    public boolean login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            throw new EntityNotFoundException("User with email " + email + " not found");
        return /*user.getPassword().equals(password);*/ true;
    }

    @Override
    public boolean login(String email, String firstName, String lastName) {
        User user = userRepository.findByEmailAndFirstNameAndLastName(email, firstName, lastName);
//            throw new EntityNotFoundException("User with email " + email + " not found");
        return user != null /*&& user.getPasswordType().equals(PasswordType.WITHOUT_PASSWORD)*/;
    }

    @Override
    public boolean register(UserDto userDto) {
        User user = mapToUser.apply(userDto);
//        user.setPasswordType(PasswordType.WITH_PASSWORD);
        User userEntity = userRepository.findByEmail(user.getEmail());
        if (userEntity != null || validator.validate(user).size() != 0)
            return false;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean register(String email, String firstName, String lastName) {
        User user = new User();
        user.setRole(Role.USER);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
//        user.setPasswordType(PasswordType.WITHOUT_PASSWORD);
        user.setPassword("qwehdchqbprfvyqiperfvwq12323148");
        User userEntity = userRepository.findByEmail(email);
        if (userEntity != null || validator.validate(user).size() != 0) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }




}
