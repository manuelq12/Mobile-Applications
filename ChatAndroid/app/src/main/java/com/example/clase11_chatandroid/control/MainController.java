package com.example.clase11_chatandroid.control;

import android.Manifest;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.example.clase11_chatandroid.R;
import com.example.clase11_chatandroid.model.User;
import com.example.clase11_chatandroid.view.ChatActivity;
import com.example.clase11_chatandroid.view.MainActivity;
import com.google.firebase.database.FirebaseDatabase;


public class MainController implements View.OnClickListener {

    private MainActivity activity;

    public MainController(MainActivity activity) {
        this.activity = activity;
        this.activity.getSigninBtn().setOnClickListener(this);

        ActivityCompat.requestPermissions(activity,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        },0);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signinBtn:
                String chatroom = activity.getChatroomET().getText().toString();
                String username = activity.getUsernameET().getText().toString();



                Intent i = new Intent(activity, ChatActivity.class);
                i.putExtra("username",username);
                i.putExtra("chatroom", chatroom);
                activity.startActivity(i);
                activity.finish();
                break;

        }
    }
}
