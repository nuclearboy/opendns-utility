package com.qduan.opendns.api;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverHelper {
	private static final boolean HEADLESS = true;
	//TODO4 the selenium webdriver for chrome is in folder lib/chorme/driver. set the path to the absolute path of the driver.	
	private static final String CHROME_DRIVER_DIRECTORY = "/workspace/ws-dwiki-boot/selenium-chrome/lib/chrome-driver";

	protected WebDriver driver;

	// ----------------------------------------------------

	public WebDriver getWebDriver() {
		Path chrome = Paths.get(CHROME_DRIVER_DIRECTORY + "/chromedriver");
		chrome.toFile().setExecutable(true);

		System.setProperty("webdriver.chrome.driver", chrome.toAbsolutePath().toString());

		System.out.println(chrome.toAbsolutePath().toString());

		ChromeOptions chromeOptions = new ChromeOptions();
		if (HEADLESS) {
			chromeOptions.addArguments("--headless");
		}

		driver = new ChromeDriver(chromeOptions);

		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

		return driver;
	}
}
