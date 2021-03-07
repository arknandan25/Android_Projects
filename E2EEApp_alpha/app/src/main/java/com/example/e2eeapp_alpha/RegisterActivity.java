package com.example.e2eeapp_alpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText registerEmail, registerPassword, registerName, registerPhone;
    private TextView already_have_Account;
    private ProgressDialog loadingBar;

    //register user firebase instance
    private FirebaseAuth mAuth;
    private DatabaseReference db_ref_register_user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        InitializeFields();

        //intent: if the user already has an account; so ask them to head over to login activityz
        already_have_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToLoginActivity();
            }
        });
        //call CreateNewAccount: upon click of the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
   
    }//end of OnCreate

    private void CreateNewAccount() {
        String email = registerEmail.getText().toString();
        String pass = registerPassword.getText().toString();
        String name = registerName.getText().toString();
        String phone = registerPhone.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter Email Address!", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Please Enter Password!", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Enter Name!", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please Enter Phone Number!", Toast.LENGTH_LONG).show();
        }
        else{
            //progress bar
            loadingBar.setTitle("Creating your account");
            loadingBar.setMessage("Please Wait...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                //user added to auth on firebase: now add phone and name to Users table in database
                                FirebaseUser registered_user = mAuth.getCurrentUser();
                                String registered_user_uid = registered_user.getUid();

//                                Map<String, UserObject> user = new HashMap<>();
//                                user.put(registered_user_uid, new UserObject(name, phone));
                                Contacts new_user = new Contacts(name, phone, "", "", registered_user_uid);

                                //standard template to add a value to firebase
//                                db_ref_register_user.child().setValue();
                                db_ref_register_user.child(registered_user_uid).setValue(new_user);

                                //

                                SendUserToLoginActivity();
                                Toast.makeText(RegisterActivity.this, "Your account has been created", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }else{
                                String error_msg = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error:" + error_msg, Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();

                            }
                        }
                    });
        }
    }// end of CreateNewAccount

    private void InitializeFields() {
        //Initialize all front-end components here
        registerButton = (Button)findViewById(R.id.register_button);
        registerEmail = (EditText)findViewById(R.id.register_email);
        registerPassword = (EditText)findViewById(R.id.register_password);
        already_have_Account = (TextView)findViewById(R.id.already_have_account);
        loadingBar = new ProgressDialog(this);
        registerName = (EditText) findViewById(R.id.register_uname);
        registerPhone = (EditText) findViewById(R.id.register_uphone);
        
        
        //firebase initializations for the activity
        //firebase settings
        mAuth = FirebaseAuth.getInstance();
        db_ref_register_user = FirebaseDatabase.getInstance().getReference("User_Data_Temp");
    }

    private void SendUserToLoginActivity() {
        //Intent Function: Send user to Login Activity
        Intent LoginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(LoginIntent);
    }


}