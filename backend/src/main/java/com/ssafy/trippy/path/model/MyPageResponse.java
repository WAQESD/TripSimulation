package com.ssafy.trippy.path.model;

import java.util.List;

import lombok.Data;

@Data
public class MyPageResponse {
//	path_id, path_key, reg_date, path_name, user_email
	private PathDto pathDto;
	
//	하나의 path에 대한 waypoint_id, place_name, arrival_time의 list 
	private List<WaypointDto> list;
}
