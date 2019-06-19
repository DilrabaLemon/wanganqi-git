package com.boye.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    private static Properties loadPropertiesByFileName(String fileName) {
        Properties prop = new Properties();
        InputStream inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream(fileName);
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;

    }

    /**
     * 是否是线上环境
     * 由于线上环境的不确定性，所以无法判断具体
     */
    public static boolean isProduction() {
        Properties properties = loadPropertiesByFileName("application.properties");
        return properties.get("spring.profiles.active").toString().equals("production");
    }

    /**
     * 是否是测试环境
     */
    public static boolean isTest() {
        Properties properties = loadPropertiesByFileName("application.properties");
        return properties.get("spring.profiles.active").toString().equals("test");
    }

    /**
     * 是否是开发环境
     */
    public static boolean isDev() {
        Properties properties = loadPropertiesByFileName("application.properties");
        return properties.get("spring.profiles.active").toString().equals("dev");
    }


    public static String get(String propertyName) {
        Properties properties = loadPropertiesByFileName("application.properties");
        String config = properties.getProperty("spring.profiles.active");
        String fileName = "application-" + config + ".properties";
        return loadPropertiesByFileName(fileName).get(propertyName).toString();
    }

}
