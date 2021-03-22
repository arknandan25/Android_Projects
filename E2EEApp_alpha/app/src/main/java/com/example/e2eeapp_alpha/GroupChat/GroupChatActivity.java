package com.example.e2eeapp_alpha.GroupChat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.e2eeapp_alpha.ChatActivity;
import com.example.e2eeapp_alpha.Chats.MessageAdapter;
import com.example.e2eeapp_alpha.Chats.MessageObject;
import com.example.e2eeapp_alpha.Encryption.EncryptionObject;
import com.example.e2eeapp_alpha.Encryption.TextEncryptor;
import com.example.e2eeapp_alpha.Fragments;
import com.example.e2eeapp_alpha.GroupChatEncryption.GCTextEncryptor;
import com.example.e2eeapp_alpha.GroupChatEncryption.GroupKeys;
import com.example.e2eeapp_alpha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.example.e2eeapp_alpha.GroupChatEncryption.GCTextEncryptor.generateSharedSecretAndRetriveGK;

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
    private String currentGroupName, currentUserId, currentUserName, currentDate, currentTime, status;
    private Context context;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference, groupReference, groupMessageRef;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //get groupName from Intent
        currentGroupName = (String) getIntent().getExtras().get("groupName");

        //shared secret and retrieving group key

        generateSharedSecretAndRetriveGK(this, currentGroupName);

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
                try {
                    SaveMessageToServer();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
                MessageInput.setText("");

            }
        });
    }

    private void SaveMessageToServer() throws BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException {
        /*Group Member's message sent to the server here
        * 1.Get the message and encrypt it, receive back the cipher object
        * 2.
        * 3.
        * 4.
        * */

        String message = MessageInput.getText().toString();
        String messageKey = groupReference.push().getKey();
        String ciphertext_tosend, ucid;
        if (TextUtils.isEmpty(message)){
            Toast.makeText(this, "Write a message!", Toast.LENGTH_LONG).show();
        }else{

            //Call EncryptAES to convert this plaintext message to cipher text
            EncryptionObject encryptionObject = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                 encryptionObject = GCTextEncryptor.EncryptAES(this, message);
            }
            ciphertext_tosend = encryptionObject.getCiphertext();
            ucid = encryptionObject.getEkey();

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
                    ciphertext_tosend,
                    messageKey,
                    currentUserName,
                    currentUserId,
                    currentTime,
                    ucid
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
        context = this;

        //set the status of the current user admin or group member
        SharedPreferences users_profile
                =   context.getSharedPreferences("dynamicUser", Context.MODE_PRIVATE);
        if (users_profile.getString("status", null) != null && users_profile.getString("status", null).equals("admin")){
            status = "Admin";
        }else {
            status = "Group Member";

        }

        //check for new messages sent by any user
        groupReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                GroupObject groupObject  =snapshot.getValue(GroupObject.class);

                Log.i("OCA", "--------------New Group Child addded -------------");
                Log.i("OCA", groupObject.getMessage());
                Log.i("OCA", groupObject.getSenderName());
                Log.i("OCA", groupObject.getSenderUid());
                Log.i("OCA", groupObject.getTime());
                Log.i("OCA", "--------------New Group Child added Ended -------------");

                //check if the 'from' id is not the current current user then ratchet chain key
                mAuth = FirebaseAuth.getInstance();
                if (!(mAuth.getCurrentUser().getUid().equals(groupObject.getSenderUid()))){
                    Log.i("StateRatchet", "-----------Ratchet Mechanism activated for this user as they are the recipient of a message!!-----------");
                    Log.i("StateRatchet", "Current Uid != sender msg for current message");
                    SharedPreferences users_profile =  context.getSharedPreferences("dynamicUser", Context.MODE_PRIVATE);
                    String curr_gk =  users_profile.getString("gk", "GrpKEY");
                    byte[] curr_gk_byte = new byte[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        curr_gk_byte = Base64.getDecoder().decode(curr_gk);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        GCTextEncryptor.ratchetKey(context, curr_gk_byte);
                    }

                }

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

        //constantly check if a user left the group
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("GroupKeys").child(currentGroupName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("DELETE-OCA", snapshot.getKey() + " is deleted! ");

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("DELETE-OCC", snapshot.getKey() + " is deleted! ");

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.i("DELETE-OCR", snapshot.getKey() + " is deleted! ");
                //check if the current user is admin
                SharedPreferences users_profile
                        =   context.getSharedPreferences("dynamicUser", Context.MODE_PRIVATE);

                if (users_profile.getString("status", null) != null && users_profile.getString("status", null).equals("admin")){
                    //"status" key exist in the Shared Preference
                    Log.i("LEAVE",  users_profile.getString("status", null));
                    //this user is admin; then call the function that will generate new group ley and set that in Group Keys
                    GroupKeys.UpdateGroupKey(context, currentGroupName);
                }


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("DELETE-OCM", snapshot.getKey() + " is deleted! ");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("DELETE-OC", error.getMessage().toString());

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



        //get current user name
        Task<DataSnapshot> q = FirebaseDatabase.getInstance().getReference()
                .child("User_Data_Temp")
                .child(currentUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        Log.i("INITIALZE",  task.getResult().child("name").getValue().toString());
                        Log.i("INITIALZE", String.valueOf(task.getResult()));
                        String name = task.getResult().child("name").getValue().toString();
                        String fname = name.substring(0, name.indexOf(" "));
                        Log.i("INITIALZE", "first name:" + fname);

                        getSupportActionBar().setTitle(currentGroupName + " - " + fname  + "(" + status + ")");


                    }
                });

        loadingBar = new ProgressDialog(this);


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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.leave_group:
//                Toast.makeText(this, "Clicked lEAVE gROUP", Toast.LENGTH_SHORT).show();
                leavegroup();

        }
        return super.onOptionsItemSelected(item);
    }

    private void leavegroup() {
        //progress bar
        loadingBar.setTitle("Remove Group");
        loadingBar.setMessage("Please wait while we remove you...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        ///
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("GroupKeys").child(currentGroupName).child("fBkbRYitTleG9N1YkMWpAPCfY782").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    for (int i =0; i<30; ++i){
                        Toast.makeText(GroupChatActivity.this, "You have left the group.Your keys have expired!", Toast.LENGTH_SHORT).show();


                    }
                    loadingBar.dismiss();
                    //Intent
                    Intent intent = new Intent(context, Fragments.class);
                    startActivity(intent);

                }
            }
        });

    }
}

