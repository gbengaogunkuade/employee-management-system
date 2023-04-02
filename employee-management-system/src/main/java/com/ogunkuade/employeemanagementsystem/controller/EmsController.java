package com.ogunkuade.employeemanagementsystem.controller;


import com.ogunkuade.employeemanagementsystem.dto.EmsNewRequest;
import com.ogunkuade.employeemanagementsystem.dto.EmsUpdateRequest;
import com.ogunkuade.employeemanagementsystem.service.EmsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class EmsController {

    private final EmsService emsService;

    public EmsController(EmsService emsService) {
        this.emsService = emsService;
    }

    @GetMapping({ "/", "/home"})
    public String gettingHome(Model model){
        return emsService.getHome(model);
    }

    @GetMapping("/{username}")
    public String gettingEms_GUI(HttpServletRequest request, @PathVariable String username, Model model) throws Exception {
        return emsService.getEms_GUI(request, username, model);
    }

    @GetMapping("/all")
    public String gettingAllEms_GUI(HttpServletRequest request, Model model) throws Exception {
        return emsService.getAllEms_GUI(request, model);
    }

    @GetMapping("/registration")
    public String creatingEms_GUI(Model model){
        return emsService.createEms_GUI(model);
    }

    @PostMapping("/registration")
    public String creatingEms_GUISuccess(HttpServletRequest request, @Valid EmsNewRequest emsNewRequest, BindingResult bindingResult, Model model) {
        return emsService.creatingEms_GUISuccess(request, emsNewRequest, bindingResult, model);
    }

    @GetMapping("/{username}/update")
    public String updatingEms_GUI(HttpServletRequest request, @PathVariable String username, Model model) throws Exception {
        return emsService.updateEms_GUI(request, username, model);
    }

    @PostMapping("/{username}/update")
    public String updatingEms_GUISuccess(HttpServletRequest request, @Valid EmsUpdateRequest emsUpdateRequest, BindingResult bindingResult, @PathVariable String username, Model model) throws Exception {
        return emsService.updateEms_GUISuccess(request, emsUpdateRequest, bindingResult, username, model);
    }

    @PostMapping("/{username}/delete")
    public String deletingEms_GUI(HttpServletRequest request, @PathVariable String username, Model model) throws Exception {
        return emsService.deleteEms_GUI(request, username, model);
    }

    @PostMapping("/{username}/to-user")
    public String convertingAdminToUser(HttpServletRequest request, @PathVariable String username, Model model){
        return emsService.convertAdminToUser(request, username, model);
    }

    @PostMapping("/{username}/to-admin")
    public String convertingUserToAdmin(HttpServletRequest request, @PathVariable String username, Model model){
        return emsService.convertUserToAdmin(request, username, model);
    }

}
