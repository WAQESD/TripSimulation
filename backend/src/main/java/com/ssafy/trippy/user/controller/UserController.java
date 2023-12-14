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

	//스프링 4.3이상부터는 단일 생성자는 @Autowired 생략가능하다함!
	// 인터페이스의 구현 클래스를 알아서 주입시켜줌  
	public UserController(UserService UserDtoService) {
		this.userService = UserDtoService;
	}
	
	
	//이메일 중복체크
	//url에서는 카멜대신 다 소문자를 사용하거나 케밥케이스를 사용하는 모양이다 
	@GetMapping("/emailcheck/{userEmail}")
	public ResponseEntity<Map<String, String>> emailCheck(@PathVariable String userEmail) {
		int count = userService.checkId(userEmail);
		
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("result", count == 1 ? "Duplicate" : "Unique");
		
		return ResponseEntity.ok().body(resultMap);
	}
	
	//userEmail, userPassword
	@PostMapping("/userLogin")
	public ResponseEntity<String> userLogin(@RequestBody String response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        UserDto loginRequest = mapper.readValue(response, UserDto.class);
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
	
	//이메일 인증번호 입
	@ResponseBody
    @PostMapping("/emailCertify")		
    public String emailCertify(@RequestBody String email) throws MessagingException, UnsupportedEncodingException, JsonMappingException, JsonProcessingException  {
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
