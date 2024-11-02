package com.internal.genaidemo.config;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.HarmCategory;
import com.google.cloud.vertexai.api.SafetySetting;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

import static com.google.cloud.vertexai.api.HarmCategory.*;

@Configuration(proxyBeanMethods = false)
public class GeminiConfiguration {

    @Value("${google.project-id}")
    private String PROJECT_ID;

    @Value("${vertex-ai.location}")
    private String LOCATION;

    @Value("${google.model-name}")
    private String MODEL_NAME;

    @Bean
    public VertexAI vertexAI(){
        return new VertexAI(PROJECT_ID,LOCATION);
    }

    @Bean
    public GenerativeModel geminiProVisionGenerativeModel(VertexAI vertexAI) {
        return new GenerativeModel(MODEL_NAME, vertexAI).
                withSafetySettings(List.of(
                        SafetySetting.newBuilder().setCategory(HarmCategory.HARM_CATEGORY_HATE_SPEECH).setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_NONE).build(),
                        SafetySetting.newBuilder().setCategory(HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT).setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_NONE).build(),
                        SafetySetting.newBuilder().setCategory(HarmCategory.HARM_CATEGORY_HARASSMENT).setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_NONE).build(),
                        SafetySetting.newBuilder().setCategory(HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT).setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_NONE).build()));
    }


}
