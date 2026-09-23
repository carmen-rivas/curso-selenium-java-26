package com.abstracta.cursoselenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class BusquedaProductoTest extends BaseTest {

    @Test
    public void buscarProductoExistente() {
        driver.get(BASE_URL);  // antes vivía en el setUp() propio — ahora hay que declararlo acá

        driver.findElement(By.cssSelector("#search input")).sendKeys("MacBook");
        driver.findElement(By.cssSelector("#search button")).click();

        List<WebElement> resultados = driver.findElements(
                By.cssSelector(".product-thumb h4 a"));

        Assert.assertFalse(resultados.isEmpty(),
                "La búsqueda de 'MacBook' debe devolver al menos un resultado. "
                        + "URL actual: " + driver.getCurrentUrl());

        String primerTitulo = resultados.get(0).getText();
        System.out.println("Primer resultado: " + primerTitulo);

        Assert.assertTrue(primerTitulo.toLowerCase().contains("macbook"),
                "El primer resultado debe contener 'macbook'. Encontrado: " + primerTitulo);
    }

    @Test
    public void buscarProductoInexistente() {
        driver.get(BASE_URL);

        String terminoInventado = "ZZZproductoQueNoExiste" + System.currentTimeMillis();

        driver.findElement(By.cssSelector("#search input")).sendKeys(terminoInventado);
        driver.findElement(By.cssSelector("#search button")).click();

        WebElement titulo = driver.findElement(By.cssSelector("#content h1"));
        Assert.assertTrue(titulo.getText().contains(terminoInventado),
                "El título debe confirmar que se buscó el término inventado. "
                        + "Texto real: '" + titulo.getText() + "'");

        List<WebElement> resultados = driver.findElements(
                By.cssSelector(".product-thumb h4 a"));

        Assert.assertTrue(resultados.isEmpty(),
                "Un término inventado no debe devolver productos. "
                        + "Devolvió: " + resultados.size());
    }

    @Test
    public void categoriaLaptops_SeLocalizaPorAtributoParcial() {
        driver.get(BASE_URL);

        WebElement linkLaptops = driver.findElement(By.cssSelector("a[href*='path=18']"));

        Assert.assertTrue(linkLaptops.isDisplayed(),
                "El link a la categoría Laptops & Notebooks debe estar visible");
        Assert.assertTrue(linkLaptops.getText().contains("Laptop"),
                "El texto del link debe corresponder a Laptops & Notebooks. "
                        + "Encontrado: " + linkLaptops.getText());
    }
}