package com.ssafy.trippy.user.model.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.trippy.user.model.UserDto;
import com.ssafy.trippy.user.model.UserResponse;

public interface UserService {
//	UserDto login(UserDto user);
//	void regist(UserDto user);
//	void modify(UserDto user);
//	void delete(String userId);
//	int checkId(String userId);
	String userLogin(UserDto user) throws Exception;
	String createToken(UserDto user) throws Exception;
	void createUser(UserDto user) throws Exception;
	String socialLogin(String code, String registrationId) throws Exception;
	String getAccessToken(String authorizationCode, String registrationId) throws Exception;
	JsonNode getSocialUserResource(String accessToken, String registrationId) throws Exception;
	UserDto getUserInfo(String userId) throws Exception;
	int checkId(String userId);
	void regist(UserDto user);
	String sendEmail(String userEmail) throws MessagingException, UnsupportedEncodingException;
	String sendEmail_(String userEmail, String string, String string2);
}
