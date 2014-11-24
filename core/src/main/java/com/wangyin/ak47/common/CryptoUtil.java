package com.wangyin.ak47.common;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Crypto-related helper class
 * 
 * WARNING: All methods are thread-safe!!!
 * 
 * 加解密工具类
 * 
 * 注： 所有方法均是线程安全的！！
 * 
 * @author hannyu
 *
 */
public class CryptoUtil {
    private static final Logger log = new Logger(CryptoUtil.class);
    
    // thread local cipher map
    private static final ThreadLocal<Map<String,Cipher>> localCipherMap = 
            new ThreadLocal<Map<String,Cipher>>();
    
    
    /**
     * DES encryption
     * 
     * Example：
     *      encryptDes(inputBytes, "12345678", "DES/CBC/PKCS5Padding")
     *      
     * DES加密
     * 
     * @param input
     * @param key
     * @param transformation
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeySpecException
     * @throws InvalidAlgorithmParameterException 
     */
    public static byte[] encryptDes(byte[] input, String key, String transformation) 
            throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, 
            NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, 
            InvalidKeySpecException, InvalidAlgorithmParameterException{
        
        return doDes(input, key, transformation, Cipher.ENCRYPT_MODE);
    }
    
    /**
     * DES decryption
     * 
     * Example：
     *      decryptDes(inputBytes, "12345678", "DES/CBC/PKCS5Padding")
     *  
     * DES解密
     * 
     * 注： 所有方法均是线程安全的！！
     * 
     * @param input
     * @param key
     * @param transformation
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeySpecException
     * @throws InvalidAlgorithmParameterException 
     */
    public static byte[] decryptDes(byte[] input, String key, String transformation) 
            throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, 
            NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, 
            InvalidKeySpecException, InvalidAlgorithmParameterException{
        
        return doDes(input, key, transformation, Cipher.DECRYPT_MODE);
    }
    
    /**
     * 处理DES加解密
     * 
     * @param input
     * @param key
     * @param transformation
     * @param mode
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeySpecException
     * @throws InvalidAlgorithmParameterException 
     */
    private static byte[] doDes(byte[] input, String key, String transformation, int mode) 
            throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, 
            NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, 
            InvalidKeySpecException, InvalidAlgorithmParameterException{
        
        if( null == key || null == transformation){
            log.warn("key and transformation should NOT be null.");
            return null;
        }
        if( null == input || 0 == input.length ){
            return input;
        }
        
        String ck = "unkown";
        if( mode == Cipher.DECRYPT_MODE ){
            ck = "dec_" + key + "_" + transformation;
        }else{
            ck = "enc_" + key + "_" + transformation;
        }
        
        Map<String, Cipher> cipherMap = localCipherMap.get();
        if( null == cipherMap ){
            cipherMap = new HashMap<String, Cipher>();
            localCipherMap.set(cipherMap);
        }
        Cipher cipher = cipherMap.get(ck);
        if( null == cipher ){
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            cipher = Cipher.getInstance(transformation);
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            cipher.init(mode, secretKey, iv);
            cipherMap.put(ck, cipher);
        }
        return cipher.doFinal(input);
    }

}
