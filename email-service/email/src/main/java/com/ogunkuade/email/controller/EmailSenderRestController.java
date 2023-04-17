package com.ogunkuade.email.controller;


import com.ogunkuade.email.service.EmailSenderRestService;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class EmailSenderRestController {


private final EmailSenderRestService emailSenderRestService;


    public EmailSenderRestController(EmailSenderRestService emailSenderRestService) {
        this.emailSenderRestService = emailSenderRestService;
    }




    @PostMapping("/sendTo")
    public String sendingEmail(@RequestParam String email,@RequestParam String firstname,@RequestParam String lastname) throws MessagingException, IOException {
        return emailSenderRestService.sendSimpleEmailAsHTML(email, firstname, lastname);
    }







}
