package com.example.clase11_chatandroid.model;

public class Message {
    public static final int TYPE_TEXT =0;
    public static final int TYPE_IMAGE =1;

    private int type;
    private String id;
    private String body;
    private String userId;;
    private long timestamp;

    public Message() {
    }

    public Message(int type, String id, String body, String userId, long timestamp) {
        this.type = type;
        this.id = id;
        this.body = body;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
