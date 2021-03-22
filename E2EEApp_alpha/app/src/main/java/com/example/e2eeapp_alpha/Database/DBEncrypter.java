package com.example.e2eeapp_alpha.Database;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/*End to End Encryption Android Client
* Made with: Java, JavaxCrypto, Android, ECDHCurve25519 DH, AES-256 CBC with HMAC-SHA256 tags
* Made By: Ark Nandan Singh Chauhan
* Computer Engineering @ Trinity College Dublin, Ireland 2021
* Github:https://github.com/arknandan25
* */
public class DBEncrypter {


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String Encrypt(String plaintext, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec secretKeySpec = generateKey(password);
        //AES-Encrypt
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encVal = cipher.doFinal(plaintext.getBytes());
        String encrypted_String = new String(encVal, "ISO-8859-1");
        return encrypted_String;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String Decrypt(String encrypted_text, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec secretKeySpec = generateKey(password);
        //AES-Encrypt
        byte[] encrypted_msg_bytes = encrypted_text.getBytes("ISO-8859-1");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] encVal = cipher.doFinal(encrypted_msg_bytes);
        String decrypted_String = new String(encVal);
        return decrypted_String;
    }

    private static SecretKeySpec generateKey(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] password_bytes = password.getBytes("UTF-8");
        digest.update(password_bytes, 0, password_bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return  secretKeySpec;
    }
}
