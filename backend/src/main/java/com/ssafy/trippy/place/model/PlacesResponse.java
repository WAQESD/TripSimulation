package com.ssafy.trippy.place.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//import lombok.Getter;
//import lombok.Setter;

//@Getter
//@Setter
@JsonIgnoreProperties(ignoreUnknown = true) // 모든 미인식 속성을 무시
public class PlacesResponse {
	private List<PlaceDto> documents;

    // Getter, Setter
    public List<PlaceDto> getDocuments() {
        return documents;
    }

    public void setDocuments(List<PlaceDto> documents) {
        this.documents = documents;
    }
}
