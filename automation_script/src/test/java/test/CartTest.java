package test;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;

public class CartTest extends BaseTest {

    @Test(description=" Add item and verify badge shows 1")
    public void verifyUserCanAddItemToCartAndBadgeShowsOne(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        CartPage cartPage = new CartPage(driver);
        cartPage.clickAddToCart();
        Assert.assertEquals(cartPage.getCartBadgeCount(), "1");
    }

    @Test(description=" Add item and verify it appears in cart page")
    public void verifyItemAppearsInCartPage(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        CartPage cartPage = new CartPage(driver);
        cartPage.clickAddToCart();
        cartPage.clickCartIcon();
        Assert.assertTrue(cartPage.isItemInCart());
    }

    @Test(description=" Remove item from inventory and verify button reverts to Add to cart")
    public void verifyUserCanRemoveItemFromInventory(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        CartPage cartPage = new CartPage(driver);
        cartPage.clickAddToCart();
        cartPage.clickRemoveButton();
        Assert.assertTrue(cartPage.isAddToCartVisible());
    }

    @Test(description=" Add then remove and verify badge disappears")
    public void verifyBadgeDisappearsAfterRemove(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        CartPage cartPage = new CartPage(driver);
        cartPage.clickAddToCart();
        cartPage.clickRemoveButton();
        Assert.assertEquals(
                driver.findElements(
                        org.openqa.selenium.By.className("shopping_cart_badge")
                ).size(), 0
        );
    }

    @Test(description=" Add item and remove from cart page")
    public void verifyUserCanRemoveItemFromCartPage(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");
        CartPage cartPage = new CartPage(driver);
        cartPage.clickAddToCart();    // اضغط Add to cart
        cartPage.clickCartIcon();     // روح لـ cart page
        cartPage.clickRemoveButton(); // احذف الـ item
        Assert.assertTrue(cartPage.isCartEmpty()); // تأكد Cart فاضية
    }

    @Test(description=" Add item verify cart icon navigates to cart page")
    public void verifyCartIconNavigatesToCartPage(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        CartPage cartPage = new CartPage(driver);
        cartPage.clickAddToCart();
        cartPage.clickCartIcon();
        Assert.assertTrue(
                driver.getCurrentUrl().contains("cart.html")
        );
    }


    @Test(description=" Checkout with empty cart should show error")
    public void verifyCheckoutWithEmptyCartShowsError(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        CartPage cartPage = new CartPage(driver);
        cartPage.clickCartIcon();
        cartPage.clickCartIcon();
        cartPage.clickCheckout();

        Assert.assertFalse(
                driver.getCurrentUrl().contains("checkout-step-one")
        );
    }

    @Test(description=" Cart icon after remove should not remove another item")
    public void verifyCartIconDoesNotRemoveExtraItems(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        CartPage cartPage = new CartPage(driver);
        cartPage.clickAddToCart();
        cartPage.clickAddBikeLight();
        cartPage.clickAddBoltTshirt();
        cartPage.clickCartIcon();
        cartPage.clickRemoveButton();
        cartPage.clickCartIcon();
        Assert.assertEquals(
                cartPage.getCartItemsCount(), 2,
                "Bug: Cart icon removed extra item!"
        );
    }

}
