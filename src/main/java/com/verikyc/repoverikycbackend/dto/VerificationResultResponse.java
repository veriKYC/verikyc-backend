package com.verikyc.repoverikycbackend.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record VerificationResultResponse(
    UUID id,
    UUID documentId,
    Map<String, Object> extractedFields,
    Map<String, Object> confidenceScores,
    Float qualityScore,
    Float faceMatchScore,
    Float tamperScore,
    Map<String, Object> boundingBoxes,
    Map<String, Object> pipelineLog,
    Map<String, Object> fieldValidations,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
