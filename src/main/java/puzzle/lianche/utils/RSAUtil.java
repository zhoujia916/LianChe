package puzzle.lianche.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtil{
    private Map<String, Object> mKeys = new HashMap<String, Object>();
    public final String KEY_ALGORITHM = "RSA";

    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCexLYcCXuasOm/S5jL4vDYDAM6Mgfh2zkqhfDL\n" +
                                            "zSRRhmF2JxQifyQgZYLoC4jOnsGO8oiv5gU28zikje/XmrmW1+TZqR3FjYgio7I1Sl/1OjXgp0rn\n" +
                                            "zv3nEAm7jMcNRhatLf2PZ8dmDumQKrUX5vKDYjWvdUN8/hZuq3Nn6gwylwIDAQAB\n" +
                                            "encrypt text:HueQEjlhXsRbOlqpXSiO5kldK7O2gnasivi6g/COWfbUZbmtU5e6facAYDZMCTH/YCkbV0C0ZrB4\n" +
                                            "xfabOenn3mmAlRmfu5zbMeXn+AL1hfxRS4SbprrPDt+aNwRWoUej6LgUUIel6xqa/8UBa3cba8H6\n" +
                                            "nj41qRjg4GIaz7cZnlg=";

    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ7EthwJe5qw6b9LmMvi8NgMAzoy\n" +
                                            "B+HbOSqF8MvNJFGGYXYnFCJ/JCBlgugLiM6ewY7yiK/mBTbzOKSN79eauZbX5NmpHcWNiCKjsjVK\n" +
                                            "X/U6NeCnSufO/ecQCbuMxw1GFq0t/Y9nx2YO6ZAqtRfm8oNiNa91Q3z+Fm6rc2fqDDKXAgMBAAEC\n" +
                                            "gYEAj0YK5NA158nAlm1c7JjWKNDmVKzzagDP3KzUOMXeAWfYKaxoQh0j98xDTq2h/ntLRnldmgZk\n" +
                                            "LEtekTi4+ILa+HXSB1hbcjqCUaTYbL+naXfVWP1mTgOasXOAJLaLSnbf/m/E1nHJtn1lHMtizEZz\n" +
                                            "yfFIVhIQgKh0Yue/LX4xjxECQQDOZWOYJIjDKSPogZF/GTJVH5pUksIJtwo+A8EDmB9dVgQiYSgv\n" +
                                            "pl9jX6MZauqw4aRY0qBqTgqwDAfdPx+9xyNvAkEAxO0CeQEw4w8rgqCcR3kXAOqn4PUE9tmCCPfw\n" +
                                            "oQRL6dvVjJKolDhY+W4VWzmDR9+nJR1wku6LJIvN/s0ubuivWQJBAJdIf9DT7p7GN/jDiAcWS+tT\n" +
                                            "B8c37xw52zsLe12MEmlSlCMy/ca3dzGatyz5Tl8qdVFZyZVB8NmLZ/RGwZhUL+cCQCU5N9JvqzIA\n" +
                                            "WBUlDuVjujluIbWgGwQeMTpIw69sBH18FG2x+zOpQwhDCr5nrK0VVZ7qZbEgbiTGWHXlivVj/mEC\n" +
                                            "QBnxcFiGt4Z1IKx4IyIMLN8qU91c1cS/rhSKqNmqqyESoifo8V8RNivuWR9xA5piLnW+A+PYz6wi";

    /**
     * 初始化密钥
     * @throws Exception
     */
    public void initKey() throws Exception{
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        generator.initialize(1024);
        KeyPair pair = generator.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey)pair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey)pair.getPublic();
        mKeys.put("public_key", publicKey);
        mKeys.put("private_key", privateKey);
    }

    /**
     * 获取私钥字符串
     */
    public String getPrivateKey(){
        Key key = (Key)mKeys.get("private_key");
        return new BASE64Encoder().encode(key.getEncoded());
    }

    /**
     * 获取公钥字符串
     */
    public String getPublicKey(){
        Key key = (Key)mKeys.get("public_key");
        return new BASE64Encoder().encode(key.getEncoded());
    }

    /**
     * 加密字符串
     * @param publicKey  公钥字符串（经过base64编码）
     * @param data       需要加密的字符串
     * @throws Exception
     */
    public String encrypt(String publicKey, String data) throws Exception {
        PublicKey key = getPublicKey(publicKey);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] enBytes = cipher.doFinal(data.getBytes());
        return (new BASE64Encoder()).encode(enBytes);
    }

    /**
     * 加密字符串
     * @param data       需要加密的字符串
     * @throws Exception
     */
    public String encrypt(String data) throws Exception {
        return encrypt(PUBLIC_KEY, data);
    }

    /**
     * 解密字符串
     * @param privateKey 私钥字符串（经过base64编码）
     * @param data       需要解密的字符串
     * @throws Exception
     */
    public String decrypt(String privateKey, String data) throws Exception {
        PrivateKey key = getPrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] deBytes = cipher.doFinal(new BASE64Decoder().decodeBuffer(data));
        return new String(deBytes);
    }

    /**
     * 解密字符串
     * @param data       需要解密的字符串
     * @throws Exception
     */
    public String decrypt(String data) throws Exception {
        return decrypt(PRIVATE_KEY, data);
    }

    /**
     * 根据字符串获取公钥对象
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 根据字符串获取私钥对象
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
}