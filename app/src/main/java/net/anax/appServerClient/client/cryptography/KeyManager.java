package net.anax.appServerClient.client.cryptography;

import android.util.Base64;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

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
        rsaPublicKey = new RSAPublicKey(android.util.Base64.decode("MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA0+1qv25tnwOWPGjFOV5wzp5gPdEXud/qTaq1s7l3cbzWjsa72Dd5VvLAEizZS6eSX4axLJxmOOy9bOaH63vfQPqq2CV5ZNouCpTp3Gli+IzYbL0NsoAZrsaH5GmPWYTZupn5If2Pz4biL+UGNctkg0D28vw4y8+eqPyTsCmaUSNkKMsZztlH7nuXDpr5i8W10VDSN8YRdxUWS8Vx8V2yFlHlmSJfmspkDr7JNZF7Pmz2F3MoPlD61EOT8YhWaaHlrzbOWotCVvLncnuBiJeBBquSkgKTQy8Q1DGPTLGvjY6/7FN3sSlP+QduRaCCj8jsFxuRd0BY8hOTOX/BNPoLtIzpjzz0dsX3/GkPG6z/Nyq0PkUG3FpsRr7mfTNzgm6qQxgnLBQFEc4KSiaMqKLiddr+d+vzdlAkfDypMzsv7p8sMwZkiZwPDjob853Z+R4HGeeL62Y/RKQxbCjWbN94RUT+06xp+zDr92+fqgrZcbDRttF2ylhYAumwnlJNpLx6qrLIr0nLdJ0eadUxSyLcjloplVYaFavGTVVsbtWHn0wknf5KRQpRI56d9Ne/MkwG3EOURgb/XP3UEIy5UznCoEhXMmMp7U9Lt/zkXVhYZIsBx1fdNiaSi5bOyE9yOyS+Hlvwm3X6kyED/YXOAJ8A5bOE81GaipVpTQl4qUv1jWcCAwEAAQ==", Base64.DEFAULT));
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
            return new AESKey(k.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("INVALID STRING LITERAL, lid: 38451323846413521384352", e);
        }
    }


}
