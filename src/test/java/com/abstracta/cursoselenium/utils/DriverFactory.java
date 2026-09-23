package com.abstracta.cursoselenium.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

    public static WebDriver crearDriver(String browser) {
        return switch (browser.toLowerCase()) {
            case "chrome"           -> new ChromeDriver(opcionesChrome(false));
            case "chrome-headless"  -> new ChromeDriver(opcionesChrome(true));
            case "firefox"          -> new FirefoxDriver(opcionesFirefox(false));
            case "firefox-headless" -> new FirefoxDriver(opcionesFirefox(true));
            default -> throw new IllegalArgumentException(
                    "Browser no soportado: '" + browser + "'. "
                            + "Opciones: chrome, chrome-headless, firefox, firefox-headless");
        };
    }

    private static ChromeOptions opcionesChrome(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }
        options.addArguments("--window-size=1920,1080");
        return options;
    }

    private static FirefoxOptions opcionesFirefox(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        options.addArguments("--width=1920", "--height=1080");
        return options;
    }
}