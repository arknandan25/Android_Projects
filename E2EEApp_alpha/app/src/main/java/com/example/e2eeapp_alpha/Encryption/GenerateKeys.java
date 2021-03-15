package com.example.e2eeapp_alpha.Encryption;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.e2eeapp_alpha.R;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import at.favre.lib.crypto.HKDF;
import de.frank_durr.ecdh_curve25519.ECDHCurve25519;

public class GenerateKeys extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_keys);
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void makeKey(Context context){
        //generating test keys
        SecureRandom random = new SecureRandom();
        SecureRandom random1 = new SecureRandom();

        // Create Alice's secret key from a big random number.
        byte[] alice_secret_key = ECDHCurve25519.generate_secret_key(random);
        // Create Alice's public key.
        byte[] alice_public_key = ECDHCurve25519.generate_public_key(alice_secret_key);

        // Bob is also calculating a key pair.
        byte[] bob_secret_key = ECDHCurve25519.generate_secret_key(random1);
        byte[] bob_public_key = ECDHCurve25519.generate_public_key(bob_secret_key);

        byte[] shared_secret = ECDHCurve25519.generate_shared_secret(alice_secret_key, bob_public_key);



        String alice_public_key_str = null;
        String alice_private_key_str = null;
        String bob_public_key_str = null;
        String bob_private_key_str = null;
        String shared_secret_str = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            alice_public_key_str = Base64.getEncoder().encodeToString(alice_public_key);
            alice_private_key_str = Base64.getEncoder().encodeToString(alice_secret_key);

            bob_public_key_str = Base64.getEncoder().encodeToString(bob_public_key);
            bob_private_key_str = Base64.getEncoder().encodeToString(bob_secret_key);

            shared_secret_str  = Base64.getEncoder().encodeToString(shared_secret);


            Log.i("Alice Public str:", alice_public_key_str);
            Log.i("Alice Private str:", alice_private_key_str);

            Log.i("Bob Public str:", bob_public_key_str);
            Log.i("Bob Private str:", bob_private_key_str);
            Log.i("Shared Secret str:", shared_secret_str);


            // base64 string to byte[]
            byte[] alice_public_key_rebyte = Base64.getDecoder().decode(alice_public_key_str);
            byte[] alice_private_key_rebyte = Base64.getDecoder().decode(alice_private_key_str);

            byte[] bob_public_key_rebyte = Base64.getDecoder().decode(bob_public_key_str);
            byte[] bob_private_key_rebyte = Base64.getDecoder().decode(bob_private_key_str);

            byte[] shared_secret_rebyte = Base64.getDecoder().decode(shared_secret_str);

            if (    Arrays.equals(alice_public_key_rebyte, alice_public_key) &&
                    Arrays.equals(alice_private_key_rebyte, alice_secret_key) &&
                    Arrays.equals(bob_public_key_rebyte, bob_public_key) &&
                    Arrays.equals(bob_private_key_rebyte, bob_secret_key) &&
                    Arrays.equals(shared_secret_rebyte, shared_secret))
            {
                Log.i("GenerateKeyPair:", "All byte string conversion succ");

            }else {
                Log.i("GenerateKeyPair:", "The byte string conversion failed");
            }
        }

        //defining the chain key and message
        byte[] chainKey_alice = null;
        byte[] chainKey_bob = null;
        byte[] messageKey_alice = null;
        byte[] messageKey_bob = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

            //chain Key
            chainKey_alice = HKDF.fromHmacSha256().expand(shared_secret, "chainKey".getBytes(StandardCharsets.UTF_8), 16);

            chainKey_bob = HKDF.fromHmacSha256().expand(shared_secret, "chainKey".getBytes(StandardCharsets.UTF_8), 16);

            Log.i("Chain Keys:",Base64.getEncoder().encodeToString(chainKey_alice) + "::bob-" + Base64.getEncoder().encodeToString(chainKey_bob));
            Log.i("Chain Keys:",Base64.getDecoder().decode(Base64.getEncoder().encodeToString(chainKey_alice))
                    + "::bob-" + Base64.getDecoder().decode(Base64.getEncoder().encodeToString(chainKey_bob)));
            if ( Arrays.equals(chainKey_alice, chainKey_bob) )
            {
                Log.i("Chain Keys:", "Same chain keys established");

            }else {
                Log.i("Chain Keys:", "Chain Keys for users not same");
            }

            //message Key
            messageKey_alice = HKDF.fromHmacSha256().expand(chainKey_alice, "messageKey".getBytes(StandardCharsets.UTF_8), 16);
            messageKey_bob = HKDF.fromHmacSha256().expand(chainKey_bob, "messageKey".getBytes(StandardCharsets.UTF_8), 16);



            Log.i("Message Keys:",Base64.getEncoder().encodeToString(messageKey_alice) + "::bob-" + Base64.getEncoder().encodeToString(messageKey_bob));
            Log.i("Message Keys:",Base64.getDecoder().decode(Base64.getEncoder().encodeToString(messageKey_alice))
                    + "::bob-" + Base64.getDecoder().decode(Base64.getEncoder().encodeToString(messageKey_bob)));
            if ( Arrays.equals(messageKey_alice, messageKey_bob) )
            {
                Log.i("Message Keys:", "Same Message keys established");

            }else {
                Log.i("Message Keys:", "Message Keys for users not same");
            }

        }
        String chainKey_alice_str = Base64.getEncoder().encodeToString(chainKey_alice)  ;
        String chainKey_bob_str = Base64.getEncoder().encodeToString(chainKey_bob);
        String messageKey_alice_str = Base64.getEncoder().encodeToString(messageKey_alice) ;
        String messageKey_bob_str = Base64.getEncoder().encodeToString(messageKey_bob);


        SaveKeysSP(context, alice_public_key_str, alice_private_key_str, bob_public_key_str,
        bob_private_key_str, shared_secret_str, chainKey_alice_str, chainKey_bob_str, messageKey_alice_str, messageKey_bob_str);

    }

    public static void SaveKeysSP(Context context, String publicKey1, String privatekey1, String publicKey2, String privateKey2,
                                  String shared_secret, String chainKey1, String chainKey2, String messageKey1, String messageKey2){
        //1st method will be shared preferences
        SharedPreferences sharedPreferences =   context.getSharedPreferences("com.example.e2eeapp_alpha.Encryption",Context.MODE_PRIVATE);

        //this commented code sets the primary keys for users along with shared secret; no need to uncomment
        //it only needs to execute once; already done that
        sharedPreferences.edit().putString("alice_public", publicKey1).apply();
        sharedPreferences.edit().putString("alice_private", privatekey1).apply();

        sharedPreferences.edit().putString("bob_public", publicKey2).apply();
        sharedPreferences.edit().putString("bob_private", privateKey2).apply();

        sharedPreferences.edit().putString("shared_secret", shared_secret).apply();
        //chain keys
        sharedPreferences.edit().putString("ck_alice", chainKey1).apply();
        sharedPreferences.edit().putString("ck_bob", chainKey2).apply();
        //message keys
        sharedPreferences.edit().putString("mk_alice", messageKey1).apply();
        sharedPreferences.edit().putString("mk_bob", messageKey2).apply();

//        to delete SP
//        SharedPreferences settings = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
//        settings.edit().clear().commit();

        Log.i("GenerateKeys:", sharedPreferences.getString("alice_public", "aaa"));
        Log.i("GenerateKeys:", sharedPreferences.getString("alice_private", "aaa"));

        Log.i("GenerateKeys:", sharedPreferences.getString("bob_public", "aaa"));
        Log.i("GenerateKeys:", sharedPreferences.getString("bob_private", "aaa"));

        Log.i("GenerateKeys:", sharedPreferences.getString("shared_secret", "aa"));

        Log.i("GenerateKeys:", sharedPreferences.getString("ck_alice", "aa"));
        Log.i("GenerateKeys:", sharedPreferences.getString("ck_bob", "aa"));

        Log.i("GenerateKeys:", sharedPreferences.getString("mk_alice", "aa"));
        Log.i("GenerateKeys:", sharedPreferences.getString("mk_bob", "aa"));

//        I/GenerateKeys:: (32 and 16 bytes)
//        ms711s+jk+qsSwFwTqkB2ud6ZQup4pTnnWHmv9dHd2s=
//        kAiNnwAAAAABAAAApq59HwAAAAABAAAAAQAAABzD/HA=
//
//        CXlpZQl/hzxu9lRkplnbJhEC+R3LQjnqY+k5ohLQYWw=
//        QIXfENDkvP8BAAAAAAAAAAAAAAABAAAAAQAAAFDlvH8=
//
//        S1saIeWSZv6JqiH6v+9hze0mwTKdzfAbD9/VtDOcrRQ=
//
//                0fSxP9NRh7138V+tPRJOBQ==
//                0fSxP9NRh7138V+tPRJOBQ==
//                Dqjp1g0y3lqT0nPrSrFrZQ==
//                Dqjp1g0y3lqT0nPrSrFrZQ==



    }

    public static void setkeysManually(Context context){
        SaveKeysSP(
                context,
                "ms711s+jk+qsSwFwTqkB2ud6ZQup4pTnnWHmv9dHd2s=",
                "kAiNnwAAAAABAAAApq59HwAAAAABAAAAAQAAABzD/HA=",
                "CXlpZQl/hzxu9lRkplnbJhEC+R3LQjnqY+k5ohLQYWw=",
                "QIXfENDkvP8BAAAAAAAAAAAAAAABAAAAAQAAAFDlvH8=",
                "S1saIeWSZv6JqiH6v+9hze0mwTKdzfAbD9/VtDOcrRQ=",
                "0fSxP9NRh7138V+tPRJOBQ==",
                "0fSxP9NRh7138V+tPRJOBQ==",
                "Dqjp1g0y3lqT0nPrSrFrZQ==",
                "Dqjp1g0y3lqT0nPrSrFrZQ=="
        );
    }


    public static void generateMsgPreference(Context context){
        //create message preference manually
        SharedPreferences x =   context.getSharedPreferences("msg_pref",Context.MODE_PRIVATE);

        x.edit().putString("msgId", "kEY1").apply();

        Log.i("Msg Pref Generated!!:", "Msg SHARED PREF" + x.getAll());
    }


}