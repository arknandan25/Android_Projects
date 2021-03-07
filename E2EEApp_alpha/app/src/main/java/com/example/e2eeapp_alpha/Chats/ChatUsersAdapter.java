package com.example.e2eeapp_alpha.Chats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e2eeapp_alpha.ChatActivity;
import com.example.e2eeapp_alpha.Contacts;
import com.example.e2eeapp_alpha.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUsersAdapter extends RecyclerView.Adapter<ChatUsersAdapter.UserListViewHolder> {

    private Context mContext;
        ArrayList<Contacts> ListToDisplay;

    public ChatUsersAdapter(Context context, ArrayList<Contacts> ListtoDisplay){
        this.mContext = context;
        this.ListToDisplay = ListtoDisplay;
    }
//    public ChatUsersAdapter(ArrayList<Contacts> ListtoDisplay){
//        this.ListToDisplay = ListtoDisplay;
//    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_display_layout,null,false);

        return new UserListViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        
        Contacts mObj = ListToDisplay.get(position);
        holder.UserName.setText(mObj.getName());
        holder.UserPhone.setText(mObj.getPhone());
        holder.UserStatus.setText(mObj.getStatus());
        Picasso.get().load(mObj.getImage()).placeholder(R.drawable.profile_image).into(holder.UserProfile);



        holder.Userlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,"User Clicked!", Toast.LENGTH_SHORT).show();
                //send user to the chat activity where the conversation happens
                String id =  mObj.getUid().toString();
                Intent intent = new Intent(mContext, ChatActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("receiver_id", id);
                intent.putExtra("receiver_id", id);
                intent.putExtra("receiver_profile_image", mObj.getImage());
                intent.putExtra("receiver_name", mObj.getName());

                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ListToDisplay.size();
    }
    
    public  static  class UserListViewHolder extends RecyclerView.ViewHolder{

        TextView UserName, UserPhone, UserStatus;
        CircleImageView UserProfile;
        RelativeLayout Userlayout;
        
        public UserListViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize them here
            UserName = itemView.findViewById(R.id.friend_name);
            UserPhone = itemView.findViewById(R.id.friend_phone);
            UserProfile = itemView.findViewById(R.id.friend_profile_image);
            UserStatus = itemView.findViewById(R.id.friend_status);
            Userlayout = itemView.findViewById(R.id.friend_item_layout);
        }

    }
}
