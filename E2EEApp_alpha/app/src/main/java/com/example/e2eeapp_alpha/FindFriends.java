package com.example.e2eeapp_alpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriends extends AppCompatActivity {

    private RecyclerView FindFriendsRecyclerList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        // initialize all front-end components for this activity
        Initialize();
    }

    private void Initialize() {
        FindFriendsRecyclerList = findViewById(R.id.find_friends_recycler_list);
        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User_Data_Temp");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //this recycler view is differnt from the image one as, there we are creating a list of data
        //feeding that list to the recycler and then displaying to the user
        //here we just use the option/query to get all contacts from the firebase db and get the job done
        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(databaseReference, Contacts.class)
                .build();

        FirebaseRecyclerAdapter<Contacts, FindFriendsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, FindFriendsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendsViewHolder holder, int position, @NonNull Contacts model) {
                        //your code for getting data from DB and displaying ti front-end goes here
                        holder.UserName.setText(model.getName());
                        holder.UserPhone.setText(model.getPhone());
                        holder.UserStatus.setText(model.getStatus());
                        Picasso.get().load(model.getImage()).into(holder.UserProfile);
                    }

                    @NonNull
                    @Override
                    public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        //connect your each item display here
                        // for this case it will be the friends_display_layout
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_display_layout,
                                parent, false);

                        FindFriendsViewHolder viewHolder = new FindFriendsViewHolder(view);
                        return viewHolder;
                    }
                };

        //Fire up the recycler view
        FindFriendsRecyclerList.setAdapter(adapter);
        adapter.startListening();


    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder{

        //add all the text views, image views that are in your individual item layout
        TextView UserName, UserPhone, UserStatus;
        CircleImageView UserProfile;

        public FindFriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize them here
            UserName = itemView.findViewById(R.id.friend_name);
            UserPhone = itemView.findViewById(R.id.friend_phone);
            UserProfile = itemView.findViewById(R.id.friend_profile_image);
            UserStatus = itemView.findViewById(R.id.friend_status);
        }
    }
}