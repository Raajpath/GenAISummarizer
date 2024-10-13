package com.internal.genaidemo.controller;

import com.internal.genaidemo.service.DocumentSummarizationService;
import com.internal.genaidemo.service.ImageSummarizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api/documents")
public class DocumentController {


    @Autowired
    private DocumentSummarizationService documentSummarizationService;

    @Autowired
    private ImageSummarizationService imageSummarizationService;

    @PostMapping("/summarize")
    public ResponseEntity<String> summarizeDocument(@RequestParam("file") MultipartFile file) {
        try {
            String summary = documentSummarizationService.summarizeDocument(file);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to process document: " + e.getMessage());
        }
    }

    @PostMapping("/image")
    public ResponseEntity<String> file(@RequestParam("file") MultipartFile file, @RequestParam String question) {
        try {
            String summary = imageSummarizationService.summarizeImage(file, question);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to process document: " + e.getMessage());
        }
    }
}
