package com.abstracta.cursoselenium.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TopBarComponent extends BaseComponent {

    private final By WISHLIST = By.id("wishlist-total");
    private final By CARRITO = By.cssSelector("a[title='Shopping Cart']");
    private final By CHECKOUT = By.cssSelector("a[title='Checkout']");

    public TopBarComponent(WebElement root, WebDriver driver, WebDriverWait wait) {
        super(root, wait);
    }

    public int obtenerCantidadWishlist() {
        // El conteo viene en el atributo title ("Wish List (0)"), no en
        // texto visible del link
        String tituloAtributo = encontrar(WISHLIST).getAttribute("title");
        try {
            return Integer.parseInt(tituloAtributo.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void irAlCarrito() {
        encontrar(CARRITO).click();
    }

    public boolean estaVisibleElLinkCheckout() {
        return existeEn(CHECKOUT);
    }
}
