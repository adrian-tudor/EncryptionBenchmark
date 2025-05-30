package encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DESUtil {

    private static final String ALGORITHM = "DES";

    public static void encryptFile(String inputFile, String outputFile, String key) throws Exception {
        byte[] inputBytes = Files.readAllBytes(Paths.get(inputFile));
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] outputBytes = cipher.doFinal(inputBytes);
        Files.write(Paths.get(outputFile), outputBytes);
    }

    public static void decryptFile(String inputFile, String outputFile, String key) throws Exception {
        byte[] inputBytes = Files.readAllBytes(Paths.get(inputFile));
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] outputBytes = cipher.doFinal(inputBytes);
        Files.write(Paths.get(outputFile), outputBytes);
    }
}
