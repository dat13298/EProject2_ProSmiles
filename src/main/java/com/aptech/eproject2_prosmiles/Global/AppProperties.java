package com.aptech.eproject2_prosmiles.Global;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {
    private static final Properties properties = new Properties();
    private static final String PROPERTIES_FILE_PATH = System.getProperty("user.home") + "/application.properties";

    static {
        try {
            InputStream inputStream = AppProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_PATH);
            if (inputStream != null) {
                properties.load(inputStream);
            }
        }catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
        String userHome = System.getProperty("user.home");
        String filePath = userHome + "/application.properties";
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            properties.store(outputStream, null);
        }catch (IOException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
