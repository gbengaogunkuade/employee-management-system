package com.ogunkuade.employeemanagementsystem.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogunkuade.employeemanagementsystem.entity.Role;
import com.ogunkuade.employeemanagementsystem.entity.User;
import com.ogunkuade.employeemanagementsystem.enums.RoleType;
import com.ogunkuade.employeemanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser(username="admin", authorities = {"SCOPE_ROLE_ADMIN"})
public class UserControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;


    private User user1;
    private User user2;


    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("gbenga");
        user1.setPassword("p@s54s+W0rd#");
        user1.setPassword2("p@s54s+W0rd#");
        user1.setFirstname("Gbenga");
        user1.setLastname("Ogunkuade");
        user1.setGender("Male");
        user1.setEmail("gbenga@ok.com");
        user1.setRoles(
                Arrays.asList(
                        new Role(RoleType.USER.name()),
                        new Role(RoleType.ADMIN.name())
                )
        );
        userRepository.save(user1);


        user2 = new User();
        user2.setId(2L);
        user2.setUsername("bola");
        user2.setPassword("p@s565s+W0rd#");
        user2.setPassword2("p@s565s+W0rd#");
        user2.setFirstname("Bola");
        user2.setLastname("Akinyemi");
        user2.setGender("Female");
        user2.setEmail("bola@ok.com");
        user2.setRoles(
                Arrays.asList(
                        new Role(RoleType.USER.name())
                )
        );
        userRepository.save(user2);
    }


    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }



    @Test
    void test_checkingForUsername() throws Exception {
        mockMvc.perform(
                        get("/api/users/username/{username}/check", user1.getUsername())
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));   //return boolean
    }


    @Test
    void test_gettingUser() throws Exception {
        mockMvc.perform(
                        get("/api/users/{username}", user1.getUsername())
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Gbenga")) //return json
                .andExpect(jsonPath("$.lastname").value("Ogunkuade"))
                .andExpect(jsonPath("$.gender").value("Male"));
    }


    @Test
    void test_gettingAllUsers() throws Exception {
        mockMvc.perform(
                        get("/api/users/all")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstname").value("Gbenga"))
                .andExpect(jsonPath("$[0].gender").value("Male"))
                .andExpect(jsonPath("$[1].firstname").value("Bola"))
                .andExpect(jsonPath("$[1].lastname").value("Akinyemi"))
                .andExpect(jsonPath("$[0].email").value("gbenga@ok.com"));
    }


    @Test
    public void test_creatingUser() throws Exception {
        User user3 = new User();
        user3.setId(3L);
        user3.setUsername("rotimi");
        user3.setPassword("p@s565s+W0rd#");
        user3.setPassword2("p@s565s+W0rd#");
        user3.setFirstname("Rotimi");
        user3.setLastname("Martins");
        user3.setGender("Male");
        user3.setEmail("rotimi@ok.com");
        user3.setRoles(
                Arrays.asList(
                        new Role(RoleType.USER.name())
                )
        );
        String user1_String = objectMapper.writeValueAsString(user3);
        mockMvc.perform(
                        post("/api/users/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(user1_String)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Rotimi"))
                .andExpect(jsonPath("$.lastname").value("Martins"))
                .andExpect(jsonPath("$.email").value("rotimi@ok.com"));
    }


    @Test
    public void test_updatingUser() throws Exception {
        user1.setLastname("Akinsoyinu");
        user1.setEmail("akinsoyinu@ok.com");
        String user1_String = objectMapper.writeValueAsString(user1);
        mockMvc.perform(
                        put("/api/users/{username}/update", user1.getUsername())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(user1_String)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastname").value("Akinsoyinu"))
                .andExpect(jsonPath("$.email").value("akinsoyinu@ok.com"))
                .andExpect(jsonPath("$.gender").value("Male"));
    }


    @Test
    void test_deletingUser() throws Exception {
        User user4 = new User();
        user4.setId(4L);
        user4.setUsername("seun");
        user4.setPassword("p@s565s+W0rd#");
        user4.setPassword2("p@s565s+W0rd#");
        user4.setFirstname("Seun");
        user4.setLastname("Lawrence");
        user4.setGender("Female");
        user4.setEmail("seun@ok.com");
        user4.setRoles(
                Arrays.asList(
                        new Role(RoleType.USER.name())
                )
        );
        userRepository.save(user4);

        mockMvc.perform(
                        delete("/api/users/{username}/delete", user4.getUsername())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }




}
