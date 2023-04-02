package com.ogunkuade.email.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.IOException;



@Service
public class EmailSenderService {

    private final JavaMailSender javaMailSender;


    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public String sendSimpleEmailAsHTML(String email) throws MessagingException, IOException {
        String formSubject = "Account Creation Successful";
        String formMessageBody = "Your Email has been successfully created";

        String htmlFormatedMessageBody = "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\"\n" +
                "          content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "    <title>Document</title>\n" +
                "\n" +
                "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC\" crossorigin=\"anonymous\">\n" +
                "\n" +
                "</head>\n" +
                "<body style=\"padding: 15px;\">\n" +
                "<br>\n" +
                "<div style=\"background-color: black; color: white; font-size: 20px; height: 1000px; width: 600px; padding: 15px;\">\n" +
                "    <img src=\"https://cdn.pixabay.com/photo/2023/02/10/08/00/woman-7780330_960_720.png\" alt=\"\" style=\"width: 100%; height: 500px;\">\n" +
                "    <br><br>\n" +
                "    <span style=\"color: white;\">Dear </span>\n" +
                "    <span style=\"color:deeppink;\">\n" +
                "firstname".toUpperCase() + " " + "lastname".toUpperCase() + "\n" +
                "    </span>\n" +
                "    <br><br>\n" +
                "    <span style=\"color: white;\">\n" +
                "        Thanks for contacting us.\n" +
                "        <br><br>\n" +
                "        We have received and will respond to your message below very soon via this email address provided: \n" +
                email + "\n" +
                "    </span>\n" +
                "    <br><br>\n" +
                "    <span style=\"color: white;\"><b>MESSAGE:</b></span>\n" +
                "    <br>\n" +
                formMessageBody + "\n" +
                "    <br><br>\n" +
                "    <a href=\"#\">Go back to our site</a>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "<div style=\"background-color: black; color: white; font-size: 20px; width: 600px; padding: 15px; border-top: 2px solid white; text-align: center\">\n" +
                "    <!--FOOTER FRAGMENT-->\n" +
                "    <span style=\"color:#ffffff\">© 2022 - Gbenga Ogunkuade</span>\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>";


        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom("go4realcode@gmail.com", "MrGo4RealCode");
        messageHelper.setTo(email);
        messageHelper.setText(htmlFormatedMessageBody, true);
        messageHelper.setSubject(formSubject);
        javaMailSender.send(message);
        System.out.println("a confirmation mail has been sent to " + email);
        return "redirect:/home?success";
    }






}
