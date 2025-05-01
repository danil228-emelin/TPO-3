import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FastpicTests extends BaseTest {

    // Test case 1: Verify home page loads correctly
    @Test
    public void testHomePageLoads() {
        String title = driver.getTitle();
        Assert.assertTrue(title.contains("FastPic"), "Home page title doesn't contain 'FastPic'");

        WebElement uploadForm = driver.findElement(By.xpath("//form[@id='upload-form']"));
        Assert.assertTrue(uploadForm.isDisplayed(), "Upload form is not displayed");
    }


}