package br.com.infnet.rodrigo_loureiro_pb_tp4.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@Getter
public class BaseTest {
    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions()
                .addArguments("--incognito")
                .addArguments("--headless=new")
                .addArguments("--no-sandbox")
                .addArguments("--disable-dev-shm-usage")
                .addArguments("--disable-gpu");

        this.driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }
}
