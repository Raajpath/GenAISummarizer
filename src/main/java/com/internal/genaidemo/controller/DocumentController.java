package com.internal.genaidemo.controller;

import com.internal.genaidemo.service.DocumentSummarizationService;
import com.internal.genaidemo.service.WordDocSummarizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api/documents")
public class DocumentController {


    @Autowired
    private DocumentSummarizationService documentSummarizationService;

    @Autowired
    private WordDocSummarizationService wordDocSummarizationService;

    @PostMapping("/summarize")
    public ResponseEntity<String> summarizeDocument(@RequestParam("file") MultipartFile file) {
        try {
            String summary = documentSummarizationService.summarizeDocument1(file);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to process document: " + e.getMessage());
        }
    }


    @PostMapping("/word/summarize")
    public ResponseEntity<String> summarizeWordDocument(@RequestParam("file") MultipartFile file)  {
        try{
            String summary = wordDocSummarizationService.processAndSummarizeDocument(file);
            return ResponseEntity.ok(summary);
        }catch (IOException e){
            return ResponseEntity.badRequest().body("Error processing document: " + e.getMessage());
        }
    }


}
