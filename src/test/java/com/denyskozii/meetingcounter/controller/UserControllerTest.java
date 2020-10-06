//package com.denyskozii.meetingcounter.controller;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import com.denyskozii.meetingcounter.rest.UserRestController;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class UserControllerTest {
//    @Autowired
//    private UserRestController controller;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void contextLoads() throws Exception {
//        assertThat(controller).isNotNull();
//    }
//
//    @Test
//    public void loginTest() throws Exception {
//
//        mockMvc.perform(get("/login")
////                .contentType())
//                .andExpect(status().isOk())
//    }
//    @Test
//    public void registrationTest() throws Exception {
//
//        mockMvc.perform(get("/registration")
//                .contentType(MediaType.TEXT_HTML))
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .contentTypeCompatibleWith(MediaType.TEXT_HTML));
//    }
//    @Test
//    public void redirectionBulletinTest() throws Exception {
//        mockMvc.perform(get("/user")
//                .contentType(MediaType.TEXT_HTML))
//                .andExpect(status().is3xxRedirection());
//    }
//    @Test
//    public void redirectionUserTest() throws Exception {
//        mockMvc.perform(get("/user/friends")
//                .contentType(MediaType.TEXT_HTML))
//                .andExpect(status().is3xxRedirection());
//    }
//}
