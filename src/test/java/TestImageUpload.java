import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

/**
 * Class containing automated tests for image upload functionalities.
 */
public class TestImageUpload extends Config {
    private static final String IMAGE_URL = "https://fastpic.org/view/125/2025/0516/_ad5fc917162183916b7dd9904b6a64fe.png.html";
    private static final String IMAGE_URL_SECOND = "https://i125.fastpic.org/big/2025/0516/cf/77717eae824fad4e397922000012b1cf.png";
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);

    /**
     * Tests uploading an image file and verifies the success message.
     */
    @Test
    public void testImageUpload() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String imagePath = "/home/danil-emelin/IdeaProjects/TPO-3.2/src/test/resources/photo.png";
        String fileName = Paths.get(imagePath).getFileName().toString();
        Assert.assertTrue(Files.exists(Paths.get(imagePath)),
                "Test image file not found: " + imagePath);
        try {
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='file']")));
            fileInput.sendKeys(imagePath);
            WebElement uploadButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[@id=\"uploadButton\"]")));
            uploadButton.click();
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("/html/body/div[2]/table[2]/tbody/tr[3]/td/div/div[2]/div[1]/h3")));
            Assert.assertTrue(successMsg.isDisplayed(), "Upload success message displayed");
        } catch (Exception e) {
            Assert.fail("Upload test failed: " + e.getMessage());
        }
    }

    /**
     * Tests uploading a file with an invalid file type and checks for an error message.
     */
    @Test
    public void testInvalidFileTypeUpload() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String filePath = "/home/danil-emelin/IdeaProjects/TPO-3.2/src/test/resources/TPO.txt";
        Assert.assertTrue(Files.exists(Paths.get(filePath)));
        WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));
        fileInput.sendKeys(filePath);
        WebElement uploadButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[@id=\"uploadButton\"]")));
        uploadButton.click();
        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[2]/table[2]/tbody/tr[2]/td/div")));
        Assert.assertTrue(errorMsg.isDisplayed());
    }

    /**
     * Tests uploading an image, selecting options, and verifying the upload success.
     */
    @Test
    public void testSaveFileWithOptions() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String imagePath = "/home/danil-emelin/IdeaProjects/TPO-3.2/src/test/resources/photo.png";
        Assert.assertTrue(Files.exists(Paths.get(imagePath)),
                "Test image file not found: " + imagePath);
        try {
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='file']")));
            fileInput.sendKeys(imagePath);
            WebElement nameChooseButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[@id=\"check_thumb\"]")));
            nameChooseButton.click();
            Assert.assertTrue(nameChooseButton.isSelected(), "Name options is enabled");
            WebElement textField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[@id=\"thumb_text\"]")));
            textField.clear();
            textField.sendKeys("NEW_FILE_NAME");
            WebElement uploadButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[@id=\"uploadButton\"]")));
            uploadButton.click();
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("/html/body/div[2]/table[2]/tbody/tr[3]/td/div/div[2]/div[1]/h3")));
            Assert.assertTrue(successMsg.isDisplayed(), "Upload success message displayed");
        } catch (Exception e) {
            Assert.fail("Upload test failed: " + e.getMessage());
        }
    }

    /**
     * Tests image rotation options before upload and verifies the success message.
     */
    @Test
    public void testImageRotationOptions() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String imagePath = "/home/danil-emelin/IdeaProjects/TPO-3.2/src/test/resources/photo.png";
        Assert.assertTrue(Files.exists(Paths.get(imagePath)), "Test image file not found");
        try {
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='file']")));
            fileInput.sendKeys(imagePath);
            WebElement rotateCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("check_orig_rotate")));
            if (!rotateCheckbox.isSelected()) {
                rotateCheckbox.click();
            }
            Assert.assertTrue(rotateCheckbox.isSelected(), "Rotate checkbox should be selected");
            WebElement rotateSelect = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.id("orig-rotate")));
            Select rotationDropdown = new Select(rotateSelect);
            rotationDropdown.selectByValue("90");
            String selectedValue = rotationDropdown.getFirstSelectedOption().getAttribute("value");
            Assert.assertEquals(selectedValue, "90",
                    "Rotation should be " + "90" + " degrees");
            WebElement uploadButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("uploadButton")));
            uploadButton.click();
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("/html/body/div[2]/table[2]/tbody/tr[3]/td/div/div[2]/div[1]/h3")));
            Assert.assertTrue(successMsg.isDisplayed(), "Upload success message displayed");
        } catch (Exception e) {
            Assert.fail("Rotation test failed: " + e.getMessage());
        }
    }

    /**
     * Uploads an image, retrieves the download link, and verifies its accessibility and format.
     */
    @Test
    public void testUploadAndVerifyDownloadLink() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String imagePath = "/home/danil-emelin/IdeaProjects/TPO-3.2/src/test/resources/photo.png";
        try {
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='file']")));
            fileInput.sendKeys(imagePath);
            WebElement uploadButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("uploadButton")));
            uploadButton.click();
            WebElement linkInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[contains(@style,'width: 87%')]")));
            String downloadUrl = linkInput.getAttribute("value");
            Assert.assertFalse(downloadUrl.isEmpty(), "Download URL should not be empty");
            Assert.assertTrue(downloadUrl.startsWith("https://fastpic.org/view/"),
                    "URL should start with fastpic domain");
            Assert.assertTrue(downloadUrl.endsWith(".png.html"),
                    "URL should point to PNG file");
            verifyFileAccessible(downloadUrl);
        } catch (Exception e) {
            Assert.fail("Upload and link verification failed: " + e.getMessage());
        }
    }

    /**
     * Verifies that a file at the given URL is accessible (HTTP 200).
     *
     * @param url the URL of the file to verify
     */
    private void verifyFileAccessible(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            Assert.assertEquals(responseCode, 200, "File should be accessible (HTTP 200)");
        } catch (Exception e) {
            Assert.fail("File accessibility check failed: " + e.getMessage());
        }
    }

    /**
     * Uploads an image via link, switches to the original image link, and uploads it.
     *
     * @throws IOException if file operations fail
     */
    @Test
    public void testUploadPngViaLink() throws IOException {
        String imagePath = "/home/danil-emelin/IdeaProjects/TPO-3.2/src/test/resources/photo.png";
        Assert.assertTrue(Files.exists(Paths.get(imagePath)), "Test image not found");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='file']")));
            fileInput.sendKeys(imagePath);
            WebElement uploadButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("uploadButton")));
            uploadButton.click();
            WebElement originalLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("/html/body/div[2]/table[2]/tbody/tr[3]/td/div/div[2]/div[2]/ul/li[1]/input")));
            String originalUrl = originalLink.getAttribute("value");
            WebElement switchLink = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("a.jslink#switch_to_copy")));
            Assert.assertTrue(switchLink.isDisplayed(), "Ссылка не отображается");
            Assert.assertTrue(switchLink.isEnabled(), "Ссылка неактивна");
            switchLink.click();
            WebElement textarea = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.id("upload_files")));
            textarea.clear();
            textarea.sendKeys(originalUrl);
            WebElement newUploadButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("uploadButton")));
            newUploadButton.click();
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("/html/body/div[2]/table[2]/tbody/tr[2]/td/div")));
            Assert.assertTrue(successMsg.isDisplayed(), "Upload success message displayed");
        } catch (Exception e) {
            Assert.fail("Image content verification failed: " + e.getMessage());
        }
    }

    /**
     * Tests that uploaded image is accessible via direct link
     * Verifies:
     * 1. Page loads successfully
     * 2. And link goes to fastpic server.
     */
    @Test
    public void testImageAccessViaDirectLink() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);

        driver.get(IMAGE_URL);

        String title = driver.getTitle();
        Assert.assertTrue(title.toLowerCase().contains("fastpic"),
                "Home page title doesn't contain 'FastPic'. Actual title: " + title);
    }

    /**
     * Tests that download link exist, and it is available
     * Verifies:
     * 1. Check page have link.
     * 2.Link is available
     */
    @Test
    public void testContentOfSite() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);

        driver.get(IMAGE_URL);

        try {
            WebElement downloadLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("/html/body/nav/div/div/a[1]")));

            Assert.assertTrue(downloadLink.isDisplayed(), "Link isn't alive");

        } catch (Exception e) {
            Assert.fail("Image content verification failed: " + e.getMessage());
        }
    }

    /**
     * Tests download photo link
     * Verifies:
     * 1. Check that link goes to main page of fastpic.
     */
    @Test
    public void testLinkForwarding() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);

        driver.get(IMAGE_URL);

        try {
            WebElement uploadLink = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.cssSelector("a.nav-item.nav-link[href='/']")
                    ));
            String link = uploadLink.getAttribute("href");

            Assert.assertTrue(link.endsWith("/"), "The link isn't damaged");
            Assert.assertTrue(link.contains("fastpic"), "The fastpic domain name doesn't exist");


        } catch (Exception e) {
            Assert.fail("Image content verification failed: " + e.getMessage());
        }
    }

    /**
     * Tests download photo link
     * Verifies:
     * 1. Check that this is exactly download file link by text attribute.
     */
    @Test
    public void testDownloadFileContainDownloadWord() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);

        driver.get(IMAGE_URL);

        try {
            WebElement uploadLink = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.cssSelector("a.nav-item.nav-link[href='/']")
                    ));
            String text = uploadLink.getText();
            Assert.assertEquals(text, "Загрузить", "This isn't link for downloading files");

        } catch (Exception e) {
            Assert.fail("Image content verification failed: " + e.getMessage());
        }
    }

    /**
     * Tests download photo link
     * Verifies:
     * 1. Test forwarding of link
     */
    @Test
    public void testDownloadFileGoesToMainPage() {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);

        driver.get(IMAGE_URL);

        try {
            WebElement uploadLink = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.cssSelector("a.nav-item.nav-link[href='/']")
                    ));
            String link = uploadLink.getAttribute("href");
            driver.get(link);
            System.out.println(driver.getCurrentUrl());
            Assert.assertTrue(driver.getCurrentUrl().endsWith("/"), "The link is damaged");
            Assert.assertTrue(driver.getCurrentUrl().contains("fastpic"), "The fastpic domain name doesn't exist");

        } catch (Exception e) {
            Assert.fail("Image content verification failed: " + e.getMessage());
        }
    }


    /**
     * Tests Image meta data.
     * Verifies:
     * 1. Test Image Meta Data.
     * 2.Check that i can get info.
     */
    @Test
    public void testImageMetaDAtaGetting() {

        driver.get(IMAGE_URL);

        try {
            By metadataBlockLocator = By.cssSelector("div.resolution.text-white-50.m-2");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement metadataElement = wait.until(ExpectedConditions.visibilityOfElementLocated(metadataBlockLocator));
            String fullText = metadataElement.getText();
            Assert.assertFalse(fullText.isEmpty(), "Don't get any metadata info");

        } catch (Exception e) {
            Assert.fail("Image content verification failed: " + e.getMessage());
        }
    }

    /**
     * Tests Image meta data.
     * Verifies:
     * 1. Test Image Meta Data.
     * 2.Check resolution
     */
    @Test
    public void testImageMetaDAtaResolution() {

        driver.get(IMAGE_URL);

        try {
            By metadataBlockLocator = By.cssSelector("div.resolution.text-white-50.m-2");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement metadataElement = wait.until(ExpectedConditions.visibilityOfElementLocated(metadataBlockLocator));
            String fullText = metadataElement.getText();
            Assert.assertTrue(fullText.contains("Resolution: 1920x1080"), "Wrong resolution");
        } catch (Exception e) {

            Assert.fail("Image content verification failed: " + e.getMessage());
        }
    }

    /**
     * Tests Image meta data.
     * Verifies:
     * 1. Test Image Meta Data.
     * 2.Check resolution
     */
    @Test
    public void testImageMetaDAtaFileSize() {

        driver.get(IMAGE_URL);

        try {
            By metadataBlockLocator = By.cssSelector("div.resolution.text-white-50.m-2");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement metadataElement = wait.until(ExpectedConditions.visibilityOfElementLocated(metadataBlockLocator));
            String fullText = metadataElement.getText();
            System.out.println(fullText);
            Assert.assertTrue(fullText.contains("FileSize: 21.5 KiB"), "Wrong file size");
        } catch (Exception e) {

            Assert.fail("Image content verification failed: " + e.getMessage());
        }
    }

    /**
     * Tests Image meta data.
     * Verifies:
     * 1. Test Image Meta Data.
     * 2. Test icons of image meta data
     */
    @Test
    public void testImageMetaIcons() {

        driver.get(IMAGE_URL);

        try {
            By metadataBlockLocator = By.cssSelector("div.resolution.text-white-50.m-2");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement metadataElement = wait.until(ExpectedConditions.visibilityOfElementLocated(metadataBlockLocator));

            String fullText = metadataElement.getText();
            System.out.println("Полный текст элемента: " + fullText);

            List<WebElement> icons = metadataElement.findElements(By.tagName("img"));
            Assert.assertEquals(icons.size(), 2, "Должно быть 2 иконки");

            // Проверка что первая иконка - лупа
            WebElement magnifyIcon = metadataElement.findElement(By.cssSelector("img[src*='magnifying-glass-solid.svg']"));
            Assert.assertTrue(magnifyIcon.isDisplayed(), "Иконка лупы не отображается");

            // Проверка что вторая иконка - файл
            WebElement fileIcon = metadataElement.findElement(By.cssSelector("img[src*='file-image-regular.svg']"));
            Assert.assertTrue(fileIcon.isDisplayed(), "Иконка файла не отображается");
        } catch (Exception e) {
            Assert.fail("Image content verification failed: " + e.getMessage());
        }
    }

    /**
     * Tests Image metadata.
     * Verifies:
     * 1. Test that image presents on page
     */

    @Test
    public void testImageVerificationPresence() {
        driver.get(IMAGE_URL);
        try {
            WebElement imageElement = driver.findElement(By.cssSelector("img.image.img-fluid"));


            Boolean isImageLoaded = (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].complete && " +
                            "typeof arguments[0].naturalWidth != 'undefined' && " +
                            "arguments[0].naturalWidth > 0", imageElement);

            Assert.assertTrue(isImageLoaded, "Image is presented");
        } catch (Exception e) {
            Assert.fail("Image content verification failed: " + e.getMessage());
        }
    }

    /**
     * Tests Image metadata.
     * Verifies:
     * 1. Test that image wide and size > 0
     */
    @Test
    public void testImageVerificationSize() {
        driver.get(IMAGE_URL);
        try {
            WebElement imageElement = driver.findElement(By.cssSelector("img.image.img-fluid"));


            int width = imageElement.getSize().getWidth();
            int height = imageElement.getSize().getHeight();
            System.out.println("Image dimensions: " + width + "x" + height);

            Assert.assertTrue(width > 0, "Width of picture must be > 0");
            Assert.assertTrue(height > 0, "Height of picture must be > 0");

        } catch (Exception e) {
            Assert.fail("Image content verification failed: " + e.getMessage());
        }
    }

    /**
     * Tests Image metadata.
     * Verifies:
     * 1. Test image url
     */
    @Test
    public void testImageVerificationUrl() {
        driver.get(IMAGE_URL);
        try {
            WebElement imageElement = driver.findElement(By.cssSelector("img.image.img-fluid"));

            //Url checking png
            String imageUrl = imageElement.getAttribute("src");
            Assert.assertTrue(imageUrl.contains(".png"), "Url must contain png.");
            Assert.assertTrue(imageUrl.contains("fastpic.org"), "URL must contain fastpic.org");

            // 4. image must have full link
            WebElement fullViewLink = driver.findElement(By.cssSelector("a.img-a"));
            String fullViewUrl = fullViewLink.getAttribute("href");
            Assert.assertTrue(fullViewUrl.contains("fullview"), "Link to original picture");
        } catch (Exception e) {
            Assert.fail("Image content verification failed: " + e.getMessage());
        }
    }

    /**
     * Tests Image metadata.
     * Verifies:
     * 1. Try to download picture(Original).
     */
    @Test
    public void testImagePropertiesDownload() throws IOException {
        driver.get(IMAGE_URL);
        try {
            WebElement imageElement = driver.findElement(By.cssSelector("img.image.img-fluid"));
            String imageUrl = imageElement.getAttribute("src");
            System.out.println("Image URL: " + imageUrl);

            URL url = new URL(imageUrl);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();

            byte[] imageBytes = out.toByteArray();

            String downloadPath = "/home/danil-emelin/IdeaProjects/TPO-3.2/src/test/resources/downloaded-image-from-site.png";
            Files.write(Paths.get(downloadPath), imageBytes);

        } catch (Exception e) {
            Assert.fail("Image content verification failed: " + e.getMessage());
        }
    }

    /**
     * Tests Image metadata.
     * Verifies:
     * 1. Try to download modified picture(not original).
     */
    @Test
    public void testImagePropertiesDownloadModifiedPicture() throws IOException {
        driver.get(IMAGE_URL_SECOND);
        try {
            WebElement thumbnail = driver.findElement(By.cssSelector("img.image.img-fluid"));
            String thumbnailUrl = thumbnail.getAttribute("src");

            Assert.assertFalse(thumbnailUrl.contains("fullview"), "This is original");

            URL url = new URL(thumbnailUrl);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();

            byte[] imageBytes = out.toByteArray();

            String downloadPath = "/home/danil-emelin/IdeaProjects/TPO-3.2/src/test/resources/downloaded-image-from-site-second-modified.png";
            Files.write(Paths.get(downloadPath), imageBytes);

        } catch (Exception e) {
            Assert.fail("Image content verification failed: " + e.getMessage());
        }
    }

}