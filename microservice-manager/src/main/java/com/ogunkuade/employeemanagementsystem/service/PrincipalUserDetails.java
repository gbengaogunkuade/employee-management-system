package com.ogunkuade.employeemanagementsystem.service;


import com.ogunkuade.employeemanagementsystem.entity.Role;
import com.ogunkuade.employeemanagementsystem.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class PrincipalUserDetails implements UserDetails {

    private User user;

    public PrincipalUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Collection<Role> userRoles = user.getRoles();
        for(Role role : userRoles){
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName());
            authorities.add(authority);
        }
        return authorities;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getPassword2() {
        return user.getPassword2();
    }

    public String getFirstname(){
        return user.getFirstname();
    }

    public String getLastname(){
        return user.getLastname();
    }

    public String getGender(){
        return user.getGender();
    }

    public String getEmail() {
        return user.getEmail();
    }


}
