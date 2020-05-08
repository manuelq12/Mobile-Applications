package com.example.clase11_chatandroid.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.clase11_chatandroid.R;
import com.example.clase11_chatandroid.control.MainController;

public class MainActivity extends AppCompatActivity {

    private EditText chatroomET, usernameET;
    private Button signinBtn;
    private MainController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatroomET = findViewById(R.id.chatroomET);
        usernameET = findViewById(R.id.usernameET);
        signinBtn = findViewById(R.id.signinBtn);

        controller = new MainController(this);
    }

    public EditText getChatroomET() {
        return chatroomET;
    }

    public void setChatroomET(EditText chatroomET) {
        this.chatroomET = chatroomET;
    }

    public EditText getUsernameET() {
        return usernameET;
    }

    public void setUsernameET(EditText usernameET) {
        this.usernameET = usernameET;
    }

    public Button getSigninBtn() {
        return signinBtn;
    }

    public void setSigninBtn(Button signinBtn) {
        this.signinBtn = signinBtn;
    }
}
