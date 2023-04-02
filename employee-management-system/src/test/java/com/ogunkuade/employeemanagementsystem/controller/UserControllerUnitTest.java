package com.ogunkuade.employeemanagementsystem.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogunkuade.employeemanagementsystem.entity.Role;
import com.ogunkuade.employeemanagementsystem.entity.User;
import com.ogunkuade.employeemanagementsystem.enums.RoleType;
import com.ogunkuade.employeemanagementsystem.service.UserRestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserRestController.class)
@WithMockUser(username="admin", authorities = {"SCOPE_ROLE_ADMIN"})
public class UserControllerUnitTest {


    @MockBean
    private UserRestService userRestService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    User user1 = new User();
    User user2 = new User();
    User user3 = new User();
    List<User> userList;


    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("gbenga");
        user1.setPassword("p@ssworD5");
        user1.setPassword2("p@ssworD5");
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

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("kazeem");
        user2.setPassword("p@ssworD5");
        user2.setPassword2("p@ssworD5");
        user2.setFirstname("Kazeem");
        user2.setLastname("Akinrinde");
        user2.setGender("Male");
        user2.setEmail("kazeem@ok.com");
        user2.setRoles(
                Arrays.asList(
                        new Role(RoleType.USER.name())
                )
        );

        user3 = new User();
        user3.setId(3L);
        user3.setUsername("tosin");
        user3.setPassword("p@ssworD5");
        user3.setPassword2("p@ssworD5");
        user3.setFirstname("Tosin");
        user3.setLastname("Olakunle");
        user3.setGender("Female");
        user3.setEmail("tosin@ok.com");
        user3.setRoles(
                Arrays.asList(
                        new Role(RoleType.USER.name())
                )
        );

        userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
    }



    @Test
    void test_checkingForUsername() throws Exception {
        when(userRestService.checkForUsername(user1.getUsername())).thenReturn(true);
        mockMvc.perform(
                get("/api/users/username/{username}/check", user1.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));   //return boolean
        verify(userRestService).checkForUsername(eq(user1.getUsername()));
    }


    @Test
    void test_gettingUser() throws Exception {
        when(userRestService.getUser(user1.getUsername())).thenReturn(user1);
        mockMvc.perform(
                get("/api/users/{username}", user1.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Gbenga")) //return json
                .andExpect(jsonPath("$.lastname").value("Ogunkuade"))
                .andExpect(jsonPath("$.gender").value("Male"));
        verify(userRestService).getUser(eq(user1.getUsername()));
    }


    @Test
    void test_gettingAllUsers() throws Exception {
        when(userRestService.getAllUsers()).thenReturn(userList);
        mockMvc.perform(
                get("/api/users/all")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstname").value("Gbenga"))
                .andExpect(jsonPath("$[0].gender").value("Male"))
                .andExpect(jsonPath("$[1].firstname").value("Kazeem"))
                .andExpect(jsonPath("$[2].firstname").value("Tosin"))
                .andExpect(jsonPath("$[2].gender").value("Female"));
        verify(userRestService).getAllUsers();
    }


    @Test
    public void test_creatingUser() throws Exception {
        when(userRestService.createUser(user1)).thenReturn(user1);
        String user1_String = objectMapper.writeValueAsString(user1);
        mockMvc.perform(
                        post("/api/users/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(user1_String)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
        verify(userRestService).createUser(any(User.class));
    }


    @Test
    public void test_updatingUser() throws Exception {
        when(userRestService.updateUser(user1, user1.getUsername())).thenReturn(user1);
        String user1_String = objectMapper.writeValueAsString(user1);
        mockMvc.perform(
                        put("/api/users/{username}/update", user1.getUsername())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(user1_String)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
        verify(userRestService).updateUser(any(User.class), eq(user1.getUsername()));
    }


    @Test
    void test_deletingUser() throws Exception {
        when(userRestService.deleteUser(user1.getUsername())).thenReturn("user successfully deleted");
        mockMvc.perform(
                delete("/api/users/{username}/delete", user1.getUsername())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
        verify(userRestService).deleteUser(eq(user1.getUsername()));
    }





}
