package com.ssafy.trippy.path.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.trippy.path.model.PathDto;
import com.ssafy.trippy.path.model.WaypointDto;
import com.ssafy.trippy.path.model.WaypointRequest;

@Mapper
public interface PathMapper {
	
	int insertPathInfo(PathDto pathDto);

	List<PathDto> getUserPaths(String userEmail);

	String getFileKey(String pathId);

	void insertWaypoint(WaypointDto waypointDto);

	void deletePath(String pathId);
	
	
}
