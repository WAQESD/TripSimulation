package com.ssafy.trippy.path.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WaypointDto {
	
	private int pathId;
	
	@JsonProperty("place_name")
	private String placeName;
	
	@JsonProperty("arrival_time")
	private String arrivalTime;
}
