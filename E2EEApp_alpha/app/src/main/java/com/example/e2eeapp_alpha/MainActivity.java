package com.example.e2eeapp_alpha;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.e2eeapp_alpha.Encryption.GenerateKeys;
import com.example.e2eeapp_alpha.GroupChatEncryption.GroupKeys;
import com.example.e2eeapp_alpha.Users.DynamicUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import static com.example.e2eeapp_alpha.Certificates.CERTCaller.CertificateCaller;

//@string/app_name
public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private String TAG = "firebase READ";
    private ListView listView;
    private DatabaseReference databaseReference;
    private String stringMessage = null;
    //128 bit- 16 byte AES
    private byte encryptionKey[] = {9,115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53};
    private Cipher cipher, decipher;
    private SecretKeySpec secretKeySpec;


    //Firebase Login
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    //DarkMode
    private boolean isChecked_past = false;

    private boolean isChecked_curr = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        //test calls

        DynamicUsers.CreateUserProfiles(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            GenerateKeys.setkeysManually(this); //this call generated keys for 2 users to demonstrate; lets roll from here
            GenerateKeys.generateMsgPreference(this);

        }


//        TextEncryptor.getMessageKey(this);
//        TextEncryptor.ratchetKey(this);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            try {
////                TextEncryptor.EncryptAES(this, "txtgggg");
//                TextEncryptor.ratchetKey(this);
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (InvalidKeyException e) {
//                e.printStackTrace();
//            } catch (IllegalBlockSizeException e) {
//                e.printStackTrace();
//            } catch (BadPaddingException e) {
//                e.printStackTrace();
//            } catch (InvalidAlgorithmParameterException e) {
//                e.printStackTrace();
//            } catch (NoSuchPaddingException e) {
//                e.printStackTrace();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }

//        TextEncryptor.testFunction(this);

        //test calls end here -----

        //conenct variables to elements in the layout
        editText = findViewById(R.id.plaintext_to_send);
        listView = findViewById(R.id.listView);

        //firebase settings
        databaseReference = FirebaseDatabase.getInstance().getReference("Message");


        //
        try {
            cipher = Cipher.getInstance("AES");
            decipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        secretKeySpec = new SecretKeySpec(encryptionKey, "AES");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stringMessage = snapshot.getValue().toString();
                stringMessage = stringMessage.substring(1,stringMessage.length()-1);
                String[] stringArray = stringMessage.split(", ");
                Arrays.sort(stringArray);
                String[] stringFinal = new String[stringArray.length*2];

                try {
                    for (int i=0; i<stringArray.length;++i){
                        String[] stringKeyValue = stringArray[i].split("=", 2);
                        stringFinal[2*i] = (String) DateFormat.format("dd--MM-yyyy hh:mm:ss", Long.parseLong(stringKeyValue[0]));
                        stringFinal[2*i+1] = decrypt_message(stringKeyValue[1]);
//                        stringFinal[2*i+1] =stringKeyValue[1];

                    }
                    listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, stringFinal));

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());

            }
        });

        //get contact access permissions
        getPermissions();



    }//End of OnCreate

//    public void get_data(){
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                stringMessage = snapshot.getValue().toString();
////                stringMessage = stringMessage.substring(1,stringMessage.length()-1);
////                String[] stringArray = stringMessage.split(",");
////                String[] stringFinal = new String[stringArray.length*2];
////                for (int i=0; i<stringArray.length;++i){
////                    String[] stringKeyValue = stringArray[i].split("=", 2);
////                    stringFinal[2*i] = (String) android.text.format.DateFormat.format("dd--MM-YYYY hh:mm:ss", Long.parseLong(stringKeyValue[0]));
////                    try {
////                        stringFinal[2*i+1] = decrypt_message(stringKeyValue[1]);
////                    } catch (UnsupportedEncodingException e) {
////                        e.printStackTrace();
////                    }
////
////
////                }
//                for (DataSnapshot ds: snapshot.getChildren()){
//                    String key = ds.getKey();
//                    stringMessage = ds.getValue().toString();
////                    listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, Collections.singletonList(stringMessage)));
//                    Log.i(TAG,"output:", );
//
//
//                }
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w(TAG, "Failed to read value.", error.toException());
//
//            }
//        });
//    }


    public void sendMessage_Button(View view){

        Date date = new Date();
        databaseReference.child(String.valueOf(date.getTime())).setValue(encrypt_message(editText.getText().toString()));
//        databaseReference.child("E1").setValue("Hello there!");

//        clear the text box once the message is sent
        editText.setText("");
//        get_data();


    }

    private String encrypt_message(String msg_to_encrypt){
        //convert input strings to byte
        byte[] stringByte = msg_to_encrypt.getBytes();
        byte[] encryptedByte = new byte[stringByte.length];

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encryptedByte = cipher.doFinal(stringByte);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        String encrypted_String = null;
        try {
            encrypted_String = new String(encryptedByte, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encrypted_String;
    }


    private String decrypt_message(String encrypted_data_string) throws UnsupportedEncodingException{
        byte[] encrypted_msg_bytes = encrypted_data_string.getBytes("ISO-8859-1");
        String decryptedString = null;
        byte[] decrypted_msg_bytes;

        try {
            decipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            decrypted_msg_bytes = decipher.doFinal(encrypted_msg_bytes);
            decryptedString = new String(decrypted_msg_bytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return decryptedString;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem checkable = menu.findItem(R.id.dark_mode);
//        checkable.setChecked(isChecked);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.image_enc:
                Intent myIntent = new Intent(MainActivity.this, MainActivity2.class);
//                myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);
                break;
            case R.id.logout:
                Toast.makeText(this, "Logging you out of the App!", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                SendUserToLoginActivity();
                return true;
            case R.id.fragmentz:
                Intent myIntent1 = new Intent(MainActivity.this, Fragments.class);
//                myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent1);
                break;
            case R.id.image_upload:
                Intent myIntent2 = new Intent(MainActivity.this, ImageUpload.class);
                MainActivity.this.startActivity(myIntent2);

                break;
            case R.id.find_friends_temp:
                Intent myIntent3 = new Intent(MainActivity.this, FindFriends.class);
                MainActivity.this.startActivity(myIntent3);
                break;
            case R.id.settings:
                Intent myIntent4 = new Intent(MainActivity.this, SettingActivity.class);
                MainActivity.this.startActivity(myIntent4);
                break;
            case R.id.csr:
                //initiate the code which generates the CSR and creates a file for User
                String k = CertificateCaller(this);
                //
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
            case R.id.create_group:
                RequestNewGroup();
                break;
            case R.id.dark_mode:
//                MenuItem checkable = menu.findItem(R.id.dark_mode);
                isChecked_curr = !item.isChecked();
                item.setChecked(isChecked_curr);

                if (isChecked_curr == true && isChecked_past == false){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

//                    item.setChecked(true);
                    item.setChecked(isChecked_curr);

//                    checkable.setChecked(isChecked);
                    Toast.makeText(this, "dark mode Enabledd", Toast.LENGTH_SHORT).show();
                    Log.i("Checkced status:", String.valueOf(isChecked_curr));
                }else{
//                if(isChecked == false){

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    item.setChecked(false);


//                    item.setChecked(isChecked);
//                    item.setChecked(!isChecked);
                    Toast.makeText(this, "dark mode Disabled", Toast.LENGTH_SHORT).show();
                    Log.i("Checked status:", String.valueOf(isChecked_curr));
                }

                isChecked_past = isChecked_curr;

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter Group name:");
        final EditText groupName = new EditText(MainActivity.this);
        builder.setView(groupName);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String gname = groupName.getText().toString();
                if (TextUtils.isEmpty(gname)){
                    Toast.makeText(MainActivity.this, "Specify a group name!", Toast.LENGTH_SHORT).show();

                }else {
                    DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
                    RootRef.child("GroupMessages").child(gname).setValue("")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(MainActivity.this, gname + " is created successfully!", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStart() {
        super.onStart();
        //user must have logged in to be on the MainActivity
        if (currentUser == null){
            SendUserToLoginActivity();
        }
    }

    private void SendUserToLoginActivity() {
        Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(LoginIntent);
    }



    private void getPermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 1);
    }
}