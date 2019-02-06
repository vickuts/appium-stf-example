package io.openstf;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class AndroidTest {
    private static final String STF_SERVICE_URL = "http://localhost:7100";  // Change this URL
    private static final String ACCESS_TOKEN = "723cd2efac7d46038c5bd5b8b925adf40d105387f7de4639942f6b66a25d802f";  // Change this access token

    private AndroidDriver androidDriver;
    private String deviceSerial;
    private DeviceApi deviceApi;
//    private AppiumServer appiumServer = new AppiumServer();

    @Factory(dataProvider = "parallelDp")
    public AndroidTest(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    @DataProvider
    public Object[][] parallelDp() {
        return new Object[][]{
//                {"5203547fece0c37f"},    // Change the device serial
                {"emulator-5554"},    // Change the device serial
        };
    }

    @BeforeClass
    public void setup() throws IOException, URISyntaxException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "ANDROID");
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "ANDROID");
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        desiredCapabilities.setCapability(MobileCapabilityType.UDID, this.deviceSerial);
        desiredCapabilities.setCapability(MobileCapabilityType.APP, new File("src/test/resources/ApiDemos-debug.apk").getAbsolutePath());

//        STFService stfService = new STFService(STF_SERVICE_URL, ACCESS_TOKEN);
//        deviceApi = new DeviceApi(stfService);
//        deviceApi.connectDevice(deviceSerial);

        androidDriver = new AndroidDriver(new URL("http://0.0.0.0:4725/wd/hub"), desiredCapabilities);
        androidDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    }

    @Test
    public void currentActivityTest() {
        Assert.assertEquals(androidDriver.currentActivity(), ".ApiDemos", "Activity not match");
    }

    @Test(dependsOnMethods = {"currentActivityTest"})
    public void scrollingToSubElement() {
        androidDriver.findElementByAccessibilityId("Views").click();
        AndroidElement list = (AndroidElement) androidDriver.findElement(By.id("android:id/list"));
        MobileElement radioGroup = list
                .findElement(MobileBy
                        .AndroidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView("
                                + "new UiSelector().text(\"Radio Group\"));"));
        Assert.assertNotNull(radioGroup.getLocation());
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (androidDriver != null) {
            androidDriver.quit();
        }
//        deviceApi.releaseDevice(deviceSerial);
//        appiumServer.killAppiumProcess();
    }
}
