package com.abstracta.cursoselenium.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    private final By CAMPO_EMAIL = By.id("input-email");
    private final By CAMPO_PASSWORD = By.id("input-password");
    private final By BOTON_LOGIN = By.cssSelector("input[type='submit'][value='Login']");
    private final By MENSAJE_ERROR = By.cssSelector(".alert.alert-danger");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        this.wait
                .withMessage("No se cargó la página de Login. URL actual: " + driver.getCurrentUrl())
                .until(ExpectedConditions.visibilityOfElementLocated(CAMPO_EMAIL));
    }

    public static LoginPage abrir(WebDriver driver, WebDriverWait wait, String baseUrl) {
        driver.get(baseUrl + "/index.php?route=account/login");
        return new LoginPage(driver, wait);
    }

    public MiCuentaPage loginExitoso(String email, String password) {
        escribirEn(CAMPO_EMAIL, email);
        escribirEn(CAMPO_PASSWORD, password);
        clickEn(BOTON_LOGIN);
        return new MiCuentaPage(driver, wait);
    }

    @Step("Hacer login esperando error — email: {email}")
    public LoginPage loginEsperandoError(String email, String password) {
        escribirEn(CAMPO_EMAIL, email);
        escribirEn(CAMPO_PASSWORD, password);
        clickEn(BOTON_LOGIN);
        return this;
    }

    @Step("Obtener texto del mensaje de error")
    public String obtenerTextoDeError() {
        return obtenerTexto(MENSAJE_ERROR);
    }
}