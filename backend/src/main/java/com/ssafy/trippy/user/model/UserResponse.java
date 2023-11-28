package com.ssafy.trippy.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
	private String code;
	private String registrationId;
	@Override
	public String toString() {
		return "UserResponse [code=" + code + ", registrationId=" + registrationId + "]";
	}
	
}
