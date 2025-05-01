import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Other {
    // Test case 2: Verify image upload functionality
    @Test
    public void testImageUpload() throws InterruptedException {
        // Absolute path to a test image - replace with your actual test image path
        String imagePath = "/path/to/your/test/image.jpg";

        WebElement fileInput = driver.findElement(By.xpath("//input[@type='file']"));
        fileInput.sendKeys(imagePath);

        // Wait for upload to complete
        Thread.sleep(5000);

        // Verify upload success
        WebElement resultLink = driver.findElement(By.xpath("//div[contains(@class,'result-link')]/input"));
        Assert.assertTrue(resultLink.isDisplayed(), "Result link is not displayed after upload");
        Assert.assertFalse(resultLink.getAttribute("value").isEmpty(), "Result link is empty");
    }

    // Test case 3: Verify login functionality
    @Test
    public void testLogin() {
        WebElement loginLink = driver.findElement(By.xpath("//a[contains(text(),'Войти')]"));
        loginLink.click();

        WebElement emailField = driver.findElement(By.xpath("//input[@name='email']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@name='password']"));
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(),'Войти')]"));

        // Replace with valid test credentials or use test data
        emailField.sendKeys("testuser@example.com");
        passwordField.sendKeys("testpassword");
        loginButton.click();

        // Verify login success
        WebElement userMenu = driver.findElement(By.xpath("//div[contains(@class,'user-menu')]"));
        Assert.assertTrue(userMenu.isDisplayed(), "User menu is not displayed after login");
    }

    // Test case 4: Verify image viewing functionality
    @Test
    public void testViewImage() {
        // Navigate to a sample image page
        driver.get("http://fastpic.ru/view/1234567.html");

        WebElement imageElement = driver.findElement(By.xpath("//div[contains(@class,'image-container')]/img"));
        Assert.assertTrue(imageElement.isDisplayed(), "Image is not displayed");

        WebElement imageTitle = driver.findElement(By.xpath("//h1[contains(@class,'image-title')]"));
        Assert.assertFalse(imageTitle.getText().isEmpty(), "Image title is empty");
    }

    // Test case 5: Verify search functionality
    @Test
    public void testSearch() {
        WebElement searchInput = driver.findElement(By.xpath("//input[@name='q']"));
        WebElement searchButton = driver.findElement(By.xpath("//button[@type='submit']"));

        searchInput.sendKeys("test");
        searchButton.click();

        // Verify search results
        WebElement resultsHeader = driver.findElement(By.xpath("//h1[contains(text(),'Результаты поиска')]"));
        Assert.assertTrue(resultsHeader.isDisplayed(), "Search results header is not displayed");

        // Check if any results are shown
        boolean hasResults = driver.findElements(By.xpath("//div[contains(@class,'search-result-item')]")).size() > 0;
        Assert.assertTrue(hasResults, "No search results found");
    }
}
