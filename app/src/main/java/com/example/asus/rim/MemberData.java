package com.example.asus.rim;

public class MemberData {
    private String name;
    private String color;

    public MemberData(String name) {
        this.name = name;
    }

    // Add an empty constructor so we can later parse JSON into MemberData using Jackson
    public MemberData() {
    }

    public String getName() {
        return name;
    }
}
