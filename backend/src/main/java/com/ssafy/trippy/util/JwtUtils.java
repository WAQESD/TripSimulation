package com.ssafy.trippy.util;

import java.rmi.UnexpectedException;

/*
JWT : JsonWebToken
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
 */

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.ssafy.trippy.user.model.UserDto;
import com.ssafy.trippy.user.model.mapper.UserMapper;
import com.ssafy.trippy.user.model.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//import com.ssafy.trippy.exception.UnAuthorizedException;
//import com.ssafy.trippy.user.model.User;
//import com.ssafy.trippy.user.model.service.UserService;

//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtils {
    public static final String SALT = "soyoung";
    //private final UserService userService;
    private final UserMapper userMapper;

    public String generateAccessToken(String userId) {
        return createToken(userId, "access-token", 10);
    }

    public String generateRefreshToken(String userId) {
        return createToken(userId, "refresh-token", 30);
    }
    
    //사용자 ID, 제목, 만료 일수를 입력받아 JWT를 생성
    public String createToken(String userId, String title, int expirationDay) {
    	LocalDateTime currentTime = LocalDateTime.now();
    	LocalDateTime expirationTime = currentTime.plus(expirationDay, ChronoUnit.DAYS);
    	ZoneId zoneId = ZoneId.systemDefault();
    	ZonedDateTime zonedDateTime = expirationTime.atZone(zoneId);
    	Date expirationDate = Date.from(zonedDateTime.toInstant());
    	return generateToken(userId, title, expirationDate);
    }
    
    //실제로 JWT를 생성, HS256 알고리즘과 SALT (비밀 키)를 사용
    public String generateToken(String userEmail, String subject, Date date) {
        return Jwts.builder()
                // Header 설정 : 토큰의 타입, 해쉬 알고리즘 정보 세팅.
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("regDate", System.currentTimeMillis()) // 생성 시간
                // Payload 설정 : 유효기간(Expiration), 토큰 제목 (Subject), 데이터 (Claim) 등 정보 세팅.
                .setExpiration(date) // 토큰 유효기간
                .setSubject(subject) // 토큰 제목 설정 ex) access-token, refresh-token
                .claim("userEmail", userEmail) // 저장할 데이터
                // Signature 설정 : secret key를 활용한 암호화.
                .signWith(SignatureAlgorithm.HS256, SALT)
                .compact(); // 직렬화 처리.
    }
    
//    JWT에서 사용자 ID를 추출
    public String getUserEmail(String jwt) {
        return (String) getPayload(jwt).get("userEmail");
    }
    
    //JWT를 파싱하여 페이로드를 반환, 유효성 검사 위함 
    public Map<String, Object> getPayload(String jwt) {
        Jws<Claims> claims = null;
        try {
            claims = Jwts.parser().setSigningKey(SALT).parseClaimsJws(jwt);
        } catch (Exception e) {
            log.error(e.getMessage());
            //throw new UnAuthorizedException("user id를 가져오는 데 실패하였습니다.");
        }
        Map<String, Object> value = claims.getBody();
        return value;
    }
    
    //JWT의 유효성을 검증, 만료시간 확인 
    public boolean checkToken(String jwt) throws UnexpectedException {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SALT).parseClaimsJws(jwt);
            Date expiration = claims.getBody().getExpiration();
            return new Date(System.currentTimeMillis()).before(expiration);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnexpectedException("token 검증 과정에서 에러가 발생했습니다");
           
        }
	
    }
    
    //토큰을 확인해서 맞다면 유저정보 반환 
    public UserDto getUserInfo(HttpServletRequest request) throws Exception {
        String token = request.getHeader("access-token");
        if (!checkToken(token)) {
            //throw new UnAuthorizedException("user 정보를 가져오는데 실패하였습니다.");
        	System.out.println("getUserInfo: user 정보를 가져오는데 실패하였습니다.");
        }

        String userEmail = getUserEmail(token);
        UserDto user = userMapper.selectByUserEmail(userEmail);
        if (user == null) {
            throw new UnexpectedException("유저 정보를 가져오는데 실패하였습니다.");
//        	System.out.println("getUserInfo: user 정보를 가져오는데 실패하였습니다.");
        }
        return user;
    }
}
