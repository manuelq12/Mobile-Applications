package com.example.clase11_chatandroid.control;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.clase11_chatandroid.R;
import com.example.clase11_chatandroid.model.FCMMessage;
import com.example.clase11_chatandroid.model.Message;
import com.example.clase11_chatandroid.model.User;
import com.example.clase11_chatandroid.util.HTTPSWebUtilDomi;
import com.example.clase11_chatandroid.util.NotificationUtils;
import com.example.clase11_chatandroid.util.UtilDomi;
import com.example.clase11_chatandroid.view.ChatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class ChatController implements View.OnClickListener {

    private static final int GALLERY_CALLBACK = 1;
    private ChatActivity activity;
    private MessagesAdapter adapter;
    private String username;
    private String chatroom;
    private User user;
    private Uri tempUri;


    public ChatController(final ChatActivity activity) {
        this.activity = activity;
        adapter = new MessagesAdapter();
        activity.getMessageList().setAdapter(adapter);

        username = activity.getIntent().getExtras().getString("username");
        chatroom = activity.getIntent().getExtras().getString("chatroom");

        activity.getSendBtn().setOnClickListener(this);
        activity.getGalBtn().setOnClickListener(this);

        Query queryBusqueda = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .orderByChild("username")
                .equalTo(username);

        queryBusqueda.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount() == 0){
                    String pushid =  FirebaseDatabase.getInstance().getReference().child("users").push().getKey();
                    user =  new User(pushid,username);
                    FirebaseDatabase.getInstance().getReference().child("users").child(pushid).setValue(user);
                }
                else {
                    for (DataSnapshot coincidence : dataSnapshot.getChildren()) {
                        user = coincidence.getValue(User.class);
                        break;
                    }
                }
                adapter.setUserID(user.getId());
                activity.getUsernameTV().setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("chats")
                .child(chatroom)
                .limitToFirst(10);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                adapter.addMessage(message);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic(chatroom).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()){
                        Log.e(">>>","Suscrito!");
                    }
                }
                );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sendBtn:
                String body = activity.getMessageET().getText().toString();
                String pushId = FirebaseDatabase.getInstance().getReference()
                        .child("chats").child(chatroom).push().getKey();

                Message message = new Message(
                        tempUri == null?Message.TYPE_TEXT:Message.TYPE_IMAGE,
                        pushId,
                        body,
                        user.getId(),
                        Calendar.getInstance().getTime().getTime());

                FirebaseDatabase.getInstance().getReference()
                        .child("chats").child(chatroom).child(pushId)
                        .setValue(message);

                FCMMessage fcm = new FCMMessage();
                fcm.setTo("/topics/"+chatroom);
                fcm.setData(message);
                Gson gson = new Gson();
                String json = gson.toJson(fcm);

                new Thread(
                        ()->{
                            HTTPSWebUtilDomi utilDomi = new HTTPSWebUtilDomi();
                            utilDomi.POSTtoFCM(FCMMessage.API_KEY, json);
                 }
                ).start();

                if(tempUri != null){
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    storage.getReference().child("chats").child(message.getId())
                            .putFile(tempUri).addOnCompleteListener(
                                    task -> {
                                        if(task.isSuccessful()){
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("chats").child(chatroom).child(pushId)
                                                    .setValue(message);
                                        }
                                    }
                    );
                }
                else{
                    FirebaseDatabase.getInstance().getReference()
                            .child("chats").child(chatroom).child(pushId)
                            .setValue(message);
                }
                activity.hideImage();
                tempUri = null;

                break;

            case R.id.galBtn:
                Intent gal = new Intent(Intent.ACTION_GET_CONTENT);
                gal.setType("image/*");
                activity.startActivityForResult(gal,GALLERY_CALLBACK);
                break;


        }
    }

    public void beforePause() {
        FirebaseMessaging.getInstance().subscribeToTopic(chatroom).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()){
                        Log.e(">>>","Suscrito!");
                    }
                }
        );
    }

    public void beforeResume() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(chatroom);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==GALLERY_CALLBACK && resultCode == RESULT_OK){
            tempUri =data.getData();
            File file = new File(UtilDomi.getPath(activity,tempUri));
            Bitmap image = BitmapFactory.decodeFile(file.toString());
            activity.getMessageIV().setImageBitmap(image);
            activity.showImage();
        }
    }


}
