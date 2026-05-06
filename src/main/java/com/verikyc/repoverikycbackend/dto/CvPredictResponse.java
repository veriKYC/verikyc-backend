package com.verikyc.repoverikycbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CvPredictResponse(
        @JsonProperty("document_type") String documentType,
        float confidence
) {
}
