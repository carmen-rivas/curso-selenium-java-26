import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class TimingLoginTest {

    private WebDriver driver;
    private static final String URL =
            "https://opencart.abstracta.us/index.php?route=account/login";

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(URL);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ❌ La forma incorrecta — espera fija, apuesta a que el servidor
    // siempre responde en menos de 2 segundos
    @Test
    public void loginConThreadSleep() throws InterruptedException {
        driver.findElement(By.id("input-email")).sendKeys("usuario-que-no-existe@falso.com");
        driver.findElement(By.id("input-password")).sendKeys("password-incorrecto-12345");

        // El cronómetro arranca acá, no en setUp(): así el arranque de Chrome
        // no contamina la comparación entre las dos estrategias
        long inicioAccion = System.currentTimeMillis();
        driver.findElement(By.cssSelector("input[type='submit'][value='Login']")).click();

        Thread.sleep(2000);

        WebElement mensajeError = driver.findElement(By.cssSelector(".alert.alert-danger"));
        Assert.assertTrue(mensajeError.getText().contains("Warning"),
                "Debe mostrarse el mensaje de credenciales inválidas");

        System.out.println(">>> [Thread.sleep] Duración de la acción: "
                + (System.currentTimeMillis() - inicioAccion) + " ms");
    }

    // ✅ La forma correcta — espera exactamente lo que el servidor tarda,
    // ni un milisegundo más
    @Test
    public void loginConExplicitWait() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.findElement(By.id("input-email")).sendKeys("usuario-que-no-existe@falso.com");
        driver.findElement(By.id("input-password")).sendKeys("password-incorrecto-12345");

        long inicioAccion = System.currentTimeMillis();
        driver.findElement(By.cssSelector("input[type='submit'][value='Login']")).click();

        WebElement mensajeError = wait
                .withMessage("El mensaje de error no apareció en 10 segundos")
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".alert.alert-danger")));

        Assert.assertTrue(mensajeError.getText().contains("Warning"),
                "Debe mostrarse el mensaje de credenciales inválidas");

        System.out.println(">>> [WebDriverWait] Duración de la acción: "
                + (System.currentTimeMillis() - inicioAccion) + " ms");
    }
}