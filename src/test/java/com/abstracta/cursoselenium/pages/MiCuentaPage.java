package com.abstracta.cursoselenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MiCuentaPage extends BasePage {

    private final By TITULO = By.xpath("//h2[contains(text(),'My Account')]");

    public MiCuentaPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        this.wait
                .withMessage("No se cargó la página de My Account. URL actual: " + driver.getCurrentUrl())
                .until(ExpectedConditions.visibilityOfElementLocated(TITULO));
    }

    public boolean estaCargada() {
        return !driver.findElements(TITULO).isEmpty();
    }
}