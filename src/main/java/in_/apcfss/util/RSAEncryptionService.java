package in_.apcfss.util;

import org.springframework.stereotype.Component;

import java.security.*;
import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;

@Component
public class RSAEncryptionService {
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    private static final String ALGORITHM = "RSA";
    private static final String TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final String MESSAGE_DIGEST = "SHA-256";
    private static final String MASK_GENERATION_FUNCTION = "MGF1";


    public RSAEncryptionService() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }

    public String getPublicKey() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public String encryptPassword(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decryptPassword(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        OAEPParameterSpec oaepParams = new OAEPParameterSpec(MESSAGE_DIGEST,
                MASK_GENERATION_FUNCTION, MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
        cipher.init(Cipher.DECRYPT_MODE, privateKey, oaepParams);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }
}
