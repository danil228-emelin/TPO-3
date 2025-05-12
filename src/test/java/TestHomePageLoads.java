import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TestHomePageLoads extends Config {

    // Test case 1: Verify home page loads correctly
    @Test
    public void testHomePageLoads() {
        // Wait for page to load completely
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Verify title
        String title = driver.getTitle();
        Assert.assertTrue(title.toLowerCase().contains("fastpic"),
                "Home page title doesn't contain 'FastPic'. Actual title: " + title);

        try {
            WebElement uploadForm = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//form[contains(@action,'upload') or contains(@id,'upload')]")));
            Assert.assertTrue(uploadForm.isDisplayed(), "Upload form is not displayed");


        } catch (TimeoutException e) {
            Assert.fail("Could not find upload form on the page. Page source: " + driver.getPageSource());
        }
    }


}