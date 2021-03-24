package com.basejava.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final File PROPS = new File("config\\resumes.properties");
    private static final Config INSTANCE = new Config();
    private final Properties properties = new Properties();
    private final String storageDir;

    public static Config getInstance() {
        return INSTANCE;
    }

    private Config() {
        try (InputStream inputStream = new FileInputStream(PROPS)) {
            properties.load(inputStream);
            storageDir = properties.getProperty("storage.dir");
        } catch (IOException exception) {
            throw new IllegalStateException("Invalid config file" + PROPS.getAbsolutePath());
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getStorageDir() {
        return storageDir;
    }
}
