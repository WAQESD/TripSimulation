package com.ssafy.trippy.crawling.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trippy.crawling.service.NaverBlogCrawlingService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin("*")
@RequestMapping("/api")
@RestController
public class CrawlingController {
	NaverBlogCrawlingService naverBlogCrawlingService;

	public CrawlingController(NaverBlogCrawlingService naverBlogCrawlingService) {
		super();
		this.naverBlogCrawlingService = naverBlogCrawlingService;
	}
	
	//db에서 
	
	
//	@GetMapping("/review")
//	@ApiOperation(value = "getAndSaveReviews", notes = "키워드에 해당하는 블로그 글 얻어와서 s3에 업로")
//    public void getAndSaveReviews(@RequestParam String keyword) {	
//       System.out.println(naverBlogCrawlingService.crawlNaverBlogReviews(keyword));
//       //JSONObject jsonObject = new JSONObject(file);
//       naverBlogCrawlingService.crawlNaverBlogReviews(keyword);
//		
//    }
	
//	@GetMapping("/review")
//	//@ResponseBody
//    public List<ReviewDto> getReviews(@RequestParam String keyword) {	
//		//String keyword = "피자";
//		//System.out.println("controller: " + kakaoMapApiService.searchRestaurants(keyword));
//       System.out.println(naverBlogCrawlingService.crawlNaverBlogReviews(keyword));
//       return naverBlogCrawlingService.crawlNaverBlogReviews(keyword);
//		
//    }
	
}
