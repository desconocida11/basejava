package com.basejava.webapp;

import com.basejava.webapp.storage.SqlStorage;
import com.basejava.webapp.storage.Storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final File PROPS = new File(getHomeDir(), "config\\resumes.properties");
    private static final Config INSTANCE = new Config();
    private final Properties properties = new Properties();
    private final String storageDir;
    private static final Storage storage = new SqlStorage(INSTANCE.getProperty("db.url"),
            INSTANCE.getProperty("db.user"),
            INSTANCE.getProperty("db.password"));

    public static Config getInstance() {
        return INSTANCE;
    }

    public static Storage getStorage() {
        return storage;
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

    private static File getHomeDir() {
        String homeDir = System.getProperty("homeDir");
        File dir = new File(homeDir == null ? "." : homeDir);
        if (!dir.isDirectory()) {
            throw new IllegalStateException(homeDir + "is not a directory");
        }
        return dir;
    }
}
