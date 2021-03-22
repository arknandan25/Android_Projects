package com.example.e2eeapp_alpha;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e2eeapp_alpha.GroupChatEncryption.GroupKeys;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.e2eeapp_alpha.Certificates.CERTCaller.CertificateCaller;

public class Fragments extends AppCompatActivity {

    private ViewPager myviewPager;
    private TabLayout mytabLayout;
    private tabsAccessorAdapter mytabsAccessorAdapter;

    //Firebase Login
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    //DarkMode
    private boolean isChecked_past = false;

    private boolean isChecked_curr = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        myviewPager = (ViewPager)findViewById(R.id.main_tabs_pager);
        mytabsAccessorAdapter = new tabsAccessorAdapter(getSupportFragmentManager());
        myviewPager.setAdapter(mytabsAccessorAdapter);

        mytabLayout = (TabLayout)findViewById(R.id.main_tabs);
        mytabLayout.setupWithViewPager(myviewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main2, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                Toast.makeText(this, "Logging you out of the App!", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                SendUserToLoginActivity();
                return true;
            case R.id.settings:
                Intent myIntent4 = new Intent(Fragments.this, SettingActivity.class);
                startActivity(myIntent4);
                break;
            case R.id.create_group:
                RequestNewGroup();
                break;
            case R.id.csr:
                //initiate the code which generates the CSR and creates a file for User
                String k = CertificateCaller(this);
                try {
                    File yourAppDir = new File(getExternalFilesDir(null).getAbsolutePath()+"_CSR");

                    if(!yourAppDir.exists() && !yourAppDir.isDirectory())
                    {
                        // create empty directory
                        if (yourAppDir.mkdirs())
                        {
                            Log.i("CreateDir","App dir created");
                        }
                        else
                        {
                            Log.w("CreateDir","Unable to create app dir!");
                        }
                    }
                    else
                    {
                        Log.i("CreateDir","App dir already exists");
                    }
                    File csr = new File(yourAppDir, "CSR_Ark.txt");
                    FileOutputStream stream = new FileOutputStream(csr);
                    try {
                        stream.write(k.getBytes());
                    } finally {
                        stream.close();
                    }

                    Log.i("csr file","created");
                    Toast.makeText(this, "CSR Generated", Toast.LENGTH_LONG ).show();

                }
                catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }

                break;



        }
        return super.onOptionsItemSelected(item);
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Fragments.this, R.style.AlertDialog);
        builder.setTitle("Enter Group name:");
        final EditText groupName = new EditText(Fragments.this);
        builder.setView(groupName);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String gname = groupName.getText().toString();
                if (TextUtils.isEmpty(gname)){
                    Toast.makeText(Fragments.this, "Specify a group name!", Toast.LENGTH_SHORT).show();

                }else {
                    DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
                    RootRef.child("GroupMessages").child(gname).setValue("")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(Fragments.this, gname + " is created successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        GroupKeys.adminSet(getApplicationContext(), gname);
                    }

                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private void SendUserToLoginActivity() {
        Intent LoginIntent = new Intent(Fragments.this, LoginActivity.class);
        startActivity(LoginIntent);
    }




}