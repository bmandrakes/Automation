import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class BranchAutomation {
    public static void main(String[] args) {
        // Setup WebDriver for Chrome
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);
        
        // Define WebDriverWait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Step 1: Navigate to login page
            driver.get("https://stg.murakoze.rw");

            // Step 2: Enter login credentials and submit
            driver.findElement(By.id("loginform-username")).sendKeys("Gaju3");
            driver.findElement(By.id("loginform-password")).sendKeys("P%8h< y5z+");
            driver.findElement(By.name("login-button")).click();

            // Validate successful login
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("adminMenu")));
                System.out.println("Login successful.");
            } catch (Exception e) {
                System.out.println("Login failed. Please check credentials.");
                driver.quit();
                return;
            }

            // Step 3: Navigate to Administration menu
            driver.findElement(By.xpath("//span[contains(text(), 'Administration')]")).click();

            // Step 4: Create a new branch
            WebElement addBranchButton = wait.until(ExpectedConditions.elementToBeClickable(
            	    By.xpath("//a[contains(text(), 'Add Branch')]")
            	));
            	addBranchButton.click();
            WebElement branchNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("branchName")));
            branchNameField.sendKeys("NIYIGIRIMBABAZIBranch");
            driver.findElement(By.id("saveBranchButton")).click();

            // Step 5: Activate the newly created branch
            WebElement activateButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//td[contains(text(), 'NIYIGIRIMBABAZIBranch')]/following-sibling::td/button[contains(@id,'activateBranch')]")
            ));
            activateButton.click();

            // Step 6: Validate branch appears in the list
            boolean isBranchPresent = wait.until(
                ExpectedConditions.textToBePresentInElementLocated(By.id("branchList"), "NIYIGIRIMBABAZIBranch")
            );
            if (isBranchPresent) {
                System.out.println("Branch successfully created and activated.");
            } else {
                System.out.println("Branch creation failed.");
            }

            // Step 7: Logout
            driver.findElement(By.id("logoutButton")).click();
            System.out.println("Logged out successfully.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            // Ensure WebDriver quits even if an error occurs
           // if (driver != null) {
           //     driver.quit();
           // }
        }
    }
}
