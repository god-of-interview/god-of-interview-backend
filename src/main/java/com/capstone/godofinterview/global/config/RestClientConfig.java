package com.capstone.godofinterview.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient fastApiRestClient() {
        return RestClient.builder()
            .baseUrl("http://localhost:8001")
            .requestFactory(clientHttpRequestFactory())
            .build();
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(5000);    // 연결 타임아웃: 5초
        factory.setReadTimeout(300000);     // 응답 타임아웃: 5분 (AI 분석용)

        return factory;
    }
}
