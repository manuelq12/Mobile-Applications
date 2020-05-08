package com.example.clase11_chatandroid.control;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.clase11_chatandroid.util.HTTPSWebUtilDomi;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
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
            File imageFile = new File(parent.getContext().getExternalFilesDir(null) + "/" + nameImage);

            if(imageFile.exists()){
                root.post(
                        ()->{
                            loadImage(imageRow,imageFile);
                        }
                );
            }
            else{
                FirebaseStorage storage = FirebaseStorage.getInstance();
                storage.getReference().child("chats").child(nameImage)
                        .getDownloadUrl().addOnSuccessListener(
                        uri -> {
                            new Thread(
                                    ()->{
                                        File f = new File(parent.getContext().getExternalFilesDir(null) + "/" + nameImage);
                                        HTTPSWebUtilDomi utilDomi = new HTTPSWebUtilDomi();
                                        utilDomi.saveURLImageOnFile(uri.toString(),f);

                                        root.post(
                                                ()->{
                                                    loadImage(imageRow,f);
                                                }
                                        );

                                    }
                            ).start();

                        }
                );
            }
        }

        return root;
    }

    public void loadImage(ImageView imageRow, File f) {
        Bitmap bitmap = BitmapFactory.decodeFile(f.toString());
        imageRow.setImageBitmap(bitmap);
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
