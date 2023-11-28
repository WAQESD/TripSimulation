package com.ssafy.trippy.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.ssafy.trippy.user.model.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
public class OauthController {
    private final Environment env;
    private final UserService userService;
    
//    @GetMapping("/redirect/{registrationId}")
//    public RedirectView redirect(@PathVariable String registrationId) {
//        log.debug("redirect/"+registrationId);
//        RedirectView redirectView = new RedirectView();
//        redirectView.setUrl(env.getProperty("oauth2."+registrationId+".login-uri"));
//        log.debug(redirectView.getUrl());
//        return redirectView;
//    }
  
    //우선 다 카카오로 설정 
    
    @GetMapping("/redirect/kakao")
    public RedirectView kakaoRedirect() {
        log.debug("redirect/kakao");
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(env.getProperty("oauth2.kakao.login-uri"));
        log.debug(redirectView.getUrl());
        return redirectView;
    }
    
//    @GetMapping("/callback/{registrationId}")
//    public ResponseEntity<Map<String,String>> socialLogin(@RequestParam String code, @PathVariable String registrationId) throws Exception {
//        log.debug("/oauth/"+registrationId);
//        Map<String,String> result=new HashMap<>();
//        result.put("access_token",userService.socialLogin(code, registrationId));
//        return ResponseEntity.ok().body(result);
//    }
    
    @GetMapping("/callback/kakao")
    public ResponseEntity<Map<String,String>> socialLogin(@RequestParam String code) throws Exception {
        log.debug("/oauth/kakao");
        Map<String,String> result=new HashMap<>();
        result.put("access_token",userService.socialLogin(code, "kakao"));
        return ResponseEntity.ok().body(result);
    }
}