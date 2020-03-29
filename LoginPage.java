package com.loco;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.MobileElement;

public class LoginPage {

	public static final String ACCOUNT_SID = "AC4c7ed31fcd7fa8fa52f0ffff4f3cee84";
	public static final String AUTH_TOKEN = "b7eec21f1f9156ee0204de1aea85fe14";
	private static AndroidDriver driver;

	public static void main(String[] args) throws MalformedURLException, Exception {
		// TODO Auto-generated method stub

		String apkpath = "/Users/703245065MB/eclipse-workspace/Loco/app/loco.apk";
		File app = new File(apkpath);
		DesiredCapabilities caps = new DesiredCapabilities();

		caps.setCapability("deviceName", "Nexus 6");
		caps.setCapability("platformVersion", "8.1");
		caps.setCapability("platformName", "Android");
		// caps.setCapability("activityName", "com.showtimeapp");

		caps.setCapability("app", app.getAbsolutePath());

		driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), caps);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		MobileElement me = (MobileElement) driver
				.findElementByXPath("//*[@resource-id='com.showtimeapp:id/language_next_bottom']");
		me.click();
		// driver.findElementByXPath("//*[@resource-id='com.showtimeapp:id/language_next_bottom']").click();
		Thread.sleep(2000);
		System.out.println("i am here");
		MobileElement me1 = (MobileElement) driver
				.findElementByXPath("//*[@resource-id='com.showtimeapp:id/rlClickConsumer']");
		me1.click();

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		MobileElement me2 = (MobileElement) driver
				.findElementByXPath("//*[@resource-id='com.showtimeapp:id/editText_search']");
		me2.sendKeys("USA");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		MobileElement me3 = (MobileElement) driver
				.findElementByXPath("//*[@resource-id='com.showtimeapp:id/textView_countryName']");
		me3.click();

		MobileElement me4 = (MobileElement) driver.findElementByXPath("//*[@resource-id='com.showtimeapp:id/number']");
		me4.sendKeys("7183954729");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		MobileElement me5 = (MobileElement) driver.findElementByXPath("//*[@resource-id='com.showtimeapp:id/phone_next_bottom']");
		me5.click();

		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		String smsBody = getMessage();
		System.out.println(smsBody);
		String OTP = smsBody.replaceAll("[^-?0-9+", " ");
		System.out.println(OTP);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		MobileElement me6 = (MobileElement) driver.findElementByXPath("//*[@resource-id='com.showtimeapp:id/outh-pv-enter-code']");
		me6.sendKeys(OTP);
		
		System.out.println("Successfully logged in to loco app");

	}
	// get OTP using twilio

	public static String getMessage() {
		return getMessages().filter(m -> m.getDirection().compareTo(Message.Direction.INBOUND) == 0)
				.filter(m -> m.getTo().equals("+17183954729")).map(Message::getBody).findFirst()
				.orElseThrow(IllegalStateException::new);
	}

	private static Stream<Message> getMessages() {
		ResourceSet<Message> messages = Message.reader(ACCOUNT_SID).read();
		return StreamSupport.stream(messages.spliterator(), false);

	}
}
