package com.denyskozii.meetingcounter.service;


import com.denyskozii.meetingcounter.dto.MeetingDto;
import com.denyskozii.meetingcounter.model.Meeting;
import com.denyskozii.meetingcounter.repository.MeetingRepository;
import com.denyskozii.meetingcounter.repository.UserRepository;
import com.denyskozii.meetingcounter.services.MeetingService;
import com.denyskozii.meetingcounter.services.impl.MeetingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;


@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class MeetingServiceTest {

    private MeetingService meetingService;

    @Mock
    MeetingRepository meetingRepository;
    @Mock
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        meetingService = new MeetingServiceImpl(meetingRepository, userRepository);
        Meeting meeting = new Meeting("FirstMeeting","none",0L,0D,0D,10D,12);
        meeting.setId(1L);
        doReturn(Optional.of(meeting)).when(meetingRepository).findById(1L);
        doReturn(List.of(meeting)).when(meetingRepository).findAll();
        doReturn(List.of(meeting)).when(meetingRepository).findAllByTitleContainingOrderByStartDate("FirstMeeting");

    }
    private MeetingDto getMeetingDto (Long id,
                                      String title,
                                      String description,
                                      Long hereAmount,
                                      Double longitude,
                                      Double latitude,
                                      Double availableDistance,
                                      Integer zoom){
        return MeetingDto.builder()
                .id(id)
                .title(title)
                .description(description)
                .hereAmount(hereAmount)
                .longitude(longitude)
                .latitude(latitude)
                .availableDistance(availableDistance)
                .zoom(zoom)
                .build();
    }

    @Test
    void test() {
        assertEquals(1, 1);
    }

    @Test
    public void getByTitle() {
        MeetingDto meetingDto = getMeetingDto(1L,"FirstMeeting","none",0L,0D,0D,10D,12);
        List<MeetingDto> actual = meetingService.getMeetingByTitle("FirstMeeting", 0L);

        assertEquals(meetingDto, actual.get(0));
//        assertThrows(EntityNotFoundException.class, () -> meetingService.getMeetingById(ID_NO_EXIST));
    }

    @Test
    public void getById() {
        MeetingDto meetingDto = getMeetingDto(1L,"FirstMeeting","none",0L,0D,0D,10D,12);
        MeetingDto actual = meetingService.getMeetingById(1L);

        assertEquals(meetingDto, actual);
//        assertThrows(EntityNotFoundException.class, () -> meetingService.getMeetingById(ID_NO_EXIST));
    }
    @Test
    public void getAll() {
        List<MeetingDto> meetingDtoList = List.of(getMeetingDto(1L,"FirstMeeting","none",0L,0D,0D,10D,12));
        List<MeetingDto> actual = meetingService.getAll();

        assertEquals(meetingDtoList, actual);
//        assertThrows(EntityNotFoundException.class, () -> meetingService.getMeetingById(ID_NO_EXIST));
    }
    @Test
    public void CreateOrUpdateMeeting() {
        MeetingDto meetingDto= getMeetingDto(1L,"FirstMeeting","none",0L,0D,0D,10D,12);
        MeetingDto actual = meetingService.createMeeting(meetingDto);

        assertEquals(1, meetingService.getAll().size());
//        assertThrows(EntityNotFoundException.class, () -> meetingService.getMeetingById(ID_NO_EXIST));
    }

    @Test
    public void getAllMeetings() {
        MeetingDto meetingDto = getMeetingDto(1L,"FirstMeeting","none",0L,0D,0D,10D,12);
        List<MeetingDto> actual = meetingService.getAllMeetings(0L);

        assertEquals(List.of(meetingDto), actual);
//        assertThrows(EntityNotFoundException.class, () -> meetingService.getMeetingById(ID_NO_EXIST));
    }
//    @Test
//    public void deleteMeetingById() {
//        Meeting meeting = new Meeting("FirstMeeting","none",0L,0D,0D,10D);
////        MeetingDto actual = meetingService.createOrUpdateMeeting(Optional.of(meetingDto));
//        meetingRepository.save(meeting);
//        meeting.setId(1L);
//        when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
//        doNothing().when(meetingRepository).deleteById(any());
//        meetingService.deleteMeetingById(1L);
////        System.out.println(meetingRepository.findByTitle("FirstMeeting").getId());
//        verify(meetingRepository).deleteById(any());
//    }
}
