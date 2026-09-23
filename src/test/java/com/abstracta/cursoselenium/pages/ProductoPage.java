package com.abstracta.cursoselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductoPage extends BasePage {

    private final By TITULO = By.cssSelector("#content h1");

    public ProductoPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        this.wait
                .withMessage("No se cargó la página de producto. URL actual: " + driver.getCurrentUrl())
                .until(ExpectedConditions.visibilityOfElementLocated(TITULO));
    }

    public String obtenerNombreDelProducto() {
        return obtenerTexto(TITULO);
    }
}
