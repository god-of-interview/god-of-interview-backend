package com.capstone.godofinterview.domain.interview.infra.s3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3FileUpload {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;


    /**
     * S3에 비디오 파일 업로드 (동적 경로)
     */
    public String uploadToS3(Long interviewId, int questionNumber, MultipartFile file) throws IOException {

        // 1. S3에 저장할 경로 동적 생성
        String s3Key = generateS3Key(interviewId, questionNumber);

        // 2. 파일 정보 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("video/webm");     // 파일 타입
        metadata.setContentLength(file.getSize()); // 파일 크기

        // 3. 실제 S3 업로드
        PutObjectRequest putRequest = new PutObjectRequest(
            bucketName,             // 동적 버킷명
            s3Key,                  // 동적 파일 경로
            file.getInputStream(),  // 파일 데이터
            metadata               // 파일 정보
        );

        amazonS3Client.putObject(putRequest);  // AWS S3로 전송!

        // 4. 업로드된 파일의 공개 URL 동적 생성
        String fileUrl = generateS3Url(s3Key);

        return fileUrl;
    }

    /**
     * S3에 업로드 되어 있는 동영상 파일 5개 URL 가져오기
     */
    public List<String> getInterviewVideoUrls(Long interviewId) {
        List<String> videoUrls = new ArrayList<>();

        try {
            String prefix = String.format("interview-videos/interview_%d/", interviewId);

            ListObjectsV2Request listRequest = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix);

            ListObjectsV2Result listResponse = amazonS3Client.listObjectsV2(listRequest);

            for (S3ObjectSummary s3Object : listResponse.getObjectSummaries()) {
                String objectKey = s3Object.getKey();
                String videoUrl = generateS3Url(objectKey);
                videoUrls.add(videoUrl);
            }

        } catch (Exception e) {
            throw new RuntimeException("비디오 URL 조회 실패", e);
        }

        return videoUrls;
    }

    /**
     * S3 Key 동적 생성
     * 예: interview-videos/interview_123/question_1.webm
     */
    private String generateS3Key(Long interviewId, int questionNumber) {
        return String.format("interview-videos/interview_%d/question_%d.webm",
            interviewId, questionNumber);
    }

    /**
     * S3 파일 URL 동적 생성
     * 예: https://god-of-interview-videos.s3.ap-northeast-2.amazonaws.com/interview-videos/interview_123/question_1.webm
     */
    private String generateS3Url(String s3Key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
            bucketName, region, s3Key);
    }
}
