package regonfinder.location.webbrowser;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import regonfinder.application.constants.ApplicationConstants;
import regonfinder.location.Location;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.By.xpath;
import static regonfinder.application.constants.ApplicationConstants.ADDRESS_BUTTON_ID;
import static regonfinder.application.constants.ApplicationConstants.CHROME_DRIVER_LOCATION;
import static regonfinder.application.constants.ApplicationConstants.COMMUNE_DROPDOWN_ID;
import static regonfinder.application.constants.ApplicationConstants.COUNTY_DROPDOWN_ID;
import static regonfinder.application.constants.ApplicationConstants.ERROR_DIALOG_ID;
import static regonfinder.application.constants.ApplicationConstants.NEXT_PAGE_BUTTON_ID;
import static regonfinder.application.constants.ApplicationConstants.NOTHING_FOUND_DIALOG_TEXT;
import static regonfinder.application.constants.ApplicationConstants.PLACE_DROPDOWN_ID;
import static regonfinder.application.constants.ApplicationConstants.PROGRESS_ICON_ID;
import static regonfinder.application.constants.ApplicationConstants.RECORD_COUND_SPAN_ID;
import static regonfinder.application.constants.ApplicationConstants.REGON_BROWSER_PAGE_ADDRESS;
import static regonfinder.application.constants.ApplicationConstants.RESULTS_TABLE_DIV;
import static regonfinder.application.constants.ApplicationConstants.RESULT_TABLE_CLASS;
import static regonfinder.application.constants.ApplicationConstants.SEARCH_BUTTON_ID;
import static regonfinder.application.constants.ApplicationConstants.STREET_DROPDOWN_ID;
import static regonfinder.application.constants.ApplicationConstants.VOIVODESHIP_DROPDOWN_ID;

public class RegonBrowser {

    private final int WAIT_FOR_ELEMENT_TIMEOUT_IN_SECONDS = 10;

    public LocationType prepareLocation(WebDriver driver, Location location) {

        driver.get(REGON_BROWSER_PAGE_ADDRESS);

        clickButton(driver, ADDRESS_BUTTON_ID,
                ExpectedConditions.numberOfElementsToBeMoreThan(xpath("//select[@id='" +
                        VOIVODESHIP_DROPDOWN_ID + "']/option"), 1));

        clickSearchButton(driver);
        selectLocation(driver, location);

        return getLocationType(driver);
    }

    public LocationType selectStreet(WebDriver driver, String streetId) {
        selectOption(driver, STREET_DROPDOWN_ID, streetId, null);
        clickSearchButton(driver);
        return getLocationType(driver);
    }

    public int getPagesCount(WebDriver driver) {
        final Optional<WebElement> pageIndexElement = elementExists(driver, ApplicationConstants.PAGE_INDEX_ID, null);

        return pageIndexElement.map(webElement -> {
            final String elementText = webElement.getText();
            return Integer.parseInt(elementText.substring(elementText.indexOf("/") + 1));
        }).orElse(1);
    }

    public List<RegonType> getRegonsFromPage(WebDriver driver) {
        List<RegonType> regons = new ArrayList<>();
        final List<WebElement> tableRows = getTableRows(driver);

        for (WebElement row : tableRows) {
            regons.add(fetchRegonFromResultTableRow(row));
        }

        return regons;
    }

    public List<RegonType> getRegons(Location location) {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.get(REGON_BROWSER_PAGE_ADDRESS);

        clickButton(driver, ADDRESS_BUTTON_ID,
                ExpectedConditions.numberOfElementsToBeMoreThan(xpath("//select[@id='" +
                        VOIVODESHIP_DROPDOWN_ID + "']/option"), 1));
        return searchInLocation(driver, location);
    }

    private List<RegonType> searchInLocation(WebDriver driver, Location location) {
        List<RegonType> regons = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_ELEMENT_TIMEOUT_IN_SECONDS);

        selectLocation(driver, location);

        clickSearchButton(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(id(RESULTS_TABLE_DIV)));

        final WebElement countSpan = driver.findElement(id(RECORD_COUND_SPAN_ID));

        List<WebElement> resultRows = getTableRows(driver);

        final int recordsCount = !countSpan.getText().equals("") ?
                Integer.parseInt(countSpan.getText()) :
                resultRows.size();

        while (regons.size() < recordsCount) {

            for (WebElement row : resultRows) {
                regons.add(fetchRegonFromResultTableRow(row));
            }
            if (regons.size() == recordsCount) {
                break;
            }

            goToNextPage(driver);
            resultRows = getTableRows(driver);
        }

        driver.quit();

        return regons;
    }

    private List<WebElement> getTableRows(WebDriver driver) {
        return driver.findElements(xpath("//table[@class='" + RESULT_TABLE_CLASS + "']/tbody/tr"));
    }

    private void selectOption(WebDriver driver, String dropdownId, String valueToSelect, String nextSelect) {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_ELEMENT_TIMEOUT_IN_SECONDS);
        final WebElement webElement = driver.findElement(id(dropdownId));
        Select dropdown = new Select(webElement);
        dropdown.selectByValue(valueToSelect);
        if (nextSelect != null) {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//select[@id='" + nextSelect + "']/option"), 1));
        }
    }

    private void clickButton(WebDriver driver, String buttonId, ExpectedCondition<List<WebElement>> waitForElement) {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_ELEMENT_TIMEOUT_IN_SECONDS);
        final WebElement addressButton = driver.findElement(id(buttonId));
        addressButton.click();
        wait.until(waitForElement);
    }

    private void selectLocation(WebDriver driver, Location location) {
        selectOption(driver, VOIVODESHIP_DROPDOWN_ID, location.getVoivodeshipId(),
                COUNTY_DROPDOWN_ID);
        selectOption(driver, COUNTY_DROPDOWN_ID, location.getCountyId(),
                COMMUNE_DROPDOWN_ID);
        selectOption(driver, COMMUNE_DROPDOWN_ID, location.getCommuneId(),
                PLACE_DROPDOWN_ID);
        selectOption(driver, PLACE_DROPDOWN_ID, location.getPlaceId(), null);
    }

    private RegonType fetchRegonFromResultTableRow(WebElement row) {
        final List<WebElement> tableCell = row.findElements(tagName("td"));

        String regon = tableCell.get(0).getText();
        String type = tableCell.get(1).getText();

        return new RegonType(regon, type);
    }

    public void goToNextPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_ELEMENT_TIMEOUT_IN_SECONDS);

        final WebElement nextPageButton = driver.findElement(id(NEXT_PAGE_BUTTON_ID));

        nextPageButton.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(id(PROGRESS_ICON_ID)));
    }

    private Optional<WebElement> elementExists(WebDriver driver, String elementId, @Nullable ExpectedCondition<Boolean> waitFor) {
        try {
            if (waitFor != null) {
                WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_ELEMENT_TIMEOUT_IN_SECONDS);
                wait.until(waitFor);
            }

            final WebElement element = driver.findElement(id(elementId));
            return (!element.getCssValue("display").equals("none") && !element.getText().isEmpty()) ?
                    Optional.of(element) : Optional.empty();
        } catch (NoSuchElementException | TimeoutException e) {
            final Alert alert = ExpectedConditions.alertIsPresent().apply(driver);
            if (alert != null) {
                alert.accept();
                return elementExists(driver, elementId, waitFor);
            } else {
                return Optional.empty();
            }
        }
    }

    private LocationType getLocationType(WebDriver driver) {
//        WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_ELEMENT_TIMEOUT_IN_SECONDS);
//        wait.until(ExpectedConditions.invisibilityOfElementLocated(id(PROGRESS_ICON_ID)));
        final Optional<WebElement> errorElement = elementExists(driver, ERROR_DIALOG_ID,
                ExpectedConditions.and(
                        ExpectedConditions.invisibilityOfElementLocated(id(PROGRESS_ICON_ID)),
                        ExpectedConditions.attributeContains(id(PROGRESS_ICON_ID), "style", "display: none")
                ));

        return errorElement.map(webElement -> webElement.getText().equals(NOTHING_FOUND_DIALOG_TEXT ) ?
                LocationType.EMPTY : LocationType.BIG).orElse(LocationType.REGULAR);
    }

    private void clickSearchButton(WebDriver driver) {
        final WebElement searchButton = driver.findElement(id(SEARCH_BUTTON_ID));
        searchButton.click();
    }
}
