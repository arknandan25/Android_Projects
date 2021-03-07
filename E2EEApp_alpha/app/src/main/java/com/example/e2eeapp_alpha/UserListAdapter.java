package com.example.e2eeapp_alpha;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e2eeapp_alpha.Chats.ChatObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder>{

    //
    private Context mContext;
    ArrayList<Contacts> ListToDisplay;

//    constructor for the adapter
    public UserListAdapter(Context context, ArrayList<Contacts> ListtoDisplay){
        mContext = context;
        this.ListToDisplay = ListtoDisplay;

    }
//    public UserListAdapter(ArrayList<Contacts> ListtoDisplay){
//        this.ListToDisplay = ListtoDisplay;
//
//    }


    @NonNull
    @Override
    public UserListAdapter.UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_display_layout,null,false);
        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_display_layout,null,false);

        return new UserListViewHolder(layoutView);

    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.UserListViewHolder holder, int position) {
        //your code for getting data from DB and displaying ti front-end goes here
        Contacts currentData = ListToDisplay.get(position);

        holder.UserName.setText(currentData.getName());
        holder.UserPhone.setText(currentData.getPhone());
        holder.UserStatus.setText(currentData.getStatus());
        Picasso.get().load(currentData.getImage()).placeholder(R.drawable.profile_image).into(holder.UserProfile);

        //upon click of any item i.e the name, phone, image, status
        //upon click of these should initiate a new session between the two users

        holder.Userlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("You clicked a user!!!", "now onto the code");
                //another method is required here to check if you have already clicked on this user
                //before; then don't create the unique session again
                /*
                * User_ONO_Chat
                *   -curr_user_id(U1)
                *       -Unique key generated on click(K) : the user(U2) upon which the click was made
                *   -U2
                *       -Unique key generated on click(K) : the user(U1)
                * */
                final Boolean[] flag = {false};
                DatabaseReference query = FirebaseDatabase.getInstance().getReference()
                        .child("User_ONO_Chat")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                String userId_clicked = ListToDisplay.get(position).getUid();

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            //the userID provided in query exits in database
                            Log.i("Inside", "if");
                            for (DataSnapshot childSnapshot : snapshot.getChildren()){
                                ChatObject mChatObj = new ChatObject(childSnapshot.getKey().toString(), childSnapshot.getValue().toString());
                                Log.i("Id we are in search for", userId_clicked);
                                Log.i("Item retrieved for ONO:", childSnapshot.getKey() + ":::" + childSnapshot.getValue());
                                if (userId_clicked.trim().equals(mChatObj.userId)){
                                    //this means that id already exist
                                    //i.e. session is already created; donot create again
                                    Log.i("Session already created", "Toast to be displayed");

                                    Toast.makeText(mContext, "You have already created a session!" , Toast.LENGTH_LONG).show();
                                    flag[0] = true;//true means that its a pre existing contact so no need to add
                                    //if false that means the contact you clicked on is not already present so add it!
                                    break;
                                }
                            }// end of for loop
                            if(flag[0] == false){
                                Log.i("Inside", "create session wit new user");

                                //generate a unique key that represents the chat session between the two users
                                String key = FirebaseDatabase.getInstance().getReference().child("User_Chats").push().getKey();

                                //There will be a child node called User_ONO_Chat
                                //each user participating in One on one chat will have their Uid as key and all the  unique keys
                                //for each ONO chat session they are currently a participating in
                                FirebaseDatabase.getInstance().getReference().child("User_ONO_Chat").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).setValue(ListToDisplay.get(position).getUid());

                                //above line sets that unique session key to me(the sender) in User_ONO_Chat
                                //now we allocate  the same  unique session key to the receiver; for that we need their Uid
                                FirebaseDatabase.getInstance().getReference().child("User_ONO_Chat").child(ListToDisplay.get(position).getUid()).child(key).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                Toast.makeText(mContext, "New Chat session created!" , Toast.LENGTH_LONG).show();
                            }

                        }else{
                            Log.i("Inside", "else");

                            //generate a unique key that represents the chat session between the two users
                            String key = FirebaseDatabase.getInstance().getReference().child("User_Chats").push().getKey();

                            //There will be a child node called User_ONO_Chat
                            //each user participating in One on one chat will have their Uid as key and all the  unique keys
                            //for each ONO chat session they are currently a participating in
                            FirebaseDatabase.getInstance().getReference().child("User_ONO_Chat").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).setValue(ListToDisplay.get(position).getUid());

                            //above line sets that unique session key to me(the sender) in User_ONO_Chat
                            //now we allocate  the same  unique session key to the receiver; for that we need their Uid
                            FirebaseDatabase.getInstance().getReference().child("User_ONO_Chat").child(ListToDisplay.get(position).getUid()).child(key).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            Toast.makeText(mContext, "New Chat session created!" , Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("Unknown error occurred", error.getMessage().toString());
                    }
                });

//                Test code with some insights
//                //pointing to the child User_ONO_Chat
//                DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("User_ONO_Chat");
//                //1st check whether the current user has any contacs ie. his id is present there
////                Query query = dataref.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                Query query = dataref.child("userID");
//
//
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()){
//                            Log.i("Userid exists", snapshot.getKey() + ":" + snapshot.getValue() + ":" + snapshot.getChildren());
////I/Userid exists: userID:{chatsessionkey3=receiverID3, chatsessionkey2=receiverID2}:com.google.firebase.database.DataSnapshot$1@14ae7f9
//
//                            //now we check if the user it clicked on is already on their contact
//                            for (DataSnapshot x : snapshot.getChildren()){
//                                ChatObject mChatObj = new ChatObject(x.getKey().toString(), x.getValue().toString());
//
//                                Log.i("data iterated:", x.)
//                            }
//
//
//                        }else{
//                            Log.i("UserId does not exit", "if");
//                            //add current user's 1st contact
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//
//
////


//
//                //generate a unique key that represents the chat session between the two users
//                String key = FirebaseDatabase.getInstance().getReference().child("User_Chats").push().getKey();
//
//                //There will be a child node called User_ONO_Chat
//                //each user participating in One on one chat will have their Uid as key and all the  unique keys
//                //for each ONO chat session they are currently a participating in
//                FirebaseDatabase.getInstance().getReference().child("User_ONO_Chat").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).setValue(ListToDisplay.get(position).getUid());
//
//                //above line sets that unique session key to me(the sender) in User_ONO_Chat
//                //now we allocate  the same  unique session key to the receiver; for that we need their Uid
//                FirebaseDatabase.getInstance().getReference().child("User_ONO_Chat").child(ListToDisplay.get(position).getUid()).child(key).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());


            }
        });

    }

    @Override
    public int getItemCount() {
        return ListToDisplay.size();
    }

    public static class UserListViewHolder extends RecyclerView.ViewHolder{
        //add all the text views, image views that are in your individual item layout
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
