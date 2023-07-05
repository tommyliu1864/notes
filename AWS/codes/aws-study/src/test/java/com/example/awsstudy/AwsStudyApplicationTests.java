package com.example.awsstudy;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.io.File;
import java.util.List;

@SpringBootTest
class AwsStudyApplicationTests {

    private static final String ACCESS_KEY_ID = "AKIA4TPH3JAQXLY6J57J";
    private static final String SECRET_KEY_ID = "vyfgmqDBxTFMFrFijMhXHcR+MZ8Tc4yOyknloC4m";
    private static final String REGION = "ap-northeast-1";

    private static final String BUCKET_NAME = "bucket-0704-test1";

    private static AmazonS3 s3Client;

    @BeforeAll
    static void setup() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_KEY_ID);
        s3Client = AmazonS3Client.builder().withRegion(REGION).withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
    }

    // 获取所有的存储桶列表
    @Test
    void testListBuckets() {
        List<Bucket> buckets = s3Client.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName());
        }
    }

    // 文件上传
    @Test
    void testUpload(){
        s3Client.putObject(BUCKET_NAME, "test.jpg", new File("angle.jpg"));
    }
}
