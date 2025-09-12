package com.capstone.godofinterview.domain.interview.infra.s3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

@Component
@RequiredArgsConstructor
public class S3FileUpload {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;


    /**
     * S3에 비디오 파일 업로드 (동적 경로)
     */
    public String uploadToS3(Long interviewId, int questionNumber, MultipartFile file) throws IOException {
        String s3Key = generateS3Key(interviewId, questionNumber);

        PutObjectRequest putRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(s3Key)
            .contentType("video/webm")
            .contentLength(file.getSize())
            .build();

        s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return generateS3Url(s3Key);
    }

    /**
     * S3에 업로드 되어 있는 동영상 파일 5개 URL 가져오기
     */
    public List<String> getInterviewVideoUrls(Long interviewId) {
        List<String> videoUrls = new ArrayList<>();
        String prefix = String.format("interview-videos/interview_%d/", interviewId);

        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
            .bucket(bucketName)
            .prefix(prefix)
            .build();

        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

        for (S3Object s3Object : listResponse.contents()) {
            videoUrls.add(generateS3Url(s3Object.key()));
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
