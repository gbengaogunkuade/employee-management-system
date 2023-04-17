package com.ogunkuade.employeemanagementsystem.service;


import com.ogunkuade.employeemanagementsystem.dto.*;
import com.ogunkuade.employeemanagementsystem.entity.Role;
import com.ogunkuade.employeemanagementsystem.entity.User;
import com.ogunkuade.employeemanagementsystem.enums.Gender;
import com.ogunkuade.employeemanagementsystem.enums.RoleType;
import com.ogunkuade.employeemanagementsystem.feignclient.AddressClient;
import com.ogunkuade.employeemanagementsystem.feignclient.EmailClient;
import com.ogunkuade.employeemanagementsystem.repository.RoleRepository;
import com.ogunkuade.employeemanagementsystem.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class EmsService {

    private String newlyRegisteredUsername;
    private final UserRepository userRepository;
    private final LoadBalancerClient loadBalancerClient;
    private final AddressClient addressClient;
    private final EmailClient emailClient;
    private final PrincipalUserDetailsService principalUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public EmsService(UserRepository userRepository, LoadBalancerClient loadBalancerClient, AddressClient addressClient, EmailClient emailClient, PrincipalUserDetailsService principalUserDetailsService, PasswordEncoder passwordEncoder,
                      RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.loadBalancerClient = loadBalancerClient;
        this.addressClient = addressClient;
        this.emailClient = emailClient;
        this.principalUserDetailsService = principalUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    public String getHome(Model model){
        String currentUser = null;
        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            model.addAttribute("currentUser", currentUser);
        }
        return "home";
    }


    public String getEms_GUI(HttpServletRequest request, String username, Model model) throws Exception {
        if(userRepository.existsByUsername(username)){
            String currentUser = null;
            if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
                currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
                model.addAttribute("currentUser", currentUser);
            }

            if(username.equals(currentUser) || request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_SUPERADMIN")){
                EmsResponse emsResponse = new EmsResponse();
                User user = userRepository.findByUsername(username);
                Collection<Role> userRolesCollection = user.getRoles();
                List<String>userRolesNames = userRolesCollection.stream()
                        .map(role -> role.getName()).collect(Collectors.toList());

                if (userRolesNames.contains("ADMIN")){
                    model.addAttribute("adminType", "adminType");
                } else if(userRolesNames.contains("USER")) {
                    model.addAttribute("userType", "userType");
                } else {
                    model.addAttribute("noRole", "noRole");
                }

                UserResponse userResponse = new UserResponse();
                userResponse.setId(user.getId());
                userResponse.setUsername(user.getUsername());
                userResponse.setFirstname(user.getFirstname());
                userResponse.setLastname(user.getLastname());
                userResponse.setGender(user.getGender());
                userResponse.setEmail(user.getEmail());

                Long id = userResponse.getId();
                AddressResponse addressResponse = addressClient.gettingAddressById(id);
                if(userResponse != null){
                    emsResponse.setUserResponse(userResponse);
                }
                if(addressResponse != null){
                    emsResponse.setAddressResponse(addressResponse);
                }
                model.addAttribute("emsResponse", emsResponse);
                return "user_profile";
            } else{
                return "redirect:/home";
            }
        } else{
            return "redirect:/home";
        }
    }


    public String getAllEms_GUI(HttpServletRequest request, Model model) throws Exception {
        String currentUser = null;
        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            model.addAttribute("currentUser", currentUser);
        }

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
        model.addAttribute("emsResponseList", emsResponseList);
        return "all_users";
    }


    public String createEms_GUI(Model model){
        String currentUser = null;
        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            model.addAttribute("currentUser", currentUser);
        }
        EmsNewRequest emsNewRequest = new EmsNewRequest();
        model.addAttribute("emsNewRequest", emsNewRequest);
        List<String> genderList = Arrays.stream(Gender.values()).map(gender -> gender.getName()).toList();
        model.addAttribute("genderList", genderList);
        model.addAttribute("newlyRegisteredUsername", newlyRegisteredUsername);
        return "registration_form";
    }


    public String creatingEms_GUISuccess(HttpServletRequest request, EmsNewRequest emsNewRequest, BindingResult bindingResult, Model model){
        if(!bindingResult.hasErrors()){
            if(userRepository.existsByUsername(emsNewRequest.getUsername())){
                return "redirect:/registration?duplicateUsername";
            } else{
                if(emsNewRequest.getPassword().equals(emsNewRequest.getPassword2())){
                    User user = new User();
                    user.setUsername(emsNewRequest.getUsername());
                    user.setPassword(passwordEncoder.encode(emsNewRequest.getPassword()));
                    user.setPassword2(passwordEncoder.encode(emsNewRequest.getPassword2()));
                    user.setFirstname(emsNewRequest.getFirstname());
                    user.setLastname(emsNewRequest.getLastname());
                    user.setGender(emsNewRequest.getGender());
                    user.setEmail(emsNewRequest.getEmail());
                    if(emsNewRequest.getUsername().equals("gbenga") || emsNewRequest.getUsername().equals("kazeem")){
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
                    userRepository.save(user);
                    AddressRequest addressRequest = new AddressRequest();
                    addressRequest.setLane1(emsNewRequest.getLane1());
                    addressRequest.setLane2(emsNewRequest.getLane2());
                    addressRequest.setZip(emsNewRequest.getZip());
                    addressRequest.setState(emsNewRequest.getState());
                    addressClient.savingAddress(addressRequest);
                    emailClient.sendingEmail(emsNewRequest.getEmail(), emsNewRequest.getFirstname(), emsNewRequest.getLastname());
                    newlyRegisteredUsername = emsNewRequest.getUsername();
                    return "redirect:/registration?success";
                } else{
                    return "redirect:/registration?unmatchedPassword";
                }
            }
        } else{
            String currentUser = null;
            if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
                currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
                model.addAttribute("currentUser", currentUser);
            }
            List<String> genderList = Arrays.stream(Gender.values()).map(gender -> gender.getName()).toList();
            model.addAttribute("genderList", genderList);
            model.addAttribute("newlyRegisteredUsername", newlyRegisteredUsername);
            return "registration_form";
        }


    }


    public String updateEms_GUI(HttpServletRequest request, String username, Model model) throws Exception {
        if(userRepository.existsByUsername(username)){
            String currentUser = null;
            if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
                currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
                model.addAttribute("currentUser", currentUser);
            }

            if(username.equals(currentUser) || request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_SUPERADMIN")){
                User user = userRepository.findByUsername(username);
                AddressResponse addressResponse = addressClient.gettingAddressById(user.getId());
                EmsUpdateRequest emsUpdateRequest = new EmsUpdateRequest();
                emsUpdateRequest.setFirstname(user.getFirstname());
                emsUpdateRequest.setLastname(user.getLastname());
                emsUpdateRequest.setGender(user.getGender());
                emsUpdateRequest.setEmail(user.getEmail());
                if(addressResponse != null){
                    emsUpdateRequest.setLane1(addressResponse.getLane1());
                    emsUpdateRequest.setLane2(addressResponse.getLane2());
                    emsUpdateRequest.setZip(addressResponse.getZip());
                    emsUpdateRequest.setState(addressResponse.getState());
                }

                List<String> genderList = Arrays.stream(Gender.values()).map(gender -> gender.getName()).toList();
                model.addAttribute("genderList", genderList);

                model.addAttribute("emsUpdateRequest", emsUpdateRequest);
                model.addAttribute("usernameToBeUpdated", username);
                return "update_form";
            } else{
                return "redirect:/home?unauthorizedUpdate";
            }
        } else{
            return "redirect:/home?unauthorizedProfile";
        }
    }


    public String updateEms_GUISuccess(HttpServletRequest request, EmsUpdateRequest emsUpdateRequest, BindingResult bindingResult, String username, Model model) {
        if(!bindingResult.hasErrors()){
            User user = userRepository.findByUsername(username);
            user.setFirstname(emsUpdateRequest.getFirstname());
            user.setLastname(emsUpdateRequest.getLastname());
            user.setGender(emsUpdateRequest.getGender());
            user.setEmail(emsUpdateRequest.getEmail());
            userRepository.save(user);

            AddressRequest addressRequest = new AddressRequest();

            addressRequest.setLane1(emsUpdateRequest.getLane1());
            addressRequest.setLane2(emsUpdateRequest.getLane2());
            addressRequest.setZip(emsUpdateRequest.getZip());
            addressRequest.setState(emsUpdateRequest.getState());
            addressClient.updatingAddressById(addressRequest, user.getId());
            return "redirect:/{username}?success";

        } else{
            String currentUser = null;
            if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
                currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
                model.addAttribute("currentUser", currentUser);
            }
            List<String> genderList = Arrays.stream(Gender.values()).map(gender -> gender.getName()).toList();
            model.addAttribute("genderList", genderList);
            model.addAttribute("usernameToBeUpdated", username);
            return "update_form";
        }
    }


    public String deleteEms_GUI(HttpServletRequest request, @PathVariable String username, Model model) {
        String currentUser = null;
        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            model.addAttribute("currentUser", currentUser);
        }
        if(username.equals(currentUser)){
            User user = userRepository.findByUsername(username);
            userRepository.delete(user);
            addressClient.deletingAddressById(user.getId());
            return "redirect:/logout";
        } else if(request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_SUPERADMIN")){
            User user = userRepository.findByUsername(username);
            userRepository.delete(user);
            addressClient.deletingAddressById(user.getId());
            return "redirect:/home";
        } else{
            return "redirect:/home?unauthorizedDelete";
        }
    }



    public String convertAdminToUser(HttpServletRequest request, @PathVariable String username, Model model){
        String currentUser = null;
        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            model.addAttribute("currentUser", currentUser);
        }
        if(username.equals(currentUser) || request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_SUPERADMIN")){
            User user = userRepository.findByUsername(username);
            Collection<Role> userRolesCollection = user.getRoles();
            userRolesCollection.removeIf(role -> role.getName().equals("ADMIN"));
            user.setRoles(userRolesCollection);
            userRepository.save(user);
            return "redirect:/{username}";
        } else{
            return "redirect:/home?unauthorizedMakeUser";
        }
    }



    public String convertUserToAdmin(HttpServletRequest request, @PathVariable String username, Model model){
        String currentUser = null;
        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            model.addAttribute("currentUser", currentUser);
        }
        if(username.equals(currentUser) || request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_SUPERADMIN")){
            User user = userRepository.findByUsername(username);
            Collection<Role> userRolesCollection = user.getRoles();
            List<String> userRolesNameList = userRolesCollection.stream()
                            .map(role -> role.getName()).collect(Collectors.toList());
            if(!userRolesNameList.contains("ADMIN")){
                userRolesCollection.add(new Role(RoleType.ADMIN.name()));
                user.setRoles(userRolesCollection);
                userRepository.save(user);
            }
            return "redirect:/{username}";
        } else{
            return "redirect:/home?unauthorizedMakeUser";
        }
    }



}
