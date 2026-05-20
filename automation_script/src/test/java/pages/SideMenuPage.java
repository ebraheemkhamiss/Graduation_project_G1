package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SideMenuPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By hamburgerIcon  = By.id("react-burger-menu-btn");
    private By closeButton    = By.id("react-burger-cross-btn");
    private By allItemsLink   = By.id("inventory_sidebar_link");
    private By aboutLink      = By.id("about_sidebar_link");
    private By logoutLink     = By.id("logout_sidebar_link");
    private By resetLink      = By.id("reset_sidebar_link");
    private By menuContainer  = By.className("bm-menu-wrap");

    public SideMenuPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void openMenu() {
        driver.findElement(hamburgerIcon).click();

        // استنى لحد ما المنيو يبان في الصفحة
        wait.until(ExpectedConditions.visibilityOfElementLocated(allItemsLink));
    }

    public void closeMenu() {
        // استخدم JavaScript بدل الـ click العادي
        org.openqa.selenium.JavascriptExecutor js =
                (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();",
                driver.findElement(closeButton));

        // استنى لحد ما الـ X button يختفي
        wait.until(ExpectedConditions.invisibilityOfElementLocated(closeButton));
    }

    public boolean isMenuOpen() {
        String ariaHidden = driver.findElement(menuContainer)
                .getAttribute("aria-hidden");
        // مفتوح لو aria-hidden = "false" أو اتشالت خالص
        return !"true".equals(ariaHidden);
    }
    private void jsClick(By locator) {
        org.openqa.selenium.JavascriptExecutor js =
                (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();",
                driver.findElement(locator));
    }

    public void clickAllItems() { jsClick(allItemsLink); }
    public void clickAbout()    { jsClick(aboutLink);    }
    public void clickLogout()   { jsClick(logoutLink);   }
    public void clickReset()    { jsClick(resetLink);    }

    public boolean areAllMenuItemsVisible() {
        return driver.findElement(allItemsLink).isDisplayed()
                && driver.findElement(aboutLink).isDisplayed()
                && driver.findElement(logoutLink).isDisplayed()
                && driver.findElement(resetLink).isDisplayed();
    }
}
