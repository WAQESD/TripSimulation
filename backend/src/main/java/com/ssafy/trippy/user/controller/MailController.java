//package com.ssafy.trippy.user.controller;
//
//
//
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.ssafy.trippy.user.model.service.MailService;
//
//import lombok.RequiredArgsConstructor;
//
//@CrossOrigin("http://localhost:5173")
//@RestController
//@RequestMapping("/user")
//@RequiredArgsConstructor
//public class MailController {
//
//    private final MailService mailService;
//
//    @ResponseBody
//    @PostMapping("/mail")
//    public String MailSend(String mail){
//
//        int number = mailService.sendMail(mail);
//
//        String num = "" + number;
//
//        return num;
//    }
//
//}