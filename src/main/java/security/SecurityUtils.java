package security;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static config.SystemConstants.AES_ALGORITHM;
import static config.SystemConstants.AES_KEY;

public class SecurityUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityUtils.class);

    public String sha512Hash(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] data = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<data.length;i++)
            {
                sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch(Exception e) {
            LOGGER.error(String.format("%s", e));
        }

        return null;
    }

}
