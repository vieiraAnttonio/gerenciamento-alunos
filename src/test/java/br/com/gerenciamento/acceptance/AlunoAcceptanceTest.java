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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AlunoAcceptanceTest {

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
    public void cadastrarAlunoComSucesso() {
        long identificador = System.currentTimeMillis();

        String nomeAluno = "Aluno Selenium";
        String matricula = "MAT" + identificador;

        driver.get("http://localhost:8080/inserirAlunos");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("nome"))
        );

        driver.findElement(By.id("nome")).sendKeys(nomeAluno);

        Select curso = new Select(
                driver.findElement(By.id("curso"))
        );
        curso.selectByValue("ADMINISTRACAO");

        driver.findElement(By.id("matricula")).sendKeys(matricula);

        Select turno = new Select(
                driver.findElement(By.id("turno"))
        );
        turno.selectByValue("NOTURNO");

        Select status = new Select(
                driver.findElement(By.id("status"))
        );
        status.selectByValue("ATIVO");

        driver.findElement(
        By.xpath("//button[@type='submit' and normalize-space()='Salvar']")
).click();

        wait.until(
                ExpectedConditions.urlContains("/alunos-adicionados")
        );

        Assert.assertTrue(
                driver.getCurrentUrl().contains("/alunos-adicionados")
        );

        Assert.assertTrue(
                driver.getPageSource().contains(nomeAluno)
        );
    }
}
