package tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import pages.LoginPage;

public class LoginTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;
    LoginPage loginPage;

    @BeforeAll
    static void launchPlaywright() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
        loginPage = new LoginPage(page);
    }


    @Test
    void testPageOpens() {
        loginPage.navigate();
        Assertions.assertTrue(page.url().contains("login"));
    }

    @Test
    void loginWithInvalidEmail() {
        loginPage.navigate();
        loginPage.enterEmail("wrong@email.com");
        loginPage.enterPassword("Qwerty123");
        loginPage.submit();

        Assertions.assertTrue(loginPage.getErrorText().contains("אימייל או סיסמה לא נכונים"));
    }

    @Test
    void loginWithInvalidPassword() {
        loginPage.navigate();
        loginPage.enterEmail("lina2001smirnova@gmail.com");
        loginPage.enterPassword("WrongPassword123");
        loginPage.submit();

        Assertions.assertTrue(loginPage.getErrorText().contains("אימייל או סיסמה לא נכונים"));
    }

    @Test
    void loginWithValidData() {
        loginPage.navigate();
        loginPage.enterEmail("lina2001smirnova@gmail.com");
        loginPage.enterPassword("Qwerty123");
        loginPage.submit();

        page.waitForURL("**/delivery");

        Assertions.assertTrue(loginPage.isAtDeliveryPage());
    }



    @AfterEach
    void closeContext() {
        context.close();
    }

    @AfterAll
    static void closePlaywright() {
        browser.close();
        playwright.close();
    }

}
