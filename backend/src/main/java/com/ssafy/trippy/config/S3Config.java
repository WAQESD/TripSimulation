//package com.ssafy.trippy.config;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class S3Config {
//    @Value("${cloud.aws.credentials.access-key}")
//    private String iamAccessKey;
//    @Value("${cloud.aws.credentials.secret-key}")
//    private String iamSecretKey;
//    @Value("${cloud.aws.region.static}")
//    private String region;
//    @Bean
//    public AmazonS3 amazonS3(){
//        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(iamAccessKey, iamSecretKey);
//        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
//                .withRegion(region).enablePathStyleAccess()
//                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//                .build();
//    }
//    
//    @Bean
//    public AmazonS3 amazonS3() {
//        return AmazonS3ClientBuilder.standard()
//                .withRegion(Regions.AP_NORTHEAST_2) // 적절한 리전 설정
//                .withCredentials(new AWSStaticCredentialsProvider(
//                    new BasicAWSCredentials(iamAccessKey, iamSecretKey))) // 접근 키 및 비밀 키 설정
//                .build();
//    }
//
//}