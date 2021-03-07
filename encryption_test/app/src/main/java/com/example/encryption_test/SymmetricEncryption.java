package com.example.encryption_test;

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
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import at.favre.lib.crypto.HKDF;

public class SymmetricEncryption {


    byte[] key;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    static void EncryptAES( byte[] key,  byte[] iv , String text_to_encrypt) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException {

        Log.i("ENCRYPT", "Key:" + new String(key) );
        Log.i("ENCRYPT", "IV:" + new String(iv) );
        Log.i("ENCRYPT", "Plaintext:" + text_to_encrypt );

        byte[] text_to_encrypt_bytes = null;
        byte[] cipherText = null;

        byte[] encKey = HKDF.fromHmacSha256().expand(key, "encKey".getBytes(StandardCharsets.UTF_8), 16);
        byte[] authKey = HKDF.fromHmacSha256().expand(key, "authKey".getBytes(StandardCharsets.UTF_8), 32); //HMAC-SHA256 key is 32 byte

        Log.i("ENCRYPT", "EncKey:" + new String(encKey) );
        Log.i("ENCRYPT", "AuthKey:" +  new String(authKey) );

        //get the string in bytes
        text_to_encrypt_bytes = text_to_encrypt.getBytes();

        final Cipher cipher;
        try {
            //            getInstance creates an object of the Cipher class
            //            takes input as "algorithm/mode/padding"
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

        DecryptAES(cipherMessage, key);

        //
//        Arrays.fill(authKey, (byte) 0);
//        Arrays.fill(encKey, (byte) 0);


    }//end of function

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static void DecryptAES(byte[] cipherMessage, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

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

        byte[] encKey = HKDF.fromHmacSha256().expand(key, "encKey".getBytes(StandardCharsets.UTF_8), 16);
        byte[] authKey = HKDF.fromHmacSha256().expand(key, "authKey".getBytes(StandardCharsets.UTF_8), 32);

        Log.i("DECRYPT", "EncKey:" + new String(encKey) );
        Log.i("DECRYPT", "AuthKey:" +  new String(authKey) );

        SecretKey macKey = new SecretKeySpec(authKey, "HmacSHA256");
        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(macKey);
        hmac.update(iv);
        hmac.update(cipherText);
        byte[] refMac = hmac.doFinal();

        if (!MessageDigest.isEqual(refMac, mac)) {
            throw new SecurityException("could not authenticate MAC");
        }else{
            Log.i("DECRYPT", "MAC authenticated!" );
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(encKey, "AES"), new IvParameterSpec(iv));
            byte[] plainText = cipher.doFinal(cipherText);
            String decryptedText = new String(plainText);
            Log.i("DECRYPT", "Decrypted message:" +  decryptedText);

        }

    }




}
