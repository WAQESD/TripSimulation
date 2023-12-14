package com.ssafy.trippy.path.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.trippy.path.model.MyPageDto;
import com.ssafy.trippy.path.model.MyPageResponse;
import com.ssafy.trippy.path.model.PathDto;
import com.ssafy.trippy.path.model.PathRequest;
import com.ssafy.trippy.path.model.WaypointDto;
import com.ssafy.trippy.path.model.WaypointRequest;
import com.ssafy.trippy.path.service.PathService;
import com.ssafy.trippy.user.model.UserDto;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.spi.service.contexts.PathContext;

@CrossOrigin("*")
@Controller
//@RequiredArgsConstructor
//@Api(value="trippy")
@RequestMapping("/path")
public class PathController {
	

	private PathService pathService;

	//private final S3Client s3Client;
	
	public PathController(PathService pathService) {
		super();
		this.pathService = pathService;
	}
	

	/**
	 * 그룹(팀) 생성
	 * @param name
	 * @param file
	 * @return
	 */
	//json데이터&유저아이디 전달받아 로컬 경로에 파일 만들어 저장하고 파일을 s3버킷에 보낸뒤 로컬 파일은 삭제, db에 파일 키 저장
	//@ApiOperation(value = "test", notes = "test.")
	//@PostMapping(path = "/path/upload", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	//@PostMapping("/upload")
	public int uploadPath(PathRequest pathRequest) {
        
        //파일생성 버킷저장
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("path:" + pathRequest);
		PathDto pathDto = new PathDto();
		String key = null;
		try {
			System.out.println(pathRequest.getPathInfo().getPathName());
			pathDto.setPathName(pathRequest.getPathInfo().getPathName());
			
			System.out.println(pathRequest.getPathInfo().getUserEmail());
			pathDto.setUserEmail(pathRequest.getPathInfo().getUserEmail());
			
			
			JSONObject jsonObject = new JSONObject(pathRequest);
			key = pathService.createTeam(jsonObject.toString());	
		} catch (NullPointerException e) {
			System.out.println("pathController: path가 null이다");
		}
		
		pathDto.setPathKey(key);
		
	    return pathService.setPathInfo(pathDto);
	    //return new ResponseEntity(null, HttpStatus.OK);
	}
	
	//s3에 uploadPath, path db에 저장한뒤 waypoint db 저장
	@PostMapping("/waypoint")
	public ResponseEntity<?> setWaypoint(@RequestBody String request ) throws JsonMappingException, JsonProcessingException{
		//먼저 path를 등록해서 pathId를 받아와야함
		ObjectMapper mapper = new ObjectMapper();
		
//		PathRequest pathRequest = new PathRequest();
		WaypointRequest waypointRequest = mapper.readValue(request, WaypointRequest.class);
		//waypointRequest.getPathRequest().setPath(waypointRequest.getPathRequest().getPath().toString());
//		pathRequest.setPathInfo(request.getPathRequest().getPathInfo());
		
//		WaypointRequest waypointRequest = mapper.readValue(request, WaypointRequest.class);
//		String waypoints = mapper.readValue(waypointRequest.getWaypoints(), String.class);
//		List<WaypointDto> list = mapper.readValue(waypoints, new TypeReference<List<WaypointDto>>(){});
//		
//		String pathContent = mapper.readValue(waypointRequest.getPathContent(), String.class);
//		PathRequest pathRequest = mapper.readValue(pathContent, PathRequest.class);
//		PathDto pathDto = mapper.readValue(pathRequest.getPathInfo(), PathDto.class);
		
		                        
		//PathRequest pathRequest = waypointRequest.getPathRequest();
		//PathRequest pathRequest = mapper.readValue(pathRequest.getPath(), Path.class);
		int id = uploadPath(waypointRequest.getPathRequest());
		waypointRequest.setPathId(id);
		System.out.println("id : " +id);
//		request.setPathId(id);
		//waypointRequest.setPathId(id);
		pathService.setWaypoint(waypointRequest);
		return new ResponseEntity(null, HttpStatus.OK);
	}
	
		
	//userEmail에 해당하는 path정보 목록 반환 
//	produces = "application/json; charset=utf8"
	@GetMapping("/list")
	public ResponseEntity<List<PathDto>> getPathList(String userEmail) throws IOException{		
		List<PathDto> list = pathService.getPathList(userEmail);

		return ResponseEntity.status(HttpStatus.OK).body(pathService.getPathList(userEmail));
	}
	
	
	//pathId에 해당하는 경로 파일 제공 
	@GetMapping("/download")
	public ResponseEntity<ResponseEntity<String>> downloadPath(String pathId) throws IOException{	
		return ResponseEntity.status(HttpStatus.OK).body(pathService.getPathFile(pathId));
	}
	
	@GetMapping("/delete")
	public void deletePath(String pathId) throws Exception {
		//s3에 있는 파일 삭제
		pathService.deletePathFile(pathId);		
		//db path&waypoint 삭제
		pathService.deletePath(pathId);
			
	}
	
	@GetMapping("/mypage/{userEmail}")
	public ResponseEntity<List<MyPageResponse>> getMyPageInfo(@PathVariable String userEmail){
		//마이페이지에 필요한 경로 목록, 경로에 따른 waypoint 목록 정보 반환
		List<MyPageResponse> list = pathService.getMyPageInfo(userEmail);
		return ResponseEntity.status(HttpStatus.OK).body(pathService.getMyPageInfo(userEmail));
	}
	
	
}
