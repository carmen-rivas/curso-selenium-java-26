package com.abstracta.cursoselenium.tests;

import com.abstracta.cursoselenium.pages.HomePage;
import com.abstracta.cursoselenium.pages.LoginPage;
import com.abstracta.cursoselenium.pages.components.TopBarComponent;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ComponenteTopBarTest extends BaseTest {

    @Test
    public void topBar_DesdeHomePage_MuestraWishlistYCheckout() {
        HomePage home = HomePage.abrir(driver, wait, BASE_URL);
        TopBarComponent topBar = home.topBar();

        int wishlist = topBar.obtenerCantidadWishlist();
        boolean checkoutVisible = topBar.estaVisibleElLinkCheckout();
        System.out.println("Desde HomePage -> wishlist: " + wishlist + " | Checkout visible: " + checkoutVisible);

        Assert.assertEquals(wishlist, 0, "El wishlist debe arrancar en 0");
        Assert.assertTrue(checkoutVisible, "El link Checkout debe estar visible");
    }

    @Test
    public void topBar_DesdeLoginPage_CarritoEsClickeableYNavegaAlCarrito() {
        LoginPage login = LoginPage.abrir(driver, wait, BASE_URL);
        TopBarComponent topBar = login.topBar();

        topBar.irAlCarrito();

        // Confirmamos que el click realmente navegó Y que la página cargó su
        // contenido real -- no alcanza con la URL sola
        WebElement mensajeCarrito = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#content h1")));

        System.out.println("Después de clickear el carrito -> título: " + mensajeCarrito.getText()
                + " | URL: " + driver.getCurrentUrl());

        Assert.assertEquals(mensajeCarrito.getText(), "Shopping Cart",
                "El ícono del carrito debe llevar a la página del carrito");
        Assert.assertTrue(driver.getCurrentUrl().contains("route=checkout/cart"),
                "La URL debe confirmar la ruta del carrito");
    }
}
