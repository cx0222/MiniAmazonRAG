package com.nova.rag.controller;

import com.nova.rag.model.Product;
import com.nova.rag.service.DocumentService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.ok("%d products added".formatted(count));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProductDocument(
            @RequestParam @Min(0) long id
    ) {
        int count = documentService.deleteProduct(id);
        return ResponseEntity.ok("%d products deleted".formatted(count));
    }
}
