package com.example.e2eeapp_alpha.GroupChat;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.e2eeapp_alpha.Chats.MessageAdapter;
import com.example.e2eeapp_alpha.Chats.MessageObject;
import com.example.e2eeapp_alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GroupChatAdapter  extends RecyclerView.Adapter<GroupChatAdapter.GroupChatViewHolder>{

    ArrayList<GroupObject> ListToDisplay;
    private Context mContext;

    FirebaseAuth mAuth;
    FirebaseDatabase userRef;


    public GroupChatAdapter( Context context, ArrayList<GroupObject> listToDisplay) {
        this.mContext = context;
        this.ListToDisplay = listToDisplay;
    }

    @NonNull
    @Override
    public GroupChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cutom_gchat_layout,parent,false);

        mAuth = FirebaseAuth.getInstance();
        return new GroupChatAdapter.GroupChatViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupChatViewHolder holder, int position) {
        GroupObject groupObject = ListToDisplay.get(position);
        String message = groupObject.getMessage();

        holder.receiver.setVisibility(View.INVISIBLE);
        holder.sender.setVisibility(View.VISIBLE);
        if (groupObject.getSenderUid().equals(mAuth.getCurrentUser().getUid())){
            //current user
            holder.SenderMessage.setBackgroundResource(R.drawable.sender_messages_layout);
            holder.SenderMessage.setTextColor(Color.BLACK);
            holder.SenderMessage.setText(message);
            holder.SenderName.setText(groupObject.getSenderName());
            holder.SenderTime.setText(groupObject.getTime());
        }else{
            holder.sender.setVisibility(View.INVISIBLE);
            holder.receiver.setVisibility(View.VISIBLE);

            holder.ReceiverMessage.setBackgroundResource(R.drawable.receiver_messages_layout);
            holder.ReceiverMessage.setTextColor(Color.BLACK);
            holder.ReceiverMessage.setText(message);
            holder.ReceiverName.setText(groupObject.getSenderName());

            holder.ReceiverTime.setText(groupObject.getTime());
        }


    }

    @Override
    public int getItemCount() {
        return ListToDisplay.size();
    }


    public class GroupChatViewHolder extends RecyclerView.ViewHolder{
        TextView SenderMessage, ReceiverMessage, ReceiverTime, SenderTime, ReceiverName, SenderName;
        LinearLayout receiver, sender;

        public GroupChatViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize them here
            SenderMessage = (TextView)itemView.findViewById(R.id.sender_message_text);
            ReceiverMessage = (TextView)itemView.findViewById(R.id.receiver_message_text);

            SenderTime =(TextView)itemView.findViewById(R.id.sender_message_time);
            ReceiverTime =(TextView)itemView.findViewById(R.id.receiver_message_time);
            receiver = (LinearLayout)itemView.findViewById(R.id.receiver_side);
            sender = (LinearLayout)itemView.findViewById(R.id.sender_side);

            ReceiverName = (TextView)itemView.findViewById(R.id.receiver_name);
            SenderName = (TextView)itemView.findViewById(R.id.sender_name);




        }
    }

}
