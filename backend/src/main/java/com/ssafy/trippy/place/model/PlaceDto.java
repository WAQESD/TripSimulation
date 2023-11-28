package com.ssafy.trippy.place.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true) // 모든 알려지지 않은 속성을 무시
public class PlaceDto {
	
	private int placeId;	//nn, ai
	
	@JsonProperty("place_name")
	private String placeName;	//nn
	
	@JsonProperty("y")
	private float lat;	//nn
	
	@JsonProperty("x")
	private float lng;	//nn
	
	private String thumbnail;
	
	@JsonProperty("category_group_name")
	private String placeCategory;	//nn
	
	private int reviewCnt;
	
	private int score;
	
	private String description;
	
	@JsonProperty("road_address_name")
	private String address;	//db테이블에는 없는 속성임, 임시로 만듦  
	
	private String sido;
	private String sigugun;
	private String dongmyun;
	
//	public PlaceDto(String placeName, float y, float x, String address){
//		this.placeName = placeName;
//		parseAddress(address);
//		this.lat = y;
//		this.lng = x;
//	}
		
//	public PlaceDto() {
//		parseAddress();
//		parseCategory();
//	}
	
	// 주소 파싱 로직
    public void parseAddress() {
        String[] parts = this.address.split(" ", 3);
        if (parts.length >= 3) {
            this.sido = parts[0];
            this.sigugun = parts[1];
            this.dongmyun = parts[2];
        }
    }
    
    //카테고리 파싱 로직 
//    public void parseCategory() {
//    	String[] parts = this.placeCategory.split(" ");
//    	this.placeCategory = parts[0];
//    } 
    
    

}
