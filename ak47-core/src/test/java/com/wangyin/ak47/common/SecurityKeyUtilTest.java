package com.wangyin.ak47.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.junit.Assert;
import org.junit.Test;

import com.wangyin.ak47.common.SecurityKeyUtil;

public class SecurityKeyUtilTest {
    
    @Test
    public void testme() throws UnrecoverableKeyException, KeyStoreException, 
    NoSuchAlgorithmException, CertificateException, IOException{
        
        Key publickey = SecurityKeyUtil.getAk47PublicKeyCached();
        Assert.assertNotNull(publickey);
        
        PrivateKey privatekey = SecurityKeyUtil.getAk47PrivateKeyCached();
        Assert.assertNotNull(privatekey);
        
    }
    
    public static void main(String[] args) throws FileNotFoundException, 
            CertificateException {

        System.out.println(SecurityKeyUtil.AK47_PUBLIC_KEY_FILE);
        System.out.println(SecurityKeyUtil.AK47_PRIVATE_KEY_FILE);
        System.out.println(SecurityKeyUtil.class.getResource(""));
        System.out.println(SecurityKeyUtil.class.getClassLoader().getResource(""));
        
        Key publickey = SecurityKeyUtil.getAk47PublicKeyCached();
        
        System.out.println("publickey.getFormat: " + publickey.getFormat());
        
    }

}
