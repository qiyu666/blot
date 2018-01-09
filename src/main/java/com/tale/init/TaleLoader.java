package com.tale.init;

import com.blade.Blade;
import com.tale.controller.admin.AttachController;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Created by biezhi on 2017/3/1.
 */
public final class TaleLoader {

    private TaleLoader() {
    }

    private static Blade blade;

    public static void init(Blade blade) {
        TaleLoader.blade = blade;
        loadPlugins();
        loadThemes();
    }

    //加载所有主题
    public static void loadThemes() {
        String themeDir = AttachController.CLASSPATH + "templates" + File.separatorChar + "themes";
        //获取文件列表
        File[] dir = new File(themeDir).listFiles();
        for (File f : dir) {
            if (f.isDirectory() && Files.isDirectory(Paths.get(f.getPath() + "/static"))) {
                String themePath = "/templates/themes/" + f.getName();
                blade.addStatics(themePath + "/style.css", themePath + "/screenshot.png", themePath + "/static/");
            }
        }
    }

    public static void loadTheme(String themePath) {
        blade.addStatics(themePath + "/style.css", themePath + "/screenshot.png", themePath + "/static/");
    }

    public static void loadPlugins() {
        File pluginDir = new File(AttachController.CLASSPATH + "plugins");
        if (pluginDir.exists() && pluginDir.isDirectory()) {
            File[] plugins = pluginDir.listFiles();
            for (File plugin : plugins) {
                loadPlugin(plugin);
            }
        }
    }

    /**
     * 加载某个插件jar包
     *
     * @param pluginFile 插件文件
     */
    public static void loadPlugin(File pluginFile) {
        try {
            if (pluginFile.isFile() && pluginFile.getName().endsWith(".jar")) {
                URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
                add.setAccessible(true);
                add.invoke(classLoader, pluginFile.toURI().toURL());

                String pluginName = pluginFile.getName().substring(6);
                blade.addStatics("/templates/plugins/" + pluginName + "/static/");
            }
        } catch (Exception e) {
            throw new RuntimeException("插件 [" + pluginFile.getName() + "] 加载失败");
        }
    }

    public static void loadDataSource() {

    }

    public static void loadDataSource(File dbFile) {

    }

}
