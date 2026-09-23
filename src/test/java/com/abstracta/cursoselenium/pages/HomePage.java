package com.abstracta.cursoselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.qameta.allure.Step;

public class HomePage extends BasePage {

    private final By CAMPO_BUSQUEDA = By.cssSelector("#search input");
    private final By BOTON_BUSQUEDA = By.cssSelector("#search button");

    public HomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        this.wait
                .withMessage("No se cargó la homepage. URL actual: " + driver.getCurrentUrl())
                .until(ExpectedConditions.visibilityOfElementLocated(CAMPO_BUSQUEDA));
    }

    // Método estático de fábrica
    public static HomePage abrir(WebDriver driver, WebDriverWait wait, String baseUrl) {
        driver.get(baseUrl);
        return new HomePage(driver, wait);
    }
    @Step("Buscar producto: {termino}")
    public ResultadosBusquedaPage buscar(String termino) {
        escribirEn(CAMPO_BUSQUEDA, termino);
        clickEn(BOTON_BUSQUEDA);
        return new ResultadosBusquedaPage(driver, wait);
    }

    // Selector del logo
    public String obtenerTitulo() {
        return obtenerTexto(By.cssSelector("#logo h1"));
    }
}