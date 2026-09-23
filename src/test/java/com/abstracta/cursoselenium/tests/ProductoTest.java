package com.abstracta.cursoselenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

// ✅ responsabilidad única + AAA + mensajes con contexto
public class ProductoTest extends BaseTest {

    @Test(description = "La búsqueda por nombre retorna productos que coinciden con el término")
    public void busqueda_PorNombreValido_RetornaProductosRelevantes() {

        // ARRANGE
        String terminoBusqueda = "MacBook";
        driver.get(BASE_URL);

        // ACT
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#search input")))
                .sendKeys(terminoBusqueda);
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("#search button"))).click();
        wait.until(ExpectedConditions.urlContains("search"));

        // ASSERT
        List<WebElement> resultados = driver.findElements(By.cssSelector(".product-thumb h4 a"));
        Assert.assertFalse(resultados.isEmpty(),
                "La búsqueda de '" + terminoBusqueda + "' debe retornar resultados. "
                        + "URL: " + driver.getCurrentUrl());

        System.out.println("Búsqueda '" + terminoBusqueda + "' -> " + resultados.size() + " resultado(s)");
    }

    @Test(description = "La página de detalle muestra la información completa del producto")
    public void paginaDetalle_ProductoExistente_MuestraInformacionCompleta() {

        // ARRANGE — navega directo al producto: NO depende del test de búsqueda
        driver.get(BASE_URL + "/index.php?route=product/product&product_id=43");

        // ASSERT — múltiples propiedades independientes → Soft Assert
        SoftAssert sa = new SoftAssert();

        // #content h1, no h1 a secas: la página tiene un segundo <h1> con el
        // logo del sitio en el header, y h1 sin acotar agarra ese primero
        WebElement titulo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#content h1")));
        sa.assertTrue(titulo.getText().contains("MacBook"),
                "El título debe contener 'MacBook'. Encontrado: '" + titulo.getText() + "'");

        // #content .col-sm-4 h2, no h2 a secas: la página tiene un segundo
        // <h2> en la sección "Write a review", y h2 sin acotar agarra ambos
        boolean precioVisible = !driver.findElements(By.cssSelector("#content .col-sm-4 h2")).isEmpty();
        sa.assertTrue(precioVisible, "El precio del producto debe ser visible");

        List<WebElement> botonCarrito = driver.findElements(By.id("button-cart"));
        sa.assertFalse(botonCarrito.isEmpty(), "El botón 'Add to Cart' debe estar presente");
        boolean botonHabilitado = !botonCarrito.isEmpty() && botonCarrito.get(0).isEnabled();
        if (!botonCarrito.isEmpty()) {
            sa.assertTrue(botonHabilitado, "El botón 'Add to Cart' debe estar habilitado");
        }

        System.out.println("Título: '" + titulo.getText() + "' | Precio visible: " + precioVisible
                + " | Botón habilitado: " + botonHabilitado);

        sa.assertAll();
    }

    @Test(description = "Agregar un producto disponible actualiza el total del carrito")
    public void agregarAlCarrito_ProductoDisponible_ActualizaElTotal() {

        // ARRANGE — navega directo, no depende de los otros dos tests
        driver.get(BASE_URL + "/index.php?route=product/product&product_id=43");

        // ACT
        driver.findElement(By.id("button-cart")).click();

        // ASSERT — el carrito se actualiza vía AJAX: esperamos la condición
        // real en vez del Thread.sleep() que tenía el "antes"
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("#cart-total"), "1"));

        String totalCarrito = driver.findElement(By.cssSelector("#cart-total")).getText();
        System.out.println("Carrito después de agregar: " + totalCarrito);

        Assert.assertTrue(totalCarrito.contains("1"),
                "El carrito debe reflejar 1 unidad agregada. Total actual: " + totalCarrito);
    }
}