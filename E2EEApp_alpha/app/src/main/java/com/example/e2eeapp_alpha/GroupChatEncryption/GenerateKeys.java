package com.example.e2eeapp_alpha.GroupChatEncryption;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.e2eeapp_alpha.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import de.frank_durr.ecdh_curve25519.ECDHCurve25519;

public class GenerateKeys {

    // Load native library ECDH-Curve25519-Mobile implementing Diffie-Hellman key
    // exchange with elliptic curve 25519.
    static {
        try {
            System.loadLibrary("ecdhcurve25519");
            Log.i("TAG", "Loaded ecdhcurve25519 library.");
        } catch (UnsatisfiedLinkError e) {
            Log.e("TAG", "Error loading ecdhcurve25519 library: " + e.getMessage());
        }
    }

    public static void generateGroupKey(){

        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32];
        secureRandom.nextBytes(key);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void adminSet(Context context, String gname){
        //generates the ECDH key pair for users
        //generate shared secrets

        /* When new group is created:
        * 1.Add admin details to GroupMember
        * 2.Generate the Group Key
        * 3.Generate shared secret with the other 2 members
        * 4.Encrypt the Group Key and add it to the Group key
        * */
        //setAdmin
        SharedPreferences users_profile
                =   context.getSharedPreferences("dynamicUser", Context.MODE_PRIVATE);

//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//        databaseReference
//                .child("GroupMembers")
//                .child(gname)
//                .child(users_profile.getString("curr_uid", "adminName")).setValue("admin")
//        .addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                    Log.i("Group", "Admin data added to groupMember");
//                }
//            }
//        });

        //generate new 32 byte Group key
        SecureRandom secureRandom = new SecureRandom();
        byte[] Groupkey = new byte[32];
        secureRandom.nextBytes(Groupkey);

        //Log the Groupkey Base64 encoding
        Log.i("Group", " 32b Group key generated by admin is:" + Base64.getEncoder().encodeToString(Groupkey));

        //generate shared secrets with other 2 members as you are the admin

        String u_alice, u_bob, u_mark;
        u_alice = "0DmlnREYuLMW8N4nLNTbViSv4Q03";
        u_bob = "ysp1Mzo0O8VVDsYbT3SIiSa7WP93";
        u_mark = "fBkbRYitTleG9N1YkMWpAPCfY782";

        byte[] shared_secret_ab = new byte[0], shared_secret_am, shared_secret_ba, shared_secret_bm, shared_secret_mb, shared_secret_ma;

        //the SP on this phone would tell what is the Uid and name of the Admin
        String adminUid = users_profile.getString("curr_uid", "name");
        if (adminUid.equals(u_alice)){
            //means shared secret is to be derived between alice-bob and alice-mark
            byte[] aliceSecretkey = Base64.getDecoder().decode(users_profile.getString("alice_prvKey", "alicePrcKey"));
            byte[] bobPublicKey = Base64.getDecoder().decode(users_profile.getString("bob_pubKey", "alicePrcKey"));
            byte[] markPublicKey = Base64.getDecoder().decode(users_profile.getString("mark_pubKey", "alicePrcKey"));

            shared_secret_ab = ECDHCurve25519.generate_shared_secret(aliceSecretkey, bobPublicKey );
            shared_secret_am = ECDHCurve25519.generate_shared_secret(aliceSecretkey, markPublicKey );

            Log.i("Group", "Shared secret generated between alice-bob and alice-mark");
            Log.i("Group", "Alice is Admin");


        }else if(adminUid.equals(u_bob)){
            //means shared secret is to be derived between bob-alice and bob-mark
//             shared_secret_ba = ECDHCurve25519.generate_shared_secret(, );
//             shared_secret_bm = ECDHCurve25519.generate_shared_secret(, );

        }else{
            //means shared secret is to be derived between mark-bob and mark-alice
//             shared_secret_mb = ECDHCurve25519.generate_shared_secret(, );
//             shared_secret_ma = ECDHCurve25519.generate_shared_secret(, );

        }

        //encrypt the group key with this shared secret; AES-CBC will be used for symmetric encryption
        //we have to encrypt the group Key here
        Cipher cipher = null, decipher = null;
        SecretKeySpec secretKeySpec =  new SecretKeySpec(shared_secret_ab, "AES");
        byte[] encrytedGroupKey = new byte[Groupkey.length];
        //initialize cipher
        try {
            cipher = Cipher.getInstance("AES");
            decipher = Cipher.getInstance("AES");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encrytedGroupKey = cipher.doFinal(Groupkey);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        String encryptedGK_str = null;
        encryptedGK_str = Base64.getEncoder().encodeToString(encrytedGroupKey);
        Log.i("Group", "your encrypted GK is:" + encryptedGK_str);

        //decipher to test GK
        byte[] encrypted_key = Base64.getDecoder().decode(encryptedGK_str);
        String decryptedString = null;
        byte[] decrypted_msg_bytes;
        try {
            decipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            decrypted_msg_bytes = decipher.doFinal(encrypted_key);
            decryptedString = new String(decrypted_msg_bytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        Log.i("Group", "your decrypted GK is:" + decryptedString);



    }

}
