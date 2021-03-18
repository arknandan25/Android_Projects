package com.example.e2eeapp_alpha.Users;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.security.SecureRandom;
import java.util.Base64;

import de.frank_durr.ecdh_curve25519.ECDHCurve25519;

public class DynamicUsers {



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

    public static void CreateUserProfiles(Context context){

        SharedPreferences users_profile
                =   context.getSharedPreferences("dynamicUser", Context.MODE_PRIVATE);

        String u_alice, u_bob, u_mark, u_thor, u_peter;
        u_alice = "0DmlnREYuLMW8N4nLNTbViSv4Q03";
        u_bob = "ysp1Mzo0O8VVDsYbT3SIiSa7WP93";
        u_mark = "fBkbRYitTleG9N1YkMWpAPCfY782";

        //
        //generating test keys
        SecureRandom random = new SecureRandom();

        SecureRandom random1 = new SecureRandom();

        SecureRandom random2 = new SecureRandom();

        SecureRandom random3 = new SecureRandom();

        // Create Alice's secret key from a big random number.
        byte[] alice_secret_key = ECDHCurve25519.generate_secret_key(random);
        // Create Alice's public key.
        byte[] alice_public_key = ECDHCurve25519.generate_public_key(alice_secret_key);

        // Bob is also calculating a key pair.
        byte[] bob_secret_key = ECDHCurve25519.generate_secret_key(random1);
        byte[] bob_public_key = ECDHCurve25519.generate_public_key(bob_secret_key);

        //mark
        byte[] mark_secret_key = ECDHCurve25519.generate_secret_key(random2);
        byte[] mark_public_key = ECDHCurve25519.generate_public_key(mark_secret_key);


        String alice_public_key_str, alice_private_key_str, bob_public_key_str, bob_private_key_str, mark_public_key_str, mark_private_key_str;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alice_public_key_str = Base64.getEncoder().encodeToString(alice_public_key);
            alice_private_key_str = Base64.getEncoder().encodeToString(alice_secret_key);

            bob_public_key_str = Base64.getEncoder().encodeToString(bob_public_key);
            bob_private_key_str = Base64.getEncoder().encodeToString(bob_secret_key);

            mark_public_key_str = Base64.getEncoder().encodeToString(mark_public_key);
            mark_private_key_str = Base64.getEncoder().encodeToString(mark_secret_key);

            Log.i("Alice Public strx:", alice_public_key_str);
            Log.i("Alice Private strx:", alice_private_key_str);

            Log.i("Bob Public strx:", bob_public_key_str);
            Log.i("Bob Private strx:", bob_private_key_str);

            Log.i("Mark Public strx:", mark_public_key_str);
            Log.i("Mark Private strx:", mark_private_key_str);
        }




//        byte[] shared_secret = ECDHCurve25519.generate_shared_secret(alice_secret_key, bob_public_key);



//        delete SP
//        users_profile.edit().clear().commit();
//                    Log.i("DynamicUser", users_profile.getAll().toString());

        String current_Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (current_Uid.equals(u_alice)){
            users_profile.edit().putString("curr_uid", u_alice).apply();
            users_profile.edit().putString("curr_uname", "Alice Fernanadez").apply();


            users_profile.edit().putString("alice_pubKey", "T1xEE867TMSw0P1x1gUbEvYnRIq7MMhZg0W0LDh0fC0=").apply();
            users_profile.edit().putString("alice_prvKey", "mIj3jgAAAAABAAAACFemqQAAAAABAAAAAQAAAByTy2Q=").apply();
            users_profile.edit().putString("bob_pubKey", "jJw/MY/KRK2RAQ6jpK2XfHv55U6fimBGb48PuiHd4Cg=").apply();
            users_profile.edit().putString("bob_prvKey", "iCRfqAAYqv8BAAAAAAAAAAAAAAABAAAAAQAAAIAYqn8=").apply();
            users_profile.edit().putString("mark_pubKey", "pgzkKDOF4OatTl/0iXJOjGY/I0TRvQk046AqZT7ZsWs=").apply();
            users_profile.edit().putString("mark_prvKey", "eOJZWQAYqv8BAAAAAAAAAAAAAAABAAAAAQAAAIAYqn8=").apply();
        }

        if (current_Uid.equals(u_bob)){
            users_profile.edit().putString("curr_uid", u_bob).apply();
            users_profile.edit().putString("curr_uname", "Bob Fischer").apply();

            users_profile.edit().putString("alice_pubKey", "T1xEE867TMSw0P1x1gUbEvYnRIq7MMhZg0W0LDh0fC0=").apply();
            users_profile.edit().putString("alice_prvKey", "mIj3jgAAAAABAAAACFemqQAAAAABAAAAAQAAAByTy2Q=").apply();
            users_profile.edit().putString("bob_pubKey", "jJw/MY/KRK2RAQ6jpK2XfHv55U6fimBGb48PuiHd4Cg=").apply();
            users_profile.edit().putString("bob_prvKey", "iCRfqAAYqv8BAAAAAAAAAAAAAAABAAAAAQAAAIAYqn8=").apply();
            users_profile.edit().putString("mark_pubKey", "pgzkKDOF4OatTl/0iXJOjGY/I0TRvQk046AqZT7ZsWs=").apply();
            users_profile.edit().putString("mark_prvKey", "eOJZWQAYqv8BAAAAAAAAAAAAAAABAAAAAQAAAIAYqn8=").apply();

            Log.i("DynamicUser", users_profile.getAll().toString());
            Toast.makeText(context, "Current User:" + users_profile.getString("curr_uname", "aaaA"), Toast.LENGTH_LONG).show();

        }

        if (current_Uid.equals(u_mark)){
            users_profile.edit().putString("curr_uid", u_mark).apply();
            users_profile.edit().putString("curr_uname", "Mark Hacker").apply();

            users_profile.edit().putString("alice_pubKey", "T1xEE867TMSw0P1x1gUbEvYnRIq7MMhZg0W0LDh0fC0=").apply();
            users_profile.edit().putString("alice_prvKey", "mIj3jgAAAAABAAAACFemqQAAAAABAAAAAQAAAByTy2Q=").apply();
            users_profile.edit().putString("bob_pubKey", "jJw/MY/KRK2RAQ6jpK2XfHv55U6fimBGb48PuiHd4Cg=").apply();
            users_profile.edit().putString("bob_prvKey", "iCRfqAAYqv8BAAAAAAAAAAAAAAABAAAAAQAAAIAYqn8=").apply();
            users_profile.edit().putString("mark_pubKey", "pgzkKDOF4OatTl/0iXJOjGY/I0TRvQk046AqZT7ZsWs=").apply();
            users_profile.edit().putString("mark_prvKey", "eOJZWQAYqv8BAAAAAAAAAAAAAAABAAAAAQAAAIAYqn8=").apply();

            Log.i("DynamicUser", users_profile.getAll().toString());
            Toast.makeText(context, "Current User:" + users_profile.getString("curr_uname", "aaaA"), Toast.LENGTH_LONG).show();

        }


//        I/Alice Public strx:: T1xEE867TMSw0P1x1gUbEvYnRIq7MMhZg0W0LDh0fC0=
//        I/Alice Private strx:: mIj3jgAAAAABAAAACFemqQAAAAABAAAAAQAAAByTy2Q=
//        I/Bob Public strx:: jJw/MY/KRK2RAQ6jpK2XfHv55U6fimBGb48PuiHd4Cg=
//        I/Bob Private strx:: iCRfqAAYqv8BAAAAAAAAAAAAAAABAAAAAQAAAIAYqn8=
//        I/Mark Public strx:: pgzkKDOF4OatTl/0iXJOjGY/I0TRvQk046AqZT7ZsWs=
//        I/Mark Private strx:: eOJZWQAYqv8BAAAAAAAAAAAAAAABAAAAAQAAAIAYqn8=
    }

}
