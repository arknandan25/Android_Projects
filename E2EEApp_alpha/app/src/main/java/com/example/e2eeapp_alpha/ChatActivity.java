package com.example.e2eeapp_alpha;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;


import com.example.e2eeapp_alpha.Chats.ChatObject;
import com.example.e2eeapp_alpha.Chats.MessageAdapter;
import com.example.e2eeapp_alpha.Chats.MessageObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity  extends AppCompatActivity {
    private RecyclerView mChatRV;
//    private  RecyclerView.Adapter mChatAdapter;
    private  MessageAdapter mChatAdapter;

    private RecyclerView.LayoutManager mChatLayoutManager;

    private ImageButton mButtonSendMsg;
    private ImageButton mButtonChooseImage;
    private EditText msg_write;
    private TextView userName;
    private CircleImageView userImage;
    public String receiverId="", senderId="", sessionId="", receiverImage = "", receiverName = "";

    private Toolbar chatToolBar;
    private DatabaseReference RootRef;


    ArrayList<MessageObject> messageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mButtonChooseImage = (ImageButton) findViewById(R.id.attach_file);
        mButtonSendMsg = (ImageButton) findViewById(R.id.send_message);
        msg_write =  (EditText) findViewById(R.id.plaintext_to_send);
        userName = (TextView) findViewById(R.id.custom_user_name);
        userImage = findViewById(R.id.custom_profile_image);
        RootRef = FirebaseDatabase.getInstance().getReference();


        chatToolBar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(chatToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.custom_chat_bar, null);
        actionBar.setCustomView(actionBarView);



        receiverId = getIntent().getExtras().getString("receiver_id");
        receiverImage = getIntent().getExtras().getString("receiver_profile_image");
        receiverName = (String) getIntent().getExtras().get("receiver_name");

        //setting name and profile image in the custom chat bar
        userName.setText(receiverName);
        Picasso.get().load(receiverImage).placeholder(R.drawable.profile_image).into(userImage);


        senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i("senderID:", senderId);
        Log.i("receiverID:", receiverId);


        // get the session Id here
        final Boolean[] flag = {false};
        DatabaseReference query = FirebaseDatabase.getInstance().getReference()
                .child("User_ONO_Chat")
                .child(senderId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot childSnapshot : snapshot.getChildren()){
                        ChatObject mChatObj = new ChatObject(childSnapshot.getKey(), (String) childSnapshot.getValue());
                        if (receiverId.equals(mChatObj.getUserId())){
                            Log.i("The sessionId found:", childSnapshot.getKey());
                            sessionId = mChatObj.getSessionId();
                            flag[0] = true;
                            break;
                        }
                    }
                    //when break hapens control comes here
                    if (flag[0] == false){
                        Log.i("The sessionId DOES:", "NOT EXIST");
                    }
                }else{
                    Log.i("The SENDER NOT :", "found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Session Id error", error.getMessage());
            }
        });

        //code for sessionid ends here

        Log.i("the session Id set:", ""+ sessionId);

        mButtonSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this,"Send Button clicked", Toast.LENGTH_SHORT).show();
                sendMesage();
            }
        });

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this,"Choose Image Button clicked", Toast.LENGTH_SHORT).show();
            }
        });


        initializeRecyclerview();
//        getChats();

    }


//    private void getChats() {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats").child(sessionId);
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                messageList.clear();
//                Map<String, MessageObject> newMessageMap = (Map<String, MessageObject>) snapshot.getValue();
//
//                Log.i("1data:", String.valueOf(newMessageMap.get(sessionId)));
//                Map<String, MessageObject> dataMap = (Map<String, MessageObject>) newMessageMap.get(sessionId);
//                Log.i("dataMap:", dataMap.toString());
//                for (Map.Entry<String, MessageObject> entry : dataMap.entrySet()){
//                    Log.i("dataMap Key:", entry.getKey());
//                    Log.i("dataMap Value:", String.valueOf(entry.getValue()));
//                    Map<String, MessageObject> dataMapValue = (Map<String, MessageObject>) entry.getValue();
//                    String   ReceiverId, SenderId, MessageText, MessageTSP, MessageType;
//
//                    ReceiverId = String.valueOf(dataMapValue.get("msg_receiver_id"));
//                    SenderId = String.valueOf(dataMapValue.get("msg_sender_id"));
//
//                    MessageText = String.valueOf(dataMapValue.get("msg_text"));
//
//                    MessageTSP = String.valueOf(dataMapValue.get("msg_timestamp"));
//
//                    MessageType = String.valueOf(dataMapValue.get("msg_type"));
//
//
//                    Log.i("dataMap Value sep:", String.valueOf(dataMapValue.get("msg_text")));
//
//                    MessageObject messageObject1 = new MessageObject(ReceiverId, SenderId, MessageText, MessageTSP, MessageType);
//                    messageList.add(messageObject1);
//
//                }
//                    mChatLayoutManager.scrollToPosition(messageList.size()-1);
//                    mChatAdapter = new MessageAdapter(messageList);
//                    mChatRV.setAdapter(mChatAdapter);
//                    mChatAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
////        attempt 2 end
//
//
//    }

    @Override
    protected void onStart() {
        super.onStart();

        RootRef.child("UserChatsTemp").child(senderId).child(receiverId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        MessageObject messageObject = snapshot.getValue(MessageObject.class);
                        messageList.add(messageObject);
                        mChatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    //    2nd working sendMessage
//    private void sendMesage() {
//        //send the message typed by the user
//        Log.i("senderID;sendMessage:", senderId);
//        Log.i("receiverID;sendMessage:", receiverId);
//        Log.i("sessionId;sendMessage:", "" + sessionId);
//
//        if (!msg_write.getText().toString().isEmpty()) {
//            //user has typed something
//            String mid = UUID.randomUUID().toString();
//            DatabaseReference uploadMsg = FirebaseDatabase.getInstance().getReference().child("Chats").child(sessionId).push();
//
//            Date date = new Date();
//
//            Map newMessageMap = new HashMap<>();
//            newMessageMap.put("msg_receiver_id", receiverId);
//            newMessageMap.put("msg_sender_id", senderId);
//            newMessageMap.put("msg_text", msg_write.getText().toString());
//
//            newMessageMap.put("msg_timestamp", String.valueOf(date.getTime()));
//
//            newMessageMap.put("msg_type", "text");
//            uploadMsg.updateChildren(newMessageMap);
//
//            msg_write.setText("");
//
//        }
//    }

//3rd send button tut follow
    private void sendMesage() {
        //send the message typed by the user
        Log.i("senderID;sendMessage:", senderId);
        Log.i("receiverID;sendMessage:", receiverId);
        Log.i("sessionId;sendMessage:", "" + sessionId);

        if (!msg_write.getText().toString().isEmpty()) {
            //user has typed something
            String msgSenderRef = "UserChatsTemp/" + senderId + "/" + receiverId;
            String msgReceiverRef = "UserChatsTemp/" + receiverId + "/" + senderId;

            //random key for each message


            DatabaseReference userMessageKeyRef = RootRef.child("UserChatsTemp")
                    .child(senderId)
                    .child(receiverId)
                    .push();

            String messagePushID = userMessageKeyRef.getKey();

            Date date = new Date();

            Map newMessageMap = new HashMap();
            newMessageMap.put("message", msg_write.getText().toString());
            newMessageMap.put("type", "text");

            newMessageMap.put("from", senderId);

            Map newMessageMapDetails = new HashMap();
            newMessageMapDetails.put(msgSenderRef + "/" + messagePushID, newMessageMap);
            //receiver
            newMessageMapDetails.put(msgReceiverRef + "/" + messagePushID, newMessageMap);


            //UPLOAD ADTA
            RootRef.updateChildren(newMessageMapDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChatActivity.this, "Message Sent!", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(ChatActivity.this, "Error occured!Try again", Toast.LENGTH_SHORT).show();

                    }
                    msg_write.setText("");

                }
            });


        }else{
            Toast.makeText(ChatActivity.this, "Type something!", Toast.LENGTH_SHORT).show();
        }
    }



    private void initializeRecyclerview() {
        messageList= new ArrayList<MessageObject>();
        mChatRV = (RecyclerView)findViewById(R.id.chatRV);
        mChatRV.setNestedScrollingEnabled(false);
        mChatRV.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(this);
        mChatRV.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new MessageAdapter(messageList);
        mChatRV.setAdapter(mChatAdapter);

    }
}