package com.abstracta.cursoselenium.tests;

import com.abstracta.cursoselenium.pages.LoginPage;
import com.abstracta.cursoselenium.pages.MiCuentaPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginBifurcacionTest extends BaseTest {

    // Reemplazá por la cuenta que registes vos mismo
    private static final String EMAIL_VALIDO = "testacademy@example.com";
    private static final String PASSWORD_VALIDA = "Test";

    @Test
    public void login_ConCredencialesValidas_NavegaAMiCuenta() {
        LoginPage login = LoginPage.abrir(driver, wait, BASE_URL);

        // Login exitoso usa el método que retorna MiCuentaPage: seguís a otra página
        MiCuentaPage miCuenta = login.loginExitoso(EMAIL_VALIDO, PASSWORD_VALIDA);

        Assert.assertTrue(miCuenta.estaCargada(),
                "Tras un login exitoso, la página My Account debe estar cargada");
    }

    @Test
    public void login_ConCredencialesInvalidas_PermaneceEnLogin() {
        LoginPage login = LoginPage.abrir(driver, wait, BASE_URL);

        login.loginEsperandoError("invalido@test.com", "wrong");

        Assert.assertTrue(login.obtenerTextoDeError().contains("Warning"),
                "El mensaje de error debe ser visible");
    }
}
