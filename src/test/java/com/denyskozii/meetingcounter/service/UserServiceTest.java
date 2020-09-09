package com.denyskozii.meetingcounter.service;

import com.denyskozii.meetingcounter.dto.MeetingDto;
import com.denyskozii.meetingcounter.dto.UserDto;
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
import org.springframework.test.context.TestExecutionListeners;
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

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository,meetingRepository);


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
}
