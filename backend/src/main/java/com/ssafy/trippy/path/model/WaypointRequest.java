package com.ssafy.trippy.path.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WaypointRequest {
	
	private int pathId;
	
	@JsonProperty("waypoints")
	private List<WaypointDto> waypoints;
	
	@JsonProperty("pathContent")
	private PathRequest pathRequest;
	
	
}
