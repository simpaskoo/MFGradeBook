package net.anax.appServerClient.client.cryptography;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class AESKey {
    private byte[] keyData;
    private byte[] iv;
    private Key key;

    public AESKey(byte[] keyData, byte[] iv){
        this.keyData = keyData;
        this.iv = iv;
        this.key = new SecretKeySpec(keyData, "AES");
    }
    public byte[] getKeyData(){
        return keyData;
    }
    public byte[] getIv(){
        return iv;
    }
    public Key getKey(){return key;}
}
