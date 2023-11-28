package com.ssafy.trippy.user.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.trippy.user.model.UserDto;
import com.ssafy.trippy.user.model.UserResponse;
import com.ssafy.trippy.user.model.service.UserService;

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/user")
public class UserController {
	private final UserService userService;


	public UserController(UserService UserDtoService) {
		this.userService = UserDtoService;
	}
	
	@GetMapping("/idcheck/{UserDtoId}")
	public ResponseEntity<Map<String, String>> idCheck(@PathVariable String UserDtoId) {
		int count = userService.checkId(UserDtoId);
		
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("result", count > 0 ? "Duplicate" : "Unique");
		
		return ResponseEntity.ok().body(resultMap);
	}
	
	//userEmail, userPassword
	@PostMapping("/userLogin")
	public ResponseEntity<String> userLogin(@RequestBody String response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        UserDto loginRequest = mapper.readValue(response, UserDto.class);
        System.out.println("controller: " + loginRequest.getUserEmail());
        System.out.println("controller: " + loginRequest.getUserPassword());
		String loginUser = userService.userLogin(loginRequest);
		
		//토큰 리턴
		System.out.println(loginUser);
		return ResponseEntity.ok().body(loginUser);
	}
	
	@GetMapping("/userLogout")
	public ResponseEntity<String> logout(HttpSession session) {
		session.invalidate();
		return ResponseEntity.ok().build();
	}
	
	
	@PostMapping("/socialLogin")
	public ResponseEntity<String> socialLogin(@RequestBody String response) throws Exception {
//		UserDto loginUserDto = UserDtoService.userLogin(UserDto);
		//String jsonResponse = response.
        ObjectMapper mapper = new ObjectMapper();
        UserResponse loginRequest = mapper.readValue(response, UserResponse.class);
        System.out.println(loginRequest);
		String loginUserDto = userService.socialLogin(loginRequest.getCode(), loginRequest.getRegistrationId());
		
		//토큰 리턴 
		return ResponseEntity.ok().body(loginUserDto);
	}
	
	@PostMapping("/regist")
	public ResponseEntity<String> regist(@RequestBody UserDto UserDto) {
		userService.regist(UserDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	
	@ResponseBody
    @PostMapping("/emailCheck")		// 이 부분은 각자 바꿔주시면 됩니다.
    public String EmailCheck(@RequestBody String email) throws MessagingException, UnsupportedEncodingException, JsonMappingException, JsonProcessingException  {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(email);
		String userEmail = jsonNode.get("email").asText();
        return userService.sendEmail_(userEmail, "test", "test");
//        return new String(authCode);	// Response body에 값을 반환해줄게요~
    }
//	@PutMapping("/modify")
//	public ResponseEntity<String> modify(@RequestBody UserDto UserDto) {
//		UserDtoService.modify(UserDto);
//		
//		return ResponseEntity.ok().build();
//	}
//	
//	@GetMapping("/userDelete")
//	public ResponseEntity<String> delete(@PathVariable String UserDtoId) {
//		userService.delete(UserDtoId);
//		
//		return ResponseEntity.ok().build();
//	}
	
	
}
