package com.wangyin.ak47.common;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


/**
 * Class-Find related helper class.
 * 
 * Class查找相关辅助类
 * 
 * @author hannyu
 *
 */
public class ClassFinder {
    
    private static final Logger log = new Logger(ClassFinder.class);

    /**
     * Find out the filtered class-list, in a single jar, 
     * or multiple jars in some directory.
     * 
     * 对某个jar文件，或某个目录下的全部jar文件（包括子目录）搜索，找出满足条件的Class列表
     * 
     * @param jarPathname       a single jar path, or a directory which has many jars.
     * @param filter
     * @return
     * @throws IOException 
     * @throws ZipException 
     */
    public static List<Class<?>> findClassesInJars(String jarPathname, ClassFinderFilter filter) 
            throws ZipException, IOException{
        
        List<Class<?>> classlist = new LinkedList<Class<?>>();
        
        File rootfile = new File(jarPathname);
        if( rootfile != null && rootfile.exists() && rootfile.canRead() ){
            if( rootfile.isDirectory() ){
                String[] childfilenames = rootfile.list();
                for(String filename : childfilenames){
                    File file = new File(rootfile, filename);
                    
                    if( filename.endsWith(".jar") ){
                        if( file.isFile() && file.canRead() ){
                            findClassesInJar(file, classlist, filter);
                        }else{
                            log.warn("File[{}] is NOT a file or NO permission to read.", filename);
                        }
                    }else if( file.isDirectory() ){
                        // 目录，深度遍历
                        classlist.addAll( findClassesInJars(file.getAbsolutePath(), filter) );
                    }
                }
            }else{
                findClassesInJar(rootfile, classlist, filter);
            }
        }else{
            log.warn("File[{}] NOT exist or NO permission to read.", jarPathname);
        }
        
        return classlist;
    }
    
    
    private static void findClassesInJar(File jarfile, List<Class<?>> classlist, ClassFinderFilter filter) 
            throws IOException{
        ZipFile zipfile = new ZipFile(jarfile);
        Enumeration<? extends ZipEntry> ens = zipfile.entries();
        while( ens.hasMoreElements() ){
            ZipEntry zipen = ens.nextElement();
            String name = zipen.getName();
            if( name.endsWith(".class") ){
                String className = jarElemName2ClassName(name);
                try {
                    Class<?> clazz = Class.forName(className);
                    if( filter.accept(clazz) ){
                        classlist.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    log.warn("ClassNotFoundException when Class.forName({}) in jar[{}].", className, jarfile);
                } catch (NoClassDefFoundError e) {
                    log.warn("NoClassDefFoundError when Class.forName({}) in jar[{}].", className, jarfile);
                } catch (Throwable e){
                    log.warn("{} when Class.forName({}) in jar[{}].", e.toString(), className, jarfile);
                }
            }
        }//while
        zipfile.close();
    }
    
    private static String jarElemName2ClassName(String elemName){
        String className = elemName.replace('\\', '.').replace('/', '.');
        className = className.substring(0, className.length() - ".class".length());
        return className;
    }
    
    
    /**
     * These instances are used to filter Class<?> in ClassFinder.
     * 
     * @author hannyu
     *
     */
    public static interface ClassFinderFilter {
        public boolean accept(Class<?> clazz);
    }
    
    
}
