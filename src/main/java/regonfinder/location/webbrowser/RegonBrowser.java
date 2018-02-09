package regonfinder.location.webbrowser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import regonfinder.application.constants.ApplicationConstants;
import regonfinder.location.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.By.xpath;
import static regonfinder.application.constants.ApplicationConstants.REGON_BROWSER_PAGE_ADDRESS;

public class RegonBrowser {

    public List<RegonType> getRegons(Location location) {
        System.setProperty("webdriver.chrome.driver", ApplicationConstants.CHROME_DRIVER_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.get(REGON_BROWSER_PAGE_ADDRESS);

        clickButton(driver, ApplicationConstants.ADDRESS_BUTTON_ID,
                ExpectedConditions.numberOfElementsToBeMoreThan(xpath("//select[@id='" +
                        ApplicationConstants.VOIVODESHIP_DROPDOWN_ID + "']/option"), 1));
        return searchInLocation(driver, location);
    }

    private List<RegonType> searchInLocation(WebDriver driver, Location location) {
        List<RegonType> regons = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(driver, 5);

        selectLocation(driver, location);

        final WebElement searchButton = driver.findElement(id(ApplicationConstants.SEARCH_BUTTON_ID));
        searchButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(id(ApplicationConstants.RESULTS_TABLE_DIV)));

        final WebElement countSpan = driver.findElement(id(ApplicationConstants.RECORD_COUND_SPAN_ID));
        final int recordsCount = Integer.parseInt(countSpan.getText());

        List<WebElement> resultRows = getTableRows(driver);

        while (regons.size() < recordsCount) {

            for (WebElement row : resultRows) {
                regons.add(fetchRegonFromResultTableRow(row));
            }
            goToNextPage(driver);
            resultRows = getTableRows(driver);
        }

        driver.close();
        driver.quit();

        return regons;
    }

    private List<WebElement> getTableRows(WebDriver driver) {
        return driver.findElements(xpath("//table[@class='" + ApplicationConstants.RESULT_TABLE_CLASS + "']/tbody/tr"));
    }

    private void selectOption(WebDriver driver, String dropdownId, String valueToSelect, String nextSelect) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        final WebElement webElement = driver.findElement(id(dropdownId));
        Select dropdown = new Select(webElement);
        dropdown.selectByVisibleText(valueToSelect);
        if (nextSelect != null) {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//select[@id='" + nextSelect + "']/option"), 1));
        }
    }

    private void clickButton(WebDriver driver, String buttonId, ExpectedCondition<List<WebElement>> waitForElement) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        final WebElement addressButton = driver.findElement(id(buttonId));
        addressButton.click();
        wait.until(waitForElement);
    }

    private void selectLocation(WebDriver driver, Location location) {
        selectOption(driver, ApplicationConstants.VOIVODESHIP_DROPDOWN_ID, location.getVoivodeshipName(),
                ApplicationConstants.COUNTY_DROPDOWN_ID);
        selectOption(driver, ApplicationConstants.COUNTY_DROPDOWN_ID, location.getCountyName(),
                ApplicationConstants.COMMUNE_DROPDOWN_ID);
        selectOption(driver, ApplicationConstants.COMMUNE_DROPDOWN_ID, location.getCommuneName(),
                ApplicationConstants.PLACE_DROPDOWN_ID);
        selectOption(driver, ApplicationConstants.PLACE_DROPDOWN_ID, location.getPlaceName(), null);
    }

    private RegonType fetchRegonFromResultTableRow(WebElement row) {
        final List<WebElement> tableCell = row.findElements(tagName("td"));

        WebElement regonElement = tableCell.get(0);

        String regon = tableCell.get(0).getText();
        String type = tableCell.get(1).getText();

        final WebElement anchor = regonElement.findElement(xpath(".//a"));

        Pattern pattern = Pattern.compile(".*\",\"(\\w+).*");
        Matcher matcher = pattern.matcher(anchor.getAttribute("href"));
        String reportName = matcher.find() ? matcher.group(1) : null;

        return new RegonType(regon, type, reportName);
    }

    private void goToNextPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 5);

        final WebElement nextPageButton = driver.findElement(id(ApplicationConstants.NEXT_PAGE_BUTTON_ID));

        nextPageButton.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(id(ApplicationConstants.PROGRESS_ICON_ID)));
    }
}
