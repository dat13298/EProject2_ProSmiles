package com.aptech.eproject2_prosmiles.Global;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {
    private static final Properties properties = new Properties();
    private static final String PROPERTIES_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/application.properties";

    static {
        try {
            InputStream inputStream = AppProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_PATH);
            if (inputStream != null) {
                properties.load(inputStream);
            }
        }catch (IOException e) {
            throw new RuntimeException("Login Failed");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
        String userHome = System.getProperty("user.dir");
        String filePath = userHome + "/src/main/resources/application.properties";
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            properties.store(outputStream, null);
        }catch (IOException e){
            throw new RuntimeException("Login Failed");
        }

    }
}
