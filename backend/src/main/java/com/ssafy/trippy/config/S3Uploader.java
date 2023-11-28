package com.ssafy.trippy.config;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class S3Uploader {
    @Autowired
    private AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 로컬 경로에 저장
     */
    public String uploadFileToS3(String file, String filePath) {
        // MultipartFile -> File 로 변환
        File uploadFile = null;
        try {
            uploadFile = convert(file)
                    .orElseThrow(() -> new IllegalArgumentException("[error]: MultipartFile -> 파일 변환 실패"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // S3에 저장된 파일 이름
        String fileName = filePath + "/" + UUID.randomUUID();

        // s3로 업로드 후 로컬 파일 삭제
        //String uploadImageUrl = putS3(uploadFile, fileName);
        putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return fileName;
    }


    /**
     * S3로 업로드
     * @param uploadFile : 업로드할 파일
     * @param fileName : 업로드할 파일 이름
     * @return 업로드 경로
     */
    public String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
                CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    

    /**
     * 로컬에 저장된 파일 지우기
     * @param targetFile : 저장된 파일
     */
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("[파일 업로드] : 파일 삭제 성공");
            return;
        }
        log.info("[파일 업로드] : 파일 삭제 실패");
    }

    /**
     * 로컬에 파일 업로드 및 변환
     * @param file : 업로드할 파일
     */
    private Optional<File> convert(String file) throws IOException {
        // 로컬에서 저장할 파일 경로 : user.dir => 현재 디렉토리 기준
    	//System.out.println("파일: " + file.getOriginalFilename());
    	System.out.println("userdir : " + System.getProperty("user.dir"));
//        String dirPath = System.getProperty("user.dir") + "/src/main/resources/static/path/" + file.getOriginalFilename();
    	String dirPath = System.getProperty("user.dir") + "/src/main/resources/static/path/" + "pathFile";
    	System.out.println("dirPath: " + dirPath);
        File convertFile = new File(dirPath);
        System.out.println("convertfile");

//        System.out.println("createNewfile: " + convertFile.createNewFile());
        if (convertFile.createNewFile()) {
        	
            // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
                System.out.println("fos.write");
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}