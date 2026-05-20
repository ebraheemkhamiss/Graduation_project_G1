package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage {

    private WebDriver driver;

    // Locators
    private By filterDropdown  = By.className("product_sort_container");
    private By productNames    = By.className("inventory_item_name");
    private By productPrices   = By.className("inventory_item_price");
    private By cartBadge       = By.className("shopping_cart_badge");
    private By filterIcon      = By.className("select_container");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    // بتفتح الـ dropdown
    public void selectFilter(String filterValue) {
        Select select = new Select(driver.findElement(filterDropdown));
        select.selectByVisibleText(filterValue);
    }

    // بترجع الـ filter اللي متختار دلوقتي
    public String getSelectedFilter() {
        Select select = new Select(driver.findElement(filterDropdown));
        return select.getFirstSelectedOption().getText();
    }

    // بترجع أسماء المنتجات بالترتيب
    public List<String> getProductNames() {
        return driver.findElements(productNames)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    // بترجع أسعار المنتجات بالترتيب
    public List<Double> getProductPrices() {
        return driver.findElements(productPrices)
                .stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());
    }

    // بترجع عدد المنتجات
    public int getProductCount() {
        return driver.findElements(productNames).size();
    }

    // بتتحقق إن الـ dropdown ظاهر
    public boolean isFilterVisible() {
        return driver.findElement(filterDropdown).isDisplayed();
    }
}
