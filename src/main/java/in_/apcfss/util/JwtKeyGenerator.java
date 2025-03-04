package in_.apcfss.util;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtKeyGenerator {
    public static String generateSecretKey(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[length];
        secureRandom.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }
    
    public static void main(String[] args) {
        String secretKey = generateSecretKey(32); // 32 bytes key (256 bits)
        System.out.println("Secret Key: " + secretKey);
    }
}
