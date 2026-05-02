package com.verikyc.repoverikycbackend.dto;

import java.util.List;

public record PageDocumentResponse(
        List<DocumentResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages
){
}
