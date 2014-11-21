package com.wangyin.ak47.common;

import java.util.Arrays;
import java.util.List;

/**
 * Collection-related helper class
 * 
 * 提供集合、数组的相关操作
 * 
 * @author wyhubingyin
 * 
 */
public class CollectionUtil {
    
    /**
     * Add obj to list if obj is NOT exsit in list, otherwise dont do any thing.
     * 
     * @param list
     * @param obj
     */
    public static <T> void addIfNotExist(List<T> list, T obj){
        boolean isexist = false;
        for(T t : list){
            if( t.equals(obj) ){
                isexist = true;
                break;
            }
        }
        if( !isexist ){
            list.add(obj);
        }
    }
    
    /**
     * Shift left the given string array with 1 step, discard the original leftmost ones.
     * 
     * See: like Linux shell shift
     * 
     * 将字符串数组左移一位，丢弃原最左边的对象
     * 
     * @param array
     * @return
     */
    public static String[] shift(String[] array){
        return shift(array, 1);
    }
    
    /**
     * Shift left the given string array with N step, discard the original leftmost ones.
     * 
     * 将字符串数组左移n位，原最左边的对象丢弃
     * 
     * @param array
     * @return
     */
    public static String[] shift(String[] array, int n){
        if( n <= 0 ){
            throw new IllegalArgumentException("n must a non-negative number.");
        }
        
        if( n >= array.length ){
            return new String[0];
        }
        
        String[] dest = new String[array.length-n];
        System.arraycopy(array, n, dest, 0, array.length-n);
        return dest;
    }
    

    /**
     * Merge an array of string to a big string.
     * 
     * 将 array 合并成 string
     * 
     * @param array
     * @return
     */
    public static <T> String array2String(T... array){
        int len = array.length;
        if( len == 0 ){
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(array[0]);
        for(int i=1; i<array.length; i++){
            sb.append(",").append(array[i]);
        }
        sb.append("]");
        
        return sb.toString();
    }
    
    /**
     * Merge a list of string to a big string.
     * 
     * 将list转成string
     * 
     * @param list
     * @return
     */
    public static String list2String(List<?> list){
        return array2String(list2Array(list));
    }
	
    
	/**
	 * Convert array to List
	 * 
	 * Example:
	 *     List<String> stooges = ArrayUtil.array2List("Larry", "Moe", "Curly");
	 *     
	 * @param array
	 * @return
	 */
    public static <T> List<T> array2List(T... array) {
	    return Arrays.asList(array);
	}
	
	/**
	 * Convert List to array
	 * 
	 * Example:
	 *     String[] myarray = (String[]) ArrayUtil.list2Array(mylist);
	 * 
	 * @param list
	 * @return
	 */
	public static <T> Object[] list2Array(List<T> list) {
	    return list.toArray();
	}
	
    /**
     * 将list转成string, 
     * 
     * @param list
     * @param separator     分隔符
     * @return
     */
    public static String join(List<String> list, String separator){

        if( list == null || list.size() == 0 ){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(list.get(0));
        int size = list.size();
        for(int i=1;i<size;i++){
            sb.append(separator).append(list.get(i));
        }
        return sb.toString();
    }
	
    
    /**
     * 得到数组成员的class类型数组
     * 
     * @param list
     * @return
     */
    @SafeVarargs
    public static <T> Class<?>[] arrayOfClassType(T... array) {
        Class<?>[] clazzs = new Class<?>[array.length];
        for(int i=0; i<array.length; i++){
            clazzs[i] = array[i].getClass();
        }
        return clazzs;
    }
}
