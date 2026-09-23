package com.abstracta.cursoselenium.tests;

import com.abstracta.cursoselenium.pages.HomePage;
import com.abstracta.cursoselenium.pages.ResultadosBusquedaPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BusquedaDataDrivenTest extends BaseTest {

    // { String termino, boolean esperaResultados, String descripcion }
    @DataProvider(name = "scenariosBusqueda")
    public Object[][] scenariosBusqueda() {
        return new Object[][] {
                // HAPPY PATH — confirmados contra el sitio real
                { "Samsung Galaxy", true, "producto existente (Samsung Galaxy Tab 10.1)" },
                { "iPhone",         true, "producto existente (iPhone)"                  },
                // UNHAPPY PATH — timestamp evita choques con el catálogo real
                { "ZZZProductoQueNoExiste" + System.currentTimeMillis(), false, "término inexistente" },
        };
    }

    @Test(dataProvider = "scenariosBusqueda", groups = { "regresion", "busqueda" },
            description = "Búsqueda con múltiples términos verifica resultados esperados")
    public void busqueda_MultiplesTerminos_ResultadoEsperado(
            String termino, boolean esperaResultados, String descripcion) {

        HomePage home = HomePage.abrir(driver, wait, BASE_URL);
        ResultadosBusquedaPage resultados = home.buscar(termino);

        System.out.println("Caso '" + descripcion + "' -> término: '" + termino
                + "' | hay resultados: " + resultados.hayResultados());

        if (esperaResultados) {
            Assert.assertTrue(resultados.hayResultados(),
                    "Caso '" + descripcion + "': '" + termino + "' debe dar resultados");
        } else {
            Assert.assertFalse(resultados.hayResultados(),
                    "Caso '" + descripcion + "': '" + termino + "' no debe dar resultados");
        }
    }
}