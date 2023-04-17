package com.ogunkuade.employeemanagementsystem.feignclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "EMAIL-SERVICE", path = "/email-app/api")
public interface EmailClient {


    @PostMapping("/sendTo")
    String sendingEmail(@RequestParam String email,@RequestParam String firstname,@RequestParam String lastname);



}


