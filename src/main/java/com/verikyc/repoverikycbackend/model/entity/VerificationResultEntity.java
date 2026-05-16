package com.verikyc.repoverikycbackend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "verification_result")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "document_id")
    private DocumentEntity document;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "extracted_fields")
    private Map<String, Object> extractedFields;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "confidence_scores")
    private Map<String, Object> confidenceScores;

    @Column(name = "quality_score")
    private Float qualityScore;

    @Column(name = "face_match_score")
    private Float faceMatchScore;

    @Column(name = "tamper_score")
    private Float tamperScore;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "bounding_boxes")
    private Map<String, Object> boundingBoxes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "pipeline_log")
    private Map<String, Object> pipelineLog;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "field_validations")
    private Map<String, Object> fieldValidations;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
