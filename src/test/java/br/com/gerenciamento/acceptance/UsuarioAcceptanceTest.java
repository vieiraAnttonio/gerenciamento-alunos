package br.com.gerenciamento.acceptance;

import java.time.Duration;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class UsuarioAcceptanceTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void configurarNavegador() {
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.manage().window().maximize();
    }

    @After
    public void fecharNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void cadastrarUsuarioComSucesso() {
        long identificador = System.currentTimeMillis();

        String email = "usuario" + identificador + "@email.com";
        String nomeUsuario = "user" + identificador;

        driver.get("http://localhost:8080/cadastro");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("email"))
        );

        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("user")).sendKeys(nomeUsuario);
        driver.findElement(By.id("senha")).sendKeys("123456");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        wait.until(
                ExpectedConditions.urlToBe("http://localhost:8080/")
        );

        Assert.assertEquals(
                "http://localhost:8080/",
                driver.getCurrentUrl()
        );
    }
}