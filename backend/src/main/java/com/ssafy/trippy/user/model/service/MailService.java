package com.ssafy.trippy.user.model.service;

import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssafy.trippy.user.model.UserDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    private final JavaMailSender javaMailSender;
    private static final String senderEmail= "";
    private static int number;

    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(""); 
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
    
    public String generateVerificationToken(UserDto userDto) {
        String token = UUID.randomUUID().toString();
        // 토큰 저장 로직 (예: 데이터베이스에 저장)
        return token;
    }
    
    public void sendVerificationEmail(UserDto userDto, String siteURL) {
        String subject = "회원가입 이메일 인증";
        String verificationToken = generateVerificationToken(userDto);
        String content = "링크를 클릭하여 이메일을 인증하세요: " + siteURL + "/verify?token=" + verificationToken;
        
        sendEmail(userDto.getUserEmail(), subject, content);
    }
    
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token) {
        // 토큰 유효성 검사 및 사용자 인증 상태 업데이트 로직
        return "verify_success";
    }
    
    
    public static void createNumber(){
        number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    public MimeMessage CreateMail(String mail){
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    
    public int sendMail(String mail){
        MimeMessage message = CreateMail(mail);
        javaMailSender.send(message);

        return number;
    }


    
    
}
