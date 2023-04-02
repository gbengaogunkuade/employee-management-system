package com.ogunkuade.employeemanagementsystem.service;


import com.ogunkuade.employeemanagementsystem.entity.User;
import com.ogunkuade.employeemanagementsystem.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class PrincipalUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    public PrincipalUserDetailsService(@Lazy PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user != null){
            PrincipalUserDetails principalUserDetails = new PrincipalUserDetails(user);
            return principalUserDetails;
        } else{
            throw new UsernameNotFoundException("Invalid Username or Password");
        }
    }



}
