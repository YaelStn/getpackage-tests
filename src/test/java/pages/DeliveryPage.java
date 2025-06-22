package pages;

import com.microsoft.playwright.Page;

public class DeliveryPage {
    private final Page page;

    public DeliveryPage(Page page) {
        this.page = page;
    }

    public void waitForLoad() {
        page.waitForURL("**/delivery", new Page.WaitForURLOptions().setTimeout(5000));
    }

    public boolean isOpen() {
        return page.url().contains("/delivery");
    }

}
