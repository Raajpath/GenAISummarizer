package com.internal.genaidemo.service;

import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class DocumentSummarizationService {


    @Value("${google.summarization-prompt}")
    private String PROMPT_FOR_SUMMARIZATION;

    @Autowired
    private GenerativeModel generativeModel;

    public void GeminiProVisionController(@Qualifier("geminiProVisionGenerativeModel") GenerativeModel generativeModel) {
        this.generativeModel = generativeModel;
    }

    public String summarizeDocument(MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile("document", ".pdf");
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
        String summary = summarizePdf(tempFile.toString());
        Files.delete(tempFile);
        return summary;
    }

    private String summarizePdf(String pdfFilePath) throws IOException {
        byte[] pdfData = Files.readAllBytes(Path.of(pdfFilePath));
        GenerateContentResponse generateContentResponse = this.generativeModel.generateContent(
                ContentMaker.fromMultiModalData(
                        PROMPT_FOR_SUMMARIZATION, PartMaker.fromMimeTypeAndData("application/pdf", pdfData)
                ));
        String output = ResponseHandler.getText(generateContentResponse);
        System.out.println(output);
        return output;

    }
}
