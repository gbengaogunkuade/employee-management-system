package com.ogunkuade.employeemanagementsystem.service;


import com.ogunkuade.employeemanagementsystem.entity.Role;
import com.ogunkuade.employeemanagementsystem.entity.User;
import com.ogunkuade.employeemanagementsystem.enums.RoleType;
import com.ogunkuade.employeemanagementsystem.exception.UserAlreadyExistsException;
import com.ogunkuade.employeemanagementsystem.exception.UserNotFoundException;
import com.ogunkuade.employeemanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class UserRestServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserRestService userRestService;

    private User user1;
    private User user2;
    private User user3;
    private List<User> userList;


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


        user2 = new User();
        user2.setId(2L);
        user2.setUsername("kazeem");
        user2.setPassword("p@s54s+W0rd#");
        user2.setPassword2("p@s54s+W0rd#");
        user2.setFirstname("Kazeem");
        user2.setLastname("Akinrinde");
        user2.setGender("Male");
        user2.setEmail("kazeem@code.com");
        user2.setRoles(
                Arrays.asList(
                        new Role(RoleType.USER.name())
                )
        );


        user3 = new User();
        user3.setId(3L);
        user3.setUsername("tosin");
        user3.setPassword("p@s54s+W0rd#");
        user3.setPassword2("p@s54s+W0rd#");
        user3.setFirstname("Tosin");
        user3.setLastname("Ogunkuade");
        user3.setGender("Female");
        user3.setEmail("tosin@yahoo.com");
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
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void test_checkForId() {
        Boolean status;
        when(userRepository.existsById(user1.getId())).thenReturn(true);
        status = userRestService.checkForId(user1.getId());
        assertThat(status).isTrue();
        verify(userRepository).existsById(user1.getId());
    }


    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void test_checkForUsername() {
        Boolean status;
        when(userRepository.existsByUsername(user1.getUsername())).thenReturn(true);
        status = userRestService.checkForUsername(user1.getUsername());
        assertThat(status).isTrue();
        verify(userRepository).existsByUsername(user1.getUsername());
    }


    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void test_getUser() {
        User foundUser;
        when(userRepository.existsByUsername(user1.getUsername())).thenReturn(true);
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(user1);
        foundUser = userRestService.getUser(user1.getUsername());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("gbenga@ok.com");
        assertThat(foundUser.getFirstname()).isEqualTo("Gbenga");
        verify(userRepository).existsByUsername(user1.getUsername());
        verify(userRepository).findByUsername(user1.getUsername());
    }



    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void test_getAllUsers() {
        List<User> foundUserList;
        when(userRepository.findAll()).thenReturn(userList);
        foundUserList = userRestService.getAllUsers();
        assertThat(foundUserList).isNotNull();
        assertThat(foundUserList.size()).isEqualTo(userList.size());
        assertThat(foundUserList.get(1).getPassword()).isEqualTo("p@s54s+W0rd#");
        verify(userRepository).findAll();
    }


    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void test_createUser() {
        User savedUser;
        when(userRepository.existsByUsername(user1.getUsername())).thenReturn(false);
        when(userRepository.save(user1)).thenReturn(user1);
        savedUser = userRestService.createUser(user1);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("gbenga");
        assertThat(savedUser.getPassword()).isEqualTo("p@s54s+W0rd#");
        verify(userRepository).existsByUsername(user1.getUsername());
        verify(userRepository).save(user1);
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void test_createUser_UserAlreadyExists() {
        when(userRepository.existsByUsername(user1.getUsername())).thenReturn(true);
        UserAlreadyExistsException userAlreadyExistsException = assertThrows(UserAlreadyExistsException.class, () -> userRestService.createUser(user1));
        assertTrue(userAlreadyExistsException.getMessage().equals(String.format("a user with the username %s already exist", "gbenga")));
        verify(userRepository).existsByUsername(user1.getUsername());
    }


    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void test_updateUser() {
        User updatedUser;
        when(userRepository.existsByUsername(user1.getUsername())).thenReturn(true);
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(user1);
        user1.setFirstname("John");
        user1.setLastname("Odutayo");
        when(userRepository.save(user1)).thenReturn(user1);
        updatedUser = userRestService.updateUser(user1, user1.getUsername());
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getFirstname()).isEqualTo("John");
        assertThat(updatedUser.getLastname()).isEqualTo("Odutayo");
        verify(userRepository).existsByUsername(user1.getUsername());
        verify(userRepository).findByUsername(user1.getUsername());
        verify(userRepository).save(user1);
    }


    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void test_updateUser_NotFound() {
        when(userRepository.existsByUsername(user1.getUsername())).thenReturn(false);
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> userRestService.updateUser(user1, user1.getUsername()));
        assertTrue(userNotFoundException.getMessage().equals(String.format("a user with the username %s does not exist", "gbenga")));
        verify(userRepository).existsByUsername(user1.getUsername());
    }


    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void test_deleteUser() {
        String returnedMessage;
        when(userRepository.existsByUsername(user1.getUsername())).thenReturn(true);
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(user1);
        doNothing().when(userRepository).delete(user1);
        returnedMessage = userRestService.deleteUser(user1.getUsername());
        assertThat(returnedMessage).isEqualTo("user successfully deleted");
        verify(userRepository).existsByUsername(user1.getUsername());
        verify(userRepository).findByUsername(user1.getUsername());
        verify(userRepository, times(1)).delete(user1);
    }


    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void test_deleteUser_NotFound() {
        when(userRepository.existsByUsername(user1.getUsername())).thenReturn(false);
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> userRestService.deleteUser(user1.getUsername()));
        assertTrue(userNotFoundException.getMessage().equals(String.format("a user with the username %s does not exist", "gbenga")));
        verify(userRepository).existsByUsername(user1.getUsername());
    }



}




