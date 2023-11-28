package com.ssafy.trippy.place.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.trippy.place.model.CoordinateResponse;
import com.ssafy.trippy.place.model.PlaceDto;
import com.ssafy.trippy.place.model.ReviewDto;
import com.ssafy.trippy.place.model.mapper.PlaceMapper;

@Service
public class PlaceServiceImpl implements PlaceService {
	private final PlaceMapper placeMapper;
		
	
	public PlaceServiceImpl(PlaceMapper placeMapper) {
		super();
		this.placeMapper = placeMapper;
	}



	@Override
	public List<PlaceDto> getPlaceAroundList(CoordinateResponse coordiResponse) {
		return placeMapper.selectPlaceAroundList(coordiResponse);

	}



	@Override
	public List<ReviewDto> getReview(int pathId) {
		return placeMapper.selectReviewList(pathId);
	}

}
