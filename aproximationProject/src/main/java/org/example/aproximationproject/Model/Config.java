package org.example.aproximationproject.Model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Config instance;
    private final Properties properties = new Properties();

    private Config(String filePath) {
        loadProperties(filePath);
    }

    private void loadProperties(String filePath) {
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить конфигурацию: " + filePath, e);
        }
    }

    public static Config getInstance(String filePath) {
        if (instance == null) {
            instance = new Config(filePath);
        }
        return instance;
    }

    // Метод для получения значения с обработкой значения по умолчанию
    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    // Существующий метод без значения по умолчанию
    public String getString(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Обязательный параметр отсутствует: " + key);
        }
        return value;
    }

    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Некорректное числовое значение для параметра: " + key, e);
        }
    }
}
