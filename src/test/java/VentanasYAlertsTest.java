import org.openqa.selenium.By;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class VentanasYAlertsTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String URL_HOME = "https://opencart.abstracta.us/";
    private static final String URL_LOGIN =
            "https://opencart.abstracta.us/index.php?route=account/login";

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();   // quit() cierra TODO, close() solo la activa
        }
    }

    @Test
    public void flujoEnDosTabsYAlert() {

        // ── PARTE 1: guardar el handle original ANTES de abrir nada ──
        driver.get(URL_HOME);
        String tabOriginal = driver.getWindowHandle();
        String tituloTab1 = driver.getTitle();

        // ── PARTE 2: abrir tab nueva (API de Selenium 4, cambia el foco sola) ──
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(URL_LOGIN);

        Assert.assertNotEquals(driver.getTitle(), tituloTab1,
                "Las dos tabs deben tener títulos diferentes");
        Assert.assertEquals(driver.getWindowHandles().size(), 2,
                "Deben existir exactamente 2 tabs");
        System.out.println("Tab 1: " + tituloTab1 + " | Tab 2: " + driver.getTitle());

        // Login fallido en la Tab 2 — mismo patrón de 3.1
        driver.findElement(By.id("input-email")).sendKeys("test@test.com");
        driver.findElement(By.id("input-password")).sendKeys("wrongpass");
        driver.findElement(By.cssSelector("input[type='submit'][value='Login']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".alert.alert-danger")));

        // ── PARTE 3: cerrar la tab activa y VOLVER — el switchTo es obligatorio ──
        driver.close();
        driver.switchTo().window(tabOriginal);

        Assert.assertEquals(driver.getTitle(), tituloTab1,
                "Debemos estar de vuelta en la Tab 1");
        Assert.assertEquals(driver.getWindowHandles().size(), 1,
                "Debe quedar solo 1 tab abierta");
        System.out.println("De vuelta en: " + driver.getTitle()
                + " | Tabs abiertas: " + driver.getWindowHandles().size());

        // ── PARTE 4: alert — esperar, leer, aceptar ──
        ((JavascriptExecutor) driver).executeScript("alert('Stock máximo: 5 unidades');");

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        String mensajeAlert = alert.getText();
        Assert.assertTrue(mensajeAlert.contains("Stock máximo"),
                "El alert debe informar el stock máximo. Recibido: " + mensajeAlert);
        alert.accept();

        // Tras aceptar, la página vuelve a ser interactuable
        WebElement campoBusqueda = driver.findElement(By.cssSelector("#search input"));
        Assert.assertTrue(campoBusqueda.isDisplayed(),
                "La página debe seguir operativa después de cerrar el alert");
        System.out.println("Alert manejado: '" + mensajeAlert + "' | página operativa: " + campoBusqueda.isDisplayed());
    }
}
