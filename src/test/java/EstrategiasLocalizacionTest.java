import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Selenium 4.44 trae Selenium Manager embebido, así que no hace falta
 * configurar el path del chromedriver a mano.
 */
public class EstrategiasLocalizacionTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String URL = "https://opencart.abstracta.us/";

    @BeforeMethod
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(URL);
    }

    @AfterMethod
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // 1) Campo de búsqueda del header
    // Pregunta 1 del marco: el contenedor "#search" tiene id único y estable.
    @Test
    void testCampoBusquedaEsVisible() {
        WebElement campoBusqueda = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#search input")));

        assertTrue(campoBusqueda.isDisplayed(), "El campo de búsqueda debería estar visible");
        assertTrue(campoBusqueda.isEnabled(), "El campo de búsqueda debería estar habilitado");
    }

    // 2) Botón de búsqueda (lupa)
    // Pregunta 2 del marco: sin id propio, pero único dentro del contenedor #search.
    @Test
    void testBotonBusquedaEsClicleable() {
        WebElement botonBusqueda = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#search button")));

        assertTrue(botonBusqueda.isDisplayed(), "El botón de búsqueda debería estar visible");
        assertTrue(botonBusqueda.isEnabled(), "El botón de búsqueda debería estar habilitado");
    }

    // 3) Link "My Account"
    // Pregunta 2 del marco: atributo title="My Account" único dentro de #top (header).
    @Test
    void testLinkMyAccountEsVisible() {
        WebElement linkMyAccount = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("#top a[title='My Account']")));

        assertTrue(linkMyAccount.isDisplayed(), "El link 'My Account' debería estar visible");
        assertTrue(linkMyAccount.isEnabled(), "El link 'My Account' debería estar habilitado");
    }

    // 4) Título del primer producto destacado
    // Pregunta 3 del marco: no hay atributo único, se resuelve por posición en el DOM.
    @Test
    void testTituloPrimerProductoEsVisible() {
        WebElement tituloPrimerProducto = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".product-layout:nth-of-type(1) h4 a")));

        assertTrue(tituloPrimerProducto.isDisplayed(), "El título del primer producto debería estar visible");

        String textoProducto = tituloPrimerProducto.getText();
        System.out.println("Título del primer producto: " + textoProducto);

        assertFalse(textoProducto.isEmpty(), "El título del primer producto no debería estar vacío");
    }
}
