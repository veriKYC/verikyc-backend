package com.verikyc.repoverikycbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record CvPredictResponse(
        @JsonProperty("document_type") String documentType,
        float confidence,
        @JsonProperty("extracted_fields") Map<String, Object> extractedFields,
        @JsonProperty("confidence_scores") Map<String, Object> confidenceScores,
        @JsonProperty("is_valid") boolean isValid,
        @JsonProperty("field_validations") Map<String, Object> fieldValidations
) {
}
