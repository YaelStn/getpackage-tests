package tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import config.ConfigReader;
import dto.User;
import pages.LoginPage;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginCrossBrowserTest {
    static Playwright playwright;
    static ConfigReader config;

    @BeforeAll
    static void setupPlaywright() {
        playwright = Playwright.create();
        config = new ConfigReader();
    }

    @AfterAll
    static void tearDownPlaywright() {
        playwright.close();
    }

    private Browser launchBrowser(String browserName) {
        return switch (browserName) {
            case "firefox" -> playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
            case "webkit" -> playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
            default -> playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        };
    }

    private void runWithBrowser(String browserName, Consumer<LoginPage> testSteps) {
        Browser browser = launchBrowser(browserName);
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        LoginPage loginPage = new LoginPage(page);

        try {
            testSteps.accept(loginPage);
        } finally {
            context.close();
            browser.close();
        }
    }

    @Test
    void loginWithValidDataChromium() {
        runWithBrowser("chromium", loginPage -> {
            loginPage.navigate(config.get("base.url"));
            User user = new User(config.get("valid.email"), config.get("valid.password"));
            loginPage.login(user);

            loginPage.getPage().waitForURL("**/delivery");
            assertTrue(loginPage.isAtDeliveryPage());
        });
    }

    @Test
    void loginWithInvalidEmailFirefox() {
        runWithBrowser("firefox", loginPage -> {
            loginPage.navigate(config.get("base.url"));
            User user = new User(config.get("invalid.email"), config.get("valid.password"));
            loginPage.login(user);

            assertTrue(loginPage.getErrorText().contains("אימייל או סיסמה לא נכונים"));
        });
    }

    @Test
    void loginWithInvalidPasswordWebkit() {
        runWithBrowser("webkit", loginPage -> {
            loginPage.navigate(config.get("base.url"));
            User user = new User(config.get("valid.email"), config.get("invalid.password"));
            loginPage.login(user);

            assertTrue(loginPage.getErrorText().contains("אימייל או סיסמה לא נכונים"));
        });
    }
}
