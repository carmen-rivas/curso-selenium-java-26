package com.abstracta.cursoselenium.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        String env = System.getProperty("env", "local");
        String archivo = env.equals("ci") ? "config-ci.properties" : "config.properties";

        try (InputStream in = ConfigReader.class.getClassLoader()
                .getResourceAsStream(archivo)) {
            if (in == null) {
                throw new RuntimeException(
                        "No se encontró " + archivo + " en src/test/resources/");
            }
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Error cargando " + archivo, e);
        }
    }

    public static String getBaseUrl() {
        return System.getProperty("baseUrl",
                properties.getProperty("baseUrl", "https://opencart.abstracta.us"));
    }

    public static String getBrowser() {
        return System.getProperty("browser", properties.getProperty("browser", "chrome"));
    }

    public static int getTimeout() {
        return Integer.parseInt(
                System.getProperty("timeout", properties.getProperty("timeout", "10")));
    }
}