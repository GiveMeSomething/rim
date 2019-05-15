package com.example.asus.rim;

import android.media.Image;

public class MemberData {
    private String name;
    private int avatar;

    public MemberData(String name) {
        this.name = name;
    }

    public MemberData(String name, int avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    // Add an empty constructor so we can later parse JSON into MemberData using Jackson
    public MemberData() {
    }

    public String getName() {
        return name;
    }

    public int getAvatar() {
        return avatar;
    }
}
