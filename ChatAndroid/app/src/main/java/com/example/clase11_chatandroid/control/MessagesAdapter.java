package com.example.clase11_chatandroid.control;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.clase11_chatandroid.R;
import com.example.clase11_chatandroid.model.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MessagesAdapter extends BaseAdapter {

    private ArrayList<Message> messages;
    private String userID ="";

    public MessagesAdapter() {
        this.messages = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root;
        if(userID.equals(messages.get(i).getUserId())){
        root = inflater.inflate(R.layout.messages_row_mine,null);
        }
        else{
            root = inflater.inflate(R.layout.messages_row_others,null);
        }

        TextView messageRow = root.findViewById(R.id.message_row);
        messageRow.setText(messages.get(i).getBody());

        if(messages.get(i).getType() == Message.TYPE_IMAGE){
            ImageView imageRow = root.findViewById(R.id.imageRow);
            imageRow.setVisibility(View.VISIBLE);

            String nameImage = messages.get(i).getId();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            storage.getReference().child("chats").child(nameImage)
                    .getDownloadUrl().addOnSuccessListener(
              uri -> {
                  Log.e(">>>", uri.toString());
                  Glide.with(parent)
                          .asBitmap()
                          .load(uri)
                          .centerCrop()
                          .into(imageRow);
              }
            );

        }

        return root;
    }

    public void addMessage(Message message){
        messages.add(message);
        notifyDataSetChanged();
    }

    public void setUserID(String userID) {
        this.userID = userID;
        notifyDataSetChanged();
    }
}
