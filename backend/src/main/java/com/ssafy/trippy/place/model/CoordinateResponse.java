package com.ssafy.trippy.place.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoordinateResponse {
	@JsonProperty("startX")
	//lng
	private float startX;
	
	//lat
	@JsonProperty("startY")
	private float startY;
	
	//lng
	@JsonProperty("endX")
	private float endX;
	
	//lat
	@JsonProperty("endY")
	private float endY;
}
