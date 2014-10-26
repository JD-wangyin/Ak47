package com.wangyin.ak47.core.record;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.yaml.snakeyaml.Yaml;

import com.wangyin.ak47.common.Logger;
import com.wangyin.ak47.core.exception.Ak47RuntimeException;


/**
 * Record池
 * 1、负责Record的持久化，提供dump和load
 * 2、按顺序、随机等方式获取Record
 * 3、
 * 
 * @author wyhanyu
 *
 */
public class RecordPool<Q, R> {
    private static final Logger log = new Logger(RecordPool.class);
    
    // 队列最大容量（不能无限制，否则内存太大）
    private static final int MAX_POOL_SIZE = 100;
    private static final String RECORD_BEGIN_LINE = "====== %03d ======";
    private static final String RECORD_END_LINE = "====== END ======";
    
    
    private String ymlPath;
    private Yaml yaml;
    
    private List<Record<Q, R>> records;
    
    private int total;
    private int dumped;
    
    public RecordPool(String ymlPath){
        this.ymlPath = ymlPath;
        yaml = new Yaml();
        
        records = new ArrayList<Record<Q, R>>();
    }
    
    public void add(Record<Q, R> record){ 
        if( total > MAX_POOL_SIZE ){
            log.warn("Add Record fail! Beyond MAX_POOL_SIZE.");
            return;
        } 
        
        records.add(record);
        total++;
    } 
    
    /**
     * 获取缓存中Records数量
     * @return
     */
    public int size(){
        return records.size();
    }
    
    /**
     * 根据index获取单条Record
     * @param index
     * @return
     */
    public Record<Q, R> get(int index){
        return records.get(index);
    }
    
    /**
     * 随机来一个
     * 
     * @return
     */
    public Record<Q, R> getRandom(){
        return records.get( new Random().nextInt(records.size()) );
    }
    
    /**
     * 获取全部
     * @return
     */
    public List<Record<Q, R>> getAll(){
        return records;
    }

    /**
     * 根据tag获取Record，若无则返回null
     * 
     * @param tag
     * @return
     */
    public Record<Q, R> getByTag(String tag){
        if( null != tag && 0 != tag.length() ){
            for( Record<Q, R> record : records ){
                List<String> tags = record.getTags();
                for(String t : tags){
                    if( tag.equals(t) ){
                        return record;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * 将缓存中的所有Record，全部持久化到yaml文件中
     * 
     * *很纠结要不要加 synchronized
     * @throws IOException 
     * 
     */
    public synchronized void dump() throws IOException{ 
        int size = records.size();
        if( size > 0 ){ 
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(ymlPath, true), "UTF-8"));
            for(int i=0;i<size;i++){
                writeOneRecord(bw, records.get(i), dumped+i);
            }
            bw.flush();
            bw.close();
            
            records.clear();
            dumped = total;
        }
    } 
    
    /**
     * 从文件load出所有records
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void load() throws IOException{ 
        records.clear();
        
        BufferedReader br = new BufferedReader(new InputStreamReader( 
                new FileInputStream(ymlPath), "UTF-8"));
        
        try{
            String line = br.readLine();
            while( null != line ){
                if( line.startsWith("====== ") && line.endsWith(" ======") ){
                    StringBuilder sb = new StringBuilder();
                    while( true ){
                        line = br.readLine();
                        if( null == line ){
                            //FIXME maybe MalformedRecordException
                            throw new Ak47RuntimeException("Malformed Record! Yaml path is "+ymlPath );
                        }
                        if( RECORD_END_LINE.equals(line) ){
                            break;
                        }
                        sb.append(line).append("\n");
                    }
                    
                    Record<Q, R> record = (Record<Q, R>) yaml.load(sb.toString());
                    if( null != record ){
                        records.add(record);
                    }
                    
                }// from begin to end
                
                line = br.readLine();
            }//while
        }finally{
            br.close();
        }
        
        total = records.size();
        dumped = total;
        
    }
    
    /**
     * 写入一条数据
     * 
     * @param record
     * @throws IOException 
     */
    private void writeOneRecord(BufferedWriter bw, Record<Q, R> record, int index) throws IOException{
        
        bw.write( String.format(RECORD_BEGIN_LINE, index) );
        bw.newLine();
        String recStr = yaml.dump(record);
        bw.write( recStr );
        bw.newLine();
        bw.write( RECORD_END_LINE );
        bw.newLine();
        bw.newLine();
        bw.newLine();
        
    } 
    
    
}
