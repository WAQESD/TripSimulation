package com.ssafy.trippy.crawling.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.trippy.place.model.ReviewDto;

//import lombok.Getter;
//import lombok.Setter;

//@Getter
//@Setter
@JsonIgnoreProperties(ignoreUnknown = true) // 모든 미인식 속성을 무시
public class ReviewResponse {
	
	@JsonProperty("documents")
	private List<ReviewDto> documents;

    // Getter, Setter
    public List<ReviewDto> getDocuments() {
        return documents;
    }

    public void setDocuments(List<ReviewDto> documents) {
        this.documents = documents;
    }
}
