package com.ssafy.trippy.path.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PathDto {
	
	@JsonProperty("pathId")
	private int pathId;
	
//	@JsonProperty("path")
//	private String pathData;
	
	private String pathKey;
	
	private String regDate;
	
	@JsonProperty("pathName")
	private String pathName;
	
	@JsonProperty("userEmail")
	private String userEmail;
	
	
}
