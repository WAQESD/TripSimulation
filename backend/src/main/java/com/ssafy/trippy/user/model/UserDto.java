package com.ssafy.trippy.user.model;

import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Data
@Getter
@Setter
//@ConstructorBinding
public class UserDto {
	private String userEmail;
	private String userPassword;
	private String name;
	private String birthDate;
	private char gender;
	

	
}
