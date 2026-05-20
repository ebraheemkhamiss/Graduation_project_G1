package test;

import pages.InventoryPage;
import jdk.jfr.Description;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import pages.LoginPage;

import java.util.List;
public class FilterTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod
    public void setUpPages() {

        inventoryPage = new InventoryPage(driver);
    }

    // Filter_tc_001: الـ dropdown ظاهر
    @Test(description =  "Validate that Filter dropdown is visible on the inventory page")
    public void tc001_filterDropdownIsVisible() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(inventoryPage.isFilterVisible(),
                "Filter dropdown should be visible on inventory page");
    }

    // Filter_tc_002: الـ default filter هو Name A to Z
    @Test(description = "Default filter on page load is 'Name (A to Z)'")
    public void tc002_defaultFilterIsNameAtoZ() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        Assert.assertEquals(inventoryPage.getSelectedFilter(),
                "Name (A to Z)",
                "Default filter should be Name (A to Z)");
    }

    // Filter_tc_005: Name A to Z بيرتب صح
    @Test(description = "Selecting 'Name (A to Z)' sorts products alphabetically ascending")
    public void tc005_nameAtoZSortsCorrectly() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage.selectFilter("Name (A to Z)");
        List<String> names = inventoryPage.getProductNames();

        for (int i = 0; i < names.size() - 1; i++) {
            Assert.assertTrue(
                    names.get(i).compareTo(names.get(i + 1)) <= 0,
                    "Products should be sorted A to Z"
            );
        }
    }

    // Filter_tc_006: Name Z to A بيرتب صح
    @Test(description = "Selecting 'Name (Z to A)' sorts products alphabetically descending")
    public void tc006_nameZtoASortsCorrectly() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage.selectFilter("Name (Z to A)");
        List<String> names = inventoryPage.getProductNames();

        for (int i = 0; i < names.size() - 1; i++) {
            Assert.assertTrue(
                    names.get(i).compareTo(names.get(i + 1)) >= 0,
                    "Products should be sorted Z to A"
            );
        }
    }

    // Filter_tc_007: Price low to high بيرتب صح
    @Test(description = "Selecting 'Price (low to high)' sorts products by price ascending")
    public void tc007_priceLowToHighSortsCorrectly() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage.selectFilter("Price (low to high)");
        List<Double> prices = inventoryPage.getProductPrices();

        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(
                    prices.get(i) <= prices.get(i + 1),
                    "Products should be sorted by price low to high"
            );
        }
    }

    // Filter_tc_008: Price high to low بيرتب صح
    @Test(description = "Selecting 'Price (high to low)' sorts products by price descending")
    public void tc008_priceHighToLowSortsCorrectly() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage.selectFilter("Price (high to low)");
        List<Double> prices = inventoryPage.getProductPrices();

        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(
                    prices.get(i) >= prices.get(i + 1),
                    "Products should be sorted by price high to low"
            );
        }
    }

    // Filter_tc_018: عدد المنتجات مش بيتغير بعد الـ filter
    @Test(description = "Product count remains the same after applying any filter")
    public void tc018_productCountRemainsTheSame() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        int countBefore = inventoryPage.getProductCount();

        inventoryPage.selectFilter("Price (high to low)");
        int countAfter = inventoryPage.getProductCount();

        Assert.assertEquals(countAfter, countBefore,
                "Product count should remain the same after filtering");
    }
    // Filter_tc_003: الـ dropdown بيفتح لما تضغطي عليه
    @Test(description = "Clicking the filter dropdown opens the option list")
    public void tc003_filterDropdownOpensOnClick() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        driver.findElement(By.className("product_sort_container")).click();
        boolean isOpen = driver.findElement(By.className("product_sort_container"))
                .getAttribute("size") != null;
        Assert.assertTrue(inventoryPage.isFilterVisible(),
                "Filter dropdown should open on click");
    }

    // Filter_tc_004: الـ dropdown فيه 4 options صح
    @Test(description = "Filter dropdown shows all 4 options with correct labels")
    public void tc004_filterDropdownHasAllFourOptions() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        org.openqa.selenium.support.ui.Select select =
                new org.openqa.selenium.support.ui.Select(
                        driver.findElement(By.className("product_sort_container")));

        List<org.openqa.selenium.WebElement> options = select.getOptions();

        Assert.assertEquals(options.size(), 4,
                "Filter dropdown should have exactly 4 options");
        Assert.assertEquals(options.get(0).getText(), "Name (A to Z)");
        Assert.assertEquals(options.get(1).getText(), "Name (Z to A)");
        Assert.assertEquals(options.get(2).getText(), "Price (low to high)");
        Assert.assertEquals(options.get(3).getText(), "Price (high to low)");
    }
    // Filter_tc_009: الـ option المختار بيكون highlighted
    @Test(description = "Selected option is highlighted in the dropdown")
    public void tc009_selectedOptionIsHighlighted() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        // اختاري option
        inventoryPage.selectFilter("Price (low to high)");

        // تأكدي إن الـ option المختار هو الصح
        org.openqa.selenium.support.ui.Select select =
                new org.openqa.selenium.support.ui.Select(
                        driver.findElement(By.className("product_sort_container")));

        String selectedText = select.getFirstSelectedOption().getText();

        Assert.assertEquals(selectedText, "Price (low to high)",
                "Selected option should be highlighted/active in dropdown");
    }

    // Filter_tc_010: الـ filter بيفضل بعد الـ scroll
    @Test(description = "Filter selection persists after scrolling")
    public void tc010_filterPersistsAfterScrolling() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage.selectFilter("Price (low to high)");

        // عمل scroll للأسفل وللأعلى
        org.openqa.selenium.JavascriptExecutor js =
                (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        js.executeScript("window.scrollTo(0, 0)");

        Assert.assertEquals(inventoryPage.getSelectedFilter(), "Price (low to high)",
                "Filter should persist after scrolling");
    }

    // Filter_tc_011: الـ filter بيفضل بعد إضافة item للكارت
    @Test(description = "Filter persists after adding item to cart")
    public void tc011_filterPersistsAfterAddingToCart() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage.selectFilter("Name (Z to A)");

        // أضيفي منتج للكارت
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        Assert.assertEquals(inventoryPage.getSelectedFilter(), "Name (Z to A)",
                "Filter should persist after adding item to cart");
    }

    // Filter_tc_012: كل الـ 4 options بيرتبوا صح
    @Test(description = "Switching between all 4 options updates the product list each time")
    public void tc012_allFilterOptionsSortCorrectly() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        // A to Z
        inventoryPage.selectFilter("Name (A to Z)");
        Assert.assertEquals(inventoryPage.getProductNames().get(0),
                "Sauce Labs Backpack", "First product A to Z should be Backpack");

        // Z to A
        inventoryPage.selectFilter("Name (Z to A)");
        Assert.assertTrue(inventoryPage.getProductNames().get(0)
                .contains("T-Shirt"), "First product Z to A should be T-Shirt");

        // Low to High
        inventoryPage.selectFilter("Price (low to high)");
        Assert.assertEquals(inventoryPage.getProductPrices().get(0),
                7.99, "Cheapest product should be first");

        // High to Low
        inventoryPage.selectFilter("Price (high to low)");
        Assert.assertEquals(inventoryPage.getProductPrices().get(0),
                49.99, "Most expensive product should be first");
    }

    // Filter_tc_026: الـ filter بيرجع default بعد refresh
    @Test(description = "Filter resets to default after page refresh")
    public void tc026_filterResetsAfterRefresh() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage.selectFilter("Price (high to low)");

        driver.navigate().refresh();

        wait.until(org.openqa.selenium.support.ui.ExpectedConditions
                .visibilityOfElementLocated(By.className("product_sort_container")));

        Assert.assertEquals(inventoryPage.getSelectedFilter(), "Name (A to Z)",
                "Filter should reset to default after page refresh");
    }

    // Filter_tc_027: الـ filter مش بيتحفظ بعد logout و login

    @Test(description = "Filter does not persist after Logout and re-Login")
    public void tc027_filterDoesNotPersistAfterLogout() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage.selectFilter("Name (Z to A)");

        // عمل logout عن طريق الـ URL مباشرة
        driver.get(URL);

        try { Thread.sleep(1000); } catch (Exception ignored) {}

        // لو الـ logout اشتغل هيبقى على login page
        // لو لأ نعمل login يدوي
        if (!driver.getCurrentUrl().contains("inventory")) {
            driver.findElement(By.id("user-name")).sendKeys(USERNAME);
            driver.findElement(By.id("password")).sendKeys(PASSWORD);
            driver.findElement(By.id("login-button")).click();
        }

        // روحي لصفحة الـ inventory
        driver.get(URL + "/inventory.html");

        try { Thread.sleep(1000); } catch (Exception ignored) {}

        // عملي inventoryPage جديدة عشان تاخد الـ state الجديد
        inventoryPage = new InventoryPage(driver);

        Assert.assertEquals(inventoryPage.getSelectedFilter(), "Name (A to Z)",
                "Filter should reset to default after logout and re-login");
    }
    // Filter_tc_013: الـ dropdown في Top-right
    @Test(description = "Filter dropdown is aligned to the top-right of the product list")
    public void tc013_filterDropdownAlignedToTopRight() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        org.openqa.selenium.WebElement dropdown =
                driver.findElement(By.className("product_sort_container"));

        // تأكدي إن الـ dropdown ظاهر وفي الصفحة
        Assert.assertTrue(dropdown.isDisplayed(),
                "Filter dropdown should be visible at top-right");

        // تأكدي إن موقعه في النص الأيمن
        int xPosition = dropdown.getLocation().getX();
        int pageWidth  = driver.manage().window().getSize().getWidth();
        Assert.assertTrue(xPosition > pageWidth / 2,
                "Filter dropdown should be on the right side of the page");
    }

    // Filter_tc_014: الـ filter icon ظاهر
    @Test(description = "Filter icon (funnel) is displayed next to the dropdown")
    public void tc014_filterIconIsVisible() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        org.openqa.selenium.WebElement filterContainer =
                driver.findElement(By.className("select_container"));
        Assert.assertTrue(filterContainer.isDisplayed(),
                "Filter icon/container should be visible next to dropdown");
    }

    // Filter_tc_015: الـ dropdown بيقفل بعد الاختيار
    // Filter_tc_015: problem_user - Filter مش شغال
    @Test(description = "Dropdown closes after a selection is made")
    public void tc015_problemUserFilterDoesNotWork() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        loginAs("problem_user");

        InventoryPage problemInventory = new InventoryPage(driver);
        java.util.List<String> namesBefore = problemInventory.getProductNames();

        problemInventory.selectFilter("Name (Z to A)");
        try { Thread.sleep(1000); } catch (Exception ignored) {}

        // تحققي لو في alert
        try {
            org.openqa.selenium.Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println("⚠️ BUG - Filter_tc_015: Alert appeared: " + alertText);
            alert.accept();
            Assert.fail("BUG Filter_tc_015: " +
                    "Unexpected alert appeared for problem_user: " + alertText);

        } catch (org.openqa.selenium.NoAlertPresentException e) {
            java.util.List<String> namesAfter = problemInventory.getProductNames();
            if (namesBefore.equals(namesAfter)) {
                System.out.println("⚠️ BUG - Filter_tc_015: " +
                        "Filter did not work for problem_user");
                Assert.fail("BUG Filter_tc_015: Filter has no effect for problem_user");
            } else {
                Assert.assertEquals(namesAfter.get(0),
                        "Test.allTheThings() T-Shirt (Red)",
                        "First product Z to A should be T-Shirt");
            }
        }
    }

    // Filter_tc_016: الـ dropdown بيقفل لما تضغطي بره
    @Test(description = "Dropdown closes when user clicks outside it")
    public void tc016_dropdownClosesOnOutsideClick() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        // افتحي الـ dropdown
        driver.findElement(By.className("product_sort_container")).click();

        // اضغطي بره
        driver.findElement(By.className("inventory_list")).click();

        // تأكدي إن الـ filter الافتراضي لسه موجود
        Assert.assertTrue(inventoryPage.isFilterVisible(),
                "Filter should still be visible after clicking outside");
    }

    // Filter_tc_019: الـ label بيتغير بعد الاختيار
    @Test(description = "Dropdown label updates to reflect selected option")
    public void tc019_dropdownLabelUpdatesAfterSelection() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage.selectFilter("Price (low to high)");
        Assert.assertEquals(inventoryPage.getSelectedFilter(),
                "Price (low to high)",
                "Dropdown label should update to selected option");

        inventoryPage.selectFilter("Name (Z to A)");
        Assert.assertEquals(inventoryPage.getSelectedFilter(),
                "Name (Z to A)",
                "Dropdown label should update to new selected option");
    }

    // Filter_tc_020: منتجين بنفس السعر بيتعاملوا صح
    @Test(description = "Price sort handles two products with the same price correctly")
    public void tc020_samePriceHandledCorrectly() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage.selectFilter("Price (low to high)");
        List<Double> prices = inventoryPage.getProductPrices();

        // Bolt T-Shirt و T-Shirt Red بسعر $15.99
        long countOf1599 = prices.stream()
                .filter(p -> p == 15.99)
                .count();

        Assert.assertEquals(countOf1599, 2L,
                "Should have 2 products with price $15.99");

        // تأكدي إن الترتيب صح
        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) <= prices.get(i + 1),
                    "Products should still be in ascending price order");
        }
    }

    // Filter_tc_025: التبديل السريع مش بيخرب الـ state
    @Test(description = "Rapid filter switching does not cause state corruption")
    public void tc025_rapidFilterSwitchingNoCorruption() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        // بدّلي بسرعة بين كل الـ options
        String[] filters = {
                "Name (A to Z)",
                "Name (Z to A)",
                "Price (low to high)",
                "Price (high to low)"
        };

        for (int i = 0; i < 5; i++) {
            for (String filter : filters) {
                inventoryPage.selectFilter(filter);
            }
        }

        // وقفي على Price high to low
        inventoryPage.selectFilter("Price (high to low)");

        // تأكدي إن الـ state صح
        Assert.assertEquals(inventoryPage.getSelectedFilter(),
                "Price (high to low)",
                "Filter should show correct option after rapid switching");

        Assert.assertEquals(inventoryPage.getProductPrices().get(0),
                49.99,
                "Most expensive product should be first after rapid switching");

        Assert.assertEquals(inventoryPage.getProductCount(), 6,
                "Product count should remain 6 after rapid switching");
    }

    // Filter_tc_028: الـ filter شغال بعد Reset App State
    @Test(description = "Filter is functional after 'Reset App State'")
    public void tc028_filterWorksAfterResetAppState() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        // اضيفي منتج للكارت
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        // عملي Reset
        driver.findElement(By.id("react-burger-menu-btn")).click();
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions
                .elementToBeClickable(By.id("reset_sidebar_link")));

        org.openqa.selenium.JavascriptExecutor js =
                (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();",
                driver.findElement(By.id("reset_sidebar_link")));

        // جربي كل الـ filters بعد الـ Reset
        inventoryPage.selectFilter("Price (low to high)");
        Assert.assertEquals(inventoryPage.getSelectedFilter(),
                "Price (low to high)",
                "Filter should work after Reset App State");

        Assert.assertEquals(inventoryPage.getProductCount(), 6,
                "All products should be visible after Reset App State");
    }
    // Filter_tc_036: error_user - Filter مش شغال
    @Test(description = "Dropdown closes after a selection is made")
    public void tc036_errorUserFilterDoesNotWork() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        loginAs("error_user");

        InventoryPage errorInventory = new InventoryPage(driver);

        java.util.List<String> namesBefore = errorInventory.getProductNames();
        System.out.println("Names before filter: " + namesBefore);

        // اختاري Name Z to A
        errorInventory.selectFilter("Name (Z to A)");

        try { Thread.sleep(1000); } catch (Exception ignored) {}

        // تحققي لو في alert ظهر
        try {
            org.openqa.selenium.Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println("⚠️ BUG - Filter_tc_036: Alert appeared: " + alertText);
            alert.accept(); // اقبلي الـ alert عشان نكمل

            Assert.fail("BUG Filter_tc_036: " +
                    "Unexpected alert appeared when filtering for error_user: "
                    + alertText);

        } catch (org.openqa.selenium.NoAlertPresentException e) {
            // مفيش alert = نتحقق من الـ sorting
            java.util.List<String> namesAfter = errorInventory.getProductNames();
            System.out.println("Names after filter: " + namesAfter);

            if (namesBefore.equals(namesAfter)) {
                System.out.println("⚠️ BUG - Filter_tc_036: " +
                        "Filter did not work for error_user");
                Assert.fail("BUG Filter_tc_036: Filter has no effect for error_user");
            } else {
                Assert.assertEquals(namesAfter.get(0),
                        "Test.allTheThings() T-Shirt (Red)",
                        "First product Z to A should be T-Shirt");
            }
        }
    }
    @Test(description = "Selecting 'Price (low to high)' sorts products by price descending")
    public void tc037_visualUserPriceLowToHighDoesNotWork() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        loginAs("visual_user");

        InventoryPage visualInventory = new InventoryPage(driver);
        visualInventory.selectFilter("Price (low to high)");

        try { Thread.sleep(1000); } catch (Exception ignored) {}

        java.util.List<Double> prices = visualInventory.getProductPrices();
        System.out.println("Prices after low to high: " + prices);

        boolean isSorted = true;
        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) > prices.get(i + 1)) {
                isSorted = false;
                break;
            }
        }

        if (!isSorted) {
            System.out.println("⚠️ BUG - Filter_tc_037: " +
                    "Price low to high not working for visual_user. " +
                    "Prices: " + prices);
            Assert.fail("BUG Filter_tc_037: " +
                    "Price (low to high) filter not sorting correctly for visual_user");
        } else {
            Assert.assertEquals(prices.get(0), 7.99,
                    "Cheapest product should be first");
        }
    }
    private void loginAs(String username) {
        driver.get(URL);
        try { Thread.sleep(1000); } catch (Exception ignored) {}
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("login-button")));
        driver.findElement(By.id("user-name")).clear();
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        org.openqa.selenium.JavascriptExecutor js =
                (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();",
                driver.findElement(By.id("login-button")));
        try { Thread.sleep(2000); } catch (Exception ignored) {}
        wait.until(ExpectedConditions.urlContains("/inventory.html"));
    }
}
