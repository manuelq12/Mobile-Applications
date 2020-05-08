package com.example.clase11_chatandroid.services;

import android.util.Log;

import com.example.clase11_chatandroid.model.Message;
import com.example.clase11_chatandroid.util.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONObject;

public class FCMService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        JSONObject object = new JSONObject(remoteMessage.getData());
        Gson gson = new Gson();
        Message message = gson.fromJson(object.toString(),Message.class);
        Log.e(">>>","Pues aquí llego");
        NotificationUtils.createNotification(this,message.getBody());
    }
}
