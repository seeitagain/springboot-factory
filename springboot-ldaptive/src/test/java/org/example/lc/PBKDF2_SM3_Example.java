package org.example.lc;

import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.digests.SM3Digest;
import java.security.SecureRandom;

public class PBKDF2_SM3_Example {
    public static void main(String[] args) {
        String password = "123456";
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        int iterations = 100000;
        int keyLength = 256; // 32 bytes * 8 bits

        PKCS5S2ParametersGenerator generator = new PKCS5S2ParametersGenerator(new SM3Digest());
        generator.init(password.getBytes(), salt, iterations);
        KeyParameter key = (KeyParameter) generator.generateDerivedMacParameters(keyLength);

        System.out.println("Salt: " + bytesToHex(salt));
        System.out.println("Derived key: " + bytesToHex(key.getKey()));
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
