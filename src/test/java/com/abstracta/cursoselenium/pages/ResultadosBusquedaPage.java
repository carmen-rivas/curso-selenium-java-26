package com.abstracta.cursoselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class ResultadosBusquedaPage extends BasePage {

    private final By RESULTADOS = By.cssSelector(".product-thumb h4 a");

    public ResultadosBusquedaPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        this.wait
                .withMessage("No se cargó la página de resultados. URL actual: " + driver.getCurrentUrl())
                .until(ExpectedConditions.urlContains("search"));
    }

    public boolean hayResultados() {
        return !driver.findElements(RESULTADOS).isEmpty();
    }

    public String obtenerNombrePrimerResultado() {
        List<WebElement> resultados = driver.findElements(RESULTADOS);
        return resultados.isEmpty() ? "" : resultados.get(0).getText();
    }

    // Retorna la página nueva a la que navega el click
    public ProductoPage abrirPrimerResultado() {
        List<WebElement> resultados = driver.findElements(RESULTADOS);
        resultados.get(0).click();
        return new ProductoPage(driver, wait);
    }
}