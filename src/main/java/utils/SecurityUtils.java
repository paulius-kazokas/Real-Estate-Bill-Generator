package utils;

import config.SystemConstants;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class SecurityUtils {

    public String sha512Hash(String password) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance(SystemConstants.SHA512);
        byte[] data = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();

        for (byte datum : data) {
            sb.append(Integer.toString((datum & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public boolean checkIfPasswordMatches(String password, String userDatabasePassword) throws NoSuchAlgorithmException {
        return sha512Hash(password).equals(userDatabasePassword);
    }

}
