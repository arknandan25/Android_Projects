package com.example.e2eeapp_alpha.GroupChat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.e2eeapp_alpha.Chats.MessageAdapter;
import com.example.e2eeapp_alpha.Chats.MessageObject;
import com.example.e2eeapp_alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton SendMessageButton;
    private EditText MessageInput;
    private ScrollView scrollView;
    private RecyclerView gChatRV;
    private GroupChatAdapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;
    private ArrayList<GroupObject> grpMessageList;

    private TextView displayGroupMessages;
    private String currentGroupName, currentUserId, currentUserName, currentDate, currentTime;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference, groupReference, groupMessageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //get groupName from Intent
        currentGroupName = (String) getIntent().getExtras().get("groupName");

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("User_Data_Temp");
        groupReference = FirebaseDatabase.getInstance().getReference()
                .child("GroupMessages")
                .child(currentGroupName);

        Initialiaze();
        GetUserInfo();

        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMessageToServer();
                MessageInput.setText("");
            }
        });
    }

    private void SaveMessageToServer() {
        String message = MessageInput.getText().toString();
        String messageKey = groupReference.push().getKey();
        if (TextUtils.isEmpty(message)){
            Toast.makeText(this, "Write a message!", Toast.LENGTH_LONG).show();
        }else{
            //current date
            Calendar calendar_date = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = simpleDateFormat.format(calendar_date.getTime());

            //current time
            Calendar calendar_time = Calendar.getInstance();
            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm:ss a");
            currentTime = simpleTimeFormat.format(calendar_time.getTime());

            //adding key to Group and separately the data using HashMap
            HashMap<String, Object> groupMessageKey = new HashMap<>();
            groupReference.updateChildren(groupMessageKey);

            groupMessageRef = groupReference.child(messageKey);

//            HashMap<String, Object> messageMap = new HashMap<>();
//            messageMap.put("senderName",currentUserName);
//            messageMap.put("senderUid",currentUserId);
//            messageMap.put("groupName",currentGroupName);
//            messageMap.put("message",message);
//            messageMap.put("messageKey",messageKey);
//            messageMap.put("date",currentDate);
//            messageMap.put("time", currentTime);

            GroupObject groupObject = new GroupObject(
                    currentDate,
                    currentGroupName,
                    message,
                    messageKey,
                    currentUserName,
                    currentUserId,
                    currentTime
            );

//            groupMessageRef.updateChildren(messageMap);
            groupMessageRef.setValue(groupObject);

        }
    }

    private void GetUserInfo() {
        databaseReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    currentUserName = snapshot.child("name").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        groupReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                if (snapshot.exists()){
//                    DisplayGroupMessages(snapshot);
//                }
                GroupObject groupObject  =snapshot.getValue(GroupObject.class);
                grpMessageList.add(groupObject);
                mChatAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                GroupObject groupObject  =snapshot.getValue(GroupObject.class);
                if (mAuth.getCurrentUser().getUid().equals(groupObject.getSenderUid())){
                    Toast.makeText(GroupChatActivity.this,"Message from:" + groupObject.getSenderName().toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void DisplayGroupMessages(DataSnapshot snapshot) {
        Iterator iterator  =snapshot.getChildren().iterator();

        while (iterator.hasNext()){
            String date  = (String)((DataSnapshot)iterator.next()).getValue();
        }
    }


    private void Initialiaze() {
//        toolbar = findViewById(R.id.gchat_toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroupName);

        SendMessageButton = (ImageButton) findViewById(R.id.gchat_send_message);
        MessageInput = (EditText) findViewById(R.id.gchat_plaintext_to_send);

        grpMessageList= new ArrayList<GroupObject>();
        gChatRV = (RecyclerView)findViewById(R.id.gchatRV);
        gChatRV.setNestedScrollingEnabled(false);
        gChatRV.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(this);
        gChatRV.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new GroupChatAdapter(this, grpMessageList);
        gChatRV.setAdapter(mChatAdapter);

//        scrollView = (ScrollView) findViewById(R.id.gchat_scrollView);
//        displayGroupMessages = (TextView) findViewById(R.id.gchat_msg_display);



    }
}

//<include
//        android:id="@+id/gchat_toolbar"
//                layout="@layout/custom_group_chat_bar"
//                android:layout_width="match_parent"
//                android:layout_height="wrap_content">
//
//</include>