package com.ssafy.trippy.place.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.trippy.crawling.model.ReviewResponse;
import com.ssafy.trippy.path.model.PathRequest;
import com.ssafy.trippy.place.model.CoordinateResponse;
import com.ssafy.trippy.place.model.DongList;
import com.ssafy.trippy.place.model.PlaceDto;
import com.ssafy.trippy.place.model.ReviewDto;
import com.ssafy.trippy.place.service.KakaoMapApiService;
import com.ssafy.trippy.place.service.PlaceService;

//import lombok.extern.slf4j.Slf4j;


@CrossOrigin("*")
@RestController
//@RequiredArgsConstructor
//@Slf4j
@RequestMapping("/place")
public class PlaceController {
	private final KakaoMapApiService kakaoMapApiService;
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final DongList dongList;
	private final PlaceService placeService;

	public PlaceController(KakaoMapApiService kakaoMapApiService, DongList dongList, PlaceService placeService) {
		super();
		this.kakaoMapApiService = kakaoMapApiService;
		this.dongList = dongList;
		this.placeService = placeService;
	}

	@GetMapping("/getPlace")
	//@ResponseBody
    public void getPlaces() throws JsonMappingException, JsonProcessingException {	
		String[] categoryList = {"CT1", "AT4", "FD6", "CE7"}; //문화시설 / 관광명소 / 음식점 / 카페 	
		String[] dongs = dongList.getDaejeonDongs();
		
		for (String dong : dongs) {
			for (String str : categoryList) {
					System.out.println("dong: " + dong);
		    	   kakaoMapApiService.savePlaces(dong, str);
		    }
		}
		//kakaoMapApiService.savePlaces();
		
    }
	
	//먼저 요청할 가게 정보를 가져옴
	@GetMapping("/list")
	public List<PlaceDto> getPlaceList(){
		return kakaoMapApiService.getPlaceList();
	}
	
//	//가게이름으로 크롤링 데이터 요청해서 db 저장 
	@GetMapping("/setReview")
	public void setReviews() {
		
		List<PlaceDto> list = getPlaceList();
		System.out.println(list);
		for (PlaceDto item : list) {
			HttpHeaders headers = new HttpHeaders();
	        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	        //headers.set("Authorization", "KakaoAK " + API_KEY);
	
	        //String url = BASE_URL + "?query=" + dong + "&category_group_code=" + category;
	        String url = "" + item.getPlaceName(); 
	  
	        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
	
	        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	        //저장 
	        String jsonResponse = response.getBody();
	        ObjectMapper mapper = new ObjectMapper();
	        try {
	            ReviewResponse review = mapper.readValue(jsonResponse, ReviewResponse.class);
	            List<ReviewDto> places = review.getDocuments();
	            System.out.println("places: " + places);
	            if(places == null || places.isEmpty()) continue;
	            //place id는 따로 
	            for (ReviewDto reviewDto : places) {
	            	reviewDto.setPlaceId(item.getPlaceId());
				}
	            
	            kakaoMapApiService.setReviewList(places);
	            // userDto 객체를 사용하여 필요한 로직 수행
	        } catch (IOException e) {
	            e.printStackTrace();
	            // 오류 처리
	        }
	        
	        
		}
		
	}
	
	//placeId에 해당하는 리뷰들 반환 
	@PostMapping("/getReview")
	public ResponseEntity<List<ReviewDto>> getReview(int placeId){
		return ResponseEntity.status(HttpStatus.OK).body(placeService.getReview(placeId));
	}
	
	
	
	//화면 크기 좌표에 따른 주변 지역의 place 정보들을 반환 (리뷰 포함 x)
	@PostMapping("/aroundList")
	public ResponseEntity<List<PlaceDto>> getPlaceAroundList(@RequestBody CoordinateResponse coordiResponse){	
		return ResponseEntity.status(HttpStatus.OK).body(placeService.getPlaceAroundList(coordiResponse));
	}


	

}

