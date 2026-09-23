import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.Dimension;

public class RegistroLocalizadoresTest {

    private WebDriver driver;
    private static final String URL =
            "https://opencart.abstracta.us/index.php?route=account/register";

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(500, 800));
        driver.get(URL);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // XPath donde CSS no llega: buscar por TEXTO VISIBLE
    @Test
    public void localizarPorTextoVisible() {
        WebElement tituloPagina = driver.findElement(
                By.xpath("//h1[contains(text(),'Account')]"));

        Assert.assertTrue(tituloPagina.isDisplayed(),
                "El título de la página de cuenta debe estar visible");
    }

    // XPath por eje de hermandad: del label a su input hermano
    @Test
    public void localizarInputDesdeSuLabel() {
        WebElement inputEmail = driver.findElement(
                By.xpath("//label[contains(text(),'E-Mail')]/following-sibling::div//input"));

        System.out.println("Campo encontrado desde el label — id: "
                + inputEmail.getAttribute("id"));

        Assert.assertEquals(inputEmail.getAttribute("id"), "input-email",
                "El input hermano del label 'E-Mail' debe ser el campo de email");
    }

    // Relative Locator: por posición visual, usando un id conocido como ancla
    @Test
    public void localizarPorPosicionVisual() {
        WebElement campoApellido = driver.findElement(By.id("input-lastname"));

        WebElement campoDebajo = driver.findElement(
                RelativeLocator.with(By.tagName("input")).below(campoApellido));

        String idEncontrado = campoDebajo.getAttribute("id");
        System.out.println("Campo debajo de 'Last Name' — id: " + idEncontrado
                + " | type: " + campoDebajo.getAttribute("type"));

        Assert.assertEquals(idEncontrado, "input-email",
                "Debajo del apellido debería estar el campo de email. Encontrado: " + idEncontrado);
    }
}
