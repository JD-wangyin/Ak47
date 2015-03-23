package com.wangyin.ak47.boot;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.wangyin.ak47.common.Ak47Constants;
import com.wangyin.ak47.common.Ak47Env;
import com.wangyin.ak47.common.DynamicClassLoader;



/**
 * Ak47 boot entry. 
 * Responsible for dynamically loading all dependent jars, and extend jars. 
 * Then forward the parameters to the real-target class and main method. 
 * 
 * WARNING: Any other class should NOT dependent on this class.
 * 
 * Ak47可执行jar包的入口，负责加载各种jar包，并将参数转交给真正的main方法。
 * 本类不能依赖Ak47下其他子包的类。
 * 
 * 注：任何其他类都不应该依赖此类。
 * 
 * @author hannyu
 *
 */
public class Ak47Boot { 
    
    private static final String CLASSPATH_SEPARATOR = System.getProperty("path.separator");// $NON-NLS-1$
    
    private static final String OS_NAME = System.getProperty("os.name");// $NON-NLS-1$
    
    private static final String OS_NAME_LC = OS_NAME.toLowerCase(java.util.Locale.ENGLISH);
    
    private static final String JAVA_CLASS_PATH = "java.class.path";
    
    
    /*
     * Does the system support UNC paths? If so, may need to fix them up
     * later
     */
    private static final boolean usesUNC = OS_NAME_LC.startsWith("windows");// $NON-NLS-1$
    

    private static DynamicClassLoader loader;
    
    
    /**
     * new DynamicClassLoader
     * 
     * @param classpath
     * @param jars
     * @return
     */
    private static DynamicClassLoader genLoaderAndSetClasspath(String classpath, final List<URL> jars){
        System.setProperty(JAVA_CLASS_PATH, classpath);
        return AccessController.doPrivileged(
                new java.security.PrivilegedAction<DynamicClassLoader>() { 
                    @Override
                    public DynamicClassLoader run() {
                        return new DynamicClassLoader(jars.toArray(new URL[jars.size()]));
                    }
                }
        );
    }
    
    /**
     * 查找jarDir目录下所有jar包(不包含子目录)，并拼装classpaths和jars
     * 
     * @param jarDir
     * @return
     */
    private static void findAndAddClasspaths(String jarDir, StringBuilder classpaths, 
            List<URL> jars){

        File libDir = new File(jarDir);
        if( !libDir.exists() ){
            return;
        }
        
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
        
    }
    
    
    
    /**
     * print usage
     */
    public static void usage(){
        System.out.println("AK47Boot -v | -h | [-x extension] MainClass [and their args]");
        System.out.println("Example: ak47boot.sh -x iso8583 -m my.company.TestMyMock wakaka");
        System.out.println("Version: " + Ak47Constants.VERSION);
        System.exit(0);
    }
    
    
    
    
    /**
     * runable entry
     * 
     * @param args              command arguments
     * @throws IOException      when NOT in ak47 project
     */
    public static void main(String[] args) throws IOException {

        // reslove args
        String extension = null;
        String mainclassname = null;
        String[] mainargs = null;
        if( args != null && args.length >= 1 ){
            if( "-v".equals(args[0]) || "-h".equals(args[0]) ){
                usage();
            }else if( "-x".equals(args[0]) ){
                if( args.length >= 3){
                    extension = args[1];
                    mainclassname = args[2];
                    mainargs = new String[args.length-3];
                    System.arraycopy(args, 3, mainargs, 0, args.length-3);
                }else{
                    System.out.println("ERROR: Wrong arguments!");
                    usage();
                }
            }else if( args[0].startsWith("-x") ){
                if( args.length >= 2){
                    extension = args[0].substring(2, args.length);
                    mainclassname = args[1];
                    mainargs = new String[args.length-2];
                    System.arraycopy(args, 2, mainargs, 0, args.length-2);
                }else{
                    System.out.println("ERROR: Wrong arguments!");
                    usage();
                }
            }else{
                mainclassname = args[0];
                mainargs = new String[args.length-1];
                System.arraycopy(args, 1, mainargs, 0, args.length-1);
            }
        }else{
            usage();
        }
        
        // update extension dir
        if( extension != null ){
            Ak47Env.AK47_HOME_EXT_DIR = Ak47Env.AK47_HOME_EXT_DIR + extension;
        }
        
        // init loader
        final List<URL> jars = new LinkedList<URL>();
        final String initialClasspaths = System.getProperty(JAVA_CLASS_PATH);
        final StringBuilder classpaths = new StringBuilder();
        
        // only when running from jar
        if( Ak47Env.IF_RUNNING_FROM_JAR ){
            // load lib/*.jar
            findAndAddClasspaths(Ak47Env.AK47_HOME_LIB_DIR, classpaths, jars);
            // load ext[-extension]/*.jar
            findAndAddClasspaths(Ak47Env.AK47_HOME_EXT_DIR, classpaths, jars);
        }
        
        // genarate loader
        loader = genLoaderAndSetClasspath(initialClasspaths + classpaths.toString(), jars);
        
        // need dynamic class loader
        Thread.currentThread().setContextClassLoader(loader);
        
        // set logback.configurationFile
        if( null == System.getProperty("logback.configurationFile") ){ 
            File file = new File(Ak47Env.AK47_HOME_CONF_DIR + File.separator + "logback.xml");
            System.setProperty("logback.configurationFile", file.getCanonicalPath());
        }

        
        // do specified job
        try { 
            Class<?> mainClass;
            mainClass = loader.loadClass(mainclassname);
            Method mainMethod = mainClass.getMethod("main", new Class[] { new String[0].getClass() });// $NON-NLS-1$
            mainMethod.invoke(mainClass, new Object[] { mainargs });
        } catch(Throwable e){
            e.printStackTrace();
//            System.err.println("AK47 home dir was detected as: " + Ak47Env.AK47_HOME_DIR);
            System.exit(1);
        }
        
        
        System.exit(0);
    }
    
}



