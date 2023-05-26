import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class EncryptionTest {
    public static void main(String[] args) {
        String targetTextFile = "Targettext.txt";
        String desOutputFile = "DES_Encrypted.txt";
        String tripleDesOutputFile = "3DES_Encrypted.txt";
        String aesOutputFile = "AES_Encrypted.txt";
        String rsaOutputFile = "RSA_Encrypted.txt";

        try {
            // Read the Targettext file
            byte[] targetTextData = readFile(targetTextFile);

            // Generate a random key for DES
            SecretKey desKey = generateKey("DES", 56);

            // Perform DES encryption
            long desStartTime = System.nanoTime();
            byte[] desEncryptedData = encrypt(targetTextData, desKey, "DES");
            long desEndTime = System.nanoTime();
            long desExecutionTime = desEndTime - desStartTime;

            // Write DES encrypted data to file
            writeFile(desOutputFile, desEncryptedData);

            // Generate a random key for 3DES
            SecretKey tripleDesKey = generateKey("DESede", 168);

            // Perform 3DES encryption
            long tripleDesStartTime = System.nanoTime();
            byte[] tripleDesEncryptedData = encrypt(targetTextData, tripleDesKey, "DESede");
            long tripleDesEndTime = System.nanoTime();
            long tripleDesExecutionTime = tripleDesEndTime - tripleDesStartTime;

            // Write 3DES encrypted data to file
            writeFile(tripleDesOutputFile, tripleDesEncryptedData);

            // Generate a random key for AES
            SecretKey aesKey = generateKey("AES", 128);

            // Perform AES encryption
            long aesStartTime = System.nanoTime();
            byte[] aesEncryptedData = encrypt(targetTextData, aesKey, "AES");
            long aesEndTime = System.nanoTime();
            long aesExecutionTime = aesEndTime - aesStartTime;

            // Write AES encrypted data to file
            writeFile(aesOutputFile, aesEncryptedData);

            // Generate a key pair for RSA
            KeyPair rsaKeyPair = generateRSAKeyPair();

            // Perform RSA encryption
            long rsaStartTime = System.nanoTime();
            byte[] rsaEncryptedData = encryptRSA(targetTextData, rsaKeyPair.getPublic());
            long rsaEndTime = System.nanoTime();
            long rsaExecutionTime = rsaEndTime - rsaStartTime;

            // Write RSA encrypted data to file
            writeFile(rsaOutputFile, rsaEncryptedData);

            System.out.println("Encryption completed successfully!");

            // Print execution times
            System.out.println("DES Execution Time: " + desExecutionTime + " ns");
            System.out.println("3DES Execution Time: " + tripleDesExecutionTime + " ns");
            System.out.println("AES Execution Time: " + aesExecutionTime + " ns");
            System.out.println("RSA Execution Time: " + rsaExecutionTime + " ns");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] readFile(String filePath) throws Exception {
        FileInputStream fis = new FileInputStream(filePath);
        byte[] data = fis.readAllBytes();
        fis.close();
        return data;
    }

    private static void writeFile(String filePath, byte[] data) throws Exception {
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(data);
        fos.close();
    }

    private static SecretKey generateKey(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(keySize);
        return keyGenerator.generateKey();
    }

    private static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    private static byte[] encrypt(byte[] data, Key key, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm + "/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    private static byte[] encryptRSA(byte[] data, Key publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        
        int blockSize = getCipherBlockSize(cipher);
        int inputLength = data.length;
        int offset = 0;
        byte[] encryptedData = new byte[0];

        while (inputLength - offset > 0) {
            int blockLength = Math.min(blockSize, inputLength - offset);
            byte[] encryptedBlock = cipher.doFinal(data, offset, blockLength);
            encryptedData = concatenateByteArrays(encryptedData, encryptedBlock);
            offset += blockSize;
        }

        return encryptedData;
    }

    private static int getCipherBlockSize(Cipher cipher) {
        int blockSize;
        try {
            blockSize = cipher.getBlockSize();
        } catch (UnsupportedOperationException e) {
            blockSize = cipher.getOutputSize(1) - 1;
        }
        return blockSize;
    }

    private static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
