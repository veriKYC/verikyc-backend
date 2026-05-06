package com.verikyc.repoverikycbackend.client;

import com.verikyc.repoverikycbackend.dto.CvPredictResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;

import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Slf4j
@Component
public class CvServiceClient {

    private final WebClient webClient;

    public CvServiceClient(@Value("${cv.service.url}") String cvServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(cvServiceUrl)
                .build();
    }

    public CvPredictResponse predict(MultipartFile file) throws IOException {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        }).contentType(MediaType.valueOf(file.getContentType()));

        return webClient.post()
                .uri("/api/v1/predict")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(CvPredictResponse.class)
                .block();
    }
}
