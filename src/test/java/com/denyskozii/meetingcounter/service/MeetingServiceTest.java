package com.denyskozii.meetingcounter.service;


import com.denyskozii.meetingcounter.dto.MeetingDto;
import com.denyskozii.meetingcounter.model.Meeting;
import com.denyskozii.meetingcounter.repository.MeetingRepository;
import com.denyskozii.meetingcounter.services.MeetingService;
import com.denyskozii.meetingcounter.services.impl.MeetingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import java.util.List;
import java.util.function.Function;


@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class MeetingServiceTest {

    private MeetingService meetingService;

    @Mock
    MeetingRepository meetingRepository;

    @BeforeEach
    public void setUp() {
        meetingService = new MeetingServiceImpl(meetingRepository);


    }
    private MeetingDto getMeetingDto (Long id,
                                      String title,
                                      Long hereAmount,
                                      Double longitude,
                                      Double latitude,
                                      Double availableDistance){
        return MeetingDto.builder()
                .id(id)
                .title(title)
                .hereAmount(hereAmount)
                .longitude(longitude)
                .latitude(latitude)
                .availableDistance(availableDistance)
                .build();
    }

    @Test
    void test() {
        Assertions.assertEquals(1, 1);
    }

}
