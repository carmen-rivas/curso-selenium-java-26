import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class FormularioComplejoTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String URL_HOME = "https://opencart.abstracta.us/";
    private static final String URL_PRODUCTO =
            "https://opencart.abstracta.us/index.php?route=product/product&product_id=42";

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void interactuarConDropdownCustomSelectYCheckbox() {

        // ── PARTE 1: dropdown CUSTOM — abrir, esperar, leer ──
        driver.get(URL_HOME);

        WebElement toggleMyAccount = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("#top a[title='My Account']")));
        toggleMyAccount.click();

        // El menú no existe visible al cargar: aparece recién tras el click.
        // Esperamos la condición real, no asumimos un tiempo fijo
        List<WebElement> opcionesMenu = wait.until(ExpectedConditions
                .visibilityOfAllElementsLocatedBy(
                        By.cssSelector("a[title='My Account'] + ul.dropdown-menu a")));

        boolean tieneRegister = opcionesMenu.stream().anyMatch(o -> o.getText().contains("Register"));
        boolean tieneLogin = opcionesMenu.stream().anyMatch(o -> o.getText().contains("Login"));
        Assert.assertTrue(tieneRegister && tieneLogin,
                "El menú 'My Account' debe ofrecer Register y Login");
        System.out.println("Menú 'My Account' -> Register: " + tieneRegister + " | Login: " + tieneLogin);

        // ── PARTE 2: select NATIVO — opciones de color del producto ──
        driver.get(URL_PRODUCTO);

        // OpenCart nombra estos <select> como "option[ID]" — el ID es dinámico
        // por producto, pero el prefijo "option" es estable: por eso [name^='option']
        Select selectColor = new Select(wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("select[name^='option']"))));
        selectColor.selectByIndex(1);   // índice 0 es "--- Please Select ---"

        String colorElegido = selectColor.getFirstSelectedOption().getText();
        System.out.println("Color elegido: " + colorElegido);
        Assert.assertTrue(colorElegido.contains("Red"),
                "El índice 1 del select debe corresponder a 'Red'. Encontrado: " + colorElegido);

        // ── PARTE 3: checkbox — verificar ANTES de actuar ──
        WebElement checkboxOpcion = driver.findElements(
                By.cssSelector("input[type='checkbox'][name^='option']")).get(0);

        boolean estabaDesmarcado = !checkboxOpcion.isSelected();
        Assert.assertTrue(estabaDesmarcado,
                "El checkbox de opción debe estar desmarcado al cargar la página");

        checkboxOpcion.click();

        boolean quedoMarcado = checkboxOpcion.isSelected();
        Assert.assertTrue(quedoMarcado,
                "El checkbox debe quedar marcado después del click");

        System.out.println("Checkbox -> antes: desmarcado=" + estabaDesmarcado
                + " | después: marcado=" + quedoMarcado);
    }
}