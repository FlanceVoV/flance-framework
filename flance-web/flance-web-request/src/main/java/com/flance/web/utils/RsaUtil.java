package com.flance.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * RSA工具类
 * @author jhf
 */
public class RsaUtil {

    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";

    /** 算法名称 */
    private final static String ALGORITHM =  "RSA";

    /** RSA签名算法 */
    private final static String RSA_SIGNATURE_ALGORITHM = "SHA256WithRSA";

    /** 默认密钥大小 */
    private final static int KEY_SIZE = 2048;

    /** 最大解密长度 */
    private final static int MAX_DECRYPT_BLOCK = 256;

    /** Base64 编码/解码器 JDK1.8 */
    private final static Base64.Decoder DECODER = Base64.getDecoder();

    private final static Base64.Encoder ENCODER = Base64.getEncoder();

    /** 私有构造器 */
    private RsaUtil(){}


    /**
     * 生成密钥对
     * 将密钥分别用Base64编码保存到#publicKey.properties#和#privateKey.properties#文件中
     * 保存的默认名称分别为publicKey和privateKey
     */
    public static synchronized void generateKeyPair() throws Exception {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator
                .getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, byte[]> keyMap = new HashMap<String, byte[]>();
        keyMap.put(PUBLIC_KEY, publicKey.getEncoded());
        keyMap.put(PRIVATE_KEY, privateKey.getEncoded());
        System.out.println("public:"+new String(ENCODER.encode(publicKey.getEncoded())));
        System.out.println("private:"+new String(ENCODER.encode(privateKey.getEncoded())));

    }

    /**
     * 将指定的密钥字符串保存到文件中,如果找不到文件，就创建
     * @param keyString 密钥的Base64编码字符串（值）
     * @param keyName  保存在文件中的名称（键）
     * @param properties 目标文件
     */
    private static void storeKey(String keyString, String keyName, Properties properties, String fileName){
        try {
            Resource res  = new ClassPathResource(fileName);
            FileOutputStream oFile = new FileOutputStream(res.getFile(), false);
            properties.setProperty(keyName, keyString);
            properties.store(oFile, keyName);
            oFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 获取密钥字符串
     * @param keyName 需要获取的密钥名
     * @param properties 密钥文件
     * @return Base64编码的密钥字符串
     */
    public static String getKeyString(String keyName,Properties properties){
        return properties.getProperty(keyName);
    }

    /**
     * 从文件获取RSA公钥
     * @return RSA公钥
     * @throws InvalidKeySpecException
     */
    public static RSAPublicKey getPublicKey(String pubKey){
        try {
            byte[] keyBytes = DECODER.decode(pubKey);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return (RSAPublicKey)keyFactory.generatePublic(x509EncodedKeySpec);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从文件获取RSA私钥
     * @return RSA私钥
     * @throws InvalidKeySpecException
     */
    public static RSAPrivateKey getPrivateKey(String priKey){
        try {
            byte[] keyBytes = DECODER.decode(priKey);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return (RSAPrivateKey)keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * RSA公钥加密
     */
    public static byte[] encryptByPublicKey(byte[] data, String pubKey) throws Exception {
        // 对数据加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(pubKey));
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > 117) {
                cache = cipher.doFinal(data, offSet, 117);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * 117;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /** 私钥解密 */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String priKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(getPrivateKey(priKey).getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * RSA公钥验签
     *
     * @param data
     *            待签名字符串
     *            公钥（Base64编码）
     * @return 验签结果
     * @throws Exception
     */
    public static boolean verify(byte[] data, String sign, String pubKey) throws Exception {
        Signature signature = Signature.getInstance(RSA_SIGNATURE_ALGORITHM);
        signature.initVerify(getPublicKey(pubKey));
        signature.update(data);
        return signature.verify(Base64Utils.decodeFromUrlSafeString(sign));
    }

    /**
     * RSA私钥签名：签名方式SHA1withRSA
     *
     * @param data
     *            待签名byte[]
     *            私钥（Base64编码）
     * @return 签名byte[]
     * @throws Exception
     */
    public static byte[] sign(byte[] data, String priKey) throws Exception {
        // Sign
        Signature signature = Signature.getInstance(RSA_SIGNATURE_ALGORITHM);
        signature.initSign(getPrivateKey(priKey));
        signature.update(data);
        return signature.sign();
    }

    public static void main(String[] args) throws Exception {
        generateKeyPair();
    }

}
