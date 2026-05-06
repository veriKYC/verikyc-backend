package com.verikyc.repoverikycbackend.service;

import com.verikyc.repoverikycbackend.client.CvServiceClient;
import com.verikyc.repoverikycbackend.dto.CvPredictResponse;
import com.verikyc.repoverikycbackend.dto.DocumentDetailResponse;
import com.verikyc.repoverikycbackend.dto.DocumentResponse;
import com.verikyc.repoverikycbackend.dto.PageDocumentResponse;
import com.verikyc.repoverikycbackend.enums.DocumentStatus;
import com.verikyc.repoverikycbackend.enums.DocumentType;
import com.verikyc.repoverikycbackend.enums.Role;
import com.verikyc.repoverikycbackend.exception.DocumentNotFoundException;
import com.verikyc.repoverikycbackend.exception.FileSizeLimitExceededException;
import com.verikyc.repoverikycbackend.exception.InvalidDocumentException;
import com.verikyc.repoverikycbackend.exception.InvalidFileTypeException;
import com.verikyc.repoverikycbackend.model.entity.DocumentEntity;
import com.verikyc.repoverikycbackend.model.entity.UserEntity;
import com.verikyc.repoverikycbackend.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final CvServiceClient cvServiceClient;

    @Value("${storage.upload.path}")
    private String uploadPath;

    public DocumentResponse uploadDocument(MultipartFile documentImage, UserEntity userEntity, MultipartFile selfieImage) throws IOException {
        if(documentImage==null || documentImage.isEmpty()){
            throw new InvalidDocumentException("Document image is empty");
        }
        boolean isSelfie = false;
        if(selfieImage != null && !selfieImage.isEmpty()){
            isSelfie = true;
            log.info("Selfie uploaded");
        }
        if(!List.of("image/jpeg", "image/png").contains(documentImage.getContentType())){
            log.warn("Upload rejected: invalid file type={}", documentImage.getContentType());
            throw new InvalidFileTypeException("Image type is not supported");
        }
        if(documentImage.getSize() > 10*1024*1024){
            throw new FileSizeLimitExceededException("Image size is too large");
        }

        Path filePath = saveFile(documentImage);

        DocumentEntity document = DocumentEntity.builder()
                .user(userEntity)
                .imageUrl(filePath.toString())
                .status(DocumentStatus.QUEUED)
                .build();

        if(isSelfie){
            filePath = saveFile(selfieImage);
            document.setSelfieUrl(filePath.toString());
        }

        DocumentEntity savedDocument = documentRepository.save(document);

        log.info("Document uploaded: id={}, user={}", savedDocument.getId(), userEntity.getId());

        try{
            CvPredictResponse cvResponse = cvServiceClient.predict(documentImage);
            log.info("CV classification: id={}, type={}, confidence={}",
                    savedDocument.getId(), cvResponse.documentType(), cvResponse.confidence());
            savedDocument.setDocumentType(DocumentType.valueOf(cvResponse.documentType()));
            savedDocument.setStatus(DocumentStatus.PROCESSING);
        }
        catch (Exception e) {
            log.error("CV service failed: id={}, cause={}", savedDocument.getId(), e.getMessage());
            savedDocument.setStatus(DocumentStatus.FAILED);
        }

        documentRepository.save(savedDocument);

        return new DocumentResponse(savedDocument.getId(),
                savedDocument.getStatus(),
                savedDocument.getCreatedAt());
    }

    public DocumentDetailResponse getDocumentById(UserEntity userEntity, UUID id) {

        Optional<DocumentEntity> document;
        if(userEntity.getRole() == Role.ADMIN){
            document = documentRepository.findById(id);
        }
        else {
            document = documentRepository.findByIdAndUser(id, userEntity);
        }

        if(document.isEmpty()){
            log.warn("Document not found: id={}, user={}", id, userEntity.getId());
            throw new DocumentNotFoundException("Document not found");
        }
        DocumentEntity documentEntity = document.get();

        return new DocumentDetailResponse(
                documentEntity.getId(),
                documentEntity.getStatus(),
                documentEntity.getDocumentType(),
                documentEntity.getImageUrl(),
                documentEntity.getSelfieUrl(),
                documentEntity.getCreatedAt(),
                documentEntity.getUpdatedAt()
        );
    }

    public PageDocumentResponse getPageDocuments(UserEntity userEntity, Pageable pageable) {

        Page<DocumentEntity> documents;
        if(userEntity.getRole() == Role.ADMIN){
            documents = documentRepository.findAll(pageable);
        }
        else {
            documents =  documentRepository.findAllByUser(userEntity, pageable);
        }

        Page<DocumentResponse> page = documents.map(doc -> new DocumentResponse(
                doc.getId(),
                doc.getStatus(),
                doc.getCreatedAt()
        ));
        log.info("Listed {} documents for user={}", page.getTotalElements(), userEntity.getId());

        return new PageDocumentResponse(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    private Path saveFile(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        if(originalName == null){
            throw new InvalidDocumentException("Document image name is empty");
        }
        String extension = originalName.substring(originalName.lastIndexOf("."));

        String fileName = UUID.randomUUID()+extension;

        Path filePath = Paths.get(uploadPath,fileName);
        Files.createDirectories(filePath.getParent());
        file.transferTo(filePath);

        log.info("File saved: {}", filePath);

        return filePath;
    }
}
