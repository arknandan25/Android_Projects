package com.example.e2eeapp_alpha.GroupChatEncryption;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.e2eeapp_alpha.Encryption.EncryptionObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.frank_durr.ecdh_curve25519.ECDHCurve25519;
import at.favre.lib.crypto.HKDF;


import static java.lang.String.*;

public class GCTextEncryptor {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void generateSharedSecretAndRetriveGK(Context context, String groupName){
        SharedPreferences users_profile =  context.getSharedPreferences("dynamicUser", Context.MODE_PRIVATE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


        String currentUid = users_profile.getString("curr_uid", "id");
        String currentUName = users_profile.getString("curr_uname", "name");

        String u_alice, u_bob, u_mark;
        u_alice = "0DmlnREYuLMW8N4nLNTbViSv4Q03";
        u_bob = "ysp1Mzo0O8VVDsYbT3SIiSa7WP93";
        u_mark = "fBkbRYitTleG9N1YkMWpAPCfY782";

        final String[] encrypted_group_key = new String[1];
        final byte[][] encrypted_key = new byte[1][1];
        final String[] decryptedString = new String[1];

//        byte[] shared_secret_ba = new byte[0];
        if (currentUid.equals(u_bob)){
            //derive bob-alice shared secret
            byte[] alicePublickey = Base64.getDecoder().decode(users_profile.getString("alice_pubKey", "alicePubKey"));

            byte[] bobPrivateKey = Base64.getDecoder().decode(users_profile.getString("bob_prvKey", "bobrvKey"));

            Log.i("GroupCActivity", "alice pub"+Base64.getEncoder().encodeToString(alicePublickey));
            Log.i("GroupCActivity", "bob prv"+Base64.getEncoder().encodeToString(bobPrivateKey));

            byte[]  shared_secret_ba = ECDHCurve25519.generate_shared_secret(bobPrivateKey, alicePublickey );

            databaseReference.child("GroupKeys").child(groupName).child(u_bob).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.i("GroupCActivity", snapshot.getKey() + snapshot.getValue());
                            encrypted_group_key[0] =  snapshot.getValue().toString();
                            Log.i("GroupCActivity", "Encrypted Gk:"+ encrypted_group_key[0]);
//                            String c =  Base64.getEncoder().encodeToString(snapshot.getValue().toString(), Base64) ;
                            encrypted_key[0] = Base64.getDecoder().decode(encrypted_group_key[0]);

                            Cipher  decipher = null;
                            SecretKeySpec secretKeySpec =  new SecretKeySpec(shared_secret_ba, "AES");

                            byte[] decrypted_msg_bytes;
                            try {
                                decipher = Cipher.getInstance("AES");
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (NoSuchPaddingException e) {
                                e.printStackTrace();
                            }
                            try {
                                decipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
                                decrypted_msg_bytes = decipher.doFinal(encrypted_key[0]);
                                decryptedString[0] = Base64.getEncoder().encodeToString(decrypted_msg_bytes);
                                users_profile.edit().putString("gk", decryptedString[0]).apply();

                            } catch (InvalidKeyException e) {
                                e.printStackTrace();
                            } catch (BadPaddingException e) {
                                e.printStackTrace();
                            } catch (IllegalBlockSizeException e) {
                                e.printStackTrace();
                            }
                            Log.i("Group", "Bob your decrypted GK inside FB Query is:" + decryptedString[0]);

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.i("GroupCActivity", "Unable to retrive data from getkey");
                        }
                    });
        }
        if (currentUid.equals(u_mark)){
            //derive mark-alice shared secret
            byte[] alicePublickey = Base64.getDecoder().decode(users_profile.getString("alice_pubKey", "alicePubKey"));

            byte[] markPrivateKey = Base64.getDecoder().decode(users_profile.getString("mark_prvKey", "markprvKey"));

            Log.i("GroupCActivity", "alice pub"+Base64.getEncoder().encodeToString(alicePublickey));
            Log.i("GroupCActivity", "mark prv"+Base64.getEncoder().encodeToString(markPrivateKey));

            byte[]  shared_secret_ma = ECDHCurve25519.generate_shared_secret(markPrivateKey, alicePublickey );

            databaseReference.child("GroupKeys").child(groupName).child(u_mark).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.i("GroupCActivity", snapshot.getKey() + snapshot.getValue());
                            encrypted_group_key[0] =  snapshot.getValue().toString();
                            Log.i("GroupCActivity", "Encrypted Gk:"+ encrypted_group_key[0]);
//                            String c =  Base64.getEncoder().encodeToString(snapshot.getValue().toString(), Base64) ;
                            encrypted_key[0] = Base64.getDecoder().decode(encrypted_group_key[0]);

                            Cipher  decipher = null;
                            SecretKeySpec secretKeySpec =  new SecretKeySpec(shared_secret_ma, "AES");

                            byte[] decrypted_msg_bytes;
                            try {
                                decipher = Cipher.getInstance("AES");
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (NoSuchPaddingException e) {
                                e.printStackTrace();
                            }
                            try {
                                decipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
                                decrypted_msg_bytes = decipher.doFinal(encrypted_key[0]);
                                decryptedString[0] = Base64.getEncoder().encodeToString(decrypted_msg_bytes);
                                users_profile.edit().putString("gk", decryptedString[0]).apply();

                            } catch (InvalidKeyException e) {
                                e.printStackTrace();
                            } catch (BadPaddingException e) {
                                e.printStackTrace();
                            } catch (IllegalBlockSizeException e) {
                                e.printStackTrace();
                            }
                            Log.i("Group", "Bob your decrypted GK inside FB Query is:" + decryptedString[0]);

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.i("GroupCActivity", "Unable to retrive data from getkey");
                        }
                    });


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static byte[] getMessageKey(Context context, byte[] gk){
        //derive message key each time a new symmetric key is obtained from admin
        //return message key
        byte[] mk = HKDF.fromHmacSha256().expand(
                gk,
                "messageKey".getBytes(StandardCharsets.UTF_8),
                16
        );
        Log.i("Group", "MessageKey generated!");
        Log.i("Group", "MessageKey Bytes:" + mk.toString());


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String message_key_str = Base64.getEncoder().encodeToString(mk);
            Log.i("Group", "MessageKey str:"+message_key_str);
            //set/update  the message key here

        }
        return mk;
    }

    public static void ratchetKey(Context context, byte[] currentGK){

        /*Given a group key, it will be ratcheted here: Forward Secrecy
        * 1.Generate a new ratcheted GK
        * 2.Update the current GK; all devices maintain the same state of the GK
        * */
        //ratchet the current Group Key
        Log.i("FORWARD SECRECY", "$$$$$$$$$$$$$$$$$----Group Chat Ratchet Working here----$$$$$$$$$$$$$$$$$$$$$$$$");

        byte[] ratchetedGK = HKDF.fromHmacSha256().expand(currentGK, new byte[]{(byte) 0x02}, 32);
        SharedPreferences users_profile =  context.getSharedPreferences("dynamicUser", Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("FORWARD SECRECY:", "Current GK:" + Base64.getEncoder().encodeToString(currentGK));
            Log.i("FORWARD SECRECY:", "Ratcheted GK:" + Base64.getEncoder().encodeToString(ratchetedGK));
            //update the Gk
            users_profile.edit().putString("gk", Base64.getEncoder().encodeToString(ratchetedGK)).apply();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static EncryptionObject EncryptAES(Context context, String text_to_encrypt) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException {
        /* Encryption happens here for group chats
        * 1.Derive 16byte IV for AES-CBC
        * 2.Retrieve Known Group Key
        * 3.Derive a 16 byte MessageKey from this 32 byte group Key
        * 4.Derive an encryption key, authentication key from the Message Key(Better Security Model)
        * 5.Encrypt using AES
        * 6.Calculate MAC code
        * 7.Encrypt-then-MAC; create a single cipher message
        * 8.Ratchet the Group key forward
        * 9.Send back the encrypted object containing the cipher text and other stuff
        * */
        Log.i("ENCRYPT", "----------------------------------- New Group Msg being encrypting here-----------------------------------");

        //derive IV 16bytes
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);

        //Retrieve Group Key from shared preferences
        SharedPreferences users_profile =  context.getSharedPreferences("dynamicUser", Context.MODE_PRIVATE);
        String curr_gk_str = users_profile.getString("gk", "currentgroupkEY");
        byte[] curr_gk_bytes = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
             curr_gk_bytes =  Base64.getDecoder().decode(curr_gk_str);
        }

        // derive message key from this
        byte[] mk = getMessageKey(context, curr_gk_bytes);
        //log MK, IV, Plaintext
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("GROUPENCRYPT", "MK:" + Base64.getEncoder().encodeToString(mk) );
            Log.i("GROUPENCRYPT", "IV:" + Base64.getEncoder().encodeToString(iv) );

        }
        Log.i("GROUPENCRYPT", "Plaintext:" + text_to_encrypt );

        //Derive EncKey, AuthKey for AES; HMAC
        byte[] text_to_encrypt_bytes = null;
        byte[] cipherText = null;

        byte[] encKey = HKDF.fromHmacSha256().expand(mk, "encKeyGrp".getBytes(StandardCharsets.UTF_8), 16);
        byte[] authKey = HKDF.fromHmacSha256().expand(mk, "authKeyGrp".getBytes(StandardCharsets.UTF_8), 32); //HMAC-SHA256 key is 32 byte

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("GROUPENCRYPT", "EncKey:" + Base64.getEncoder().encodeToString(encKey) );
            Log.i("GROUPENCRYPT", "AuthKey:" +  Base64.getEncoder().encodeToString(authKey) );
        }

        //get the string in bytes
        text_to_encrypt_bytes = text_to_encrypt.getBytes();

        //AES symmetric encryption starts here
        final Cipher cipher;
        try {
            //getInstance creates an object of the Cipher class
            //takes input as "algorithm/mode/padding"
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            //Initialize the object
            try {
                cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encKey, "AES"), new IvParameterSpec(iv));
                cipherText = cipher.doFinal(text_to_encrypt_bytes);
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        //AES Encryption finished

        //adding MAC authentication code
        SecretKey macKey = new SecretKeySpec(authKey, "HmacSHA256");
        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(macKey);
        hmac.update(iv);
        hmac.update(cipherText);

        //Then calculate the mac.
        byte[] mac = hmac.doFinal();

        //Finally serialize all of it to a single message.
        ByteBuffer byteBuffer = ByteBuffer.allocate(1 + iv.length + 1 + mac.length + cipherText.length);
        byteBuffer.put((byte) iv.length);
        byteBuffer.put(iv);
        byteBuffer.put((byte) mac.length);
        byteBuffer.put(mac);
        byteBuffer.put(cipherText);
        byte[] cipherMessage = byteBuffer.array();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("GROUPENCRYPT:", "ENC+MAC:" + Base64.getEncoder().encodeToString(cipherMessage));
        }

        //send the cipher object back
        EncryptionObject encryptionObject = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            encryptionObject = new EncryptionObject(
                    Base64.getEncoder().encodeToString(cipherMessage),
                    Base64.getEncoder().encodeToString(mk));

        }else {
            encryptionObject = new EncryptionObject("","");

        }

        //before returning the cipher text ratchet the Group key forward : FORWARD SECRECY
        ratchetKey(context, curr_gk_bytes);

        //return the encryption object
        return encryptionObject;
        //end of EncryptAES
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String DecryptAES(byte[] cipherMessage, Context context, byte[] DecKey) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        /*Group Messages are decrypted here
        * 1.
        *
        * */

        Log.i("GROUPDECRYPT", "----------------------------------- New Group Msg decrypting here-----------------------------------");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("GROUPDECRYPT", "Encrypted Message with MAC and IV:" + Base64.getEncoder().encodeToString(cipherMessage) );

        }
        //start decryption
        ByteBuffer byteBuffer = ByteBuffer.wrap(cipherMessage);
        int ivLength = (byteBuffer.get());
        if (ivLength != 16) { // check input parameter
            throw new IllegalArgumentException("invalid iv length");
        }
        byte[] iv = new byte[ivLength];
        byteBuffer.get(iv);
        Log.i("GROUPDECRYPT", "IV Length:" + ivLength );
        Log.i("GROUPDECRYPT", "IV:" +  new String(iv) );

        int macLength = (byteBuffer.get());
        if (macLength != 32) { // check input parameter
            throw new IllegalArgumentException("invalid mac length");
        }
        byte[] mac = new byte[macLength];
        byteBuffer.get(mac);

        Log.i("GROUPDECRYPT", "MAC Length:" +  macLength );
        Log.i("GROUPDECRYPT", "MAC:" +  new String(mac) );


        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);


        //MAC authentiion >>

        byte[] encKey = HKDF.fromHmacSha256().expand(DecKey, "encKeyGrp".getBytes(StandardCharsets.UTF_8), 16);
        byte[] authKey = HKDF.fromHmacSha256().expand(DecKey, "authKeyGrp".getBytes(StandardCharsets.UTF_8), 32);

        Log.i("GROUPDECRYPT", "EncKey:" + new String(encKey) );
        Log.i("GROUPDECRYPT", "AuthKey:" +  new String(authKey) );

        SecretKey macKey = new SecretKeySpec(authKey, "HmacSHA256");
        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(macKey);
        hmac.update(iv);
        hmac.update(cipherText);
        byte[] refMac = hmac.doFinal();

        String decryptedText = null;

        if (!MessageDigest.isEqual(refMac, mac)) {
            throw new SecurityException("could not authenticate MAC");
        }else{
            Log.i("DECRYPT", "MAC authenticated!" );
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(encKey, "AES"), new IvParameterSpec(iv));
            byte[] plainText = cipher.doFinal(cipherText);
            decryptedText = new String(plainText);
            Log.i("GROUPDECRYPT", "Decrypted message:" +  decryptedText);

        }

        return decryptedText;

    }





}
