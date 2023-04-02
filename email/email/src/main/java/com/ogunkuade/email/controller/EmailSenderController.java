package com.ogunkuade.email.controller;


import com.ogunkuade.email.service.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.io.IOException;


@Controller
@RequestMapping("/email")
public class EmailSenderController {


    private EmailSenderService emailSenderService;


    public EmailSenderController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }



    @GetMapping("/home")
    public String home() {
        return "home";
    }



    @PostMapping("/sendTo")
    public String sendingEmail(@RequestParam(defaultValue = "electro4real@gmail.com") String email) throws MessagingException, IOException {
        return emailSenderService.sendSimpleEmailAsHTML(email);
    }







}
