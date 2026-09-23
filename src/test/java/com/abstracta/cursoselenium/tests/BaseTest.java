package com.abstracta.cursoselenium.tests;

import com.abstracta.cursoselenium.utils.ConfigReader;
import com.abstracta.cursoselenium.utils.DriverFactory;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    // Ya no es "static final": se puebla en cada setUp() desde ConfigReader.
    // Se mantiene el nombre BASE_URL a propósito -- ningún test existente
    // que ya escribe "BASE_URL" necesita cambiar una sola línea
    protected String BASE_URL;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        BASE_URL = ConfigReader.getBaseUrl();
        int timeout = ConfigReader.getTimeout();

        driver = DriverFactory.crearDriver(ConfigReader.getBrowser());
        wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        driver.manage().window().maximize();
    }

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