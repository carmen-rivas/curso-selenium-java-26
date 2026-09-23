package com.abstracta.cursoselenium.tests;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected static final String BASE_URL = "https://opencart.abstracta.us";

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    // CAMBIO EN 7.2 — recibe ITestResult para saber si el test falló
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult resultado) {
        // Screenshot SOLO si el test falló, antes de cerrar el driver
        if (!resultado.isSuccess() && driver != null) {
            capturarScreenshotEnFallo(resultado.getName());
        }
        if (driver != null) driver.quit();
    }

    private void capturarScreenshotEnFallo(String nombreDelTest) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot — " + nombreDelTest, "image/png",
                    new ByteArrayInputStream(screenshot), ".png");
        } catch (Exception e) {
            // Si el driver está en mal estado, no interrumpir el teardown
            System.err.println("Screenshot no disponible: " + e.getMessage());
        }
    }
}