package hashing;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class SHA256Util {

    public static byte[] hashFile(String inputFile) throws Exception {
        byte[] inputBytes = Files.readAllBytes(Paths.get(inputFile));
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(inputBytes);
    }
}
