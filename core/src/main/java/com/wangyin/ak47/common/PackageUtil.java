package com.wangyin.ak47.common;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.wangyin.ak47.common.DynamicClassLoader;

/**
 * Package-related helper class
 * 
 * 包路径相关辅助类
 * 
 * @author hannyu
 *
 */
public class PackageUtil {
    
    /**
     * Simplify a Class or Package full name, such as:
     * 
     *  com.wangyin.ak47.common.PackageUtil  {@code ===>}  c.w.a.c.PackageUtil
     * 
     * 简化一个类或包的路径，为了避免太长。
     * 
     * @param classname
     * @return
     */
    public static String simplifyName(String classname){
        String[] cns = classname.split("\\.");
        StringBuilder sb = new StringBuilder(32);
        for(int i=0; i<cns.length-1; i++){
            sb.append(cns[i].charAt(0)).append(".");
        }
        sb.append(cns[cns.length-1]);
        return sb.toString();
    }
    
    
    
    private static final String CLASSPATH_SEPARATOR = System.getProperty("path.separator");// $NON-NLS-1$
    private static final String JAVA_CLASS_PATH = "java.class.path";
    private static final String OS_NAME = System.getProperty("os.name");// $NON-NLS-1$
    private static final String OS_NAME_LC = OS_NAME.toLowerCase(java.util.Locale.ENGLISH);
    private static final boolean usesUNC = OS_NAME_LC.startsWith("windows");// $NON-NLS-1$
    
    
    /**
     * add one or many jars to the context ClassLoader.
     * 
     * 动态加载jar包
     * 
     * @param jarfile       a jar file or a directory which includes some jars (not include subdirectories).
     */
    public static void addExtJars(String jarfile){
        
        File libDir = new File(jarfile);
        if( !libDir.exists() ){
            return;
        }

        List<URL> jars = new LinkedList<URL>();
        StringBuilder classpaths = new StringBuilder();
        
        // jarDir is a file
        if( libDir.isFile() ){
            try {
                jars.add(libDir.toURI().toURL());
                classpaths.append(CLASSPATH_SEPARATOR);
                classpaths.append(libDir.getPath());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return;
        }
        
        // jarDir is a directory
        File[] libJars = libDir.listFiles(new FilenameFilter(){

            @Override
            public boolean accept(File dir, String name) {
                if( name.endsWith(".jar") ){ // $NON-NLS-1$
                    File jar = new File(dir, name);
                    return jar.isFile() && jar.canRead();
                }
                return false;
            }
            
        });
        
        if (libJars == null) { 
            new Throwable("Could not access " + libDir).printStackTrace();
            return;
        }
        
        Arrays.sort(libJars);
        for (File libJar : libJars) { 
            try {
                String s = libJar.getPath();
                
                // Fix path to allow the use of UNC URLs
                if (usesUNC) {
                    if (s.startsWith("\\\\") && !s.startsWith("\\\\\\")) {// $NON-NLS-1$ $NON-NLS-2$
                        s = "\\\\" + s;// $NON-NLS-1$
                    } else if (s.startsWith("//") && !s.startsWith("///")) {// $NON-NLS-1$ $NON-NLS-2$
                        s = "//" + s;// $NON-NLS-1$
                    }
                } // usesUNC

                jars.add(new File(s).toURI().toURL());// See Java bug 4496398
                classpaths.append(CLASSPATH_SEPARATOR);
                classpaths.append(s);
            } catch (MalformedURLException e) { 
                e.printStackTrace();
            }
        }//for
        
        // update loader
        DynamicClassLoader.updateLoader(jars);
        classpaths.append(CLASSPATH_SEPARATOR);
        classpaths.append(System.getProperty(JAVA_CLASS_PATH));
        System.setProperty(JAVA_CLASS_PATH, classpaths.toString());
    }

}
