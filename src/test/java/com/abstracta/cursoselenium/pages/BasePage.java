package com.abstracta.cursoselenium.pages;

import com.abstracta.cursoselenium.pages.components.TopBarComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    protected WebElement esperarVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement esperarClickeable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void escribirEn(By locator, String texto) {
        WebElement campo = esperarVisible(locator);
        campo.clear();
        campo.sendKeys(texto);
    }

    protected void clickEn(By locator) {
        esperarClickeable(locator).click();
    }

    protected String obtenerTexto(By locator) {
        return esperarVisible(locator).getText();
    }

    // NUEVO EN 5.4 — nav#top es chrome estructural: aparece en cualquier
    // página, no es lógica de negocio de ninguna en particular
    public TopBarComponent topBar() {
        WebElement root = driver.findElement(By.cssSelector("nav#top"));
        return new TopBarComponent(root, driver, wait);
    }
}
