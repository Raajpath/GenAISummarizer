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
    private String LOCATION;    // e.g., "us-central1"

    @Bean
    public VertexAI vertexAI(){
        return new VertexAI(PROJECT_ID,LOCATION);
    }

    public GenerativeModel generativeModel(VertexAI vertexAI){
        return new GenerativeModel("gemini-pro-vision",vertexAI);
    }
}
