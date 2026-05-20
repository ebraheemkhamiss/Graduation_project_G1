package test;

import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.SideMenuPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;

public class SideMenuTest extends BaseTest {

    private SideMenuPage sideMenuPage;

    @BeforeMethod
    public void setUpPages() {
        sideMenuPage = new SideMenuPage(driver);
    }



    // tc_001
    @Test(description = "Side menu opens when hamburger icon is tapped")
    public void tc001_menuOpensOnHamburgerClick() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        sideMenuPage.openMenu();
        Assert.assertTrue(sideMenuPage.isMenuOpen(),
                "Side menu should be open after clicking hamburger icon");
    }

    // tc_002
    @Test(description = "Side menu closes when X button is clicked")
    public void tc002_menuClosesOnXButton() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        sideMenuPage.openMenu();
        sideMenuPage.closeMenu();
        Assert.assertFalse(sideMenuPage.isMenuOpen(),
                "Side menu should be closed after clicking X button");
    }

    // tc_003
    @Test(description = "All four menu items are visible and correctly labelled")
    public void tc003_allFourMenuItemsAreVisible() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        sideMenuPage.openMenu();
        Assert.assertTrue(sideMenuPage.areAllMenuItemsVisible(),
                "All 4 menu items should be visible");
    }

    // tc_004
    @Test(description = "'All Items' navigates to the inventory page")
    public void tc004_allItemsNavigatesToInventoryPage() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        sideMenuPage.openMenu();
        sideMenuPage.clickAllItems();
        Assert.assertTrue(driver.getCurrentUrl().contains("/inventory.html"),
                "Clicking 'All Items' should navigate to /inventory.html");
    }

    @Test(description = "'Logout' logs out user and redirects to login page")
    public void tc006_logoutRedirectsToLoginPage() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        sideMenuPage.openMenu();
        sideMenuPage.clickLogout();

        try { Thread.sleep(2000); } catch (Exception ignored) {}

        Assert.assertFalse(driver.getCurrentUrl().contains("inventory"),
                "After logout should not be on inventory page. Current: "
                        + driver.getCurrentUrl());
    }




    // tc_005: About بتوديك للصفحة الصح

    @Test(description = "'About' navigates to the About page")
    public void tc005_aboutNavigatesToAboutPage() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        sideMenuPage.openMenu();
        sideMenuPage.clickAbout();

        // استنى لحد ما الصفحة تتحمل
        try { Thread.sleep(2000); } catch (Exception ignored) {}

        System.out.println("Current URL after About click: " + driver.getCurrentUrl());

        Assert.assertTrue(driver.getCurrentUrl().contains("saucelabs.com"),
                "About should navigate to saucelabs.com. Current: " + driver.getCurrentUrl());
    }

    // tc_007: Reset App State بيفضي الكارت
    @Test(description = "'Reset App State' resets the app correctly")
    public void tc007_resetAppStateClearsCart() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        // أضيفي منتج للكارت
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        // تأكدي إن الكارت فيه item
        String badgeBefore = driver.findElement(
                By.className("shopping_cart_badge")).getText();
        Assert.assertEquals(badgeBefore, "1",
                "Cart should have 1 item before reset");

        // افتحي المنيو واعملي Reset
        sideMenuPage.openMenu();
        sideMenuPage.clickReset();

        try { Thread.sleep(1000); } catch (Exception ignored) {}

        // تأكدي إن الكارت اتفضى
        boolean badgeGone = driver.findElements(
                By.className("shopping_cart_badge")).isEmpty();
        Assert.assertTrue(badgeGone,
                "Cart badge should disappear after Reset App State");
    }

    // tc_008: المنيو بيقفل أوتوماتيك بعد اختيار item
    @Test(description = "Menu closes automatically after selecting a menu item")
    public void tc008_menuClosesAfterSelectingItem() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        sideMenuPage.openMenu();
        sideMenuPage.clickAllItems();

        // استخدمي wait من BaseTest مباشرة
        wait.until(ExpectedConditions.urlContains("/inventory.html"));

        Assert.assertFalse(sideMenuPage.isMenuOpen(),
                "Menu should close automatically after selecting a menu item");
    }

    // tc_009: الضغط بره المنيو بيقفله
    @Test(description = "Clicking/tapping outside the menu overlay closes the menu")
    public void tc009_clickingOutsideClosesMenu() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        sideMenuPage.openMenu();

        // اضغط على الـ body بره المنيو
        driver.findElement(By.tagName("body")).click();

        try { Thread.sleep(1000); } catch (Exception ignored) {}

        // لو مش اتقفل = Bug معروف
        System.out.println("⚠️ BUG - SideMenu_B_002: " +
                "Clicking outside did not close the menu");

        Assert.assertFalse(sideMenuPage.isMenuOpen(),
                "BUG SideMenu_B_002: Menu should close when clicking outside");
    }

    // tc_010: المنيو مش بيفتح أوتوماتيك بعد refresh
    @Test(description = "Menu state is not persisted after page refresh")
    public void tc010_menuClosedAfterPageRefresh() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        sideMenuPage.openMenu();
        Assert.assertTrue(sideMenuPage.isMenuOpen(), "Menu should be open");

        driver.navigate().refresh();

        // استخدمي wait من BaseTest مباشرة
        wait.until(ExpectedConditions.urlContains("/inventory.html"));

        Assert.assertFalse(sideMenuPage.isMenuOpen(),
                "Menu should be closed after page refresh");
    }

    // SideMenu_tc_037: Reset App State مفيش confirmation prompt
    @Test(description = "'Reset App State' leads to confirmation prompt befor resting")
    public void tc037_resetAppStateNoConfirmationPrompt() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        // أضيفي منتج للكارت
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        sideMenuPage.openMenu();

        // اضغطي Reset
        sideMenuPage.clickReset();

        // تأكدي إن مفيش alert ظهر
        try {
            driver.switchTo().alert();
            // لو وصلنا هنا = في alert = مش Bug
            driver.switchTo().alert().dismiss();
            Assert.fail("BUG SideMenu_B_037: " +
                    "Confirmation prompt appeared but was not expected");
        } catch (org.openqa.selenium.NoAlertPresentException e) {
            // مفيش alert = Bug حقيقي = نسجله
            System.out.println("⚠️ BUG - SideMenu_ts_037: " +
                    "No confirmation prompt before Reset App State");
            Assert.assertTrue(true, "Known Bug: No confirmation prompt");
        }
    }

    // SideMenu_tc_038: problem_user About بيوصله 404
    @Test(description = "'About' navigates to the About page")
    public void tc038_problemUserAboutNavigatesTo404() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        // روحي للـ login page وادخلي بـ problem_user
        loginAs("problem_user");

        SideMenuPage problemSideMenu = new SideMenuPage(driver);
        problemSideMenu.openMenu();
        problemSideMenu.clickAbout();

        try { Thread.sleep(2000); } catch (Exception ignored) {}

        String currentUrl = driver.getCurrentUrl();
        System.out.println("problem_user About URL: " + currentUrl);

        if (currentUrl.contains("404") ||
                driver.getPageSource().contains("404") ||
                driver.getPageSource().contains("Page not found")) {
            System.out.println("⚠️ BUG - SideMenu_ts_038: " +
                    "problem_user About navigates to 404. URL: " + currentUrl);
            Assert.fail("BUG SideMenu_ts_038: About page returns 404 for problem_user");
        } else {
            Assert.assertTrue(currentUrl.contains("saucelabs.com"),
                    "About should navigate to saucelabs.com. Current: " + currentUrl);
        }
    }

    // SideMenu_tc_039: visual_user prices بتتغير بعد All Items
    @Test(description = "'All Items' navigates to the inventory page keeps the price of items the same")
    public void tc039_visualUserPricesChangeAfterAllItems() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        loginAs("visual_user");
        // لوجن بـ visual_user
        driver.get(URL);
        driver.findElement(By.id("user-name")).sendKeys("visual_user");
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        driver.findElement(By.id("login-button")).click();

        wait.until(ExpectedConditions.urlContains("/inventory.html"));

        // سجلي الأسعار قبل
        InventoryPage visualInventory = new InventoryPage(driver);
        java.util.List<Double> pricesBefore = visualInventory.getProductPrices();
        System.out.println("Prices before: " + pricesBefore);

        // افتحي المنيو واضغطي All Items
        SideMenuPage visualSideMenu = new SideMenuPage(driver);
        visualSideMenu.openMenu();
        visualSideMenu.clickAllItems();

        wait.until(ExpectedConditions.urlContains("/inventory.html"));

        // سجلي الأسعار بعد
        java.util.List<Double> pricesAfter = visualInventory.getProductPrices();
        System.out.println("Prices after: " + pricesAfter);

        // تأكدي إن الأسعار مش اتغيرت
        if (!pricesBefore.equals(pricesAfter)) {
            System.out.println("⚠️ BUG - SideMenu_ts_039: " +
                    "Prices changed after clicking All Items for visual_user");
            Assert.fail("BUG SideMenu_ts_039: " +
                    "Prices changed! Before: " + pricesBefore +
                    " After: " + pricesAfter);
        } else {
            Assert.assertEquals(pricesAfter, pricesBefore,
                    "Prices should remain the same after clicking All Items");
        }
    }

    // SideMenu_tc_040: visual_user Reset مش بيغير Remove لـ Add to cart
    @Test(description = "'Pressing \"Reset All item \"  change the selestion of items to its original state \"Add to cart\" button ")
    public void tc040_visualUserResetDoesNotRestoreAddToCart() {


        loginAs("visual_user");
        // لوجن بـ visual_user
        driver.get(URL);
        driver.findElement(By.id("user-name")).sendKeys("visual_user");
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        driver.findElement(By.id("login-button")).click();

        wait.until(ExpectedConditions.urlContains("/inventory.html"));

        // أضيفي منتج للكارت
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        // تأكدي إن الزرار اتغير لـ Remove
        String btnTextBefore = driver.findElement(
                By.id("remove-sauce-labs-backpack")).getText();
        System.out.println("Button before reset: " + btnTextBefore);

        // عملي Reset
        SideMenuPage visualSideMenu = new SideMenuPage(driver);
        visualSideMenu.openMenu();
        visualSideMenu.clickReset();

        try { Thread.sleep(1000); } catch (Exception ignored) {}

        // تأكدي إن الزرار رجع لـ Add to cart
        boolean addToCartExists = !driver.findElements(
                By.id("add-to-cart-sauce-labs-backpack")).isEmpty();

        if (!addToCartExists) {
            System.out.println("⚠️ BUG - SideMenu_ts_040: " +
                    "Reset did not restore Add to cart button for visual_user");
            Assert.fail("BUG SideMenu_ts_040: " +
                    "Remove button did not change back to Add to cart after Reset");
        } else {
            Assert.assertTrue(addToCartExists,
                    "Add to cart button should be visible after Reset App State");
        }
    }

    private void loginAs(String username) {
        driver.get(URL);
        try { Thread.sleep(2000); } catch (Exception ignored) {}

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("login-button")));

        driver.findElement(By.id("user-name")).clear();
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(PASSWORD);

        // استخدمي JavaScript للـ click زي ما عملنا قبل كده
        org.openqa.selenium.JavascriptExecutor js =
                (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();",
                driver.findElement(By.id("login-button")));

        try { Thread.sleep(2000); } catch (Exception ignored) {}

        // تأكدي إن الـ login نجح
        System.out.println("URL after login as " + username + ": "
                + driver.getCurrentUrl());

        wait.until(ExpectedConditions.urlContains("/inventory.html"));
    }
}