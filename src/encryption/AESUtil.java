package encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class AESUtil {

    private static final String ALGORITHM = "AES";

    public static void encryptFile(String inputFile, String outputFile, String key) throws Exception {
        byte[] inputBytes = Files.readAllBytes(Paths.get(inputFile));
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(inputBytes);

        // Encode to Base64 and save as text
        String base64Encrypted = Base64.getEncoder().encodeToString(encryptedBytes);
        Files.write(Paths.get(outputFile), base64Encrypted.getBytes());
    }


    public static void decryptFile(String inputFile, String outputFile, String key) throws Exception {
        String base64Encrypted = new String(Files.readAllBytes(Paths.get(inputFile)));

        byte[] encryptedBytes = Base64.getDecoder().decode(base64Encrypted);

        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        Files.write(Paths.get(outputFile), decryptedBytes);
    }


    public static String decrypt(String encryptedData, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

}
