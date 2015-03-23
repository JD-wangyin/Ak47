package com.wangyin.ak47.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Compress-related helper class
 *
 * 压缩相关操作
 * 
 * @author wyhubingyin
 * 
 */
public class CompressUtil {

    public static final int READ_BUFFER_SIZE = 1024 * 8;

    /**
     * GZIP compression
     * 
     * GZIP压缩
     * 
     * @param content                       source bytes to be compressed
     * @return                              compressed bytes 
     * @throws IOException                  i/o wrong
     */
    public static byte[] compressGzip(byte[] content) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gos = new GZIPOutputStream(bos);
        try {
            gos.write(content);
            gos.flush();
        } finally {
            gos.close();
        }

        return bos.toByteArray();
    }

    /**
     * GZIP decompression
     * 
     * GZIP解压
     * 
     * @param compressed                    sources bytes to be decompressed
     * @return                              decompressed bytes
     * @throws IOException                  i/o wrong
     */
    public static byte[] decompressGzip(byte[] compressed) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(bis);
        try {
            byte[] buffer = new byte[READ_BUFFER_SIZE];
            int read = 0;
            while ((read = gis.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }
        } finally {
            gis.close();
        }

        return bos.toByteArray();
    }

    
    
    
}
