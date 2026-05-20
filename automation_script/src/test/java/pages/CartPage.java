package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class  CartPage {

    WebDriver driver;

    public CartPage(WebDriver driver){

        this.driver = driver;
    }

    By cartIcon = By.className("shopping_cart_link");

    By addToCartButton = By.id("add-to-cart-sauce-labs-backpack");

    By removeButton = By.id("remove-sauce-labs-backpack");

    public void clickCartIcon(){

        driver.findElement(cartIcon).click();
    }

    public void clickAddToCart(){

        driver.findElement(addToCartButton).click();
    }

    public void clickRemoveButton(){

        driver.findElement(removeButton).click();
    }

    // للتحقق إن الـ badge بيتغير
    By cartBadge = By.className("shopping_cart_badge");

    public String getCartBadgeCount(){

        return driver.findElement(cartBadge).getText();
    }

    // للتحقق إن الـ item موجود في الـ cart page
    By cartItem = By.className("cart_item");

    public boolean isItemInCart(){

        return !driver.findElements(cartItem).isEmpty();
    }

    // للتحقق إن الـ Remove button اتغير لـ Add to cart
    By addToCartAfterRemove = By.id("add-to-cart-sauce-labs-backpack");

    public boolean isAddToCartVisible(){

        return driver.findElement(addToCartAfterRemove).isDisplayed();
    }

    // تحقق إن الـ cart page فاضية
    By removeButtonCart = By.className("cart_item");

    public boolean isCartEmpty(){

        return driver.findElements(removeButtonCart).isEmpty();
    }

    // Checkout button
    By checkoutButton = By.id("checkout");

    public void clickCheckout(){

        driver.findElement(checkoutButton).click();
    }

    // Sort dropdown
    By sortDropdown = By.className("product_sort_container");

    public void selectSort(){
        driver.findElement(sortDropdown).click();
    }

    // عدد الـ items في الـ cart
    By cartItems = By.className("cart_item");

    public int getCartItemsCount(){

        return driver.findElements(cartItems).size();
    }


    By addBikeLight = By.id("add-to-cart-sauce-labs-bike-light");
    By addBoltTshirt = By.id("add-to-cart-sauce-labs-bolt-t-shirt");

    public void clickAddBikeLight(){

        driver.findElement(addBikeLight).click();
    }

    public void clickAddBoltTshirt(){

        driver.findElement(addBoltTshirt).click();
    }
}

