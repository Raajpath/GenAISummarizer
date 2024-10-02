package com.internal.genaidemo.service;

import com.google.api.gax.rpc.ApiException;
import com.google.cloud.aiplatform.v1.*;
import com.google.cloud.documentai.v1.*;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.protobuf.ByteString;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class WordDocSummarizationService {

    @org.springframework.beans.factory.annotation.Value("${google.project-id}")
    private String PROJECT_ID;

    @org.springframework.beans.factory.annotation.Value("${vertex-ai.location}")
    private String LOCATION;    // e.g., "us-central1"

    @org.springframework.beans.factory.annotation.Value("${document-ai.processor-id}")
    private String PROCESSOR_ID;

    @org.springframework.beans.factory.annotation.Value("${vertex-ai.endpoint}")
    private String VERTEX_AI_ENDPOINT;

    public String processAndSummarizeDocument(MultipartFile file) throws IOException {

        //Load the word document
        ByteString fileContent = ByteString.readFrom(file.getInputStream());
        //Step 1: Extract text using Document AI
       String extractedText=  extractTextFromDocument(fileContent);
        return summarizeTextWithGemini(extractedText);

    }

    private String extractTextFromDocument( ByteString fileContent) throws IOException{
        //Initialize Document AI client
        try(DocumentProcessorServiceClient
        client = DocumentProcessorServiceClient.create()
        ){
            ProcessorName processorName = ProcessorName.of(PROJECT_ID, LOCATION, PROCESSOR_ID);
            //Create a request for processing the document
            ProcessRequest request = ProcessRequest.newBuilder()
                    .setName(processorName.toString())
                    .setRawDocument(RawDocument.newBuilder().setContent(fileContent )
                            .setMimeType("application/vnd.openxmlformats-officedocument.wordprocessingml.document").build()
                    ).build();

            //Process the document to extract text
            ProcessResponse response = client.processDocument(request);
            Document document = response.getDocument();

            //Extract text content from document
            return document.getText();
        }
    }

    private String summarizeTextWithGemini(String extractedText) throws  IOException{
        //Initialize Vertex AI prediction client
        try(PredictionServiceClient predictionServiceClient = PredictionServiceClient.create()) {

            String endpoint = String.format("projects/%s/locations/%s/endpoints/%s", PROJECT_ID, LOCATION, "gemini-1.5-flash-001");

            Value.Builder textInput = Value.newBuilder().setStringValue(extractedText);

            PredictRequest request = PredictRequest.newBuilder()
                    .setEndpoint(endpoint)
                    .setParameters(textInput).build();


            PredictResponse response = predictionServiceClient.predict(request);

            //Extract summarized text from the response
            return response.getPredictions(0).getStructValue().getFieldsOrThrow("summary")
                    .getStringValue();

              }catch (ApiException | IOException e){
            throw new RuntimeException("Failed to summarize text using Google Gemini", e);
        }
    }






}
