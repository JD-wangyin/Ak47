package com.wangyin.ak47.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.wangyin.ak47.core.exception.Ak47RuntimeException;

/**
 * 
 * Config loader will automatically load the AK47 or user's config file,
 * formated in YAML, give the prefix such as 'ak47.yml'.
 * 
 * Loading strategy (assume the prefix is 'xxx'): 1. Check whether there
 * {class_path}/xxx-test.yml, may be within the jar package. If exists, load
 * xxx-test.yml, if not, load {class_path}/xxx.yml, if both are absent, throw an
 * exception. 2. Check whether there {$AK47_HOME}/conf/xxx.yml, load it if
 * exists. 3. Check whether there {$HOME}/.ak47/xxx.yml, load it if exists.
 * 
 * NOTICE: If a config value both in two conf file, the after loaded will cover
 * the before loaded.
 * 
 * WARNING: {class_path}/xxx-test.yml just be used in beta test, should be
 * invisible in official release.
 * 
 * 配置加载类
 * 
 * 加载顺序（后面覆盖前面）：
 * 
 * 1、 首先，检查是否有 getClassLoader()/xxx-test.yml，有可能在jar包内。 若有，则加载
 * xxx-test.yml，若没有，则加载 xxx.yml，若两者都无，则抛异常。
 * 
 * 2、 其次，检查是否有 $APP/conf/xxx.yml，若有则加载，若无则不加载。
 * 
 * 3、 最后，检查是否有 $HOME/.ak47/xxx.yml，若有则加载，若无则不加载。
 * 
 * 注：对 xxx-test.yml 的加载策略只是用于AK47开发内测，正式发布版应该是看不见的。
 * 
 * @author hannyu
 *
 */
public class ConfigLoader {
    private static final Logger log = new Logger(ConfigLoader.class);

    private static final String POSTFIX_YML = ".yml";
    private static final String POSTFIX_TEST_YML = "-test.yml";

    private static final ClassLoader CLASS_LOADER = ConfigLoader.class
            .getClassLoader();

    /**
     * loading
     * 
     * 加载
     * 
     * @param prefix
     *            prefix of configuration file, like {prefix}.yml
     * @return map of configuration items
     * @throws IOException
     *             i/o wrong
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> load(String prefix) throws IOException {

        // init map
        Map<String, Object> omap;
        Yaml yaml = new Yaml();

        // in jar ( xxx-test.yml or xxx.yml )
        String testYmlName = prefix + POSTFIX_TEST_YML;
        String ymlName = prefix + POSTFIX_YML;
        System.out.println(CLASS_LOADER.getResource(""));
        InputStream testYmlIs = CLASS_LOADER.getResourceAsStream(testYmlName);
        if (null == testYmlIs) {
            InputStream ymlIs = CLASS_LOADER.getResourceAsStream(ymlName);
            if (null == ymlIs) {
                log.error("NOT found {} and {}.", testYmlName, ymlName);
                throw new Ak47RuntimeException(testYmlName + " and " + ymlName
                        + " both NOT found.");
            }

            try {
                omap = (Map<String, Object>) yaml.load(ymlIs);
            } catch (Exception e) {
                log.error("load {} fail.", ymlName);
                throw new Ak47RuntimeException("load " + ymlName + " fail.", e);
            } finally {
                ymlIs.close();
            }

            log.info("load {} success.", ymlName);
        } else {
            try {
                omap = (Map<String, Object>) yaml.load(testYmlIs);
            } catch (Exception e) {
                log.error("load {} fail.", testYmlName);
                throw new Ak47RuntimeException(
                        "load " + testYmlName + " fail.", e);
            } finally {
                testYmlIs.close();
            }

            log.info("load {} success.", testYmlName);
        }

        if (omap == null || omap.size() == 0) {
            throw new Ak47RuntimeException("load " + testYmlName + " or "
                    + ymlName + " fail.");
        }

        // app conf dir ( ak47/conf/xxx.yml )
        String appConfYmlPath = Ak47Env.AK47_HOME_CONF_DIR + File.separator
                + ymlName;
        File appConfYmlFile = new File(appConfYmlPath);
        if (!appConfYmlFile.exists()) {
            log.info("NOT found {}.", appConfYmlPath);
        } else if (!appConfYmlFile.canRead()) {
            log.warn("Can NOT read {}.", appConfYmlPath);
        } else {
            FileInputStream fis = new FileInputStream(appConfYmlFile);
            Map<String, Object> newMap;
            try {
                newMap = (Map<String, Object>) yaml.load(fis);
            } catch (Exception e) {
                log.error("load {} fail.", appConfYmlPath);
                throw new Ak47RuntimeException("load " + appConfYmlPath
                        + " fail.", e);
            } finally {
                fis.close();
            }

            if (null == newMap) {
                log.error("load {} fail.", appConfYmlPath);
                throw new Ak47RuntimeException("load " + appConfYmlPath
                        + " fail. result is null.");
            }

            log.info("load {} success.", appConfYmlPath);
            newCoverOld(omap, newMap);
        }

        // local home share dir ( /home/wy/.ak47/xxx.yml )
        String homeYmlPath = Ak47Env.AK47_LOCAL_SHARE_DIR + File.separator
                + ymlName;
        File homeYmlFile = new File(homeYmlPath);
        if (!homeYmlFile.exists()) {
            log.info("NOT found {}.", homeYmlPath);
        } else if (!homeYmlFile.canRead()) {
            log.warn("can NOT read {}.", homeYmlPath);
        } else {
            FileInputStream fis = new FileInputStream(homeYmlFile);
            Map<String, Object> newMap;
            try {
                newMap = (Map<String, Object>) yaml.load(fis);
            } catch (Exception e) {
                log.error("load {} fail.", homeYmlPath);
                throw new Ak47RuntimeException(
                        "load " + homeYmlPath + " fail.", e);
            } finally {
                fis.close();
            }

            if (null == newMap) {
                log.error("load {} fail.", homeYmlPath);
                throw new Ak47RuntimeException("load " + homeYmlPath
                        + " fail. result is null.");
            }

            log.info("load {} success.", homeYmlPath);
            newCoverOld(omap, newMap);
        }

        // return
        return omap;
    }

    /**
     * New cover old After cover before
     * 
     * @param oldMap
     * @param newMap
     */
    private static void newCoverOld(Map<String, Object> oldMap,
            Map<String, Object> newMap) {
        for (String key : newMap.keySet()) {
            Object value = newMap.get(key);
            if (oldMap.containsKey(key)) {
                log.info("cover key[{}] from {} to {}.", key, oldMap.get(key),
                        value);
            } else {
                log.info("added key[{}] with {}.", key, value);
            }
            oldMap.put(key, value);
        }
    }

}
