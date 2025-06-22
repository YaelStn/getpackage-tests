package pages;

import com.microsoft.playwright.*;
import dto.User;

public class LoginPage {
    private final Page page;
    private final Locator emailInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator errorMessage;

    public LoginPage(Page page) {
        this.page = page;
        this.emailInput = page.locator("input[formcontrolname='userName']");
        this.passwordInput = page.locator("input[formcontrolname='password']");
        this.loginButton = page.locator("button:has-text('התחברות')");
        this.errorMessage = page.locator("text=אימייל או סיסמה לא נכונים");
    }

    public void navigate(String url) {
        page.navigate(url);
        page.locator("text=כתובת אימייל וסיסמה").click();
    }

    public void login(User user) {
        emailInput.fill(user.email);
        passwordInput.fill(user.password);
        loginButton.click();
    }

    public DeliveryPage loginAndGoToDelivery(User user) {
        login(user);
        page.waitForURL("**/delivery", new Page.WaitForURLOptions().setTimeout(5000));
        return new DeliveryPage(page);
    }

    public void waitForErrorMessage() {
        errorMessage.waitFor(new Locator.WaitForOptions().setTimeout(5000));
    }

    public String getErrorText() {
        return errorMessage.textContent();
    }

    public Page getPage() {
        return this.page;
    }

    public boolean isAtDeliveryPage() {
        return page.url().contains("/delivery");
    }

}
