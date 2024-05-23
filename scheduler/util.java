package scheduler;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Util {
    private static final int HASH_STRENGTH = 10;
    private static final int KEY_LENGTH = 16;

    public Util() {
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] generateHash(String password, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10, 16);
        SecretKeyFactory factory = null;
        byte[] hash = null;

        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return hash;
        } catch (InvalidKeySpecException | NoSuchAlgorithmException var6) {
            throw new IllegalStateException();
        }
    }

    public static byte[] trim(byte[] bytes) {
        int i;
        for(i = bytes.length - 1; i >= 0 && bytes[i] == 0; --i) {
        }

        return Arrays.copyOf(bytes, i + 1);
    }
}
