package encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TripleDESUtil {
    private static final String ALGORITHM = "DESede";

    public static void encryptFile(String inputFile, String outputFile, String key) throws Exception {
        byte[] inputBytes = Files.readAllBytes(Paths.get(inputFile));
        SecretKeySpec secretKey = new SecretKeySpec(padKey(key).getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] outputBytes = cipher.doFinal(inputBytes);
        Files.write(Paths.get(outputFile), outputBytes);
    }

    public static void decryptFile(String inputFile, String outputFile, String key) throws Exception {
        byte[] inputBytes = Files.readAllBytes(Paths.get(inputFile));
        SecretKeySpec secretKey = new SecretKeySpec(padKey(key).getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] outputBytes = cipher.doFinal(inputBytes);
        Files.write(Paths.get(outputFile), outputBytes);
    }

    private static String padKey(String key) {
        while (key.length() < 24) key += "0";
        return key.substring(0, 24);
    }
}
