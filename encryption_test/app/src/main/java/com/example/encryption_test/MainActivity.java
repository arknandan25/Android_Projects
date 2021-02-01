package com.example.encryption_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.whispersystems.curve25519.Curve25519;
import org.whispersystems.curve25519.Curve25519KeyPair;

import java.security.SecureRandom;

import de.frank_durr.ecdh_curve25519.ECDHCurve25519;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";

    static {
        // Load native library ECDH-Curve25519-Mobile implementing Diffie-Hellman key
        // exchange with elliptic curve 25519.
        try {
            System.loadLibrary("ecdhcurve25519");
            Log.i(TAG, "Loaded ecdhcurve25519 library.");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Error loading ecdhcurve25519 library: " + e.getMessage());
        }
    }

    static private String binarytoHexString(byte[] binary)
    {
        StringBuilder sb = new StringBuilder(binary.length*2);

        // Go backwards (left to right in the string) since typically you print the low-order
        // bytes to the right.
        for (int i = binary.length-1; i >= 0; i--) {
            // High nibble first, i.e., to the left.
            // Note that bytes are signed in Java. However, "int x = abyte&0xff" will always
            // return an int value of x between 0 and 255.
            // "int v = binary[i]>>4" (without &0xff) does *not* work.
            int v = (binary[i]&0xff)>>4;
            char c;
            if (v < 10) {
                c = (char) ('0'+v);
            } else {
                c = (char) ('a'+v-10);
            }
            sb.append(c);
            // low nibble
            v = binary[i]&0x0f;
            if (v < 10) {
                c = (char) ('0'+v);
            } else {
                c = (char) ('a'+v-10);
            }
            sb.append(c);
        }

        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Curve25519KeyPair keyPair = Curve25519.getInstance(Curve25519.BEST).generateKeyPair();

        //---------------------
        // Create Alice's secret key from a big random number.
        SecureRandom random = new SecureRandom();
        byte[] alice_secret_key = ECDHCurve25519.generate_secret_key(random);
        // Create Alice's public key.
        byte[] alice_public_key = ECDHCurve25519.generate_public_key(alice_secret_key);

        // Bob is also calculating a key pair.
        SecureRandom random1 = new SecureRandom();

        byte[] bob_secret_key = ECDHCurve25519.generate_secret_key(random1);
        byte[] bob_public_key = ECDHCurve25519.generate_public_key(bob_secret_key);

        // Assume that Alice and Bob have exchanged their public keys.

        // Alice is calculating the shared secret.
        byte[] alice_shared_secret = ECDHCurve25519.generate_shared_secret(alice_secret_key, bob_public_key);

        // Bob is also calculating the shared secret.
        byte[] bob_shared_secret = ECDHCurve25519.generate_shared_secret(bob_secret_key, alice_public_key);

        String alice_private_str = binarytoHexString(alice_secret_key);
        String alice_public_str = binarytoHexString(alice_public_key);

        String bob_private_str = binarytoHexString(bob_secret_key);
        String bob_public_str = binarytoHexString(bob_public_key);

        String alice_shared_secret_str = binarytoHexString(alice_shared_secret);
        String bob_shared_secret_str = binarytoHexString(bob_shared_secret);



        Log.i(TAG, "Alice's Private Key is:" + alice_private_str);
        Log.i(TAG, "Alice's Public Key is:" + alice_public_str);
        Log.i(TAG, "Bob's Private Key is:" + bob_private_str);
        Log.i(TAG, "Bob's Public Key is:" + bob_public_str);
        Log.i(TAG, "Alice's Shared Key is:" + alice_shared_secret_str);
        Log.i(TAG, "Bob's Shared Key is:" + bob_shared_secret_str);

    }
}