package org.example.meetingcounter.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.meetingcounter.dto.UserDto;
import org.example.meetingcounter.exception.EntityNotFoundException;
import org.example.meetingcounter.model.Meeting;
import org.example.meetingcounter.model.Role;
import org.example.meetingcounter.model.User;
import org.example.meetingcounter.repository.MeetingRepository;
import org.example.meetingcounter.repository.UserRepository;
import org.example.meetingcounter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    UserRepository userRepository;

    MeetingRepository meetingRepository;

    @Autowired
    Validator validator;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, MeetingRepository meetingRepository) {
        this.userRepository = userRepository;
        this.meetingRepository = meetingRepository;
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        return mapToUserDto.apply(userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " doesn't exists!")));
    }

    @Override
    public Long getUserIdByName(String userFullName) {
        return userRepository
                .findByFirstNameAndLastName(
                        userFullName.split(" ")[0],
                        userFullName.split(" ")[1])
                .getId();
    }

    @Override
    public List<UserDto> getAllByRole(String role) {
        return userRepository.
                getAllByRole(Role.valueOf(role.toUpperCase())).stream().
                map(mapToUserDto).
                collect(Collectors.toList());
    }

    @Override
    public boolean addUserToMeeting(Long userId, Long meetingId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Meeting> meeting = meetingRepository.findById(meetingId);

        if(meeting.isPresent() && user.isPresent() && !user.get().getMeetings().contains(meeting.get())) {
            Meeting meetingNew = meetingRepository.getOne(meetingId);
            User userNew = userRepository.getOne(userId);
            userNew.getMeetings().add(meetingNew);
            userRepository.save(userNew);
            meetingRepository.increaseHereAmountByMeetingId(meetingId);
            return true;
        }
        return false;
    }

    @Override
    public List<UserDto> getAllByMeetingId(Long meetingId) {
        return userRepository.getAllByMeetingId(meetingId).stream().map(mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId) {
        userRepository.delete(mapToUser.apply(getUserById(userId)));
    }

    @Override
    public boolean removeFromMeeting(Long userId, Long meetingId) {
        if (userRepository.findById(userId).isPresent()) {
            User user = userRepository.getOne(userId);
            user.getMeetings().remove((meetingRepository.findById(meetingId).orElseThrow(() -> new EntityNotFoundException("Meeting with id " + meetingId + " doesn't exists!"))));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean login(String login, String password) {
        User user = userRepository.findByEmailAndPassword(login, password);
        return user != null;
    }

    @Override
    public boolean register(UserDto userDto) {
        User user = mapToUser.apply(userDto);
        User userEntity = userRepository.findByEmail(user.getEmail());
        if (userEntity != null || validator.validate(user).size() != 0) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }


    Function<UserDto, User> mapToUser = (userDto -> {
        User user = new User();
        user.setPassword(userDto.getPassword());
        user.setConfirmPassword(userDto.getConfirmPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setRole(Role.USER);
        return user;
    });

    Function<User, UserDto> mapToUserDto = (user -> UserDto.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .password(user.getPassword())
            .build());
}
