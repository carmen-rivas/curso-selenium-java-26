package com.abstracta.cursoselenium.tests;

import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.NORMAL;

public class TiposDeFalloTest extends BaseTest {

    // PASA — la assertion se cumple. Espera explícita antes de leer el
    // título: sin esto, corriendo en medio de una suite larga, el título
    // puede no haber terminado de poblarse cuando getTitle() lo lee.
    @Test @Story("Resultado exitoso") @Severity(NORMAL)
    public void test_Correcto_Pasa() {
        driver.get(BASE_URL);
        wait.until(ExpectedConditions.titleIs("Your Store"));
        Assert.assertEquals(driver.getTitle(), "Your Store",
                "El título debe ser 'Your Store'");
    }

    // FAILED (Product error) — AssertionError: la app difiere de lo esperado
    @Test @Story("Bug en la aplicación") @Severity(CRITICAL)
    public void test_AssertionError_GeneraFailed() {
        driver.get(BASE_URL);
        Assert.assertEquals(driver.getTitle(), "Título Que No Existe",
                "Provoca AssertionError intencional");
    }

    // BROKEN (Test error) — NoSuchElementException: el flujo no se completó
    @Test @Story("Selector roto") @Severity(NORMAL)
    public void test_ExcepcionTecnica_GeneraBroken() {
        driver.get(BASE_URL);
        driver.findElement(By.id("elemento-que-no-existe-jamas")); // NoSuchElementException
    }
}