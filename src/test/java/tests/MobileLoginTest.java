package tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import pages.LoginPage;

public class MobileLoginTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;
    LoginPage loginPage;

    @BeforeAll
    static void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
    }

    @BeforeEach
    void createMobileContext() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(375, 812) // iPhone X
                .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 13_5 like Mac OS X) " +
                        "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1 " +
                        "Mobile/15E148 Safari/604.1")
        );
        page = context.newPage();
        loginPage = new LoginPage(page);
    }

    @Test
    void loginWithInvalidEmailMobile() {
        loginPage.navigate();
        loginPage.enterEmail("wrong@email.com");
        loginPage.enterPassword("Qwerty123");
        loginPage.submit();

        Assertions.assertTrue(loginPage.getErrorText().contains("אימייל או סיסמה לא נכונים"));
    }

    @Test
    void loginWithInvalidPasswordMobile() {
        loginPage.navigate();
        loginPage.enterEmail("lina2001smirnova@gmail.com");
        loginPage.enterPassword("WrongPassword123");
        loginPage.submit();

        Assertions.assertTrue(loginPage.getErrorText().contains("אימייל או סיסמה לא נכונים"));
    }

    @Test
    void loginWithValidDataMobile() {
        loginPage.navigate();
        loginPage.enterEmail("lina2001smirnova@gmail.com");
        loginPage.enterPassword("Qwerty123");
        loginPage.submit();

        page.waitForURL("**/delivery");
        Assertions.assertTrue(loginPage.isAtDeliveryPage());
    }

    @AfterEach
    void cleanup() {
        context.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }
}

