package com.ssafy.trippy.path.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PathRequest {
	@JsonProperty("path")
	private String path;
	
	@JsonProperty("pathInfo")
	private PathDto pathInfo;
}
