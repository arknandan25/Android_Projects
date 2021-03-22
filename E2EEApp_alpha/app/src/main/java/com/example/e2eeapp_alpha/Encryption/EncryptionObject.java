package com.example.e2eeapp_alpha.Encryption;

public class EncryptionObject {
    String ciphertext;
    String ekey;
    String chainKey;


    public String getChainKey() {
        return chainKey;
    }

    public void setChainKey(String chainKey) {
        this.chainKey = chainKey;
    }


    public EncryptionObject(String ciphertext, String ekey, String chainKey) {
        this.ciphertext = ciphertext;
        this.ekey = ekey;
        this.chainKey = chainKey;
    }

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
