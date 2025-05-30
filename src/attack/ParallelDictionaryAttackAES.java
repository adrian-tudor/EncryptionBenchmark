package attack;

import encryption.AESUtil;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParallelDictionaryAttackAES {

    private static final AtomicBoolean found = new AtomicBoolean(false);

    public static boolean attack(String encryptedFilePath, String dictionaryFilePath, String knownPlainText, int threadCount) throws Exception {
        List<String> passwords = Files.readAllLines(Paths.get(dictionaryFilePath));
        String encryptedContent = new String(Files.readAllBytes(Paths.get(encryptedFilePath)));

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        int chunkSize = passwords.size() / threadCount;

        for (int i = 0; i < threadCount; i++) {
            int start = i * chunkSize;
            int end = (i == threadCount - 1) ? passwords.size() : (i + 1) * chunkSize;
            List<String> passwordChunk = passwords.subList(start, end);

            executor.submit(new DictionaryWorker(passwordChunk, encryptedContent, knownPlainText));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);

        return found.get();
    }

    private static class DictionaryWorker implements Runnable {
        private final List<String> passwords;
        private final String encryptedContent;
        private final String knownPlainText;

        public DictionaryWorker(List<String> passwords, String encryptedContent, String knownPlainText) {
            this.passwords = passwords;
            this.encryptedContent = encryptedContent;
            this.knownPlainText = knownPlainText;
        }

        @Override
        public void run() {
            for (String password : passwords) {
                if (found.get()) break;
                try {
                    String key = padKey(password.trim());
                    String decrypted = AESUtil.decrypt(encryptedContent, key);
                    if (decrypted.contains(knownPlainText)) {
                        System.out.println(">>> Parola gasita: " + password);
                        found.set(true);
                        break;
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static String padKey(String key) {
        while (key.length() < 16) {
            key += "0";
        }
        return key.substring(0, 16);
    }
}
