import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

public class TestImageUpload extends BaseTest {
    @Test
    public void testImageUpload() {
        String imagePath = "/home/danil-emelin/IdeaProjects/TPO-3.2/src/test/resources/photo.png";
        String fileName = Paths.get(imagePath).getFileName().toString();

        // Verify file exists
        Assert.assertTrue(Files.exists(Paths.get(imagePath)),
                "Test image file not found: " + imagePath);

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            // 1. Upload the file
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='file']")));
            fileInput.sendKeys(imagePath);

            // 1. Send the file
            WebElement uploadButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[@id=\"uploadButton\"]")));
            uploadButton.click();

            // Check for success message
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("/html/body/div[2]/table[2]/tbody/tr[3]/td/div/div[2]/div[1]/h3")));
            Assert.assertTrue(successMsg.isDisplayed(), "Upload success message displayed");




        } catch (Exception e) {
            Assert.fail("Upload test failed: " + e.getMessage());
        }
    }
}