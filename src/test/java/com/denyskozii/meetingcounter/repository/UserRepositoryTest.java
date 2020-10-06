package com.denyskozii.meetingcounter.repository;



import com.denyskozii.meetingcounter.model.Role;
import com.denyskozii.meetingcounter.model.User;
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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeetingRepository meetingRepository;


    private List<User> loadDb() {
        User user1 = new User();
        user1.setRole(Role.USER);
        user1.setEmail("first@gmail.com");
        user1.setFirstName("first");
        user1.setLastName("first");
        user1.setPassword("first");
        userRepository.save(user1);

        User user2 = new User();
        user2.setRole(Role.USER);
        user2.setEmail("second@gmail.com2");
        user2.setFirstName("second");
        user2.setLastName("second");
        user2.setPassword("second");
        userRepository.save(user2);

        User user3 = new User();
        user3.setRole(Role.USER);
        user3.setEmail("third@gmail.com3");
        user3.setFirstName("third");
        user3.setLastName("third");
        user3.setPassword("third");
        userRepository.save(user3);

        User user4 = new User();
        user4.setRole(Role.ADMIN);
        user4.setEmail("fourth@gmail.com4");
        user4.setFirstName("fourth");
        user4.setLastName("fourth");
        user4.setPassword("fourth");
        userRepository.save(user4);

        return new ArrayList<>() {{
            add(user1);
            add(user2);
            add(user3);
            add(user4);
        }};
    }

    @Test
    public void testUserAdd() {
        User expected = new User();
        expected.setRole(Role.USER);
        expected.setEmail("user@gmail.com");
        expected.setFirstName("user");
        expected.setLastName("user");
        expected.setPassword("useruser");

        userRepository.save(expected);
        User actual = userRepository.findByEmail("user@gmail.com");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testEmptyDb() {
        List<User> expected = new ArrayList<>();
        List<User> actual = userRepository.findAll();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAll() {
        List<User> expected = loadDb();
        List<User> actual = userRepository.findAll();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindById() {
        User expected = loadDb().get(0);
        User actual = userRepository.findById(1L).orElse(null);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindByRoleUser() {
        List<User> expected = loadDb().subList(0, 3);
        List<User> actual = userRepository.getAllByRole(Role.USER);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindByRoleAdmin() {
        List<User> expected = loadDb().subList(3, 4);
        List<User> actual = userRepository.getAllByRole(Role.ADMIN);

        Assertions.assertEquals(expected, actual);
    }
}
