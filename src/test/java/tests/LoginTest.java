package tests;

import config.ConfigReader;
import dto.User;
import org.junit.jupiter.api.Test;
import pages.DeliveryPage;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends BaseTest {

    @Test
    void loginWithValidCredentials() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate(ConfigReader.get("base.url"));

        User user = new User(
                ConfigReader.get("valid.email"),
                ConfigReader.get("valid.password")
        );

        DeliveryPage deliveryPage = loginPage.loginAndGoToDelivery(user);
        assertTrue(deliveryPage.isOpen());
    }

    @Test
    void loginWithInvalidEmail() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate(ConfigReader.get("base.url"));

        User user = new User(
                ConfigReader.get("invalid.email"),
                ConfigReader.get("valid.password")
        );

        loginPage.login(user);
        loginPage.waitForErrorMessage();
        assertTrue(loginPage.getErrorText().contains("אימייל או סיסמה לא נכונים"));
    }

    @Test
    void loginWithInvalidPassword() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate(ConfigReader.get("base.url"));

        User user = new User(
                ConfigReader.get("valid.email"),
                ConfigReader.get("invalid.password")
        );

        loginPage.login(user);
        loginPage.waitForErrorMessage();
        assertTrue(loginPage.getErrorText().contains("אימייל או סיסמה לא נכונים"));
    }
}
