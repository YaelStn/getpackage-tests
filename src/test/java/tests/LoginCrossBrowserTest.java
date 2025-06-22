package tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import pages.LoginPage;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginCrossBrowserTest {
    static Playwright playwright;

    @BeforeAll
    static void setupPlaywright() {
        playwright = Playwright.create();
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
            loginPage.navigate();
            loginPage.enterEmail("lina2001smirnova@gmail.com");
            loginPage.enterPassword("Qwerty123");
            loginPage.submit();

            loginPage.getPage().waitForURL("**/delivery");

            assertTrue(loginPage.isAtDeliveryPage());


        });
    }

    @Test
    void loginWithInvalidEmailFirefox() {
        runWithBrowser("firefox", loginPage -> {
            loginPage.navigate();
            loginPage.enterEmail("wrong@email.com");
            loginPage.enterPassword("Qwerty123");
            loginPage.submit();
            assertTrue(loginPage.getErrorText().contains("אימייל או סיסמה לא נכונים"));
        });
    }

    @Test
    void loginWithInvalidPasswordWebkit() {
        runWithBrowser("webkit", loginPage -> {
            loginPage.navigate();
            loginPage.enterEmail("lina2001smirnova@gmail.com");
            loginPage.enterPassword("WrongPassword123");
            loginPage.submit();
            assertTrue(loginPage.getErrorText().contains("אימייל או סיסמה לא נכונים"));
        });
    }
}




