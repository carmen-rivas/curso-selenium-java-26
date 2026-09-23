import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PrimerTest {

    // A nivel de clase, para que @BeforeMethod, @Test y @AfterMethod la compartan
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Selenium Manager configura ChromeDriver automáticamente
        driver = new ChromeDriver();
        driver.get("https://opencart.abstracta.us");
    }

    @Test
    public void verificarTituloDeLaTienda() {
        Assert.assertEquals(driver.getTitle(), "Your Store",
                "El título de la página no coincide con el esperado");
    }

    @Test
    public void verificarCarritoVacio() {
        WebElement linkCarrito = driver.findElement(
                By.cssSelector("[title='Shopping Cart']"));
        linkCarrito.click();

        WebElement mensajeCarrito = driver.findElement(
                By.cssSelector("#content p"));

        Assert.assertEquals(mensajeCarrito.getText(),
                "Your shopping cart is empty!",
                "El carrito debería estar vacío al iniciar");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

