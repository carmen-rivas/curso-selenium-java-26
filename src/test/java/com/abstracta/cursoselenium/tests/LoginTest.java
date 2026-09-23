package com.abstracta.cursoselenium.tests;

import com.abstracta.cursoselenium.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.qameta.allure.SeverityLevel.CRITICAL;

@Epic("Autenticación")
@Feature("Login")
public class LoginTest extends BaseTest {

    @Test(groups = { "smoke", "login" },
            description = "Login con credenciales inválidas debe mostrar error")
    @Story("Login fallido")
    @Severity(CRITICAL)
    public void loginConCredencialesInvalidas() {
        LoginPage login = LoginPage.abrir(driver, wait, BASE_URL);
        login.loginEsperandoError("usuario-que-no-existe@falso.com", "password-incorrecto-12345");

        Assert.assertTrue(login.obtenerTextoDeError().contains("Warning"),
                "Debe mostrarse advertencia de credenciales inválidas");
    }

    @Test
    public void verificarAtributosDelFormulario() {
        driver.get(BASE_URL + "/index.php?route=account/login");

        WebElement campoEmail = driver.findElement(By.id("input-email"));
        WebElement campoPassword = driver.findElement(By.id("input-password"));

        String tipoEmail = campoEmail.getAttribute("type");
        Assert.assertTrue(tipoEmail.equals("text") || tipoEmail.equals("email"),
                "El campo email debe ser de tipo text o email. Es: " + tipoEmail);

        Assert.assertEquals(campoPassword.getAttribute("type"), "password",
                "El campo password debe enmascarar lo que se escribe");

        System.out.println("getText() en input vacío  -> '" + campoEmail.getText() + "'");
        System.out.println("getAttribute('value')     -> '"
                + campoEmail.getAttribute("value") + "'");

        Assert.assertEquals(campoEmail.getText(), "",
                "getText() en un input siempre devuelve cadena vacía");
    }
}