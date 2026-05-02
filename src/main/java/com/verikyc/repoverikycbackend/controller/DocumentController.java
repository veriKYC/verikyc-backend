package com.verikyc.repoverikycbackend.controller;

import com.verikyc.repoverikycbackend.dto.DocumentDetailResponse;
import com.verikyc.repoverikycbackend.dto.DocumentResponse;
import com.verikyc.repoverikycbackend.dto.PageDocumentResponse;
import com.verikyc.repoverikycbackend.model.entity.UserEntity;
import com.verikyc.repoverikycbackend.service.DocumentService;
import com.verikyc.repoverikycbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;
    private final UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentResponse> uploadDocument(@RequestParam MultipartFile file, @RequestParam(required = false) MultipartFile selfie) throws IOException {
        log.info("Uploading document");
        UserEntity entity = getAuthenticatedUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(documentService.uploadDocument(file, entity, selfie));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDetailResponse> getDocumentById(@PathVariable UUID id) {
        log.info("Getting document with id {}", id);
        UserEntity entity = getAuthenticatedUser();
        return ResponseEntity.status(HttpStatus.OK).body(documentService.getDocumentById(entity, id));
    }

    @GetMapping
    public ResponseEntity<PageDocumentResponse> getDocuments(Pageable pageable) {
        log.info("Getting documents");
        UserEntity entity = getAuthenticatedUser();

        return ResponseEntity.status(HttpStatus.OK).body(documentService.getPageDocuments(entity, pageable));
    }

    private UserEntity getAuthenticatedUser() {
        return userService.findUserByEmail((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
