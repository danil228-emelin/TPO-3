import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

public class TestImageUpload extends Config {


    @Test
    public void testImageUpload() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        String imagePath = "/home/danil-emelin/IdeaProjects/TPO-3.2/src/test/resources/photo.png";
        String fileName = Paths.get(imagePath).getFileName().toString();

        // Verify file exists
        Assert.assertTrue(Files.exists(Paths.get(imagePath)),
                "Test image file not found: " + imagePath);

        try {

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

    @Test
    public void testInvalidFileTypeUpload() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        String filePath = "/home/danil-emelin/IdeaProjects/TPO-3.2/src/test/resources/TPO.txt";
        Assert.assertTrue(Files.exists(Paths.get(filePath)));

        WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));
        fileInput.sendKeys(filePath);

        // 1. Send the file
        WebElement uploadButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[@id=\"uploadButton\"]")));
        uploadButton.click();

        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[2]/table[2]/tbody/tr[2]/td/div")));
        Assert.assertTrue(errorMsg.isDisplayed());
    }


    @Test
    public void testSaveFileWithOptions() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));


        String imagePath = "/home/danil-emelin/IdeaProjects/TPO-3.2/src/test/resources/photo.png";

        // Verify file exists
        Assert.assertTrue(Files.exists(Paths.get(imagePath)),
                "Test image file not found: " + imagePath);

        try {

            // 1. Upload the file
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='file']")));
            fileInput.sendKeys(imagePath);

            // Locate the radio button element

            WebElement nameChooseButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[@id=\"check_thumb\"]")));
            nameChooseButton.click();

            Assert.assertTrue(nameChooseButton.isSelected(), "Name options is enabled");

            // 2. Create new description for file
            WebElement textField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[@id=\"thumb_text\"]")));
            textField.clear();
            textField.sendKeys("NEW_FILE_NAME");

            // 3. Send the file
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

    @Test
    public void testImageRotationOptions() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String imagePath = "/home/danil-emelin/IdeaProjects/TPO-3.2/src/test/resources/photo.png";

        Assert.assertTrue(Files.exists(Paths.get(imagePath)), "Test image file not found");

        try {
            // 1. Загрузка изображения
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='file']")));
            fileInput.sendKeys(imagePath);

            // 2. Активация чекбокса поворота
            WebElement rotateCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("check_orig_rotate")));

            if (!rotateCheckbox.isSelected()) {
                rotateCheckbox.click();
            }
            Assert.assertTrue(rotateCheckbox.isSelected(), "Rotate checkbox should be selected");

            // 3. Тестирование всех вариантов поворота
            WebElement rotateSelect = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.id("orig-rotate")));
            Select rotationDropdown = new Select(rotateSelect);


            // Выбираем вариант поворота
            rotationDropdown.selectByValue("90");

            // Проверяем, что выбор сохранился
            String selectedValue = rotationDropdown.getFirstSelectedOption().getAttribute("value");
            Assert.assertEquals(selectedValue, "90",
                    "Rotation should be " + "90" + " degrees");


            WebElement uploadButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("uploadButton")));
            uploadButton.click();

            // Check for success message
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("/html/body/div[2]/table[2]/tbody/tr[3]/td/div/div[2]/div[1]/h3")));
            Assert.assertTrue(successMsg.isDisplayed(), "Upload success message displayed");


        } catch (Exception e) {
            Assert.fail("Rotation test failed: " + e.getMessage());
        }
    }

}