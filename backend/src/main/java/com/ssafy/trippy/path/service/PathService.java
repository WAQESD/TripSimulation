package com.ssafy.trippy.path.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.trippy.path.model.PathDto;
import com.ssafy.trippy.path.model.WaypointDto;
import com.ssafy.trippy.path.model.WaypointRequest;

public interface PathService {
	//void saveUserPath(String path);

	String createTeam(String file);

	List<PathDto> getPathList(String filePath) throws IOException;
	int setPathInfo(PathDto pathDto);

	ResponseEntity<String> getPathFile(String pathId) throws IOException;
	
	void setWaypoint(WaypointRequest waypointRequest);
	
	void deletePathFile(String key) throws Exception;

	void deletePath(String pathId);
}
