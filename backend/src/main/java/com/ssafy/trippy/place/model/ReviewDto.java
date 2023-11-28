package com.ssafy.trippy.place.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {
	
	private int reviewId;	//auto increment
	private int placeId;	//foreign key
	
	@JsonProperty("url")
	private String url;
	
	@JsonProperty("reviewTitle")
	private String reviewTitle;
	
	@JsonProperty("reviewContent")
	private String reviewContent;
	
	@JsonProperty("thumbnail")
	private String thumbnail;
	
	
	
}
