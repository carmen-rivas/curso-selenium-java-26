package com.abstracta.cursoselenium.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public abstract class BaseComponent {

    protected final WebElement root;   // el elemento raíz del componente
    protected final WebDriverWait wait;

    public BaseComponent(WebElement root, WebDriverWait wait) {
        this.root = root;
        this.wait = wait;
    }

    // Busca DENTRO del componente, no en toda la página
    protected WebElement encontrar(By locator) {
        return root.findElement(locator);
    }

    protected List<WebElement> encontrarTodos(By locator) {
        return root.findElements(locator);
    }

    protected boolean existeEn(By locator) {
        return !root.findElements(locator).isEmpty();
    }
}
