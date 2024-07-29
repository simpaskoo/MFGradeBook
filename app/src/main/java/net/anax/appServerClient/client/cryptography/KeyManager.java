package net.anax.appServerClient.client.cryptography;

import android.util.Base64;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class KeyManager {
    static KeyManager INSTANCE = null;
    AESKey aesKey = null;
    RSAPublicKey rsaPublicKey = null;

    public static KeyManager getINSTANCE(){
        if(INSTANCE == null){
            INSTANCE = new KeyManager();
        }
        return INSTANCE;
    }
    public KeyManager(){
        rsaPublicKey = new RSAPublicKey(android.util.Base64.decode("MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA97vD2oQjzDl+T3zAVRJ5pgMPy62OThfcndg+m/KKEVUa0ToensfeKI1TdZgR5x9lhHLv7iuV2RfU7VjzWOE4EwJye2QT8TLAIEBpg8yDjNB+gAjO2lVzW6pL4JPFk5a35JkSuoeaK97urnlGf4UNowGJjTVoJog0ZE3tVG+PFgUrWrzPE0TDjH6dBQRVjlzc5IeCyKtpX8yQSfBMV1D3M1M70b5TcnJs/KSPnIEvyE0IdbVw0FmUPJg5/awmwmKV1Hkz9hBkeml9CCNaTt0Aon6IwswLPAQSuv6DZTo86IcaxN8KyjZmSyhyxJ7zkwiwbYha3zBYTFTPwfoViIQ0dJzQ31ilk6IPScGrQ6ggu1uFiwSfoCZYsoLR14I313sh175SaA4P0lT19WdWRXToeoRzrNEzGLVCHusRYzi5hAz9xy0MEf30tq7l9ufYU5s/kcDoXg/qB/k6lmgZnlEjAlUhrniiyTqOvigMw+nm/doAP/WUR0SH8pOyMdP2jtoP3IYDq7D3xw8QcluPdQ9smhpg/IcPwbuRYZ+jx0mG9XBE6gXkILzIL+6wzwqUYgBF7KXS1VVPG5GOTs7ZDrQfkkveXkYNuxnbi0LZ9J20kG2lMw0P9/QzctKUuXz9DmfMTUNOEZQAKl25/3MbjGERSnk2yNbDvh8QZIUK3p3eT/8CAwEAAQ==", Base64.DEFAULT));
    }
    public void setRsaPublicKey(RSAPublicKey key){
        this.rsaPublicKey = key;
    }
    public void setAesKey(AESKey key){
        this.aesKey = key;
    }
    public AESKey getAesKey(){
        if(aesKey == null){
            aesKey = generateAesKey();
        }
        return aesKey;
    }
    public RSAPublicKey getRSAPublicKey() {
        return rsaPublicKey;
    }

    public void refreshAesKey(){
        aesKey = generateAesKey();
    }
    AESKey generateAesKey(){
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            Key k = generator.generateKey();

            byte[] iv = new byte[16];
            SecureRandom sr = new SecureRandom();
            sr.nextBytes(iv);
            return new AESKey(k.getEncoded(), iv);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("INVALID STRING LITERAL, lid: 38451323846413521384352", e);
        }
    }


}
