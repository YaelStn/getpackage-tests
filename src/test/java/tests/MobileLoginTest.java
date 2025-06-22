package tests;

import config.ConfigReader;
import dto.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pages.DeliveryPage;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MobileLoginTest extends BaseTest {

    @Test
    void loginOnMobileSuccessfully() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate(ConfigReader.get("base.url"));

        User user = new User(
                ConfigReader.get("valid.email"),
                ConfigReader.get("valid.password")
        );

        DeliveryPage deliveryPage = loginPage.loginAndGoToDelivery(user);
        assertTrue(deliveryPage.isOpen());
    }

    @BeforeAll
    public static void activateMobileMode() {
        mobileMode = true;
    }
}
