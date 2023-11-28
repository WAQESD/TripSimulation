package com.ssafy.trippy.place.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.trippy.place.model.CoordinateResponse;
import com.ssafy.trippy.place.model.PlaceDto;
import com.ssafy.trippy.place.model.ReviewDto;

@Mapper
public interface PlaceMapper {
	
	void savePlaces(List<PlaceDto> list);

	List<PlaceDto> getPlaceList();

	void setReviewList(List<ReviewDto> list);

	List<PlaceDto> selectPlaceAroundList(CoordinateResponse coordiResponse);

	List<ReviewDto> selectReviewList(int pathId);
	
	
}
