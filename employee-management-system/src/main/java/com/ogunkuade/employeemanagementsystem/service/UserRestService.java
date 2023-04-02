package com.ogunkuade.employeemanagementsystem.service;


import com.ogunkuade.employeemanagementsystem.entity.Role;
import com.ogunkuade.employeemanagementsystem.entity.User;
import com.ogunkuade.employeemanagementsystem.enums.RoleType;
import com.ogunkuade.employeemanagementsystem.exception.UnmatchedPasswordException;
import com.ogunkuade.employeemanagementsystem.exception.UserAlreadyExistsException;
import com.ogunkuade.employeemanagementsystem.exception.UserNotFoundException;
import com.ogunkuade.employeemanagementsystem.repository.UserRepository;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Arrays;
import java.util.List;

@Service
public class UserRestService {

    private final LoadBalancerClient loadBalancerClient;
    private final UserRepository userRepository;
    private final PrincipalUserDetailsService principalUserDetailsService;
    private final PasswordEncoder passwordEncoder;


    public UserRestService(LoadBalancerClient loadBalancerClient, UserRepository userRepository, PrincipalUserDetailsService principalUserDetailsService, PasswordEncoder passwordEncoder) {
        this.loadBalancerClient = loadBalancerClient;
        this.userRepository = userRepository;
        this.principalUserDetailsService = principalUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public Boolean checkForId(Long id){
        return userRepository.existsById(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public Boolean checkForUsername(String username){
        return userRepository.existsByUsername(username);
    };

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public User getUser(String username) throws UserNotFoundException {
        if(userRepository.existsByUsername(username)){
            return userRepository.findByUsername(username);
        } else{
            throw new UserNotFoundException(String.format("a user with the username %s does not exist", username));
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public User createUser(User user) throws UserAlreadyExistsException, UnmatchedPasswordException {

        if(userRepository.existsByUsername(user.getUsername())){
            throw new UserAlreadyExistsException(String.format("a user with the username %s already exist", user.getUsername()));
        }
        if(!user.getPassword().equals(user.getPassword2())){
            throw new UnmatchedPasswordException(String.format("Passwords do not match"));
        }

        User userNew = new User();
        userNew.setUsername(user.getUsername());
        userNew.setPassword(passwordEncoder.encode(user.getPassword()));
        userNew.setPassword2(passwordEncoder.encode(user.getPassword2()));
        userNew.setFirstname(user.getFirstname());
        userNew.setLastname(user.getLastname());
        userNew.setGender(user.getGender());
        userNew.setEmail(user.getEmail());
        if(user.getUsername().equals("gbenga") || user.getUsername().equals("kazeem")){
            user.setRoles(
                    Arrays.asList(
                            new Role(RoleType.USER.name()),
                            new Role(RoleType.ADMIN.name())
                    )
            );
        } else{
            user.setRoles(
                    Arrays.asList(
                            new Role(RoleType.USER.name())
                    )
            );
        }
        return userRepository.save(user);
    }


    public User updateUser(User user, String username) throws UserNotFoundException {
        if(userRepository.existsByUsername(username)){
            User userToBeUpdated = userRepository.findByUsername(username);
            userToBeUpdated.setFirstname(user.getFirstname());
            userToBeUpdated.setLastname(user.getLastname());
            userToBeUpdated.setGender(user.getGender());
            userToBeUpdated.setEmail(user.getEmail());
            return userRepository.save(userToBeUpdated);
        } else{
            throw new UserNotFoundException(String.format("a user with the username %s does not exist", username));
        }
    }


    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public String deleteUser(@PathVariable String username) throws UserNotFoundException {
        if(userRepository.existsByUsername(username)){
            User user = userRepository.findByUsername(username);
            userRepository.delete(user);
            return "user successfully deleted";
        } else{
            throw new UserNotFoundException(String.format("a user with the username %s does not exist", username));
        }
    }



}
