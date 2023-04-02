package com.ogunkuade.employeemanagementsystem.repository;


import com.ogunkuade.employeemanagementsystem.entity.Role;
import com.ogunkuade.employeemanagementsystem.entity.User;
import com.ogunkuade.employeemanagementsystem.enums.RoleType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;


    private User user1;
    private User user2;
    private User user3;


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
        userRepository.save(user2);

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
        userRepository.save(user3);
    }

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }


    @Test
    void findByUsername(){
        User foundUser;
        foundUser = userRepository.findByUsername(user1.getUsername());
        assertThat(foundUser.getUsername()).isEqualTo("gbenga");
        assertThat(foundUser.getEmail()).isEqualTo("gbenga@ok.com");
    }

    @Test
    void findByLastnameContainsIgnoreCase(){
        List<User> userList;
        userList = userRepository.findByLastnameContainsIgnoreCase(user1.getLastname());
        assertThat(userList.size()).isEqualTo(2);
        assertThat(userList.get(0).getFirstname()).isEqualTo("Gbenga");
        assertThat(userList.get(1).getFirstname()).isEqualTo("Tosin");
    }

    @Test
    void existsByUsername(){
        Boolean status;
        status = userRepository.existsByUsername("kazeem");
        assertThat(status).isTrue();
    }




}
