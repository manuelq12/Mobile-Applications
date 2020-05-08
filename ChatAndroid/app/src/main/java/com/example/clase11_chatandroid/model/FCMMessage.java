package com.example.clase11_chatandroid.model;

public class FCMMessage {

    public static final String API_KEY = "AAAAqdNyWIc:APA91bFa1jM8iiMzEsRV9P3L9MPcDf4pgMZWRN-ixCoi5gsP3_v8os66nS0kNKJCQGxa-vNavL0mRFuggsu-7Dgleqhq1h9aFAEu43WEloqcRYurBSoxWe-FIa3XQUVxRTFjpWK2FUQ8";
    private String to;
    private Message data;

    public FCMMessage() {
    }

    public FCMMessage(String to, Message data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Message getData() {
        return data;
    }

    public void setData(Message data) {
        this.data = data;
    }
}
