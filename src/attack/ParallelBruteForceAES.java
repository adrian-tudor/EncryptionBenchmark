package attack;

import encryption.AESUtil;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParallelBruteForceAES {

    private static final char[] CHARSET = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final AtomicBoolean found = new AtomicBoolean(false);

    public static boolean attack(String encryptedFile, String knownText, int maxKeyLength, int threadCount) throws Exception {
        byte[] encryptedBytes = Files.readAllBytes(Paths.get(encryptedFile));
        String base64Encrypted = Base64.getEncoder().encodeToString(encryptedBytes);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (char prefix : CHARSET) {
            String prefixStr = String.valueOf(prefix);
            executor.submit(() -> {
                bruteForceRecursive(prefixStr, maxKeyLength, base64Encrypted, knownText);
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);

        return found.get();
    }

    private static void bruteForceRecursive(String current, int maxLength, String encrypted, String knownText) {
        if (found.get()) return;

        if (current.length() == maxLength) {
            try {
                String key = padKey(current);
                String decrypted = AESUtil.decrypt(encrypted, key);
                if (decrypted.contains(knownText)) {
                    System.out.println(">>> Parola gasita: " + current);
                    found.set(true);
                }
            } catch (Exception ignored) {}
            return;
        }

        for (char c : CHARSET) {
            if (found.get()) return;
            bruteForceRecursive(current + c, maxLength, encrypted, knownText);
        }
    }

    private static String padKey(String key) {
        while (key.length() < 16) key += "0";
        return key.substring(0, 16);
    }
}
