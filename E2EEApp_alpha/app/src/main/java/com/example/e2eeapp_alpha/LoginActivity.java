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

public class LoginActivity extends AppCompatActivity {
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private Button loginButton;
    private EditText loginEmail, loginPassword;
    private TextView needNewAccount;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //my code goes here
        InitializeFields();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //intent for register user via the login page
        needNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToRegisterActivity();
            }
        });
        
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserLogin();
            }
        });
    }

    private void AllowUserLogin() {
        String email = loginEmail.getText().toString();
        String pass = loginPassword.getText().toString();
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter Email Address!", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Please Enter Password!", Toast.LENGTH_LONG).show();
        }
        else{

            //progress bar
            loadingBar.setTitle("Logging In");
            loadingBar.setMessage("Please Wait...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                SendUserToMainActivity();
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();

                            }else {
                                String error_msg = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Error:" + error_msg, Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }

                        }
                    });


        }
    }

    private void InitializeFields() {
        loginButton = (Button)findViewById(R.id.login_button);
        loginEmail = (EditText)findViewById(R.id.login_email);
        loginPassword = (EditText)findViewById(R.id.login_password);
        needNewAccount = (TextView)findViewById(R.id.need_new_account);
        loadingBar = new ProgressDialog(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        //if the user is already logged in; don't show him the login activity
        // send him to the main activity
        if (currentUser != null){
            SendUserToMainActivity();
        }
    }

    private void SendUserToMainActivity() {
        Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(MainIntent);
    }
    private void SendUserToRegisterActivity() {
        //if the user wouldlike to create a new account i.e register
        Intent RegisterIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(RegisterIntent);
    }

}