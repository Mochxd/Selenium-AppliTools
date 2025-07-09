package com.applitools.example;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.EyesRunner;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.ChromeEmulationInfo;
import com.applitools.eyes.visualgrid.model.DesktopBrowserInfo;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class BankTest {
    public static EyesRunner eyesRunner;
    public static Eyes eyes;
    public static WebDriver driver;
    private final static BatchInfo BATCH = new BatchInfo("First Test Cases with Applitools");

    @BeforeAll
    public static void configurations(){
        System.out.println("Starting Execution our Framework....");
        eyesRunner = new VisualGridRunner(new RunnerOptions().testConcurrency(5));
        eyes = new Eyes(eyesRunner);
        Configuration config = (Configuration) eyes.getConfiguration()
                .setApiKey(System.getenv("APPLITOOLS_API_KEY"))
                .addBrowsers(
                        new DesktopBrowserInfo(800, 1024, BrowserType.CHROME),
                        new DesktopBrowserInfo(1600, 1200, BrowserType.FIREFOX),
                        new DesktopBrowserInfo(1024, 768, BrowserType.SAFARI),
                        new ChromeEmulationInfo(DeviceName.Pixel_2, ScreenOrientation.PORTRAIT),
                        new ChromeEmulationInfo(DeviceName.Nexus_10, ScreenOrientation.LANDSCAPE))
        .setBatch(BATCH);
        eyes.setConfiguration(config);
    }

    @BeforeEach
    public void setUp(){
        ChromeOptions options = new ChromeOptions().addArguments("--headless=new");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
    }

    @Test
    public void testloginPageVisualization() throws InterruptedException {
        eyes.open(driver, "Acme Bank", "Test login visualization using selenium and applitools");
        driver.get("https://sandbox.applitools.com/bank");

        eyes.check(Target.window().fully().withName("Login Page"));
        driver.findElement(By.id("username")).sendKeys("Mohamed Mostafa");
        driver.findElement(By.id("password")).sendKeys("Test@2025");
        driver.findElement(By.id("log-in")).click();
        Thread.sleep(2000);
        eyes.check(Target.window().fully().withName("Main Page"));
    }

    @AfterEach
    public void applitoolsServerTearDown(){
        System.out.println("Closing appliTools Server...");
        eyes.closeAsync();
    }

    @AfterAll
    public static void tearDown(){
        System.out.println("Closing Our Test Cases...");
        driver.quit();
        System.out.println(eyesRunner.getAllTestResults());
    }
}
