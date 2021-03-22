package com.example.e2eeapp_alpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private EditText UserName, UserStatus;
    private Button UpdateDetails;
    private CircleImageView ProfilePicture;

    //firebase
    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize
        Initialize();
        RetriveUserCurrentInfo();

        UpdateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetails();
            }
        });
    }

    private void RetriveUserCurrentInfo() {
        databaseReference.child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.hasChild("name") ){
                            String curr_uname = snapshot.child("name").getValue().toString();
                            UserName.setText(curr_uname);
                        }
                        if(snapshot.exists() && snapshot.hasChild("status")){
                            String curr_status = snapshot.child("status").getValue().toString();
                            UserStatus.setText(curr_status);
                        }
                        if(snapshot.exists() && snapshot.hasChild("image")){
                            String curr_uimage = snapshot.child("image").getValue().toString();
                            Picasso.get().load(curr_uimage).placeholder(R.drawable.profile_image).into(ProfilePicture);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void updateDetails() {
        String Name_to_Set = UserName.getText().toString();
        String Status_to_Set = UserStatus.getText().toString();
        final Integer[] flagName = new Integer[1];
        final Integer[] flagStatus = new Integer[1];
        HashMap<String, String> profileMap = new HashMap<>();
        profileMap.put("name", Name_to_Set);
        profileMap.put("status", Status_to_Set);
//        databaseReference.child(currentUserId).setValue(profileMap)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(SettingActivity.this, "Profile Updated!", Toast.LENGTH_LONG).show();
//                        }else{
//                            Toast.makeText(SettingActivity.this, "Error while updating profile:"+ task.getException().toString(), Toast.LENGTH_LONG).show();
//
//                        }
//                    }
//                });


        databaseReference.child(currentUserId).child("name").setValue(Name_to_Set)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            flagName[0] = 1;
                            Toast.makeText(SettingActivity.this, "Profile Updated!", Toast.LENGTH_LONG).show();

                        }
                    }
                });
        databaseReference.child(currentUserId).child("status").setValue(Status_to_Set)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            flagStatus[0] = 1;
                            Toast.makeText(SettingActivity.this, "Profile Updated!", Toast.LENGTH_LONG).show();

                        }
                    }
                });
//
//        if(flagName[0] == 1 && flagStatus[0] == 1){
//            Toast.makeText(SettingActivity.this, "Profile Updated!", Toast.LENGTH_LONG).show();
//
//        }else{
//            Toast.makeText(SettingActivity.this, "Unable to Update Profile!", Toast.LENGTH_LONG).show();
//
//        }

    }

    private void Initialize() {
        UserName = findViewById(R.id.user_name);
        UserStatus = findViewById(R.id.user_status);
        UpdateDetails = findViewById(R.id.update_user_info);
        ProfilePicture = findViewById(R.id.user_profile_image);
        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("User_Data_Temp");

    }
}