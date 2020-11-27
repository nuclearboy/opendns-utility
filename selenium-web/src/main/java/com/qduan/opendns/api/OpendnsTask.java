package com.qduan.opendns.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class OpendnsTask {

	WebDriver driver = new WebDriverHelper().getWebDriver();
	WebDriverWait wait = new WebDriverWait(driver, 10);

	String username;
	String password;
	String settingsId;
	boolean headless;

	String openDnsSettingsPageUrl = "https://dashboard.opendns.com/settings/SETTINGS_ID/content_filtering";
	String openDnsLoginUrl = "https://login.opendns.com";

	public OpendnsTask(String username, String password, String settingsId) {
		this.username = username;
		this.password = password;
		this.openDnsSettingsPageUrl = openDnsSettingsPageUrl.replace("SETTINGS_ID", settingsId);
	}

	public void login() {
		driver.get(openDnsLoginUrl);
		driver.findElement(By.cssSelector("#username")).sendKeys(username);
		driver.findElement(By.cssSelector("#password")).sendKeys(password);
		driver.findElement(By.cssSelector("#sign-in")).click();
		// System.out.println(driver.getPageSource());
	}

	public void gotoSetings() {
		driver.get(openDnsSettingsPageUrl);
	}

	public void deleteAllBlockedDomains() {
		List<WebElement> blockedDomainLables = driver.findElements(By.cssSelector("div#always-block-container label"));
		for (WebElement element : blockedDomainLables) {
			System.out.println("existing domain: " + element.getText());
		}

		List<WebElement> blockedDomainCheckboxes = driver
				.findElements(By.cssSelector("div#always-block-container input"));
		System.out.println("checkboxes number: " + blockedDomainCheckboxes.size());
		for (WebElement element : blockedDomainCheckboxes) {
			element.click();
			wait.until(ExpectedConditions.visibilityOf(element));
		}

		WebElement deleteButton = driver.findElement(By.cssSelector("#delete-domains"));
		deleteButton.click();
		System.out.println("All existing domain unblocked.");
	}

	public void unblockDomain(String name) {

		List<WebElement> blockedDomainLables = driver.findElements(By.cssSelector("div#always-block-container label"));

		List<WebElement> blockedDomainCheckboxes = driver
				.findElements(By.cssSelector("div#always-block-container input"));
		System.out.println("checkboxes number: " + blockedDomainCheckboxes.size());
		boolean domainFound = false;
		for (int i = 0; i < blockedDomainLables.size(); i++) {
			WebElement element = blockedDomainLables.get(i);
			System.out.println("found: " + element.getText());
			if (element.getText().equals(name)) {
				WebElement checkboxElement = blockedDomainCheckboxes.get(i);
				checkboxElement.click();
				System.out.println("domain selected: " + name);
				domainFound = true;
				break;
			}
			wait.until(ExpectedConditions.visibilityOf(element));
		}
		if (domainFound) {
			WebElement deleteButton = driver.findElement(By.cssSelector("#delete-domains"));
			deleteButton.click();
			System.out.println("domain unblocked: " + name);
		} else {
			System.out.println("nothing to unblock because no existing domain found: " + name);
		}
	}

	public void blockDomains(List<String> domainNames) {
		for (String name : domainNames) {
			blockDomain(name.trim());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	public void unblockDomains(List<String> domainNames) {
		for (String name : domainNames) {
			unblockDomain(name.trim());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	public void blockDomain(String name) {

		WebElement inputElement = null;
		try {
			inputElement = driver.findElement(By.cssSelector("#block-domain"));
		} catch (Exception e) {
			try {
				System.out.println("waiting for page to load.");
				Thread.sleep(2000);
				inputElement = driver.findElement(By.cssSelector("#block-domain"));
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		System.out.println("blocking domain: " + name);
		wait.until(ExpectedConditions.visibilityOf(inputElement));
		inputElement.sendKeys(name);
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#add-domain"))).click();
		// driver.findElement(By.cssSelector("#add-domain")).click();
		// WebElement confirmButton =
		// driver.findElement(By.cssSelector("#confirm-add-domain"));
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#confirm-add-domain"))).click();
		} catch (Exception e) {
			System.out.println("no confirm button, move on");
		}

	}

	public void done() {
		driver.close();
		System.out.println("Task successfully finished.");
	}

	public Set<String> getBlockedDomains() {
		List<WebElement> blockedDomainLables = driver.findElements(By.cssSelector("div#always-block-container label"));
		Set<String> rval = new HashSet<String>();
		for (WebElement element : blockedDomainLables) {
			rval.add(element.getText());
		}
		return rval;
	}
}
