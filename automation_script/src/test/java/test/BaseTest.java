package test;

import Utilities.ScreenShotUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.LoginPage;

import java.time.Duration;

public class BaseTest {
    WebDriver driver;
    LoginPage loginPage;
    protected WebDriverWait wait;

    // Common test constants used across tests
    protected final String URL = "https://www.saucedemo.com";
    protected final String USERNAME = "standard_user";
    protected final String PASSWORD = "secret_sauce";
    @BeforeMethod
    public void preconditions() {
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginPage.navigate();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
         if (result.getStatus() == ITestResult.FAILURE) {
             ScreenShotUtility.takeScreenshot(driver, result.getName());
         }
        if (driver != null) {
            driver.quit();
        }
    }
}
