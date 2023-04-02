package com.ogunkuade.employeemanagementsystem.service;


import com.ogunkuade.employeemanagementsystem.dto.*;
import com.ogunkuade.employeemanagementsystem.entity.Role;
import com.ogunkuade.employeemanagementsystem.entity.User;
import com.ogunkuade.employeemanagementsystem.enums.RoleType;
import com.ogunkuade.employeemanagementsystem.exception.UnauthorizedRequestException;
import com.ogunkuade.employeemanagementsystem.exception.UnmatchedPasswordException;
import com.ogunkuade.employeemanagementsystem.exception.UserAlreadyExistsException;
import com.ogunkuade.employeemanagementsystem.exception.UserNotFoundException;
import com.ogunkuade.employeemanagementsystem.feignclient.AddressClient;
import com.ogunkuade.employeemanagementsystem.feignclient.EmailClient;
import com.ogunkuade.employeemanagementsystem.repository.UserRepository;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class EmsRestService {

    private final LoadBalancerClient loadBalancerClient;
    private final UserRepository userRepository;
    private final AddressClient addressClient;
    private final EmailClient emailClient;
    private final PrincipalUserDetailsService principalUserDetailsService;
    private final PasswordEncoder passwordEncoder;


    public EmsRestService(LoadBalancerClient loadBalancerClient, UserRepository userRepository, AddressClient addressClient, EmailClient emailClient, PrincipalUserDetailsService principalUserDetailsService, PasswordEncoder passwordEncoder) {
        this.loadBalancerClient = loadBalancerClient;
        this.userRepository = userRepository;
        this.addressClient = addressClient;
        this.emailClient = emailClient;
        this.principalUserDetailsService = principalUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    public EmsResponse getEms(String username) throws UserNotFoundException, UnauthorizedRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> authorityList = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList());

        if(authentication.getName().equals(username) || authorityList.contains("SCOPE_ROLE_ADMIN")){
            if(userRepository.existsByUsername(username)){
                EmsResponse emsResponse = new EmsResponse();
                User user = userRepository.findByUsername(username);
                UserResponse userResponse = new UserResponse();
                userResponse.setId(user.getId());
                userResponse.setUsername(user.getUsername());
                userResponse.setFirstname(user.getFirstname());
                userResponse.setLastname(user.getLastname());
                userResponse.setGender(user.getGender());
                userResponse.setEmail(user.getEmail());
                Long id = user.getId();
                AddressResponse addressResponse = addressClient.gettingAddressById(id);
                if(userResponse != null){
                    emsResponse.setUserResponse(userResponse);
                }
                if(addressResponse != null){
                    emsResponse.setAddressResponse(addressResponse);
                }
                return emsResponse;
            } else{
                throw new UserNotFoundException(String.format("a user with the username %s does not exist", username));
            }
        } else{
            throw new UnauthorizedRequestException(String.format("Only %s or An Admin can perform this task", username));
        }
    }



    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public List<EmsResponse> getAllEms() {
        List<EmsResponse> emsResponseList = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();
        for(User user : userList){
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setUsername(user.getUsername());
            userResponse.setFirstname(user.getFirstname());
            userResponse.setLastname(user.getLastname());
            userResponse.setGender(user.getGender());
            userResponse.setEmail(user.getEmail());
            userResponseList.add(userResponse);
        }
        ListIterator<UserResponse> iteratedUserResponseList = userResponseList.listIterator(0);
        UserResponse i;
        while(iteratedUserResponseList.hasNext()){
            i = iteratedUserResponseList.next();
            AddressResponse addressResponse = addressClient.gettingAddressById(i.getId());
            EmsResponse emsResponse = new EmsResponse();
            emsResponse.setUserResponse(i);
            emsResponse.setAddressResponse(addressResponse);
            emsResponseList.add(emsResponse);
        }
        return emsResponseList;
    }


    public EmsResponse createEms(EmsNewRequest emsNewRequest) throws UserAlreadyExistsException, UnmatchedPasswordException {
        if(userRepository.existsByUsername(emsNewRequest.getUsername())){
            throw new UserAlreadyExistsException(String.format("a user with the username %s already exist", emsNewRequest.getUsername()));
        } else{

            if(!emsNewRequest.getPassword().equals(emsNewRequest.getPassword2())){
                throw new UnmatchedPasswordException(String.format("Passwords do not match"));
            }
            EmsResponse emsResponse = new EmsResponse();
            User user = new User();
            user.setUsername(emsNewRequest.getUsername());
            user.setPassword(passwordEncoder.encode(emsNewRequest.getPassword()));
            user.setPassword2(passwordEncoder.encode(emsNewRequest.getPassword2()));
            user.setFirstname(emsNewRequest.getFirstname());
            user.setLastname(emsNewRequest.getLastname());
            user.setGender(emsNewRequest.getGender());
            user.setEmail(emsNewRequest.getEmail());
            if(emsNewRequest.getUsername().equals("gbenga") || emsNewRequest.getUsername().equals("rotimi")){
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
            User savedUser = userRepository.save(user);
            UserResponse userResponse = new UserResponse();
            userResponse.setId(savedUser.getId());
            userResponse.setUsername(savedUser.getUsername());
            userResponse.setFirstname(savedUser.getFirstname());
            userResponse.setLastname(savedUser.getLastname());
            userResponse.setGender(savedUser.getGender());
            userResponse.setEmail(savedUser.getEmail());
            AddressRequest addressRequest = new AddressRequest();
            addressRequest.setLane1(emsNewRequest.getLane1());
            addressRequest.setLane2(emsNewRequest.getLane2());
            addressRequest.setZip(emsNewRequest.getZip());
            addressRequest.setState(emsNewRequest.getState());
            AddressResponse addressResponse = addressClient.savingAddress(addressRequest);
            emsResponse.setUserResponse(userResponse);
            emsResponse.setAddressResponse(addressResponse);
            //send a new ems confirmation mail
            emailClient.sendingEmail(emsNewRequest.getEmail(), emsNewRequest.getFirstname(), emsNewRequest.getLastname());
            return emsResponse;
        }
    }


    public EmsResponse updateEms(EmsUpdateRequest emsUpdateRequest, String username) throws UserNotFoundException, UnauthorizedRequestException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> authorityList = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList());

        if(authentication.getName().equals(username) || authorityList.contains("SCOPE_ROLE_ADMIN")){
            EmsResponse emsResponse = new EmsResponse();
            if(userRepository.existsByUsername(username)){
                User user = userRepository.findByUsername(username);
                user.setFirstname(emsUpdateRequest.getFirstname());
                user.setLastname(emsUpdateRequest.getLastname());
                user.setGender(emsUpdateRequest.getGender());
                user.setEmail(emsUpdateRequest.getEmail());
                User savedUser = userRepository.save(user);
                UserResponse userResponse = new UserResponse();
                userResponse.setId(savedUser.getId());
                userResponse.setUsername(savedUser.getUsername());
                userResponse.setFirstname(savedUser.getFirstname());
                userResponse.setLastname(savedUser.getLastname());
                userResponse.setGender(savedUser.getGender());
                userResponse.setEmail(savedUser.getEmail());
                AddressResponse addressResponse = null;
                if(addressClient.checkForId(savedUser.getId())){
                    AddressRequest addressRequest = new AddressRequest();
                    addressRequest.setLane1(emsUpdateRequest.getLane1());
                    addressRequest.setLane2(emsUpdateRequest.getLane2());
                    addressRequest.setZip(emsUpdateRequest.getZip());
                    addressRequest.setState(emsUpdateRequest.getState());
                    addressResponse = addressClient.updatingAddressById(addressRequest, savedUser.getId());
                }
                emsResponse.setUserResponse(userResponse);
                emsResponse.setAddressResponse(addressResponse);
                return emsResponse;
            } else{
                throw new UserNotFoundException(String.format("a user with the username %s does not exist", username));
            }
        } else{
            throw new UnauthorizedRequestException(String.format("Only %s or An Admin can perform this task", username));
        }
    }



    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public String deleteEms(@PathVariable String username) throws UserNotFoundException {
        String message = null;
        if(userRepository.existsByUsername(username)){
            User user = userRepository.findByUsername(username);
            userRepository.delete(user);
            message = "only user was successfully deleted";
            if(addressClient.checkForId(user.getId())){
                addressClient.deletingAddressById(user.getId());
                message = "user with address was successfully deleted";
            }
            return message;
        } else{
            throw new UserNotFoundException(String.format("a user with the username %s does not exist", username));
        }
    }


    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public String convertAdminToUser(String username) throws UserNotFoundException {
        if(userRepository.existsByUsername(username)){
            User user = userRepository.findByUsername(username);
            Collection<Role> userRolesCollection = user.getRoles();
            userRolesCollection.removeIf(role -> role.getName().equals("ADMIN"));
            user.setRoles(userRolesCollection);
            userRepository.save(user);
            return "Admin successfully converted to a User";
        } else{
            throw new UserNotFoundException(String.format("a user with the username %s does not exist", username));
        }
    }


    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public String convertUserToAdmin(String username) throws UserNotFoundException {
        if(userRepository.existsByUsername(username)){
            User user = userRepository.findByUsername(username);
            Collection<Role> userRolesCollection = user.getRoles();
            List<String> userRolesNamesCollection = userRolesCollection.stream()
                    .map(role -> role.getName()).collect(Collectors.toList());
            if(!userRolesNamesCollection.contains("ADMIN")){
                userRolesCollection.add(new Role(RoleType.ADMIN.name()));
                user.setRoles(userRolesCollection);
                userRepository.save(user);
            }
            return "User successfully converted to an Admin";
        } else{
            throw new UserNotFoundException(String.format("a user with the username %s does not exist", username));
        }
    }



}
