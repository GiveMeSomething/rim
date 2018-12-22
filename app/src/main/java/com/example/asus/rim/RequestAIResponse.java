package com.example.asus.rim;

import ai.api.AIServiceException;
import ai.api.android.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

public class RequestAIResponse extends Thread {
    private AIDataService aiDataService;
    private AIRequest aiRequest;
    private AIResponse aiResponse;

    public RequestAIResponse(AIDataService aiDataService, AIRequest aiRequest) {
        this.aiDataService = aiDataService;
        this.aiRequest = aiRequest;
    }

    @Override
    public void run() {
        try {
            final AIResponse aiResponse = aiDataService.request(aiRequest);
        } catch (AIServiceException e) {
            e.printStackTrace();
        }
    }

    public AIResponse getAiResponse() {
        if (aiResponse != null) {
            return aiResponse;
        }
        return null;
    }
}
