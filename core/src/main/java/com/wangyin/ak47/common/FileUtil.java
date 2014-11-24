package com.wangyin.ak47.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




/**
 * File-related helper class
 * 
 * 文件操作工具类
 * 
 * @author hannyu
 *
 */
public class FileUtil {
    
    private static final Logger log = new Logger(FileUtil.class);
    
    public static final int READ_BUFFER_SIZE = 1024*8;
    
    public static final int MAX_FILE_SIZE = 1024*1024*50; // max 50M 
    
    // cachedFileMap cache file content
    // key : fileName (String)
    // value:
    //      list[0] : lastTs (Long)
    //      list[1] : data (byte[])
    private static Map<String, List<Object>> cachedFileMap = new HashMap<String, List<Object>>();
    
    
    
    /**
     * Read file to a single string, and cached it in mem, using UTF-8 encoding.
     * 
     * Check the 'lastModified' time of data file whenever call, 
     * if changed, re-read it from disk, otherwise return the cached copy in mem.
     * 
     * 读取文件并缓存到内存中，每次读取检查lastModifed时间，若有修改则重新读取，若无修改则直接返回缓存中的数据。
     * 
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String readStringCached(String filePath) throws IOException {
        return readStringCached(filePath, Ak47Constants.DEFAULT_ENCODING);
    }
    
    /**
     * Read file to a single string, and cached it in mem, using given encoding.
     * 
     * Check the 'lastModified' time of data file whenever call, 
     * if changed, re-read it from disk, otherwise return the cached copy in mem.
     * 
     * 读取文件并缓存到内存中，每次读取检查lastModifed时间，若有修改则重新读取，若无修改则直接返回缓存中的数据。
     * 
     * @param filePath
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String readStringCached(String filePath, String encoding) throws IOException {
        return new String(readBytesCached(filePath), encoding);
    }
    
    /**
     * Read file to a byte array, and cached it in mem, using UTF-8 encoding.
     * 
     * Check the 'lastModified' time of data file whenever call, 
     * if changed, re-read it from disk, otherwise return the cached copy in mem.
     * 
     * 读取文件并缓存到内存中，每次读取检查lastModifed时间，若有修改则重新读取，若无修改则直接返回缓存中的数据。
     * 
     * 
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] readBytesCached(String filePath) throws IOException {
        File file = new File(filePath);
        long nowTs = file.lastModified();
        if( 0 == nowTs ){
            log.warn("{} NOT exist.", filePath);
            return new byte[0];
        }
        
        List<Object> fileInfo = cachedFileMap.get(filePath);
        long lastTs = (null == fileInfo) ? 0L : (Long)fileInfo.get(0);
        if( nowTs > lastTs ){
            log.debug("'{}' NOT cached, really read.", filePath);
            byte[] bytes = readByteArray(file);
            fileInfo = new ArrayList<Object>(2);
            fileInfo.add(Long.valueOf(nowTs));
            fileInfo.add(bytes);
            cachedFileMap.put(filePath, fileInfo);
            return bytes;
        }else{
            log.debug("read '{}' from cached.", filePath);
            return (byte[]) fileInfo.get(1);
        }
        
    }
    
    /**
     * Compare the create time of copy in mem with the 'lastModified' time of the file.
     * 
     * 检查fileName的内容相对于缓存是否改变，通过check lastModifed 来判断。
     * 
     * @param filePath
     * @return
     */
    public static boolean isCachedChanged(String filePath) {
        File file = new File(filePath);
        long nowTs = file.lastModified();
        
        List<Object> fileInfo = cachedFileMap.get(filePath);
        long lastTs = (null == fileInfo) ? 0L : (Long)fileInfo.get(0);
        
        return nowTs > lastTs;
    }
    
    
    
    /**
     * Read all the file content to a single string, using UTF-8 encoding.
     * 
     * 将文件全部读入为字符串，默认编码UTF-8
     * 
     * @param filePath      文件名
     * @return
     * @throws IOException
     */
    public static String readString(String filePath) throws IOException {
        return readString(filePath, Ak47Constants.DEFAULT_ENCODING);
    }
    
    /**
     * Read all the file content to a single string, using given encoding.
     * 
     * 将文件全部读入为字符串，需指定编码
     * 
     * @param filePath      文件路径
     * @param encoding      必须指定编码
     * @return
     * @throws IOException
     */
    public static String readString(String filePath, String encoding) throws IOException{
        byte[] bytes = readByteArray(filePath);
        return new String(bytes, encoding);
    }
    
    /**
     * Read all the file content to a single string, using given encoding.
     * 
     * 将文件全部读入为字符串，需指定编码
     * 
     * @param file			文件
     * @param encoding		必须指定编码
     * @return
     * @throws IOException
     */
    public static String readString(File file, String encoding) throws IOException{
        byte[] bytes = readByteArray(file);
        return new String(bytes, encoding);
    }
    
    
    /**
     * Read all the file content to a byte array. 
     * 
     * 将文件全部读入为字节数组
     * 若文件不存在或内容为空，则返回 数组长度为0，注意不是null。
     * 
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static byte[] readByteArray(String filePath) throws IOException{
        File file = new File(filePath);
        return readByteArray(file);
    }
    
    
    /**
     * Read all the file content to a byte array. 
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] readByteArray(File file) throws IOException{ 
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if( file.exists() ){
            if( file.length() > MAX_FILE_SIZE ){
                throw new IOException(file.getName() + " is too large.");
            }
            FileInputStream fis = new FileInputStream(file);
            try{
                byte[] buffer = new byte[READ_BUFFER_SIZE];
                int read = 0;
                while ( (read = fis.read(buffer)) != -1 ) {
                    bos.write(buffer, 0, read);
                }
            }finally{
                fis.close();
            }
            
        }else{
            log.warn("{} NOT exist.", file.getName());
        }
        
        return bos.toByteArray();
    }

    
   
    
    
    /**
     * Writes the a string to a file, append or not.
     * 
     * 将字符串写入文件，默认编码UTF-8
     * 
     * @param filePath          文件名
     * @param append            是否追加
     * @param content           写入内容
     * @throws IOException
     */
    public static void writeString(String filePath, boolean append, String content)
            throws IOException{
        writeString(filePath, append, content, "UTF-8");
    }
    
    /**
     * Writes the a string to a file, append or not.
     * 
     * 将字符串写入文件，需指定编码
     * 
     * @param filePath          文件名
     * @param append            是否追加
     * @param content           写入内容
     * @param encoding          编码
     * @throws IOException
     */
    public static void writeString(String filePath, boolean append, String content, String encoding)
            throws IOException{
        writeBytes(filePath, append, content.getBytes(encoding));
    }
    
    /**
     * Writes a byte array to a file.
     * 
     * 将字节数组写入文件
     * 
     * @param filePath          文件名
     * @param append            是否追加
     * @param content           写入内容
     * @throws IOException 
     */
    public static void writeBytes(String filePath, boolean append, byte[] content) 
            throws IOException{
        FileOutputStream fos = new FileOutputStream(filePath, append);
        try{
            fos.write(content);
            fos.flush();
        }finally{
            fos.close();
        }
        
    }



}
