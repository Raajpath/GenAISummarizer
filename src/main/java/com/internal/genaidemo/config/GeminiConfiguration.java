package com.internal.genaidemo.config;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        return new GenerativeModel(MODEL_NAME, vertexAI);
    }


}
