package com.example.clase11_chatandroid.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.clase11_chatandroid.R;
import com.example.clase11_chatandroid.control.ChatController;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ChatActivity extends AppCompatActivity {

    private TextView usernameTV;
    private ListView messageList;
    private EditText messageET;
    private Button galBtn,sendBtn;
    private ImageView messageIV;
    private ConstraintLayout controlsContainer;
    private ChatController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        usernameTV = findViewById(R.id.usernameTV);
        messageList = findViewById(R.id.messagesList);
        messageET = findViewById(R.id.messageET);
        galBtn = findViewById(R.id.galBtn);
        sendBtn = findViewById(R.id.sendBtn);
        messageIV = findViewById(R.id.messageIV);
        controlsContainer = findViewById(R.id.controlsContainer);

        controller = new ChatController(this);

    }

    public TextView getUsernameTV() {
        return usernameTV;
    }

    public ListView getMessageList() {
        return messageList;
    }

    public EditText getMessageET() {
        return messageET;
    }

    public Button getGalBtn() {
        return galBtn;
    }

    public Button getSendBtn() {
        return sendBtn;
    }

    public ImageView getMessageIV() {
        return messageIV;
    }

    public ConstraintLayout getControlsContainer() {
        return controlsContainer;
    }

    @Override
    protected void onPause() {
        controller.beforePause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        controller.beforeResume();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        controller.onActivityResult(requestCode,resultCode,data);
    }

    public void hideImage(){
        messageIV.setVisibility(View.GONE);
    }

    public void showImage(){
        messageIV.setVisibility(View.VISIBLE);
    }
}
