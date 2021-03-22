package com.example.e2eeapp_alpha;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;


import com.example.e2eeapp_alpha.Chats.ChatObject;
import com.example.e2eeapp_alpha.Chats.MessageAdapter;
import com.example.e2eeapp_alpha.Chats.MessageObject;
import com.example.e2eeapp_alpha.Database.DBEncrypter;
import com.example.e2eeapp_alpha.Database.DatabaseHelper;
import com.example.e2eeapp_alpha.Encryption.EncryptionObject;
import com.example.e2eeapp_alpha.Encryption.TextEncryptor;
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

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
    private FirebaseAuth mAuth;
    private Context context = this;

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;


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
                        String curr_message_key = snapshot.getKey();
                        MessageObject messageObject = snapshot.getValue(MessageObject.class);
                        Log.i("OCA", "--------------Child addded -------------");
                        Log.i("OCA", messageObject.getMessage());
                        Log.i("OCA", messageObject.getFrom());
                        Log.i("OCA", messageObject.getTimestamp());
                        Log.i("OCA", messageObject.getType());
                        Log.i("OCA", "--------------Child added Ended -------------");

                        //decrypt message
                        messageList.add(messageObject);
                        mChatAdapter.notifyDataSetChanged();

                        //check if the 'from' id is not the current current user then ratchet chain key
                        mAuth = FirebaseAuth.getInstance();
                        if (!(mAuth.getCurrentUser().getUid().equals(messageObject.getFrom()))){
                            Log.i("StateRatchet", "Current Uid != sender msg for current message");
                            SharedPreferences sharedPreferences =   context.getSharedPreferences("ono_chat_keys",Context.MODE_PRIVATE);
                            String ck =  sharedPreferences.getString("ck", "CHAINKEY ONOsp");
                            byte[] ck_byte = new byte[0];
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                 ck_byte = Base64.getDecoder().decode(ck);
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                TextEncryptor.ratchetKey(context, ck_byte);
                            }

                        }


                        //add plaintext message->encrypt using password based encryption to SQLITE DB
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            try {
                                addDataToDB(messageObject, curr_message_key);
                            } catch (NoSuchPaddingException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (IllegalBlockSizeException e) {
                                e.printStackTrace();
                            } catch (BadPaddingException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (InvalidKeyException e) {
                                e.printStackTrace();
                            }
                        }
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addDataToDB(MessageObject messageObject, String curr_message_key) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        db = openHelper.getWritableDatabase();

        //decrypt the ciphertext first re-encrypt it then to local DB
        String decrypted_message = getDecryptedMessage(messageObject.getMessage(), messageObject.getUcid());
        String password = "123456";
        Log.i("DB-ENCRYPT", DBEncrypter.Encrypt(decrypted_message, password));
        Log.i("DB-DECRYPT", DBEncrypter.Decrypt(DBEncrypter.Encrypt(decrypted_message, password), password));

//        //insert data from the messageObject to the Local SQLite database
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.col2, curr_message_key);
        contentValues.put(DatabaseHelper.col3, DBEncrypter.Encrypt(decrypted_message, password));
        contentValues.put(DatabaseHelper.col4, messageObject.getTime());
        contentValues.put(DatabaseHelper.col5, messageObject.getFrom());
        contentValues.put(DatabaseHelper.col6, messageObject.getTo());
        long id = db.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
        if (id > 0){
            Toast.makeText(this, "Encrypted Message added to Local Database", Toast.LENGTH_LONG).show();
            Log.i("LOCAL-DB", "Message added to database");

        }else{
            Toast.makeText(this, "Unable to add Encrypted Message to Local Database", Toast.LENGTH_LONG).show();
            Log.i("LOCAL-DB", "Unable to add Message to database");


        }
    }

    public  String getDecryptedMessage(String message, String decKey){
        //set the decrypted text here
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    byte[] x = new byte[0];
                    byte[] y = new byte[0];
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        x = Base64.getDecoder().decode(message);
                        y = Base64.getDecoder().decode(decKey);
                        Log.i("ChatActivity:Message", message);
                    }
                    return  TextEncryptor.DecryptAES(x, context, y);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
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

         String currentDate, currentTime;

        //current date
        Calendar calendar_date = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        currentDate = simpleDateFormat.format(calendar_date.getTime());

        //current time
        Calendar calendar_time = Calendar.getInstance();
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm:ss a");
        currentTime = simpleTimeFormat.format(calendar_time.getTime());

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

            //map used to input data to the server
            Map newMessageMap = new HashMap();
//            newMessageMap.put("message", msg_write.getText().toString());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    EncryptionObject x = TextEncryptor.EncryptAES(this, msg_write.getText().toString());
                    newMessageMap.put("message", x.getCiphertext());
                    newMessageMap.put("ucid", x.getEkey());

                    Log.i("sendMessage>Ciphertext", x.getCiphertext());
                    Log.i("sendMessage>CHAINKey", "" + x.getChainKey());
                    Log.i("sendMessage>MSGKey", "" + x.getEkey());

//                    newMessageMap.put("message", TextEncryptor.EncryptAES(this, msg_write.getText().toString()));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            newMessageMap.put("type", "text");

            newMessageMap.put("from", senderId);

            newMessageMap.put("to", receiverId);

            newMessageMap.put("timestamp", String.valueOf(date.getTime()));

            newMessageMap.put("time", currentTime);

            newMessageMap.put("date", currentDate);



            Map newMessageMapDetails = new HashMap();
            newMessageMapDetails.put(msgSenderRef + "/" + messagePushID, newMessageMap);
            //receiver
            newMessageMapDetails.put(msgReceiverRef + "/" + messagePushID, newMessageMap);


            //UPLOAD DATA CHECK
            RootRef.updateChildren(newMessageMapDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChatActivity.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                        //if the message upload is successful


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




    //encryption test- send button


//    private void sendMesage() {
//        //send the message typed by the user
//        Log.i("senderID;sendMessage:", senderId);
//        Log.i("receiverID;sendMessage:", receiverId);
//        Log.i("sessionId;sendMessage:", "" + sessionId);
//
//        if (!msg_write.getText().toString().isEmpty()) {
//            //user has typed something
//            String msgSenderRef = "UserChatsTemp/" + senderId + "/" + receiverId;
//            String msgReceiverRef = "UserChatsTemp/" + receiverId + "/" + senderId;
//
//            //random key for each message
//
//
////            newMessageMap.put("message", msg_write.getText().toString());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                try {
//
//                    Log.i("sendMesageENC: ", TextEncryptor.EncryptAES(this, msg_write.getText().toString()));
//                    byte[] x = new byte[0];
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                        x = Base64.getDecoder().decode(TextEncryptor.EncryptAES(this, msg_write.getText().toString()));
//                    }
//                    Log.i("sendMesageDEC: ", TextEncryptor.DecryptAES( x, this));
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                } catch (InvalidKeyException e) {
//                    e.printStackTrace();
//                } catch (IllegalBlockSizeException e) {
//                    e.printStackTrace();
//                } catch (BadPaddingException e) {
//                    e.printStackTrace();
//                } catch (InvalidAlgorithmParameterException e) {
//                    e.printStackTrace();
//                } catch (NoSuchPaddingException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }else{
//            Toast.makeText(ChatActivity.this, "Type something!", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//




    //end of send button



    private void initializeRecyclerview() {
        messageList= new ArrayList<MessageObject>();
        mChatRV = (RecyclerView)findViewById(R.id.chatRV);
        mChatRV.setNestedScrollingEnabled(false);
        mChatRV.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(this);
        mChatRV.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new MessageAdapter(this, messageList);
        mChatRV.setAdapter(mChatAdapter);
        openHelper = new DatabaseHelper(this);

    }
}