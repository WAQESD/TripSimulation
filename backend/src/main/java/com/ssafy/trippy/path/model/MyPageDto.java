package com.ssafy.trippy.path.model;

import lombok.Data;

@Data
public class MyPageDto {
	private int pathId;
	private String pathKey;
	private String regDate;
	private String pathName;
	private String userEmail;
	private String waypointId;
	private String placeName;
	private String arrivalTime;
}
