package com.ogunkuade.employeemanagementsystem.controller;


import com.ogunkuade.employeemanagementsystem.dto.EmsNewRequest;
import com.ogunkuade.employeemanagementsystem.dto.EmsResponse;
import com.ogunkuade.employeemanagementsystem.dto.EmsUpdateRequest;
import com.ogunkuade.employeemanagementsystem.exception.UnauthorizedRequestException;
import com.ogunkuade.employeemanagementsystem.exception.UnmatchedPasswordException;
import com.ogunkuade.employeemanagementsystem.exception.UserAlreadyExistsException;
import com.ogunkuade.employeemanagementsystem.exception.UserNotFoundException;
import com.ogunkuade.employeemanagementsystem.service.EmsRestService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api")
public class EmsRestController {

    private final EmsRestService emsRestService;

    public EmsRestController(EmsRestService emsRestService) {
        this.emsRestService = emsRestService;
    }

    @GetMapping("/{username}")
    public EmsResponse gettingEms(@PathVariable String username) throws UserNotFoundException, UnauthorizedRequestException {
        return emsRestService.getEms(username);
    }

    @GetMapping("/all")
    public List<EmsResponse> gettingAllEms() {
        return emsRestService.getAllEms();
    }

    @PostMapping("/register")
    public EmsResponse creatingEms(@Valid @RequestBody EmsNewRequest emsNewRequest) throws UserAlreadyExistsException, UnmatchedPasswordException {
        return emsRestService.createEms(emsNewRequest);
    }

    @PutMapping("/{username}/update")
    public EmsResponse updatingEms(@Valid @RequestBody EmsUpdateRequest emsUpdateRequest, @PathVariable String username) throws UserNotFoundException, UnauthorizedRequestException {
        return emsRestService.updateEms(emsUpdateRequest, username);
    }

    @DeleteMapping("/{username}/delete")
    public String deletingEms(@PathVariable String username) throws UserNotFoundException {
        return emsRestService.deleteEms(username);
    }

    @GetMapping("/{username}/to-user")
    public String convertingAdminToUser(@PathVariable String username) throws UserNotFoundException {
        return emsRestService.convertAdminToUser(username);
    }

    @GetMapping("/{username}/to-admin")
    public String convertingUserToAdmin(@PathVariable String username) throws UserNotFoundException {
        return emsRestService.convertUserToAdmin(username);
    }


}
