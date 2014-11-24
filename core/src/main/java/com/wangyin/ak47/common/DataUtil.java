package com.wangyin.ak47.common;

import java.io.FileNotFoundException;
import java.io.IOException;




/**
 * Data-related helper class
 * 
 * This 'Data' means files in {ak47-project}/data/ .
 * Such as 'qinglong.res' means '{ak47-project}/data/qinglong.res'.
 * 
 * 数据文件辅助类
 * 
 * data这里特指 ak47/data/ 下的文件
 * 
 * @author hannyu
 *
 */
public class DataUtil {
    

    /**
     * Read data to a single string, and cached it in mem.
     * 
     * Check the 'lastModified' time of data file whenever call, 
     * if changed, re-read it from disk, otherwise return the cached copy in mem.
     * 
     * 读取data并缓存到内存中，每次读取检查lastModifed时间，若有修改则重新读取，若无修改则直接返回缓存中的数据。
     * 
     * @param dataName
     * @return
     * @throws IOException
     */
    public static String readStringCached(String dataName) throws IOException {
        String fileName = Ak47Env.AK47_HOME_DATA_DIR + dataName;
        return FileUtil.readStringCached(fileName);
    }
    

    /**
     * Compare the create time of copy in mem with the 'lastModified' time of data file.
     * 
     * 对比缓存与文件的修改时间，判断是否发生改变。
     * 
     * @param dataName
     * @return
     */
    public static boolean isCachedChanged(String dataName) {
        String fileName = Ak47Env.AK47_HOME_DATA_DIR + dataName;
        return FileUtil.isCachedChanged(fileName);
    }
    
    
    /**
     * Read all the data to a single string, using UTF-8 encoding.
     * 
     * 将data全部读入为字符串，默认编码UTF-8
     * 
     * dataName: 
     *      qinglong.res ===> ak47/data/qinglong.res
     * 
     * @param fileName      文件名
     * @return
     * @throws IOException
     */
    public static String readString(String dataName) throws IOException {
        return readString(dataName, Ak47Constants.DEFAULT_ENCODING);
    }
    
    
    /**
     * Read all the data to a single string, using given encoding.
     * 
     * 将data全部读入为字符串，需指定编码
     * 
     * dataName: qinglong.res ==> ak47/data/qinglong.res
     * 
     * @param filename      文件路径
     * @param encoding      必须指定编码
     * @return
     * @throws IOException
     */
    public static String readString(String dataName, String encoding) throws IOException{
        String fileName = Ak47Env.AK47_HOME_DATA_DIR + dataName;
        return FileUtil.readString(fileName, encoding);
    }

    
    /**
     * Read all the data to a byte array. 
     * 
     * 将data全部读入为字符串
     * 若文件不存在或内容为空，则返回 数组长度为0，注意不是null。
     * 
     * @param filename
     * @return
     * @throws FileNotFoundException
     */
    public static byte[] readBytes(String dataName) throws IOException{
        String fileName = Ak47Env.AK47_HOME_DATA_DIR + dataName;
        return FileUtil.readByteArray(fileName);
    }
    

    /**
     * Writes the a to a data file, append or not.
     * 
     * 写入字符串到data文件，默认编码UTF-8。
     * 
     * @param dataName
     * @param append
     * @param content
     * @throws IOException
     */
    public static void writeString(String dataName, boolean append, String content) throws IOException {
        String fileName = Ak47Env.AK47_HOME_DATA_DIR + dataName;
        FileUtil.writeString(fileName, false, content);
    }
    
    
    /**
     * Writes a byte array to a data file.
     * 
     * 写入str到data文件，并更新cache，如果有的话。默认编码UTF-8。
     * 
     * @param dataName
     * @param append
     * @param str
     * @throws IOException
     */
    public static void writeBytes(String dataName, boolean append, byte[] bytes) throws IOException {
        String fileName = Ak47Env.AK47_HOME_DATA_DIR + dataName;
        FileUtil.writeBytes(fileName, append, bytes);
    }

   
    
    
}
