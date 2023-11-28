package com.ssafy.trippy.place.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ssafy.trippy.place.model.CoordinateResponse;
import com.ssafy.trippy.place.model.PlaceDto;
import com.ssafy.trippy.place.model.ReviewDto;

public interface PlaceService {

	List<PlaceDto> getPlaceAroundList(CoordinateResponse coordiResponse);

	List<ReviewDto> getReview(int pathId);

}
