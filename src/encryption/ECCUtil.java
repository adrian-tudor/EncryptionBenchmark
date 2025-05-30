package encryption;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;

public class ECCUtil {

    private static final String ALGORITHM = "EC";
    private static final String SIGNATURE_ALGO = "SHA256withECDSA";

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(256);
        return keyGen.generateKeyPair();
    }

    public static byte[] signFile(String inputFile, PrivateKey privateKey) throws Exception {
        byte[] inputBytes = Files.readAllBytes(Paths.get(inputFile));
        Signature signature = Signature.getInstance(SIGNATURE_ALGO);
        signature.initSign(privateKey);
        signature.update(inputBytes);
        return signature.sign();
    }

    public static boolean verifyFile(String inputFile, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        byte[] inputBytes = Files.readAllBytes(Paths.get(inputFile));
        Signature signature = Signature.getInstance(SIGNATURE_ALGO);
        signature.initVerify(publicKey);
        signature.update(inputBytes);
        return signature.verify(signatureBytes);
    }
}
