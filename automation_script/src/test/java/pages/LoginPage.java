package pages;

import Utilities.ElementActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class LoginPage {
    // variables
    final private String url="https://www.saucedemo.com/";
    // drivers
    final  WebDriver driver;
    // constructor
    public LoginPage(WebDriver driver){
        this.driver=driver;
    }
    // locators
    By Username_textBox=By.id("user-name");
    By Password_textBox=By.id("password");
    By Login_Btn =By.id("login-button");
    By Login_Err_Msg=By.cssSelector("[data-test='error']");
    // Actions
    public void navigate(){
        driver.get(url);
    }

    public void login(String username,String password){
       driver.findElement(Username_textBox).sendKeys(username);
       driver.findElement(Password_textBox).sendKeys(password);
       ElementActions.click(driver,Login_Btn);
    }

    public void clickBrowserBack(){
        driver.navigate().back();
    }

    // assertions
    public void AssertUserLogedin(){
        Assert.assertEquals(driver.getCurrentUrl(),"https://www.saucedemo.com/inventory.html");
        Assert.assertTrue(driver.findElement(By.xpath("//span[@class='title']")).isDisplayed());
    }
    public void AssertUsercannotLogedin(){
        Assert.assertTrue(driver.findElement(Login_Err_Msg).isDisplayed());
        Assert.assertEquals(driver.getCurrentUrl(),url);
    }

}
