package com.example.e2eeapp_alpha.Encryption;

public class EncryptionObject {
    String ciphertext, ekey;

    public EncryptionObject(String ciphertext, String ekey) {
        this.ciphertext = ciphertext;
        this.ekey = ekey;
    }

    public String getCiphertext() {
        return ciphertext;
    }

    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }

    public String getEkey() {
        return ekey;
    }

    public void setEkey(String ekey) {
        this.ekey = ekey;
    }
}
