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
    private String text;
    private String[] keyPackage = {"Applications", "Locations"};

    public AIResponseProcessor(AIResponse aiResponse) {
        this.aiResponse = aiResponse;
        process();
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

    public String getText() {
        return text;
    }

    private void process() {
        Result result = aiResponse.getResult();
        Metadata metadata = result.getMetadata();
        HashMap<String, JsonElement> parameters = result.getParameters();
        String[] data = getDataFromMetadata(metadata);

        section = data[0];
        usage = data[1];
        targetObject = getDataFromMap(parameters);
        text = result.getFulfillment().getSpeech();
    }

    private String getDataFromMap(HashMap<String, JsonElement> data) {
        for (String key : keyPackage) {
            if (data.get(key) != null) {
                return data.get(key).getAsString();
            }
        }
        return null;
    }

    //Intent name will be in form : section.usage
    private String[] getDataFromMetadata(Metadata metadata) {
        return metadata.getIntentName().split(".");
    }
}
