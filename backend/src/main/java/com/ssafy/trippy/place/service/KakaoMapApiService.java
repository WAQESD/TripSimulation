package com.ssafy.trippy.place.service;

import java.util.ArrayList;
//import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.ResponseBody;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.jackson.JacksonConverterFactory;
//
//import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.trippy.place.model.PlaceDto;
import com.ssafy.trippy.place.model.PlacesResponse;
import com.ssafy.trippy.place.model.ReviewDto;
import com.ssafy.trippy.place.model.mapper.PlaceMapper;


//@Service
//public class KakaoMapApiService {
//	
//	private KakaoMapService kakaoMapService;
//
//    public KakaoMapApiService() {
//    	Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://dapi.kakao.com")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//            kakaoMapService = retrofit.create(KakaoMapService.class);
//    }
//
//    public ResponseBody searchRestaurants(String keyword) {
//        //Call<ResponseBody> call = kakaoMapService.searchPlaces(keyword, "FD6");
//        Call<ResponseBody> call = kakaoMapService.searchPlaces(keyword, "FD6");
//       
//        // FD6은 맛집 카테고리 코드입니다.
//        try {
//            Response<ResponseBody> response = call.execute();
//            System.out.println("res " + response);
//         
//            System.out.println("headers " + response.headers());
//            if (response.isSuccessful()) {
//            	System.out.println("test " + response.body());
//                return response.body();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}

@Service
public class KakaoMapApiService {
	
    private RestTemplate restTemplate = new RestTemplate();
    private PlaceMapper placeMapper;
    
    private static final String BASE_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
    private static final String API_KEY = ""; // 카카오 API 키


    public KakaoMapApiService(PlaceMapper placeMapper) {
		super();
		//this.restTemplate = restTemplate;
		this.placeMapper = placeMapper;
	}

	public void savePlaces(String dong, String category) throws JsonMappingException, JsonProcessingException {	//동별 검색 
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "KakaoAK " + API_KEY);

        //String url = BASE_URL + "?query=" + dong + "&category_group_code=" + category;
        String url = BASE_URL + "?query=대전 " + dong + "&category_group_code=" + category + "&size=15";

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("response: " + response);
        if (response.getStatusCode().is2xxSuccessful()) {
            //System.out.println("Response: " + response.getBody());
        	List<PlaceDto> list = mapPlaces(response.getBody());
        	if(list != null) {
        		placeMapper.savePlaces(mapPlaces(response.getBody()));
        	}
        	
//            return response.getBody();
        } else {
            System.out.println("Error: " + response.getStatusCode());
//            return null;
        }
    }
    
    //불러온 json데이터 가공해서 dto로 반환하는 메서드 
    public List<PlaceDto> mapPlaces(String jsonPlace) throws JsonMappingException, JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
    	 //List<PlaceDto> response = mapper.readValue(jsonPlace, PlaceDto.class);
    	PlacesResponse response = mapper.readValue(jsonPlace, PlacesResponse.class);
    	//System.out.println("mapPlace: " +response);
    	//List<PlaceDto> places = mapper.readValue(jsonPlace, new TypeReference<List<PlaceDto>>() {});
    	
    	List<PlaceDto> places = response.getDocuments();
    	if(places == null || places.isEmpty()) {
    		return null;
    	}
    	//System.out.println("Places: " +places);
    	//System.out.println("ㅎ");
    	for (PlaceDto dto : places) {
    		dto.parseAddress();
    		System.out.println(dto.toString());
 		}
    	 return places;
    }
    
    //db에 저장된 Place 정보들을 반환받아오는 메서드 
    public List<PlaceDto> getPlaceList(){
    	return placeMapper.getPlaceList();
    }
    
    public void setReviewList(List<ReviewDto> list) {
    	placeMapper.setReviewList(list);
    }
    
    
}


