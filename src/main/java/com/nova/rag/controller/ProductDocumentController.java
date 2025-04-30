package com.nova.rag.controller;

import com.nova.rag.entity.Product;
import com.nova.rag.service.DocumentService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "document")
@CrossOrigin
public class ProductDocumentController {
    private final DocumentService documentService;

    @Autowired
    public ProductDocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<String> addProductDocument(
            @RequestBody @Validated Product product
    ) {
        int count = documentService.addProduct(product);
        if (count == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add products");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("%d products added".formatted(count));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProductDocument(
            @RequestParam @Min(0) long id
    ) {
        int count = documentService.deleteProduct(id);
        if (count == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete products");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("%d products deleted".formatted(count));
    }
}
