package com.example.asus.rim;

public class ChatData {
    private String body;
    private boolean isMine;

    public ChatData(String sender, String receiver, String data, boolean isMine) {
        body = data;
        this.isMine = isMine;
    }

}
