package hashing;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class SHA512Util {

    public static byte[] hashFile(String inputFile) throws Exception {
        byte[] inputBytes = Files.readAllBytes(Paths.get(inputFile));
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        return digest.digest(inputBytes);
    }
}
