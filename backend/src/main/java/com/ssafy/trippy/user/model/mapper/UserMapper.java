package com.ssafy.trippy.user.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.trippy.user.model.UserDto;
import com.ssafy.trippy.user.model.UserResponse;

@Mapper
public interface UserMapper {
	UserDto login(UserDto user);
	void insert(UserDto user);
	void update(UserDto user);
	void delete(String userId);
	int countId(String userId);
	void insertUser(UserDto user);
	UserDto selectByUserEmail(String userEmail);
	UserDto checkPassword(UserDto user);
}
