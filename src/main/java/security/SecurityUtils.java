package security;

import config.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class SecurityUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityUtils.class);

    public String sha512Hash(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance(SystemConstants.SHA512);
            byte[] data = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte datum : data) {
                sb.append(Integer.toString((datum & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch (Exception e) {
            LOGGER.error(String.format("%s", e));
        }

        return null;
    }

}
