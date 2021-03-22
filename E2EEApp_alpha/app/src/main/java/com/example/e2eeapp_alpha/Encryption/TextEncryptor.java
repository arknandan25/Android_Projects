 package com.example.e2eeapp_alpha.Encryption;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

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
import at.favre.lib.crypto.HKDF;

public class TextEncryptor {


//    public static byte[] getMessageKey(Context context){
//        SharedPreferences sharedPreferences =   context.getSharedPreferences("com.example.e2eeapp_alpha.Encryption",Context.MODE_PRIVATE);
//        byte[] mk = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            mk =  Base64.getDecoder().decode(sharedPreferences.getString("mk_alice", ""));
//        }
//
//        Log.i("GenerateKeys:", sharedPreferences.getString("mk_alice", "aa"));
//        Log.i("GenerateKeys:", sharedPreferences.getString("mk_bob", "aa"));
//
//        return mk;
//    }



    public static void testFunction(Context context){
        //create and access and edit shared preferences: string
        SharedPreferences sharedPreferences =   context.getSharedPreferences("com.example.e2eeapp_alpha.Encryption",Context.MODE_PRIVATE);
//        SharedPreferences sharedPreferences =   context.getSharedPreferences("sp2",Context.MODE_PRIVATE);

        sharedPreferences.edit().putString("testvar", "ark").apply();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("testvar", "ark is great!!").apply();
        Log.i("FORWARD SECRECY:", "SHARED PREF" + sharedPreferences.getAll());
    }

//    public static void ratchetKey(Context context, byte[] currentMK){
//        //Message Key
////        byte[] mk = getMessageKey(context);
//        Log.i("FORWARD SECRECY", "$$$$$$$$$$$$$$$$$----Ratchet Working here----$$$$$$$$$$$$$$$$$$$$$$$$");
//
//        byte[] encKey1 = HKDF.fromHmacSha256().expand(currentMK, new byte[]{(byte) 0x01}, 16);
//
////        byte[] encKey2 = HKDF.fromHmacSha256().expand(encKey1, new byte[]{(byte) 0x01}, 16);
////
////        byte[] encKey3 = HKDF.fromHmacSha256().expand(encKey2, new byte[]{(byte) 0x01}, 16);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //code to update the message key in shared preference
//            SharedPreferences sharedPreferences =   context.getSharedPreferences("com.example.e2eeapp_alpha.Encryption",Context.MODE_PRIVATE);
//
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("mk_bob", Base64.getEncoder().encodeToString(encKey1)).apply();
//            editor.putString("mk_alice", Base64.getEncoder().encodeToString(encKey1)).apply();
//
//
//            Log.i("FORWARD SECRECY:", "Current MK:" + Base64.getEncoder().encodeToString(currentMK));
//            Log.i("FORWARD SECRECY:", "New MK:" + Base64.getEncoder().encodeToString(encKey1));
//            Log.i("FORWARD SECRECY:", "Updated SP:" + sharedPreferences.getAll());
//
//
//            Log.i("FORWARD SECRECY", "$$$$$$$$$$$$$$$$$----Ratchet Working END----$$$$$$$$$$$$$$$$$$$$$$$$");
//
///*Log.i("FORWARD SECRECY:", "SHARED PREF" + sharedPreferences.getAll());
//* I/FORWARD SECRECY:: SHARED PREF{testvar=ark is great!!, mk_bob=Dqjp1g0y3lqT0nPrSrFrZQ==, bob_public=CXlpZQl/hzxu9lRkplnbJhEC+R3LQjnqY+k5ohLQYWw=, shared_secret=S1saIeWSZv6JqiH6v+9hze0mwTKdzfAbD9/VtDOcrRQ=, ck_alice=0fSxP9NRh7138V+tPRJOBQ==, alice_public=ms711s+jk+qsSwFwTqkB2ud6ZQup4pTnnWHmv9dHd2s=, bob_private=QIXfENDkvP8BAAAAAAAAAAAAAAABAAAAAQAAAFDlvH8=, ck_bob=0fSxP9NRh7138V+tPRJOBQ==, alice_private=kAiNnwAAAAABAAAApq59HwAAAAABAAAAAQAAABzD/HA=, mk_alice=Dqjp1g0y3lqT0nPrSrFrZQ==}
//
// * */
//        }
//    }

    public static byte[] getCurrentMessageKey(Context context){
        /*Fetch Message key
        * 1.This Method dosen't generate a new Message Key, it only fetches the current Message Key
        * 2.Fetch from ono_chat_keys Shared Preference
        * */
        SharedPreferences sharedPreferences =   context.getSharedPreferences("ono_chat_keys",Context.MODE_PRIVATE);
        byte[] mk = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mk =  Base64.getDecoder().decode(sharedPreferences.getString("mk", "MESSAGEKEY ONOsp"));
        }

        Log.i("Fetched Current MK:", sharedPreferences.getString("mk", "aa"));

        return mk;
    }

    public static byte[] getCurrentChainKey(Context context){
        /*Fetch Message key
         * 1.This Method dosen't generate a new Chain Key, it only fetches the current Message Key
         * 2.Fetch from ono_chat_keys Shared Preference
         * */
        SharedPreferences sharedPreferences =   context.getSharedPreferences("ono_chat_keys",Context.MODE_PRIVATE);
        byte[] ck = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ck =  Base64.getDecoder().decode(sharedPreferences.getString("ck", "CHAINKEY ONOsp"));
        }

        Log.i("Fetched Current CK:", sharedPreferences.getString("ck", "aa"));

        return ck;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void ratchetKey(Context context, byte[] currentCK){
        /*Chain Key will be ratcheted here
        * 1. Get the current Chain Key
        * 2. Feed it into a HMAC-SHA256 adding additional data as 0x02
        * 3. Generate a new 32 byte Chain Key
        * 4. Using new current key, generate new 32 byte Message Key
        *
        * */
        Log.i("FORWARD SECRECY", "$$$$$$$$$$$$$$$$$----Ratchet Working here----$$$$$$$$$$$$$$$$$$$$$$$$");

        byte[] newChainKey = HKDF.fromHmacSha256().expand(currentCK, new byte[]{(byte) 0x02}, 32);
        byte[] newMessagekey = HKDF.fromHmacSha256().expand(newChainKey, "messageKey".getBytes(StandardCharsets.UTF_8), 32);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //code to update the chain key, mk in shared preference
            SharedPreferences sharedPreferences =   context.getSharedPreferences("ono_chat_keys",Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("ck", Base64.getEncoder().encodeToString(newChainKey)).apply();
            editor.putString("mk", Base64.getEncoder().encodeToString(newMessagekey)).apply();


            Log.i("FORWARD SECRECY:", "Current ChainKey:" + Base64.getEncoder().encodeToString(currentCK));
            Log.i("FORWARD SECRECY:", "New ChainKey:" + Base64.getEncoder().encodeToString(newChainKey));
            Log.i("FORWARD SECRECY:", "New MessageKey:" + Base64.getEncoder().encodeToString(newMessagekey));

            Log.i("FORWARD SECRECY:", "Updated SP ono_chat_sp:" + sharedPreferences.getAll());


            Log.i("FORWARD SECRECY", "$$$$$$$$$$$$$$$$$----Ratchet Working END----$$$$$$$$$$$$$$$$$$$$$$$$");

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static EncryptionObject EncryptAES(Context context, String text_to_encrypt) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException {
        /* Encryption in One on One Chats- Symmetric ENC AES-HMAC
        * 1.Generate 16 byte IV
        * 2.Fetch 32 byte Message Key
        * 3.Generate a 32 byte Encryption Key and 32 Byte Authentication Key
        * 4.Encrypt using Enc Key with AES256-CBC
        * 5.Add MAC Authentication code 32 byte using HMAC-SHA256
        * 2.
        * */
        Log.i("ENCRYPT", "----------------------------------- New Msg being encrypting here-----------------------------------");

        //derive IV
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);

        //Chain Key
        byte[] ck = getCurrentChainKey(context);
        //Message Key
        byte[] mk = getCurrentMessageKey(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("ENCRYPT", "Key 32B:" + Base64.getEncoder().encodeToString(mk) );
            Log.i("ENCRYPT", "IV 16B:" + Base64.getEncoder().encodeToString(iv) );

        }
        Log.i("ENCRYPT", "Plaintext:" + text_to_encrypt );

        byte[] text_to_encrypt_bytes = null;
        byte[] cipherText = null;

//        byte[] encKey = HKDF.fromHmacSha256().expand(mk, "encKey".getBytes(StandardCharsets.UTF_8), 16);
        byte[] encKey = HKDF.fromHmacSha256().expand(mk, "encKey".getBytes(StandardCharsets.UTF_8), 32);
        byte[] authKey = HKDF.fromHmacSha256().expand(mk, "authKey".getBytes(StandardCharsets.UTF_8), 32); //HMAC-SHA256 key is 32 byte

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("ENCRYPT", "EncKey:" + Base64.getEncoder().encodeToString(encKey) );
            Log.i("ENCRYPT", "AuthKey:" +  Base64.getEncoder().encodeToString(authKey) );

        }

        //get the string in bytes
        text_to_encrypt_bytes = text_to_encrypt.getBytes();

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


        //adding MAC
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
            Log.i("ENCRYPT:", "ENC+MAC:" + Base64.getEncoder().encodeToString(cipherMessage));
        }

        DecryptAES(cipherMessage, context, mk);



        //send the cipher text back
        EncryptionObject encryptionObject = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             encryptionObject = new EncryptionObject(
                    Base64.getEncoder().encodeToString(cipherMessage),
                    Base64.getEncoder().encodeToString(mk),
                     Base64.getEncoder().encodeToString(ck));

        }else {
             encryptionObject = new EncryptionObject("","");

        }


//        before returning the ciphertext ratchet the chain key forward : FORWARD SECRECY
        ratchetKey(context, ck);


        return encryptionObject;


    }//end of function



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String DecryptAES(byte[] cipherMessage, Context context, byte[] DecKey) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {


        Log.i("DECRYPT", "----------------------------------- New Msg decrypting here-----------------------------------");
        Log.i("DECRYPT", "Encrypted Message with MAC and IV:" + new String(cipherMessage) );

        ByteBuffer byteBuffer = ByteBuffer.wrap(cipherMessage);

        int ivLength = (byteBuffer.get());
        if (ivLength != 16) { // check input parameter
            throw new IllegalArgumentException("invalid iv length");
        }
        byte[] iv = new byte[ivLength];
        byteBuffer.get(iv);
        Log.i("DECRYPT", "IV Length:" + ivLength );
        Log.i("DECRYPT", "IV:" +  new String(iv) );

        int macLength = (byteBuffer.get());
        if (macLength != 32) { // check input parameter
            throw new IllegalArgumentException("invalid mac length");
        }
        byte[] mac = new byte[macLength];
        byteBuffer.get(mac);

        Log.i("DECRYPT", "MAC Length:" +  macLength );
        Log.i("DECRYPT", "MAC:" +  new String(mac) );


        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);


        //MAC authentiion >>

//        byte[] encKey = HKDF.fromHmacSha256().expand(DecKey, "encKey".getBytes(StandardCharsets.UTF_8), 16);
        byte[] encKey = HKDF.fromHmacSha256().expand(DecKey, "encKey".getBytes(StandardCharsets.UTF_8), 32);

        byte[] authKey = HKDF.fromHmacSha256().expand(DecKey, "authKey".getBytes(StandardCharsets.UTF_8), 32);

        Log.i("DECRYPT", "EncKey:" + new String(encKey));
        Log.i("DECRYPT", "AuthKey:" +  new String(authKey));

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

            Log.i("DECRYPT", "Decrypted message:" +  decryptedText);

        }

        return decryptedText;

    }

}
