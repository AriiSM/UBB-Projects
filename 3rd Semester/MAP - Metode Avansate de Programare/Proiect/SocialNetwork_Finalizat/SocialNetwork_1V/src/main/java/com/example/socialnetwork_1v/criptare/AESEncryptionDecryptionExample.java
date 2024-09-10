package com.example.socialnetwork_1v.criptare;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESEncryptionDecryptionExample {
    private SecretKey secretKey;

    public AESEncryptionDecryptionExample() throws Exception {
        // Generare cheie secreta
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        secretKey = keyGenerator.generateKey();
    }

    public String encrypt(String message) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Mesajul de intrare este convertit
            // într-un șir de octeți și apoi transmis algoritmului de hash
            md.update(message.getBytes());

            // Se obține rezultatul hash-ului sub formă de șir de octeți
            byte[] digest = md.digest();

            //Rezultatul hash-ului este codificat în format Base64, ceea ce
            // înseamnă că rezultatul este convertit într-un șir de caractere
            // reprezentând acești octeți sub o formă ușor citită.
            return Base64.getEncoder().encodeToString(digest);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}