package com.ssafy.trippy.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.trippy.path.model.PathDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class S3Downloader {
	@Autowired
    private AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    
    //private final S3Client s3Client;
    
//    
    public String convertToJsonString(InputStream inputStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(inputStream);
        return jsonNode.toString();
    }
    
    
	    
	//파일 접근하기 
	public ResponseEntity<String> getObject(String key) throws IOException{
			
		S3Object o = amazonS3Client.getObject(new GetObjectRequest(bucket, key));
		String str = convertToJsonString(o.getObjectContent());
			
		
//        S3Object o = amazonS3Client.getObject(new GetObjectRequest(bucket, storedFileName));
//        String str = convertToJsonString(o.getObjectContent());
//        Scanner sc = new Scanner(o.getObjectContent());
//        StringBuilder sb = new StringBuilder();
//        while(sc.hasNext()) sb.append(sc.next());
//        String str = sb.toString();
        //S3ObjectInputStream objectInputStream = o.getObjectContent();
        
        //byte[] bytes = IOUtils.toByteArray(objectInputStream);
        //추가 
        //byte[] decodedBytes = Base64.getDecoder().decode(objectInputStream.toString());
        //String jsonStr = new String(decodedBytes, StandardCharsets.UTF_8);
        //JSONObject jsonObj = new JSONObject(jsonStr);
        //String jsonData = jsonObj.toString();
       

        String fileName = URLEncoder.encode(key, "UTF-8").replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(str.length());
        httpHeaders.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<String>(str, httpHeaders, HttpStatus.OK);
//
    }
	
	/**
     * S3에 있는 파일 삭제
     * 영어 파일만 삭제 가능 -> 한글 이름 파일은 안됨
     */
    public void deleteS3(String key) throws Exception {
        try{
            //String key = filePath.substring(56); // 폴더/파일.확장자

            try {
                amazonS3Client.deleteObject(bucket, key);
            } catch (AmazonServiceException e) {
                log.info(e.getErrorMessage());
            }

        } catch (Exception exception) {
            log.info(exception.getMessage());
        }
        log.info("[S3Uploader] : S3에 있는 파일 삭제");
    }
	
//	public String readJsonFileFromS3(String bucket, String key) {
//        S3Object s3Object = amazonS3Client.getObject(GetObjectRequest.builder()
//                .bucket(bucket)
//                .key(key)
//                .build());
//
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(s3Object))) {
//            return reader.lines().collect(Collectors.joining("\n"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
	    
}
