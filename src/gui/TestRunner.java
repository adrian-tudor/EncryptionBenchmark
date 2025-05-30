package gui;

import attack.ParallelBruteForceAES;
import attack.HashCollision;
import encryption.*;
import hashing.MD5Util;
import hashing.SHA1Util;
import hashing.SHA256Util;
import attack.ParallelDictionaryAttackAES;
import hashing.SHA512Util;
import utils.TimerUtil;

import java.security.KeyPair;

public class TestRunner {

    private final ResultsTableModel tableModel;

    public TestRunner(ResultsTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public void runCryptoTests() throws Exception {
        tableModel.clearResults();

        String keyAES = "qwerty0000000000";
        String keyDES = "12345678";
        String inputFile = "resources/input.txt";

        String encryptedAES = "resources/encrypted_aes.txt";
        String decryptedAES = "resources/decrypted_aes.txt";
        String encryptedDES = "resources/encrypted_des.txt";
        String decryptedDES = "resources/decrypted_des.txt";
        String key3DES = "123456789012345678901234"; // 24 chars
        String encrypted3DES = "resources/encrypted_3des.txt";
        String decrypted3DES = "resources/decrypted_3des.txt";
        KeyPair rsaKeyPair = RSAUtil.generateKeyPair();
        String encryptedRSA = "resources/encrypted_rsa.txt";
        String decryptedRSA = "resources/decrypted_rsa.txt";

        // AES
        TimerUtil timer = new TimerUtil("Criptare AES");
        timer.start();
        AESUtil.encryptFile(inputFile, encryptedAES, keyAES);
        double aesEncryptTime = timer.stopAndGet();
        tableModel.addResult("AES", "Criptare", aesEncryptTime);

        timer = new TimerUtil("Decriptare AES");
        timer.start();
        AESUtil.decryptFile(encryptedAES, decryptedAES, keyAES);
        double aesDecryptTime = timer.stopAndGet();
        tableModel.addResult("AES", "Decriptare", aesDecryptTime);

        // DES
        timer = new TimerUtil("Criptare DES");
        timer.start();
        DESUtil.encryptFile(inputFile, encryptedDES, keyDES);
        double desEncryptTime = timer.stopAndGet();
        tableModel.addResult("DES", "Criptare", desEncryptTime);

        timer = new TimerUtil("Decriptare DES");
        timer.start();
        DESUtil.decryptFile(encryptedDES, decryptedDES, keyDES);
        double desDecryptTime = timer.stopAndGet();
        tableModel.addResult("DES", "Decriptare", desDecryptTime);

        //3DES
        timer = new TimerUtil("Criptare 3DES");
        timer.start();
        TripleDESUtil.encryptFile(inputFile, encrypted3DES, key3DES);
        double encrypt3des = timer.stopAndGet();
        tableModel.addResult("3DES", "Criptare", encrypt3des);

        timer = new TimerUtil("Decriptare 3DES");
        timer.start();
        TripleDESUtil.decryptFile(encrypted3DES, decrypted3DES, key3DES);
        double decrypt3des = timer.stopAndGet();
        tableModel.addResult("3DES", "Decriptare", decrypt3des);

        //RSA
        timer = new TimerUtil("Criptare RSA");
        timer.start();
        RSAUtil.encryptFile(inputFile, encryptedRSA, rsaKeyPair.getPublic());
        double rsaEncrypt = timer.stopAndGet();
        tableModel.addResult("RSA", "Criptare", rsaEncrypt);

        timer = new TimerUtil("Decriptare RSA");
        timer.start();
        RSAUtil.decryptFile(encryptedRSA, decryptedRSA, rsaKeyPair.getPrivate());
        double rsaDecrypt = timer.stopAndGet();
        tableModel.addResult("RSA", "Decriptare", rsaDecrypt);

        //ECC
        KeyPair eccKeyPair = ECCUtil.generateKeyPair();
        timer = new TimerUtil("ECC Sign/Verify");

        timer.start();
        byte[] eccSignature = ECCUtil.signFile(inputFile, eccKeyPair.getPrivate());
        boolean eccValid = ECCUtil.verifyFile(inputFile, eccSignature, eccKeyPair.getPublic());
        double eccTime = timer.stopAndGet();

        tableModel.addResult("ECC", eccValid ? "Semnatura valida" : "Semnatura esuata", eccTime);
    }

    public void runHashingTests() throws Exception {
        String inputFile = "resources/input.txt";
        TimerUtil timer;

        timer = new TimerUtil("Hashing SHA-256");
        timer.start();
        SHA256Util.hashFile(inputFile);
        double shaTime = timer.stopAndGet();
        tableModel.addResult("SHA-256", "Hashing", shaTime);

        timer = new TimerUtil("Hashing SHA-1");
        timer.start();
        SHA1Util.hashFile(inputFile);
        double sha1Time = timer.stopAndGet();
        tableModel.addResult("SHA-1", "Hashing", sha1Time);

        timer = new TimerUtil("Hashing SHA-512");
        timer.start();
        SHA512Util.hashFile(inputFile);
        double sha512Time = timer.stopAndGet();
        tableModel.addResult("SHA-512", "Hashing", sha512Time);

        timer = new TimerUtil("Hashing MD5");
        timer.start();
        MD5Util.hashFile(inputFile);
        double md5Time = timer.stopAndGet();
        tableModel.addResult("MD5", "Hashing", md5Time);
    }

    public void runParallelDictionaryAttack(int threadCount, String knownText) throws Exception {
        String encryptedFile = "resources/encrypted_aes.txt";
        String dictionaryFile = "resources/dictionary.txt";

        TimerUtil timer = new TimerUtil("Parallel Dictionary Attack AES");
        timer.start();
        boolean success = ParallelDictionaryAttackAES.attack(encryptedFile, dictionaryFile, knownText, threadCount);
        double attackTime = timer.stopAndGet();

        if (success) {
            tableModel.addResult("AES", "Dictionary Attack (Found)", attackTime);
        } else {
            tableModel.addResult("AES", "Dictionary Attack (Not Found)", attackTime);
        }
    }
}
