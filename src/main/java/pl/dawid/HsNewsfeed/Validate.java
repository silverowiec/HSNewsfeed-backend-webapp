package pl.dawid.HsNewsfeed;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Created by dawid on 13/06/16.
 */
public class Validate {

    public boolean valid;

    public Validate(String schoolPass, String password) throws NoSuchAlgorithmException {
        MessageDigest encryptor = MessageDigest.getInstance("SHA-256");
        byte[] hash = encryptor.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for(byte b: hash)
            sb.append(b);
        password = sb.toString();
        this.valid = Objects.equals(password, schoolPass);
    }

    public boolean getValid(){
        return valid;
    }

}
