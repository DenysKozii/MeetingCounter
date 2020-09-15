package com.denyskozii.meetingcounter.repository;

import com.denyskozii.meetingcounter.model.Meeting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MeetingRepositoryTest {
    @Autowired
    private MeetingRepository meetingRepository;

    //    @Autowired
//    public MeetingRepositoryTest(MeetingRepository meetingRepository) {
//        this.meetingRepository = meetingRepository;
//    }
    private List<Meeting> loadDb() {
        Meeting meeting1 = new Meeting();
        meeting1.setTitle("First");
        meetingRepository.save(meeting1);

        Meeting meeting2 = new Meeting();
        meeting2.setTitle("Second");
        meetingRepository.save(meeting2);

        Meeting meeting3 = new Meeting();
        meeting3.setTitle("Third");
        meetingRepository.save(meeting3);


        return new ArrayList<>() {{
            add(meeting1);
            add(meeting2);
            add(meeting3);
        }};
    }
    @Test
    public void testAdd() {
        List<Meeting> expected2 = loadDb();

        Meeting expected = new Meeting();
        expected.setTitle("fourth");
        meetingRepository.save(expected);
        Meeting actual = meetingRepository.findById(1L).orElse(null);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testEmptyDb() {
        List<Meeting> expected = new ArrayList<>();
        List<Meeting> actual = meetingRepository.findAll();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAll() {
        List<Meeting> expected = loadDb();
        List<Meeting> actual = meetingRepository.findAll();

        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testDelete() {
        List<Meeting> all = loadDb();
        meetingRepository.delete(meetingRepository.findById(3L).get());
        meetingRepository.delete(meetingRepository.findById(2L).get());

        List<Meeting> expected = all.subList(0, 1);
        List<Meeting> actual = meetingRepository.findAll();

        Assertions.assertEquals(expected, actual);
    }
}
