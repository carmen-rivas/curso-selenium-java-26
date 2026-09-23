package com.abstracta.cursoselenium.tests;

import com.abstracta.cursoselenium.pages.HomePage;
import com.abstracta.cursoselenium.pages.ProductoPage;
import com.abstracta.cursoselenium.pages.ResultadosBusquedaPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FlujosCompletosTest extends BaseTest {

    @Test
    public void flujoBusqueda_ProductoExistente_MuestraDetalle() {

        // Encadenamiento de retornos: cada línea usa el tipo que devolvió
        // la anterior sin locators, sin findElement, sin URLs
        HomePage home = HomePage.abrir(driver, wait, BASE_URL);
        ResultadosBusquedaPage resultados = home.buscar("MacBook");
        ProductoPage producto = resultados.abrirPrimerResultado();
        String nombreProducto = producto.obtenerNombreDelProducto();

        System.out.println("Producto abierto: " + nombreProducto);

        Assert.assertFalse(nombreProducto.isEmpty(),
                "El nombre del producto no debe estar vacío");
    }
}
