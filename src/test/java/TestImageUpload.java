import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class TestImageUpload extends BaseTest {
    @Test
    public void testImageUpload() {
        String imagePath = "/home/danil-emelin/IdeaProjects/TPO-3.2/src/test/resources/photo.png";

        // Verify file exists
        Assert.assertTrue(Files.exists(Paths.get(imagePath)), "Test image file not found: " + imagePath);

        try {
            // Wait for and interact with file input
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='file']")));
            fileInput.sendKeys(imagePath);

            // More robust waiting for upload completion
            waitForUploadCompletion(wait);

            // Verify the result
            verifyUploadResult(wait);

        } catch (Exception e) {
            takeScreenshot("upload_failure");
            Assert.fail("Upload test failed: " + e.getMessage());
        }
    }

    private void waitForUploadCompletion(WebDriverWait wait) {
        // Wait for any of these indicators that upload is complete
        try {


            // Option 2: Wait for success message
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.textToBePresentInElementLocated(
                            By.xpath("//*[contains(@class,'status')]"), "complete"),
                    ExpectedConditions.textToBePresentInElementLocated(
                            By.xpath("//*[contains(@class,'message')]"), "success")
            ));

            // Small additional wait for result processing
            Thread.sleep(1000);
        } catch (Exception e) {
            // Continue even if these indicators aren't found
        }
    }

    private void verifyUploadResult(WebDriverWait wait) {
        // Try multiple possible locations for the result link
        List<By> possibleLocators = Arrays.asList(
                By.xpath("//*[contains(@class,'result')]//input"),
                By.xpath("//*[contains(@class,'link')]//input"),
                By.xpath("//input[contains(@id,'result')]"),
                By.xpath("//*[contains(text(),'Your image:')]/following::input"),
                By.xpath("//textarea[contains(@class,'url')]")
        );

        WebElement resultElement = null;
        for (By locator : possibleLocators) {
            try {
                resultElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                break;
            } catch (TimeoutException e) {
                continue;
            }
        }

        Assert.assertNotNull(resultElement, "Could not find result element after upload");
        Assert.assertTrue(resultElement.isDisplayed(), "Result element is not displayed");

        String resultValue = resultElement.getAttribute("value");
        if (resultValue == null || resultValue.isEmpty()) {
            resultValue = resultElement.getText(); // Try text if value is empty
        }

        Assert.assertFalse(resultValue.isEmpty(), "Result link is empty");
        Assert.assertTrue(resultValue.contains("http") || resultValue.contains("fastpic"),
                "Result doesn't contain URL. Actual value: " + resultValue);
    }

    private void takeScreenshot(String name) {
        try {
            File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(),
                    Paths.get("screenshots/" + name + "_" + System.currentTimeMillis() + ".png"));
        } catch (Exception e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
        }
    }
}

