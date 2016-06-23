package puzzle.lianche.utils;

import com.thoughtworks.xstream.core.util.Base64Encoder;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class EncryptUtil {

	public static String SHA1(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(input.getBytes("utf-8"));
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String SHA(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA");
			digest.update(input.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String MD5(String input) {
		try {
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(input.getBytes());
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < md.length; i++) {
				String shaHex = Integer.toHexString(md[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

    public static String MD5(String input, int length) {
        input = MD5(input);
        if(input.length() > 0 && length == 16){
            input = input.substring(8, 24);
        }
        return input;
    }

	public static byte[] AESEncrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] AESDecrypt(byte[] content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}



    public class RSAUtil{
        private Map<String, Object> mKeys = new HashMap<String, Object>();
        public static final String KEY_ALGORITHM = "SHA1WithRSA";
        private static final String PUBLIC_KEY = "RSAPublicKey";
        private static final String PRIVATE_KEY = "RSAPrivateKey";

        /**
         * 初始化密钥
         * @throws Exception
         */
        public void initKey() throws Exception{
            KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            generator.initialize(1024);
            KeyPair pair = generator.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey)pair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey)pair.getPrivate();
            mKeys.put(PUBLIC_KEY, publicKey);
            mKeys.put(PRIVATE_KEY, privateKey);
        }

        /**
         * 获取私钥字符串
         */
        public String getPrivateKey(Map<String, Object> map){
            Key key = (Key)map.get(PRIVATE_KEY);
            return new BASE64Encoder().encode(key.getEncoded());
        }

        /**
         * 获取公钥字符串
         */
        public String getPublicKey(Map<String, Object> map){
            Key key = (Key)map.get(PUBLIC_KEY);
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
}