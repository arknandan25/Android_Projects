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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        //Initialize
        Initialize();

        UpdateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetails();
            }
        });
    }

    private void updateDetails() {
        String Name_to_Set = UserName.getText().toString();
        String Status_to_Set = UserStatus.getText().toString();
        final Integer[] flagName = new Integer[1];
        final Integer[] flagStatus = {0};
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
                        }
                    }
                });
        databaseReference.child(currentUserId).child("status").setValue(Status_to_Set)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            flagStatus[0] = 1;
                        }
                    }
                });

        if(flagName[0] == 1 && flagStatus[0] == 1){
            Toast.makeText(SettingActivity.this, "Profile Updated!", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(SettingActivity.this, "Unable to Update Profile!", Toast.LENGTH_LONG).show();

        }

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