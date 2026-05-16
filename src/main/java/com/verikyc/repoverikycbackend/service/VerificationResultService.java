package com.verikyc.repoverikycbackend.service;

import com.verikyc.repoverikycbackend.dto.CvPredictResponse;
import com.verikyc.repoverikycbackend.dto.VerificationResultResponse;
import com.verikyc.repoverikycbackend.enums.Role;
import com.verikyc.repoverikycbackend.exception.DocumentNotFoundException;
import com.verikyc.repoverikycbackend.exception.ResourceNotFoundException;
import com.verikyc.repoverikycbackend.model.entity.DocumentEntity;
import com.verikyc.repoverikycbackend.model.entity.UserEntity;
import com.verikyc.repoverikycbackend.model.entity.VerificationResultEntity;
import com.verikyc.repoverikycbackend.repository.VerificationResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationResultService {

    private final VerificationResultRepository verificationResultRepository;

    public VerificationResultEntity createResult(DocumentEntity document, CvPredictResponse cvResponse) {

        VerificationResultEntity result = VerificationResultEntity.builder()
                .document(document)
                .extractedFields(cvResponse.extractedFields())
                .confidenceScores(cvResponse.confidenceScores())
                .fieldValidations(cvResponse.fieldValidations())
                .build();

        VerificationResultEntity saved = verificationResultRepository.save(result);
        log.info("VerificationResult created: id={}, document={}", saved.getId(), document.getId());

        return saved;
    }

    public VerificationResultResponse getVerificationResultByDocumentId(UserEntity entity, UUID id) {

        Optional<VerificationResultEntity> verificationResultEntity = verificationResultRepository.findByDocumentId(id);
        if(verificationResultEntity.isEmpty()){
            log.warn("Verification result not found: id={}", id);
            throw new ResourceNotFoundException("Verification result not found");
        }
        VerificationResultEntity resultEntity = verificationResultEntity.get();
        if(!resultEntity.getDocument().getUser().getId().equals(entity.getId()) && entity.getRole()== Role.USER){
            throw new DocumentNotFoundException("Document not found");
        }

        return new VerificationResultResponse(
                resultEntity.getId(),
                resultEntity.getDocument().getId(),
                resultEntity.getExtractedFields(),
                resultEntity.getConfidenceScores(),
                resultEntity.getQualityScore(),
                resultEntity.getFaceMatchScore(),
                resultEntity.getTamperScore(),
                resultEntity.getBoundingBoxes(),
                resultEntity.getPipelineLog(),
                resultEntity.getFieldValidations(),
                resultEntity.getCreatedAt(),
                resultEntity.getUpdatedAt()
        );
    }
}