package com.example.e2eeapp_alpha.Chats;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e2eeapp_alpha.Contacts;
import com.example.e2eeapp_alpha.R;
import com.example.e2eeapp_alpha.UserListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{


    ArrayList<MessageObject> ListToDisplay;
    FirebaseAuth mAuth;
    FirebaseDatabase userRef;


    public MessageAdapter( ArrayList<MessageObject> listToDisplay) {
        this.ListToDisplay = listToDisplay;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_message_layout,parent,false);

        mAuth = FirebaseAuth.getInstance();
        return new MessageViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {

        String messageSenderId = mAuth.getCurrentUser().getUid();
        MessageObject messageObject = ListToDisplay.get(position);
        String fromUserID = messageObject.getFrom();
        String MessageType = messageObject.getType();
        String message = messageObject.getMessage();
        if (MessageType.equals("text")){
            holder.ReceiverMessage.setVisibility(View.INVISIBLE);
            holder.SenderMessage.setVisibility(View.VISIBLE);
            if (fromUserID.equals(messageSenderId)){
                //current user
                holder.SenderMessage.setBackgroundResource(R.drawable.sender_messages_layout);
                holder.SenderMessage.setTextColor(Color.BLACK);
                holder.SenderMessage.setText(message);
            }else{
                holder.SenderMessage.setVisibility(View.INVISIBLE);
                holder.ReceiverMessage.setVisibility(View.VISIBLE);

                holder.ReceiverMessage.setBackgroundResource(R.drawable.receiver_messages_layout);
                holder.ReceiverMessage.setTextColor(Color.BLACK);
                holder.ReceiverMessage.setText(message);
            }


        }


    }

    @Override
    public int getItemCount() {
        return ListToDisplay.size();
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder{
        //add all the text views, image views that are in your individual item layout
        TextView SenderMessage, ReceiverMessage;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize them here
            SenderMessage = (TextView)itemView.findViewById(R.id.sender_message_text);
            ReceiverMessage = (TextView)itemView.findViewById(R.id.receiver_message_text);
        }
    }



}
