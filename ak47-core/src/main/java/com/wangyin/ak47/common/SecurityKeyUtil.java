package com.wangyin.ak47.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;



/**
 * Certification file-reading related helper class, support X.509, PKCS12, JKS.
 * 
 * 负责X.509、PKCS12、JKS等证书的公私钥读取
 * 
 * @author hannyu
 *
 */
public class SecurityKeyUtil {
    
    private static final Logger log = new Logger(SecurityKeyUtil.class);
    
    //公钥编号: pubKeyId = custPubKey_id
    //私钥编号: priKeyId = bankPriKey_id

    
    /**
     * Read the public key from PKCS12 file.
     * 
     * 从文件中读取PKCS12公钥证书
     * 
     * @param fileName  文件路径
     * @param keyId     一个文件里面可能有多个证书，每个证书都有id，如果取第一个则设为null即可
     * @param pass      证书密码
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     * @throws UnrecoverableKeyException
     */
    public static PublicKey readPublicPKCS12KeyFromFile(String fileName, String keyId, String pass) 
            throws KeyStoreException, NoSuchAlgorithmException, 
            CertificateException, IOException, UnrecoverableKeyException {
        
        InputStream is = new FileInputStream(fileName);
        return readPublicPKCS12KeyFromFile(is, keyId, pass);
    }
    
    /**
     * Read the public key from PKCS12 file.
     * 
     * 从文件中读取PKCS12公钥证书
     * 
     * @param is
     * @param keyId
     * @param pass
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     * @throws UnrecoverableKeyException
     */
    public static PublicKey readPublicPKCS12KeyFromFile(InputStream is, String keyId, String pass) 
            throws KeyStoreException, NoSuchAlgorithmException, 
            CertificateException, IOException, UnrecoverableKeyException {
        if( null == is ){
            throw new IllegalArgumentException("InputStream is null.");
        }
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        char[] a = pass.toCharArray();
        keystore.load(is, a);
        PublicKey key = ((Certificate) keystore.getCertificate(keyId)).getPublicKey();
        return key;
    }
    
    
    
    /**
     * Read the private key from PKCS12 file.
     * 
     * 从文件中读取PKCS12私钥证书
     * 
     * @param fileName
     * @param keyId
     * @param pass
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     * @throws UnrecoverableKeyException
     */
    public static PrivateKey readPrivatePKCS12KeyFromFile(String fileName, String keyId, String pass) 
            throws KeyStoreException, NoSuchAlgorithmException, 
            CertificateException, IOException, UnrecoverableKeyException {
        
        InputStream is = new FileInputStream(fileName);
        return readPrivatePKCS12KeyFromFile(is, keyId, pass);
    }
    
    /**
     * Read the private key from PKCS12 file.
     * 
     * 从文件中读取PKCS12私钥证书
     * 
     * @param is
     * @param keyId
     * @param pass
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     * @throws UnrecoverableKeyException
     */
    public static PrivateKey readPrivatePKCS12KeyFromFile(InputStream is, String keyId, String pass)
            throws KeyStoreException, NoSuchAlgorithmException, 
            CertificateException, IOException, UnrecoverableKeyException {
        if( null == is ){
            throw new IllegalArgumentException("InputStream is null.");
        }
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        char[] a = pass.toCharArray();
        keystore.load(is, a);
        String sKeyId = keyId;
        Enumeration<String> enumeration = keystore.aliases();
        if( keyId == null || keyId.trim().length() == 0 ) {
            int i = 0;
            for ( ; enumeration.hasMoreElements(); i++) {
                sKeyId = enumeration.nextElement().toString();
            }
            if (i > 1) {
                log.warn("Key file contains {} keys, only the last key works.", i);
            }
        }
        PrivateKey key = (PrivateKey) keystore.getKey(sKeyId, a);
        return key;
    }
    
    
    /**
     * Read the key from X509 file.
     * 
     * 从文件中读取X.509证书
     * 
     * @param fileName
     * @return
     * @throws FileNotFoundException
     * @throws CertificateException
     */
    public static Key readX509KeyFromFile(String fileName) 
            throws FileNotFoundException, CertificateException {
        
        InputStream is = new FileInputStream(fileName);
        return readX509KeyFromFile(is);
    }
    
    /**
     * Read the key from X509 file.
     * 
     * 从文件中读取X.509证书
     * 
     * @param is
     * @return
     * @throws FileNotFoundException
     * @throws CertificateException
     */
    public static Key readX509KeyFromFile(InputStream is) 
            throws FileNotFoundException, CertificateException {
        if( null == is ){
            throw new IllegalArgumentException("InputStream is null.");
        }
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate certificate = cf.generateCertificate(is);
        
        return (PublicKey) (certificate != null ? certificate.getPublicKey() : null);
    }

    
    /**
     * Read the key from JKS file.
     * 
     * 从文件中读取JKS证书
     * 
     * @param fileName
     * @param keyId
     * @param pass
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     * @throws UnrecoverableKeyException
     */
    public static Key readJKSKeyFromFile(String fileName, String keyId, String pass) 
            throws KeyStoreException, NoSuchAlgorithmException, 
            CertificateException, IOException, UnrecoverableKeyException {
        
        InputStream is = new FileInputStream(fileName);
        return readJKSKeyFromFile(is, keyId, pass);
    }
    
    
    /**
     * Read the key from JKS file.
     * 
     * 从文件中读取JKS证书
     * 
     * @param is
     * @param keyId
     * @param pass
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     * @throws UnrecoverableKeyException
     */
    public static Key readJKSKeyFromFile(InputStream is, String keyId, String pass) 
            throws KeyStoreException, NoSuchAlgorithmException, 
            CertificateException, IOException, UnrecoverableKeyException {
        if( null == is ){
            throw new IllegalArgumentException("InputStream is null.");
        }
        KeyStore keystore = KeyStore.getInstance("JKS");
        char[] a = pass.toCharArray();
        keystore.load(is, a);
        Key key = keystore.getKey(keyId, a);
        return key;
    }
    
    
    
    
    // ============ Ak47自己的公钥和私钥 ==============
    
    // read key file in jar or not
    public static final String AK47_PRIVATE_KEY_FILE = "ak47_private_123123.pfx";
    public static final String AK47_PRIVATE_KEY_PASSWORD = "123123";
    public static final String AK47_PUBLIC_KEY_FILE = "ak47_public.cer";
    
    private static PrivateKey privateKey;
    private static Key publicKey;
    
    /**
     * Read the ak47-default-private-key from PKCS12 file, and cached it in mem.
     * 
     * 读取AK47自己提供的默认私钥，只读一次。后面的将会缓存在内存中。
     * 
     * @return
     * @throws UnrecoverableKeyException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     */
    public static PrivateKey getAk47PrivateKeyCached() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException{
        if (privateKey == null) {
            InputStream is = SecurityKeyUtil.class.getClassLoader().getResourceAsStream(AK47_PRIVATE_KEY_FILE);
            privateKey = SecurityKeyUtil.readPrivatePKCS12KeyFromFile(is, null, AK47_PRIVATE_KEY_PASSWORD);
        }
        return privateKey;
    }
    
    /**
     * Read the ak47-default-public-key from PKCS12 file, and cached it in mem.
     * 
     * 读取AK47自己提供的默认公钥，只读一次。后面的将会缓存在内存中。
     * 
     * @return
     * @throws CertificateException 
     * @throws FileNotFoundException 
     */
    public static Key getAk47PublicKeyCached() throws FileNotFoundException, CertificateException{
        if (publicKey == null) {
            InputStream is = SecurityKeyUtil.class.getClassLoader().getResourceAsStream(AK47_PUBLIC_KEY_FILE);
            publicKey = SecurityKeyUtil.readX509KeyFromFile(is);
        }
        return publicKey;
    }
    
}
