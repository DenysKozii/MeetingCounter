package com.denyskozii.meetingcounter.service;

import com.denyskozii.meetingcounter.dto.MeetingDto;
import com.denyskozii.meetingcounter.dto.UserDto;
import com.denyskozii.meetingcounter.model.Role;
import com.denyskozii.meetingcounter.model.User;
import com.denyskozii.meetingcounter.repository.MeetingRepository;
import com.denyskozii.meetingcounter.repository.UserRepository;
import com.denyskozii.meetingcounter.services.MeetingService;
import com.denyskozii.meetingcounter.services.UserService;
import com.denyskozii.meetingcounter.services.impl.MeetingServiceImpl;
import com.denyskozii.meetingcounter.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestExecutionListeners;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class UserServiceTest {
    private UserService userService;

    @Mock
    UserRepository userRepository;
    @Mock
    MeetingRepository meetingRepository;
    @Mock
    RedisTemplate<Long, Long> redisTemplate;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository,meetingRepository/*,redisTemplate*/);
        User user = new User(1L,"Denys", "Kozii","denys.kozii@gmail.com","123123","123123", Role.USER, Date.valueOf(LocalDate.now()),new ArrayList<>());
        doReturn(Optional.of(user)).when(userRepository).findById(1L);
        doReturn(user).when(userRepository).findByFirstNameAndLastName("Denys", "Kozii");
    }

    private UserDto getUserDto (Long id,
                                String firstName,
                                String lastName,
                                String email,
                                String password){
        return UserDto.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .build();
    }

    @Test
    void test() {
        assertEquals(1, 1);
    }

    @Test
    public void getById() {
        UserDto userDto = getUserDto(1L,"Denys","Kozii","denys.kozii@gmail.com","123123");
        UserDto actual = userService.getUserById(1L);

        assertEquals(userDto, actual);
    }
    @Test
    public void getUserIdByName() {
        Long actual = userService.getUserIdByName("Denys Kozii");
        assertEquals(1L, actual);
    }
}
