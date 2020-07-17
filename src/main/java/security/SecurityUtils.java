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
import java.security.NoSuchAlgorithmException;

import static config.SystemConstants.AES_ALGORITHM;
import static config.SystemConstants.AES_KEY;

public class SecurityUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityUtils.class);

    public String encrypt(String data) {

        try {
            byte[] dataToSend = data.getBytes();

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            Assertions.assertNotNull(cipher);

            SecretKeySpec k = new SecretKeySpec(AES_KEY, AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, k);

            byte[] encryptedData = cipher.doFinal(dataToSend);
            byte[] encryptedByteValue = new Base64().encode(encryptedData);

            return new String(encryptedByteValue);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            LOGGER.error(e.toString());
        }

        return null;
    }

    public String decrypt(String data) {

        try {
            byte[] encryptedData = new Base64().decode(data);

            Cipher c = Cipher.getInstance(AES_ALGORITHM);
            Assertions.assertNotNull(c);

            SecretKeySpec k = new SecretKeySpec(AES_KEY, AES_ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, k);

            byte[] decrypted = c.doFinal(encryptedData);
            Assertions.assertNotNull(decrypted);

            return new String(decrypted);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            LOGGER.error(e.toString());
        }

        return null;
    }

}
