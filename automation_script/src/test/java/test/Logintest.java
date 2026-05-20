package test;

import jdk.jfr.Description;
import org.testng.annotations.Test;

public class Logintest extends BaseTest {

//description="Login with valid username and password should succeed"
    @Test(description="Login with valid username and password should succeed")
    public void validateLoginWithValidCredentials() {
        loginPage.login("standard_user", "secret_sauce");
        loginPage.AssertUserLogedin();

    }

    @Test(description = "Login with valid username and invalid password should fail")
    public void validateLoginWithInvalidPassword() {
        loginPage.login("standard_user", "invalid");
        loginPage.AssertUsercannotLogedin();
    }

    @Test(description="Login with invalid username and valid password should fail")
    public void validateLoginWithInvalidUsername() {
        loginPage.login("invalid", "secret_sauce");
        loginPage.AssertUsercannotLogedin();
    }

    @Test(description="Login with empty username and empty password should fail")
    public void validateLoginWithEmptyUsernameAndPassword() {
        loginPage.login("", "");
        loginPage.AssertUsercannotLogedin();
    }

    @Test(description="Login with valid credentials that include leading/trailing spaces should succeed")
    public void validateLoginWithLeadingAndTrailingSpaces() {
        loginPage.login("  standard_user  ", "  secret_sauce  ");
        loginPage.AssertUserLogedin();
    }

    @Test(description="After successful login, clicking browser back should not redirect to login page")
    public void validateUserStaysOnHomePageAfterBrowserBack() {
        loginPage.login("standard_user", "secret_sauce");
        loginPage.AssertUserLogedin();
        loginPage.clickBrowserBack();
        loginPage.AssertUserLogedin();
    }

    @Test(description="Locked out user cannot login even with valid password")
    public void validateLockedOutUserCannotLogin() {
        loginPage.login("locked_out_user", "secret_sauce");
        loginPage.AssertUsercannotLogedin();
    }

    @Test(description="Password is case sensitive; incorrect casing should prevent login")
    public void validatePasswordIsCaseSensitive() {
        loginPage.login("standard_user", "Secret_Sauce");
        loginPage.AssertUsercannotLogedin();
    }

    @Test(dataProvider = "loginData", dataProviderClass = LoginTestData.LoginData.class)
    @Description("Verify that each demo user can login successfully")
    public void validateAllUsersCanLogin(String username, String password) {
        loginPage.login(username, password);
        loginPage.AssertUserLogedin();
    }

}
