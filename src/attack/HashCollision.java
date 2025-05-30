package attack;

import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;

public class HashCollision {

    public static boolean simulateHashCollisions(String algorithm, int maxAttempts) throws Exception {
        Set<String> seenHashes = new HashSet<>();
        MessageDigest digest = MessageDigest.getInstance(algorithm);

        for (int i = 0; i < maxAttempts; i++) {
            String input = "input" + i;
            byte[] hash = digest.digest(input.getBytes());
            String hashHex = bytesToHex(hash);

            if (seenHashes.contains(hashHex)) {
                System.out.println(">>> Coliziune gasita dupa " + i + " incercari: " + hashHex);
                return true;
            }
            seenHashes.add(hashHex);
        }

        System.out.println("Nicio coliziune dupa " + maxAttempts + " incercari.");
        return false;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
