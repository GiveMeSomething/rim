package com.example.asus.rim;

import com.google.gson.JsonElement;

import java.util.HashMap;

import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;

public class AIResponseProcessor {
    private AIResponse aiResponse;
    private String section;
    private String usage;
    private String targetObject;
    private String[] keyPackage = {"Applications"};

    public AIResponseProcessor(AIResponse aiResponse) {
        this.aiResponse = aiResponse;
    }

    public String getSection() {
        return section;
    }

    public String getUsage() {
        return usage;
    }

    public String getTargetObject() {
        return targetObject;
    }

    private void process() {
        Result result = aiResponse.getResult();
        Metadata metadata = result.getMetadata();
        HashMap<String, JsonElement> parameters = result.getParameters();
    }
}
