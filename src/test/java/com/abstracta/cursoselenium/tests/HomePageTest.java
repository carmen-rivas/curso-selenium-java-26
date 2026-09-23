package com.abstracta.cursoselenium.tests;

import com.abstracta.cursoselenium.pages.HomePage;
import com.abstracta.cursoselenium.pages.ResultadosBusquedaPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomePageTest extends BaseTest {

    @Test
    public void buscar_TerminoExistente_MuestraResultados() {
        HomePage home = HomePage.abrir(driver, wait, BASE_URL);
        ResultadosBusquedaPage resultados = home.buscar("MacBook");

        boolean hayResultados = resultados.hayResultados();
        String primerNombre = resultados.obtenerNombrePrimerResultado();
        System.out.println("Hay resultados: " + hayResultados + " | Primer resultado: " + primerNombre);

        Assert.assertTrue(hayResultados, "La búsqueda de 'MacBook' debe devolver resultados");
    }

    @Test(groups = { "smoke", "homepage" },
            description = "La homepage carga con el título correcto")
    public void homepage_AlCargar_TituloEsCorrecto() {
        HomePage home = HomePage.abrir(driver, wait, BASE_URL);
        String titulo = home.obtenerTitulo();
        System.out.println("Título de la homepage: " + titulo);

        Assert.assertEquals(titulo, "Your Store",
                "Título incorrecto en " + BASE_URL);
    }
}