package com.wangyin.ak47.common;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Byte-related helper class
 * 
 * Byte相关的所有方法，特别是编解码中必须用到
 * 
 * @author hannyu
 *
 */
public class ByteUtil {
    private static final String C64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="; //default base64.

    private static final char[] BASE16 = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'}, BASE64 = C64.toCharArray();

    private static final int MASK4 = 0x0f, MASK6 = 0x3f, MASK8 = 0xff;

    private static final Map<Integer, byte[]> DECODE_TABLE_MAP = new ConcurrentHashMap<Integer, byte[]>();

    
    /**
     * Merge a List of byte[] to one big byte[].
     * 
     * 合并多个byte[] list
     * 
     * @param bs                    a collection of byte[]
     * @return                      merged byte[], or byte[0]. Never return null.
     */
    public static byte[] merge(List<byte[]> bs){
        int len = 0;
        for(int i=0; i<bs.size(); i++){
            len += bs.get(i).length;
        }
        byte[] merged = new byte[len];
        int x = 0;
        for(int i=0; i<bs.size(); i++){
            byte[] b = bs.get(i);
            int blen = b.length;
            // copy(byte[] src, int srcPos, byte[] dest, int destPos, int length)
            copy(b, 0, merged, x, blen);
            x += blen;
        }
        
        return merged;
    }
    
    
    /**
     * Merge multiple byte[] to one big byte[].
     * 
     * 合并多个byte[] array
     * 
     * @param bs                    a collection of byte[]
     * @return                      merged byte[], or byte[0]. Never return null.
     */
    public static byte[] merge(byte[]... bs){
        int len = 0;
        for(int i=0; i<bs.length; i++){
            len += bs[i].length;
        }
        byte[] merged = new byte[len];
        int x = 0;
        for(int i=0; i<bs.length; i++){
            byte[] b = bs[i];
            int blen = b.length;
            // copy(byte[] src, int srcPos, byte[] dest, int destPos, int length)
            copy(b, 0, merged, x, blen);
            x += blen;
        }
        
        return merged;
    }
    
    /**
     * byte array copy.
     * 
     * @param src                   source of byte[]
     * @param length                length of bytes to be copied
     * @return                      copied byte[]
     */
    public static byte[] copyOf(byte[] src, int length)
    {
        return copyOf(src, 0, length);
    }
    
    /**
     * byte array copy.
     * 
     * @param src                   source of byte[]
     * @param offset                from where to start
     * @param length                length of bytes to be copied
     * @return                      copied byte[]
     */
    public static byte[] copyOf(byte[] src, int offset, int length){
        byte[] dest = new byte[length];
        System.arraycopy(src, offset, dest, 0, length);
        return dest;
    }
    
    /**
     * copy bytes from src to dest
     * 
     * @param src                   source
     * @param srcPos                source position
     * @param dest                  destination 
     * @param destPos               destination position
     * @param length                length
     */
    public static void copy(byte[] src, int srcPos, byte[] dest, int destPos, int length){
        System.arraycopy(src, srcPos, dest, destPos, length);
    }
    
    /**
     * copy bytes from src to dest
     * 
     * @param src                   source
     * @param dest                  destination
     * @param destPos               destination position
     */
    public static void copy(byte[] src, byte[] dest, int destPos){
        System.arraycopy(src, 0, dest, destPos, src.length);
    }
    
    
    
    /**
     * dest[destPos] = src;
     * 
     * @param src                   source
     * @param dest                  destination
     * @param destPos               destination position
     */
    public static void copy(byte src, byte[] dest, int destPos){
        dest[destPos] = src;
    }

    
    /**
     * to byte array.
     * 
     * @param v value.
     * @return byte[].
     */
    public static byte[] short2bytes(short v)
    {
        byte[] ret = { 0, 0 };
        short2bytes(v, ret);
        return ret;
    }

    /**
     * to byte array.
     * 
     * @param v value.
     * @param b byte array.
     */
    public static void short2bytes(short v, byte[] b)
    {
        short2bytes(v, b, 0);
    }

    /**
     * to byte array.
     * 
     * @param v value.
     * @param b byte array.
     * @param off cursor.
     */
    public static void short2bytes(short v, byte[] b, int off)
    {
        b[off + 1] = (byte) v;
        b[off + 0] = (byte) (v >>> 8);
    }

    /**
     * to byte array.
     * 
     * @param v value.
     * @return byte[].
     */
    public static byte[] int2bytes(int v)
    {
        byte[] ret = { 0, 0, 0, 0 };
        int2bytes(v, ret);
        return ret;
    }

    /**
     * to byte array.
     * 
     * @param v value.
     * @param b byte array.
     */
    public static void int2bytes(int v, byte[] b)
    {
        int2bytes(v, b, 0);
    }

    /**
     * to byte array.
     * 
     * @param v value.
     * @param b byte array.
     * @param off array offset.
     */
    public static void int2bytes(int v, byte[] b, int off)
    {
        b[off + 3] = (byte) v;
        b[off + 2] = (byte) (v >>> 8);
        b[off + 1] = (byte) (v >>> 16);
        b[off + 0] = (byte) (v >>> 24);
    }

    /**
     * to byte array.
     * 
     * @param v value.
     * @return byte[].
     */
    public static byte[] float2bytes(float v)
    {
        byte[] ret = { 0, 0, 0, 0 };
        float2bytes(v, ret);
        return ret;
    }

    /**
     * to byte array.
     * 
     * @param v value.
     * @param b byte array.
     */
    public static void float2bytes(float v, byte[] b)
    {
        float2bytes(v, b, 0);
    }

    /**
     * to byte array.
     * 
     * @param v value.
     * @param b byte array.
     * @param off array offset.
     */
    public static void float2bytes(float v, byte[] b, int off)
    {
        int i = Float.floatToIntBits(v);
        b[off + 3] = (byte) i;
        b[off + 2] = (byte) (i >>> 8);
        b[off + 1] = (byte) (i >>> 16);
        b[off + 0] = (byte) (i >>> 24);
    }

    /**
     * to byte array.
     * 
     * @param v value.
     * @return byte[].
     */
    public static byte[] long2bytes(long v)
    {
        byte[] ret = { 0, 0, 0, 0, 0, 0, 0, 0 };
        long2bytes(v, ret);
        return ret;
    }

    /**
     * to byte array.
     * 
     * @param v value.
     * @param b byte array.
     */
    public static void long2bytes(long v, byte[] b)
    {
        long2bytes(v, b, 0);
    }

    /**
     * to byte array.
     * 
     * @param v value.
     * @param b byte array.
     * @param off array offset.
     */
    public static void long2bytes(long v, byte[] b, int off)
    {
        b[off + 7] = (byte) v;
        b[off + 6] = (byte) (v >>> 8);
        b[off + 5] = (byte) (v >>> 16);
        b[off + 4] = (byte) (v >>> 24);
        b[off + 3] = (byte) (v >>> 32);
        b[off + 2] = (byte) (v >>> 40);
        b[off + 1] = (byte) (v >>> 48);
        b[off + 0] = (byte) (v >>> 56);
    }

    /**
     * to byte array.
     * 
     * @param v value.
     * @return byte[].
     */
    public static byte[] double2bytes(double v)
    {
        byte[] ret = { 0, 0, 0, 0, 0, 0, 0, 0 };
        double2bytes(v, ret);
        return ret;
    }

    /**
     * to byte array.
     * 
     * @param v value.
     * @param b byte array.
     */
    public static void double2bytes(double v, byte[] b)
    {
        double2bytes(v, b, 0);
    }

    /**
     * to byte array.
     * 
     * @param v value.
     * @param b byte array.
     * @param off array offset.
     */
    public static void double2bytes(double v, byte[] b, int off)
    {
        long j = Double.doubleToLongBits(v);
        b[off + 7] = (byte) j;
        b[off + 6] = (byte) (j >>> 8);
        b[off + 5] = (byte) (j >>> 16);
        b[off + 4] = (byte) (j >>> 24);
        b[off + 3] = (byte) (j >>> 32);
        b[off + 2] = (byte) (j >>> 40);
        b[off + 1] = (byte) (j >>> 48);
        b[off + 0] = (byte) (j >>> 56);
    }

    /**
     * to short.
     * 
     * @param b byte array.
     * @return short.
     */
    public static short bytes2short(byte[] b)
    {
        return bytes2short(b, 0);
    }

    /**
     * to short.
     * 
     * @param b byte array.
     * @param off offset.
     * @return short.
     */
    public static short bytes2short(byte[] b, int off)
    {
        return (short) (((b[off + 1] & 0xFF) << 0) +
            ((b[off + 0]) << 8));
    }

    /**
     * to int.
     * 
     * @param b byte array.
     * @return int.
     */
    public static int bytes2int(byte[] b)
    {
        return bytes2int(b, 0);
    }

    /**
     * to int.
     * 
     * @param b byte array.
     * @param off offset.
     * @return int.
     */
    public static int bytes2int(byte[] b, int off)
    {
        return ((b[off + 3] & 0xFF) << 0) +
           ((b[off + 2] & 0xFF) << 8) +
           ((b[off + 1] & 0xFF) << 16) +
           ((b[off + 0]) << 24);
    }

    /**
     * to int.
     * 
     * @param b byte array.
     * @return int.
     */
    public static float bytes2float(byte[] b)
    {
        return bytes2float(b, 0);
    }

    /**
     * to int.
     * 
     * @param b byte array.
     * @param off offset.
     * @return int.
     */
    public static float bytes2float(byte[] b, int off)
    {
        int i = ((b[off + 3] & 0xFF) << 0) +
            ((b[off + 2] & 0xFF) << 8) +
            ((b[off + 1] & 0xFF) << 16) +
            ((b[off + 0]) << 24);
        return Float.intBitsToFloat(i);
    }

    /**
     * to long.
     * 
     * @param b byte array.
     * @return long.
     */
    public static long bytes2long(byte[] b)
    {
        return bytes2long(b,0);
    }

    /**
     * to long.
     * 
     * @param b byte array.
     * @param off offset.
     * @return long.
     */
    public static long bytes2long(byte[] b,int off)
    {
        return ((b[off + 7] & 0xFFL) << 0) +
           ((b[off + 6] & 0xFFL) << 8) +
           ((b[off + 5] & 0xFFL) << 16) +
           ((b[off + 4] & 0xFFL) << 24) +
           ((b[off + 3] & 0xFFL) << 32) +
           ((b[off + 2] & 0xFFL) << 40) +
           ((b[off + 1] & 0xFFL) << 48) +
           (((long) b[off + 0]) << 56);
    }

    /**
     * to long.
     * 
     * @param b byte array.
     * @return double.
     */
    public static double bytes2double(byte[] b)
    {
        return bytes2double(b,0);
    }

    /**
     * to long.
     * 
     * @param b byte array.
     * @param off offset.
     * @return double.
     */
    public static double bytes2double(byte[] b, int off)
    {
        long j = ((b[off + 7] & 0xFFL) << 0) +
             ((b[off + 6] & 0xFFL) << 8) +
             ((b[off + 5] & 0xFFL) << 16) +
             ((b[off + 4] & 0xFFL) << 24) +
             ((b[off + 3] & 0xFFL) << 32) +
             ((b[off + 2] & 0xFFL) << 40) +
             ((b[off + 1] & 0xFFL) << 48) +
             (((long) b[off + 0]) << 56);
        return Double.longBitsToDouble(j);
    }

    
    /**
     * to String
     * 
     * @param b byte array
     * @param off offset
     * @param len length
     * @param encoding encoding
     * @return String
     * @throws UnsupportedEncodingException encoding wrong
     */
    public static String bytes2String(byte[] b, int off, int len, String encoding) 
            throws UnsupportedEncodingException{
        return new String(b, off, len, encoding);
    }
    
    /**
     * to String with UTF-8 encoding
     * 
     * @param b byte array
     * @param off offset
     * @param len length
     * @return String
     * @throws UnsupportedEncodingException encoding wrong
     */
    public static String bytes2String(byte[] b, int off, int len) 
            throws UnsupportedEncodingException{
        return bytes2String(b, off, len, Ak47Constants.DEFAULT_ENCODING);
    }
    
    /**
     * to String with UTF-8 encoding
     * 
     * @param b byte array
     * @return String
     * @throws UnsupportedEncodingException encoding wrong
     */
    public static String bytes2String(byte[] b) 
            throws UnsupportedEncodingException{
        return new String(b, Ak47Constants.DEFAULT_ENCODING);
    }
    
    /**
     * to String with specific encoding
     * 
     * @param b byte array
     * @param encoding encoding
     * @return String
     * @throws UnsupportedEncodingException encoding wrong
     */
    public static String bytes2String(byte[] b, String encoding) 
            throws UnsupportedEncodingException{
        return new String(b, encoding);
    }
    
    
    /**
     * from String to byte[] with UTF-8 encoding
     * 
     * @param str String
     * @return bytes
     * @throws UnsupportedEncodingException encoding wrong
     */
    public static byte[] string2Bytes(String str) 
            throws UnsupportedEncodingException{
        return string2Bytes(str, Ak47Constants.DEFAULT_ENCODING);
    }
    
    
    /**
     * from String to byte[] with specific encoding
     * 
     * @param str String
     * @param encoding encoding
     * @return bytes
     * @throws UnsupportedEncodingException encoding wrong
     */
    public static byte[] string2Bytes(String str, String encoding) 
            throws UnsupportedEncodingException{
        return str.getBytes(encoding);
    }
    
    /**
     * to hex string.
     * 
     * @param bs byte array.
     * @return hex string.
     */
    public static String bytes2hex(byte[] bs)
    {
        return bytes2hex(bs, 0, bs.length);
    }

    /**
     * to hex string.
     * 
     * @param bs byte array.
     * @param off offset.
     * @param len length.
     * @return hex string.
     */
    public static String bytes2hex(byte[] bs, int off, int len)
    {
        if( off < 0 )
            throw new IndexOutOfBoundsException("bytes2hex: offset < 0, offset is " + off );
        if( len < 0 )
            throw new IndexOutOfBoundsException("bytes2hex: length < 0, length is " + len );
        if( off + len > bs.length )
            throw new IndexOutOfBoundsException("bytes2hex: offset + length > array length.");

        byte b;
        int r = off, w = 0;
        char[] cs = new char[len*2];
        for(int i=0;i<len;i++)
        {
            b = bs[r++];
            cs[w++] = BASE16[ b >> 4 & MASK4 ];
            cs[w++] = BASE16[ b & MASK4 ];
        }
        return new String(cs);
    }

    /**
     * from hex string.
     * 
     * @param str hex string.
     * @return byte array.
     */
    public static byte[] hex2bytes(String str)
    {
        return hex2bytes(str, 0, str.length());
    }

    /**
     * from hex string.
     * 
     * @param str hex string.
     * @param off offset.
     * @param len length.
     * @return byte array.
     */
    public static byte[] hex2bytes(final String str, final int off, int len)
    {
        if( ( len & 1 ) == 1 )
            throw new IllegalArgumentException("hex2bytes: ( len & 1 ) == 1.");

        if( off < 0 )
            throw new IndexOutOfBoundsException("hex2bytes: offset < 0, offset is " + off );
        if( len < 0 )
            throw new IndexOutOfBoundsException("hex2bytes: length < 0, length is " + len );
        if( off + len > str.length() )
            throw new IndexOutOfBoundsException("hex2bytes: offset + length > array length.");

        int num = len / 2, r = off, w = 0;
        byte[] b = new byte[num];
        for(int i=0;i<num;i++) {
            b[w++] = (byte)( hex(str.charAt(r++)) << 4 | hex(str.charAt(r++)) );
        }
        return b;
    } 

    
    
    /**
     * to human-friendly string.
     * 
     * @param bs byte array.
     * @return hex string.
     */
    public static String bytes2human(byte[] bs)
    {
        return bytes2human(bs, 0, bs.length);
    }

    /**
     * to human-friendly string.
     * 
     * @param bs byte array.
     * @param off offset.
     * @param len length.
     * @return hex string.
     */
    public static String bytes2human(byte[] bs, int off, int len)
    {
        if( off < 0 )
            throw new IndexOutOfBoundsException("bytes2human: offset < 0, offset is " + off );
        if( len < 0 )
            throw new IndexOutOfBoundsException("bytes2human: length < 0, length is " + len );
        if( off + len > bs.length )
            throw new IndexOutOfBoundsException("bytes2human: offset + length > array length.");

        byte b;
        int r = off, w = 0;
        char[] cs = new char[len*4];
        for(int i=0;i<len;i++)
        {
            b = bs[r++];
            cs[w++] = '\\';
            cs[w++] = 'x';
            cs[w++] = BASE16[ b >> 4 & MASK4 ];
            cs[w++] = BASE16[ b & MASK4 ];
        }
        return new String(cs);
    }

    /**
     * from human-friendly string.
     * 
     * @param str hex string.
     * @return byte array.
     */
    public static byte[] human2bytes(String str)
    {
        return human2bytes(str, 0, str.length());
    }

    /**
     * from human string.
     * 
     * @param str hex string.
     * @param off offset.
     * @param len length.
     * @return byte array.
     */
    public static byte[] human2bytes(final String str, final int off, int len)
    {
        if( ( len & 1 ) == 1 )
            throw new IllegalArgumentException("human2bytes: ( len & 1 ) == 1.");

        if( off < 0 )
            throw new IndexOutOfBoundsException("human2bytes: offset < 0, offset is " + off );
        if( len < 0 )
            throw new IndexOutOfBoundsException("human2bytes: length < 0, length is " + len );
        if( off + len > str.length() )
            throw new IndexOutOfBoundsException("human2bytes: offset + length > array length.");

        int num = len / 4, r = off, w = 0;
        byte[] b = new byte[num];
        for(int i=0;i<num;i++) {
            r+=2;
            b[w++] = (byte)( hex(str.charAt(r++)) << 4 | hex(str.charAt(r++)) );
        }
        return b;
    } 
    
    
    
    /**
     * to base64 string.
     * 
     * @param b byte array.
     * @return base64 string.
     */
    public static String bytes2base64(byte[] b)
    {
        return bytes2base64(b, 0, b.length, BASE64);
    }

    /**
     * 
     * 
     * @param b byte array.
     * @return base64 string.
     */
    /**
     * to base64 string.
     * 
     * @param b byte array
     * @param offset offset
     * @param length length
     * @return base64 string
     */
    public static String bytes2base64(byte[] b, int offset, int length)
    {
        return bytes2base64(b, offset, length, BASE64);
    }

    /**
     * to base64 string.
     * 
     * @param b byte array.
     * @param code base64 code string(0-63 is base64 char,64 is pad char).
     * @return base64 string.
     */
    public static String bytes2base64(byte[] b, String code)
    {
        return bytes2base64(b, 0, b.length, code);
    }

    
    /**
     *  to base64 string.
     *  
     * @param b byte array
     * @param offset offset
     * @param length length
     * @param code base64 code string(0-63 is base64 char,64 is pad char)
     * @return base64 string
     */
    public static String bytes2base64(byte[] b, int offset, int length, String code)
    {
        if( code.length() < 64 )
            throw new IllegalArgumentException("Base64 code length < 64.");

        return bytes2base64(b, offset, length, code.toCharArray());
    }

    /**
     * to base64 string.
     * 
     * @param b byte array.
     * @param code base64 code(0-63 is base64 char,64 is pad char).
     * @return base64 string.
     */
    public static String bytes2base64(byte[] b, char[] code)
    {
        return bytes2base64(b, 0, b.length, code);
    }

    
    /**
     * to base64 string.
     * 
     * @param bs byte array.
     * @param off offset.
     * @param len length.
     * @param code base64 code(0-63 is base64 char,64 is pad char).
     * @return base64 string.
     */
    public static String bytes2base64(final byte[] bs, final int off, final int len, final char[] code)
    {
        if( off < 0 )
            throw new IndexOutOfBoundsException("bytes2base64: offset < 0, offset is " + off );
        if( len < 0 )
            throw new IndexOutOfBoundsException("bytes2base64: length < 0, length is " + len );
        if( off + len > bs.length )
            throw new IndexOutOfBoundsException("bytes2base64: offset + length > array length.");

        if( code.length < 64 )
            throw new IllegalArgumentException("Base64 code length < 64.");

        boolean pad = code.length > 64; // has pad char.
        int num = len / 3, rem = len % 3, r = off, w = 0;
        char[] cs = new char[ num * 4 + ( rem == 0 ? 0 : pad ? 4 : rem + 1 ) ];

        for(int i=0;i<num;i++)
        {
            int b1 = bs[r++] & MASK8, b2 = bs[r++] & MASK8, b3 = bs[r++] & MASK8;

            cs[w++] = code[ b1 >> 2 ];
            cs[w++] = code[ ( b1 << 4 ) & MASK6 | ( b2 >> 4 ) ];
            cs[w++] = code[ ( b2 << 2 ) & MASK6 | ( b3 >> 6 ) ];
            cs[w++] = code[ b3 & MASK6 ];
        }

        if( rem == 1 )
        {
            int b1 = bs[r++] & MASK8;
            cs[w++] = code[ b1 >> 2 ];
            cs[w++] = code[ ( b1 << 4 ) & MASK6 ];
            if( pad )
            {
                cs[w++] = code[64];
                cs[w++] = code[64];
            }
        }
        else if( rem == 2 )
        {
            int b1 = bs[r++] & MASK8, b2 = bs[r++] & MASK8;
            cs[w++] = code[ b1 >> 2 ];
            cs[w++] = code[ ( b1 << 4 ) & MASK6 | ( b2 >> 4 ) ];
            cs[w++] = code[ ( b2 << 2 ) & MASK6 ];
            if( pad )
                cs[w++] = code[64];
        }
        return new String(cs);
    }

    
    /**
     * from base64 string.
     * 
     * @param str base64 string.
     * @return byte array.
     */
    public static byte[] base642bytes(String str)
    {
        return base642bytes(str, 0, str.length());
    }

    
    /**
     * from base64 string.
     * 
     * @param str base64 string.
     * @param offset offset.
     * @param length length.
     * @return byte array.
     */
    public static byte[] base642bytes(String str, int offset, int length)
    {
        return base642bytes(str, offset, length, C64);
    }

    
    /**
     * from base64 string.
     * 
     * @param str base64 string.
     * @param code base64 code(0-63 is base64 char,64 is pad char).
     * @return byte array.
     */
    public static byte[] base642bytes(String str, String code)
    {
        return base642bytes(str, 0, str.length(), code);
    }

    
    /**
     * from base64 string.
     * 
     * @param str base64 string.
     * @param off offset.
     * @param len length.
     * @param code base64 code(0-63 is base64 char,64 is pad char).
     * @return byte array.
     */
    public static byte[] base642bytes(final String str, final int off, final int len, final String code)
    {
        if( off < 0 )
            throw new IndexOutOfBoundsException("base642bytes: offset < 0, offset is " + off );
        if( len < 0 )
            throw new IndexOutOfBoundsException("base642bytes: length < 0, length is " + len );
        if( off + len > str.length() )
            throw new IndexOutOfBoundsException("base642bytes: offset + length > string length.");

        if( code.length() < 64 )
            throw new IllegalArgumentException("Base64 code length < 64.");

        int rem = len % 4;
        if( rem == 1 )
            throw new IllegalArgumentException("base642bytes: base64 string length % 4 == 1.");

        int num = len / 4, size = num * 3;
        if( code.length() > 64 )
        {
            if( rem != 0 )
                throw new IllegalArgumentException("base642bytes: base64 string length error.");

            char pc = code.charAt(64);
            if( str.charAt(off+len-2) == pc )
            {
                size -= 2;
                --num;
                rem = 2;
            }
            else if( str.charAt(off+len-1) == pc )
            {
                size--;
                --num;
                rem = 3;
            }
        }
        else
        {
            if( rem == 2 )
                size++;
            else if( rem == 3 )
                size += 2;
        }

        int r = off, w = 0;
        byte[] b = new byte[size], t = decodeTable(code);
        for(int i=0;i<num;i++)
        {
            int c1 = t[str.charAt(r++)], c2 = t[str.charAt(r++)];
            int c3 = t[str.charAt(r++)], c4 = t[str.charAt(r++)];

            b[w++] = (byte)( ( c1 << 2 ) | ( c2 >> 4 ) );
            b[w++] = (byte)( ( c2 << 4 ) | ( c3 >> 2 ) );
            b[w++] = (byte)( ( c3 << 6 ) | c4 );
        }

        if( rem == 2 )
        {
            int c1 = t[str.charAt(r++)], c2 = t[str.charAt(r++)];

            b[w++] = (byte)( ( c1 << 2 ) | ( c2 >> 4 ) );
        }
        else if( rem == 3 )
        {
            int c1 = t[str.charAt(r++)], c2 = t[str.charAt(r++)], c3 = t[str.charAt(r++)];

            b[w++] = (byte)( ( c1 << 2 ) | ( c2 >> 4 ) );
            b[w++] = (byte)( ( c2 << 4 ) | ( c3 >> 2 ) );
        }
        return b;
    }

    
    /**
     * from base64 string.
     * 
     * @param str base64 string.
     * @param code base64 code(0-63 is base64 char,64 is pad char).
     * @return byte array.
     */
    public static byte[] base642bytes(String str, char[] code)
    {
        return base642bytes(str, 0, str.length(), code);
    }

    
    /**
     * from base64 string.
     * 
     * @param str base64 string.
     * @param off offset.
     * @param len length.
     * @param code base64 code(0-63 is base64 char,64 is pad char).
     * @return byte array.
     */
    public static byte[] base642bytes(final String str, final int off, final int len, final char[] code)
    {
        if( off < 0 )
            throw new IndexOutOfBoundsException("base642bytes: offset < 0, offset is " + off );
        if( len < 0 )
            throw new IndexOutOfBoundsException("base642bytes: length < 0, length is " + len );
        if( off + len > str.length() )
            throw new IndexOutOfBoundsException("base642bytes: offset + length > string length.");

        if( code.length < 64 )
            throw new IllegalArgumentException("Base64 code length < 64.");

        int rem = len % 4;
        if( rem == 1 )
            throw new IllegalArgumentException("base642bytes: base64 string length % 4 == 1.");

        int num = len / 4, size = num * 3;
        if( code.length > 64 )
        {
            if( rem != 0 )
                throw new IllegalArgumentException("base642bytes: base64 string length error.");

            char pc = code[64];
            if( str.charAt(off+len-2) == pc )
                size -= 2;
            else if( str.charAt(off+len-1) == pc )
                size--;
        }
        else
        {
            if( rem == 2 )
                size++;
            else if( rem == 3 )
                size += 2;
        }

        int r = off, w = 0;
        byte[] b = new byte[size];
        for(int i=0;i<num;i++)
        {
            int c1 = indexOf(code, str.charAt(r++)), c2 = indexOf(code, str.charAt(r++));
            int c3 = indexOf(code, str.charAt(r++)), c4 = indexOf(code, str.charAt(r++));

            b[w++] = (byte)( ( c1 << 2 ) | ( c2 >> 4 ) );
            b[w++] = (byte)( ( c2 << 4 ) | ( c3 >> 2 ) );
            b[w++] = (byte)( ( c3 << 6 ) | c4 );
        }

        if( rem == 2 )
        {
            int c1 = indexOf(code, str.charAt(r++)), c2 = indexOf(code, str.charAt(r++));

            b[w++] = (byte)( ( c1 << 2 ) | ( c2 >> 4 ) );
        }
        else if( rem == 3 )
        {
            int c1 = indexOf(code, str.charAt(r++)), c2 = indexOf(code, str.charAt(r++)), c3 = indexOf(code, str.charAt(r++));

            b[w++] = (byte)( ( c1 << 2 ) | ( c2 >> 4 ) );
            b[w++] = (byte)( ( c2 << 4 ) | ( c3 >> 2 ) );
        }
        return b;
    }


    private static byte hex(char c)
    {
        if( c <= '9' ) return (byte)( c - '0' );
        if( c >= 'a' && c <= 'f' ) return (byte)( c - 'a' + 10 );
        if( c >= 'A' && c <= 'F' ) return (byte)( c - 'A' + 10 );
        throw new IllegalArgumentException("hex string format error [" + c + "].");
    }

    
    private static int indexOf(char[] cs, char c)
    {
        for(int i=0,len=cs.length;i<len;i++)
            if( cs[i] == c ) return i;
        return -1;
    }

    
    private static byte[] decodeTable(String code)
    {
        int hash = code.hashCode();
        byte[] ret = DECODE_TABLE_MAP.get(hash);
        if( ret == null )
        {
            if( code.length() < 64 )
                throw new IllegalArgumentException("Base64 code length < 64.");
            // create new decode table.
            ret = new byte[128];
            for(int i=0;i<128;i++) // init table.
                ret[i] = -1;
            for(int i=0;i<64;i++)
                ret[code.charAt(i)] = (byte)i;
            DECODE_TABLE_MAP.put(hash, ret);
        }
        return ret;
    }


    
    private ByteUtil(){}
}