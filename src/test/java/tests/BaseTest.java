package tests;

import com.microsoft.playwright.*;
import config.ConfigReader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext context;
    protected static Page page;

    protected static boolean mobileMode = false;

    @BeforeAll
    public static void setup() {
        playwright = Playwright.create();

        browser = switch (ConfigReader.get("browser")) {
            case "firefox" -> playwright.firefox().launch(
                    new BrowserType.LaunchOptions().setHeadless(ConfigReader.getBoolean("headless")));
            case "webkit" -> playwright.webkit().launch(
                    new BrowserType.LaunchOptions().setHeadless(ConfigReader.getBoolean("headless")));
            default -> playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(ConfigReader.getBoolean("headless")));
        };

        Browser.NewContextOptions options = mobileMode
                ? new Browser.NewContextOptions()
                .setViewportSize(375, 812)
                .setIsMobile(true)
                .setDeviceScaleFactor(2)
                .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 13_5 like Mac OS X) " +
                        "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.1 Mobile/15E148 Safari/604.1")
                : new Browser.NewContextOptions()
                .setViewportSize(ConfigReader.getInt("viewport.width"), ConfigReader.getInt("viewport.height"))
                .setIsMobile(ConfigReader.getBoolean("isMobile"))
                .setDeviceScaleFactor(Double.parseDouble(ConfigReader.get("deviceScaleFactor")))
                .setUserAgent(ConfigReader.get("userAgent").isBlank() ? null : ConfigReader.get("userAgent"))
                .setLocale(ConfigReader.get("locale"));

        context = browser.newContext(options);
        page = context.newPage();
    }

    @AfterAll
    public static void teardown() {
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
