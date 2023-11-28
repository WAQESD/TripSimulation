package com.ssafy.trippy.user.model.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.trippy.user.model.UserDto;
import com.ssafy.trippy.user.model.mapper.UserMapper;
import com.ssafy.trippy.util.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;




@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserMapper userMapper;
    //private final JwtAuthTokenProvider tokenProvider;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Environment env;
    //private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    
    private final JavaMailSender emailSender;
    private String authNum; // 인증 번호
    



	
    //일반로그인
	@Override
    public String userLogin(UserDto user) throws Exception{
        log.info("class:=================userLogin====================");
        //해당 이메일이 있는지 확인 
        UserDto idChecked = userMapper.selectByUserEmail(user.getUserEmail());
        if(idChecked==null) {
            //예외처리 
        	System.out.println("유저 정보 없음 로그인 실패");
        	return null;
        	//throw new CredentialException(CredentialErrorCode.NotFoundId.getCode(),CredentialErrorCode.NotFoundId.getDescription());
        }
        log.info("userLogin user: " + idChecked.toString());
        //boolean check =passwordEncoder.matches(user.getUserPassword(),loginUser.getUserPassword());
        UserDto pwdChecked = userMapper.checkPassword(user);
        boolean check = (user.getUserPassword().equals(pwdChecked.getUserPassword())) ? true : false;
//        System.out.println(pwdChecked);
//        System.out.println(pwdChecked.getUserEmail());
//        System.out.println(pwdChecked.getUserPassword());
        System.out.println("check: " + check);
        
        if(!check) {
        	//예외처리
        	System.out.println("비밀번호 불일치 로그인 실패");
        	return null;
        	//return ResponseEntity.ok().body(null);;
        	//throw new CredentialException(CredentialErrorCode.NotMatchIdPassword.getCode(),CredentialErrorCode.NotMatchIdPassword.getDescription());
        }
        return createToken(pwdChecked);
    }
	
    @Override
    public String createToken(UserDto user) throws Exception{
        log.info("class:=================createToken====================");
        return jwtUtils.generateAccessToken(user.getUserEmail());
    }
	
//    @Override
//    public String createToken(UserDto user) throws Exception{
//        log.info("class:=================createToken====================");
//        Date expireDate=Date.from(LocalDateTime.now().plusMinutes(180).atZone(ZoneId.systemDefault()).toInstant());
//        Map<String,Object> claims=new HashMap<>();
//        claims.put("id",user.getUserEmail());
//        String title = "";
//        String authToken=jwtUtils.createToken(user.getUserEmail(), title, expireDate);
//        return jwtAuthToken.getToken();
//    }
    
    @Override
    public void createUser(UserDto user) throws Exception {
        log.info("class:=================createUser====================");
//        UserDto userById=userMapper.selectByUserEmail(user.getUserEmail());
//        if(userById!=null) {
//            //throw new CredentialException(CredentialErrorCode.AlreadyExistId.getCode(),CredentialErrorCode.AlreadyExistId.getDescription());
//        }
        
        UserDto userByEmailId=userMapper.selectByUserEmail(user.getUserEmail());
        if(userByEmailId!=null) {	//회원가입 정보 존재
        	System.out.println("회원가입 정보가 존재합니다");
            //throw new CredentialException(CredentialErrorCode.AlreadyEmailId.getCode(),CredentialErrorCode.AlreadyEmailId.getDescription());
        }
//        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        //user.setUserPassword(user.getUserPassword());
        log.info("insertUser: "+user.toString());
        userMapper.insertUser(user);
    }
    
    
    //소셜로그인
    @Override
    public String socialLogin(String code, String registrationId) throws Exception {
        log.info("class:=================socialLogin====================");
        String accessToken = getAccessToken(code, registrationId);
        log.debug(accessToken);
        JsonNode userNode = getSocialUserResource(accessToken, registrationId);
        

        UserDto socialUser = new UserDto(); 
        System.out.println("userNode: " + userNode);
        System.out.println("resId: " + registrationId);
        switch (registrationId) {        	
            case "google": {
                socialUser.setUserEmail(userNode.get("email").asText());
//                String[] emailInfo = userNode.get("email").asText().split("@");
                socialUser.setName(userNode.get("name").asText());                
                break;
            } case "kakao": {
            	System.out.println("enter switch");
//                socialUser.setUserId(userNode.get("id").asText());
                //String[] emailInfo=userNode.get("kakao_account").get("email").asText().split("@");
                socialUser.setUserEmail(userNode.get("kakao_account").get("email").asText());
                System.out.println("email: " + userNode.get("kakao_account").get("email").asText());
                socialUser.setName(userNode.get("properties").get("nickname").asText());
                System.out.println("nickname: " + userNode.get("properties").get("nickname").asText());
                //socialUser.setUserName(userNode.get("kakao_account").get("profile").get("nickname").asText());
                socialUser.setBirthDate(userNode.get("kakao_account").get("birthday").asText());
                System.out.println("birth: " + userNode.get("kakao_account").get("birthday").asText());
                String gender = userNode.get("kakao_account").get("gender").asText();
                char c = '?';
                if(gender.equals("male")) {
                	c = 'm';
                }else if(gender.equals("female")) {
                	c = 'f';
                }
                socialUser.setGender(c);
                //System.out.println("gender: " + c);
                break;
            } case "naver": {
                socialUser.setUserEmail(userNode.get("response").get("email").asText());
                //String[] emailInfo=userNode.get("response").get("email").asText().split("@");
                //socialUser.setUserEmailId(emailInfo[0]);
                //socialUser.setUserEmailDomain(emailInfo[1]);
                socialUser.setBirthDate(userNode.get("response").get("birthda").asText());
                socialUser.setName(userNode.get("response").get("nickname").asText());
                String gender = userNode.get("response").get("gender").asText();
                char c = '?';
                if(gender.equals("male")) {
                	c = 'm';
                }else if(gender.equals("female")) {
                	c = 'f';
                }
                socialUser.setGender(c);
                break;
            } default: {
                throw new RuntimeException("UNSUPPORTED SOCIAL TYPE");
            }
        }
        socialUser.setUserPassword("social"+socialUser.getUserEmail()); 
        UserDto userByEmail = userMapper.selectByUserEmail(socialUser.getUserEmail());
        if(userByEmail==null) { //우리 db에 있는 유저라면 유저정보 새로 만들 필요 없 
            createUser(socialUser);
        }
        return createToken(socialUser);
    }
    
    
    @Override
    public String getAccessToken(String authorizationCode, String registrationId) throws Exception{
        log.info("class:=================getAccessToken====================");
        String clientId = env.getProperty("oauth2." + registrationId + ".client-id");
        String clientSecret = env.getProperty("oauth2." + registrationId + ".client-secret");
        String redirectUri = env.getProperty("oauth2." + registrationId + ".redirect-uri");
        String tokenUri = env.getProperty("oauth2." + registrationId + ".token-uri");
        log.debug("tokenUri: "+tokenUri);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);
        log.debug(entity.toString());
        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }
    
    @Override
    public JsonNode getSocialUserResource(String accessToken, String registrationId) throws Exception {
        log.info("class:=================getSocialUserResource====================");
        String resourceUri = env.getProperty("oauth2."+registrationId+".resource-uri");
        log.debug("resourceUri: "+resourceUri);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        log.debug(entity.toString());
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }

	@Override
	public UserDto getUserInfo(String userEmail) throws Exception {
		//return new UserDto(userMapper.selectByUserEmail(userEmail));
		return userMapper.selectByUserEmail(userEmail);
	}

	@Override
	public int checkId(String userId) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void regist(UserDto user) {
		userMapper.insertUser(user);
		
	}
	
	 // 인증번호 8자리 무작위 생성
    public void createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0; i<8; i++) {
        	// 0~2 사이의 값을 랜덤하게 받아와 idx에 집어넣습니다.
            int idx = random.nextInt(3);

			// 랜덤하게 idx를 받았으면, 그 값을 switchcase를 통해 또 꼬아버립니다.
			// 숫자와 ASCII 코드를 이용합니다.
            switch (idx) {
                case 0 :
                	// a(97) ~ z(122)
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                	// A(65) ~ Z(90)
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                	// 0 ~ 9
                    key.append(random.nextInt(9));
                    break;
            }
        }
        
        authNum = key.toString();
    }
    
 // 메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {
            // 코드를 생성합니다.
            createCode();
            String setFrom = "";	// 보내는 사람
            System.out.println("setFrom address:" +setFrom);
            String toEmail = email;		// 받는 사람(값 받아옵니다.)
            System.out.println("toEmail:" +email);
            String title = "trippy 인증번호 테스트";		// 메일 제목		

            MimeMessage message = emailSender.createMimeMessage();
            
            message.addRecipients(MimeMessage.RecipientType.TO, toEmail);	// 받는 사람 설정
            message.setSubject(title);		// 제목 설정

            // 메일 내용 설정
            String msgOfEmail="";
            msgOfEmail += "<div style='margin:20px;'>";
            msgOfEmail += "<h1> 안녕하세요 test 입니다. </h1>";
            msgOfEmail += "<br>";
            msgOfEmail += "<p>아래 코드를 입력해주세요<p>";
            msgOfEmail += "<br>";
            msgOfEmail += "<p>감사합니다.<p>";
            msgOfEmail += "<br>";
            msgOfEmail += "<div align='center' style='border:1px solid black; font-family:verdana';>";
            msgOfEmail += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
            msgOfEmail += "<div style='font-size:130%'>";
            msgOfEmail += "CODE : <strong>";
            msgOfEmail += authNum + "</strong><div><br/> ";
            msgOfEmail += "</div>";

            message.setFrom(new InternetAddress(setFrom));		// 보내는 사람 설정
            // 위 String으로 받은 내용을 아래에 넣어 내용을 설정합니다.
            message.setText(msgOfEmail, "utf-8", "html");

            return message;
        }

   
  //실제 메일 전송
    public String sendEmail(String email) throws MessagingException, UnsupportedEncodingException {

        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(email);
        //실제 메일 전송
        emailSender.send(emailForm);

        return authNum; //인증 코드 반환
    }
    
    
    
    //userEmail, 제목, 내
    public String sendEmail_(String to, String subject, String content) {
        // 메일 서버 설정
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", "smtp.gmail.com");
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtps.ssl.protocols", "TLSv1.2");

        // 사용자 인증 정보 설정
        props.put("mail.smtps.user", "");
        props.put("mail.smtps.password", "");
        
//        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465"); // TLS를 사용하는 경우
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");


        // 세션 생성
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(props.getProperty("mail.smtps.user"),
                                                  props.getProperty("mail.smtps.password"));
            }
        });

        try {
            // 이메일 메시지 생성
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(""));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            createCode();
            message.setSubject(subject + " authNum: " + authNum);
            message.setText(content);

            // 이메일 전송
            Transport.send(message);
            return authNum;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    
//    @Override
//    public UserResponse getUserInfo(String userId) throws Exception{
//        return new UserResponse(userMapper.selectByUserId(userId));
//    }

//	@Override
//	public UserDto login(UserDto user) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//	@Override
//	public void modify(UserDto user) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void delete(String userId) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public int checkId(String userId) {
//		// TODO Auto-generated method stub
//		return 0;
//	}


}
