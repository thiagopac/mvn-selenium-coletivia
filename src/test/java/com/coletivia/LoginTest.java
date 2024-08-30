package com.coletivia;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static org.junit.Assert.assertEquals;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "/Applications/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-web-security");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testLoginSuccess() {
        System.out.println("Abrindo a página de login...");
        driver.get("http://localhost:4200/auth/login");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("email")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        
        driver.findElement(By.name("email")).sendKeys("john@doe.com");
        driver.findElement(By.name("password")).sendKeys("password");
        
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/start"));

        assertEquals("http://localhost:4200/start", driver.getCurrentUrl());
    }

    @Test
    public void testLoginFailure() {
        System.out.println("Abrindo a página de login...");
        driver.get("http://localhost:4200/auth/login");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("email")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));

        driver.findElement(By.name("email")).sendKeys("wrong@example.com");
        driver.findElement(By.name("password")).sendKeys("wrongpassword");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-text")));

        String errorMessage = driver.findElement(By.cssSelector(".alert-text")).getText();
        assertEquals("Você não possui uma conta cadastrada com o e-mail informado. Crie uma conta clicando acima em Cadastre-se agora", errorMessage);
    }

    @Test
    public void testGoogleLoginRedirect() {
        System.out.println("Verificando redirecionamento ao clicar em Entrar com conta Google...");
        driver.get("http://localhost:4200/auth/login");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),'Entrar com conta Google')]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Entrar com conta Google')]")));

        driver.findElement(By.xpath("//a[contains(text(),'Entrar com conta Google')]")).click();

        wait.until(ExpectedConditions.urlContains("https://accounts.google.com/"));

        String currentUrl = driver.getCurrentUrl();
        assertEquals(true, currentUrl.contains("https://accounts.google.com/"));
    }

    @Test
    public void testForgotPasswordNavigation() {
        System.out.println("Verificando navegação para a página Esqueceu a senha...");
        driver.get("http://localhost:4200/auth/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Esqueceu a senha ?")));

        driver.findElement(By.linkText("Esqueceu a senha ?")).click();

        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/auth/forgot-password"));

        assertEquals("http://localhost:4200/auth/forgot-password", driver.getCurrentUrl());
    }

    @Test
    public void testRegistrationNavigation() {
        System.out.println("Verificando navegação para a página de Cadastro...");
        driver.get("http://localhost:4200/auth/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Cadastre-se agora!")));

        driver.findElement(By.linkText("Cadastre-se agora!")).click();

        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/auth/registration"));

        assertEquals("http://localhost:4200/auth/registration", driver.getCurrentUrl());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
