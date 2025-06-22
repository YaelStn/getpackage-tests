package pages;

import com.microsoft.playwright.*;

public class LoginPage {
    private final Page page;

    private final Locator emailInput;
    private  final Locator passwordInput;
    private final Locator loginButton;
    private  final Locator errorMessage;

    public LoginPage(Page page) {
        this.page = page;

        this.emailInput = page.locator("input[formcontrolname='userName']");
        this.passwordInput = page.locator("input[formcontrolname='password']");
        this.loginButton = page.locator("button:has-text('התחברות')");
        this.errorMessage = page.locator("text=אימייל או סיסמה לא נכונים");
    }

    public void navigate() {
        page.navigate("https://sender.getpackage.com/login");
        page.locator("text=כתובת אימייל וסיסמה").click();
    }

    public void enterEmail(String email) {
        emailInput.fill(email);
    }

    public void enterPassword(String password) {
        passwordInput.fill(password);
    }

    public void submit() {
        loginButton.click();
    }

    public String getErrorText() {
        return errorMessage.textContent();
    }

    public boolean isAtDeliveryPage() {
        return page.url().contains("/delivery");
    }

    public Page getPage() {
        return this.page;
    }

}
