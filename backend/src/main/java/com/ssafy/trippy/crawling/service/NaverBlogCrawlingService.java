package com.ssafy.trippy.crawling.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.ssafy.trippy.place.model.ReviewDto;


@Service
public class NaverBlogCrawlingService {
	
	
	
    public List<ReviewDto> crawlNaverBlogReviews(String titleKeyword) {
        List<ReviewDto> reviews = new ArrayList<>();
        String url = "https://section.blog.naver.com/Search/Post.naver?pageNo=1&rangeType=ALL&orderBy=sim&keyword=" + titleKeyword;
        //String url = "https://section.blog.naver.com/Search/Post.naver?pageNo=1&rangeType=ALL&orderBy=sim&keyword=피자";
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select(".area_list_search"); // 네이버 블로그 리뷰에 해당하는 CSS 선택자
            System.out.println(doc);

            for (Element element : elements) {
                String reviewTitle = element.select(".title_post").text();
                String reviewContent = element.select(".text").text();
                // 기타 정보 추출 및 가공
                ReviewDto dto = new ReviewDto();
                dto.setReviewTitle(reviewTitle);
                dto.setReviewContent(reviewContent);
                reviews.add(dto);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 예외 처리
        }

        return reviews;
    }
}