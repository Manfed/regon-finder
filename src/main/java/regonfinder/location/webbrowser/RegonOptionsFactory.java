package regonfinder.location.webbrowser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import regonfinder.application.constants.ApplicationConstants;
import regonfinder.location.Commune;
import regonfinder.location.County;
import regonfinder.location.Place;
import regonfinder.location.Voivodeship;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.openqa.selenium.By.id;
import static regonfinder.application.constants.ApplicationConstants.REGON_BROWSER_PAGE_ADDRESS;

public class RegonOptionsFactory {

    private final static String ADDRESS_BUTTON_ID = "btnMenuSzukajPoAdresie";
    private final static String VOIVODESHIP_DROPDOWN_ID = "selWojewodztwo";
    private final static String COUNTY_DROPDOWN_ID = "selPowiat";
    private final static String COMMUNE_DROPDOWN_ID = "selGmina";
    private final static String PLACE_DROPDOWN_ID = "selMiejscowosc";

    private final static String DROPDOWN_OPTION_VALUE_ARGUMENT_NAME = "value";

    public static Map<String, Voivodeship> VOIVODESHIPS;

    public static void getVoivodeshipsData() {
        System.setProperty("webdriver.chrome.driver", ApplicationConstants.CHROME_DRIVER_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.get(REGON_BROWSER_PAGE_ADDRESS);

        clickAddressButton(driver);

        List<String> voivodeshipNames = getDropdownItems(driver, VOIVODESHIP_DROPDOWN_ID);
        VOIVODESHIPS = getVoivodeships(driver, voivodeshipNames);

        driver.close();
        driver.quit();
    }

    private static Map<String, Voivodeship> getVoivodeships(WebDriver driver, List<String> voivodeshipNames) {
        return voivodeshipNames.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        voivodeshipName -> fetchVoivodeship(driver, voivodeshipName)
                ));
    }

    private static Voivodeship fetchVoivodeship(WebDriver driver, String voivodeshipName) {
        selectOption(driver, VOIVODESHIP_DROPDOWN_ID, voivodeshipName, COUNTY_DROPDOWN_ID);
        List<County> communes = getDropdownItems(driver, COUNTY_DROPDOWN_ID).stream()
                .map(countyName -> fetchCounty(driver, countyName))
                .collect(Collectors.toList());
        return new Voivodeship(voivodeshipName, communes);
    }

    private static County fetchCounty(WebDriver driver, String countyName) {
        selectOption(driver, COUNTY_DROPDOWN_ID, countyName, COMMUNE_DROPDOWN_ID);
        List<Commune> communes = getDropdownItems(driver, COMMUNE_DROPDOWN_ID).stream()
                .map(communeName -> fetchCommune(driver, communeName))
                .collect(Collectors.toList());
        return new County(countyName, communes);
    }

    private static Commune fetchCommune(WebDriver driver, String communeName) {
        selectOption(driver, COMMUNE_DROPDOWN_ID, communeName, PLACE_DROPDOWN_ID);
        List<Place> placeNames = getDropdownItems(driver, PLACE_DROPDOWN_ID).stream()
                .map(Place::new)
                .collect(Collectors.toList());
        return new Commune(communeName, placeNames);
    }

    private static List<String> getDropdownItems(WebDriver driver, String dropdownId) {
        final WebElement webElement = driver.findElement(id(dropdownId));
        Select dropdown = new Select(webElement);
        return dropdown.getOptions().stream()
                .filter(option ->  !option.getAttribute(DROPDOWN_OPTION_VALUE_ARGUMENT_NAME).isEmpty() &&
                        Integer.parseInt(option.getAttribute(DROPDOWN_OPTION_VALUE_ARGUMENT_NAME)) > 0)
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    private static void selectOption(WebDriver driver, String dropdownId, String valueToSelect, String nextSelect) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        final WebElement webElement = driver.findElement(id(dropdownId));
        Select dropdown = new Select(webElement);
        dropdown.selectByVisibleText(valueToSelect);
        if (nextSelect != null) {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//select[@id='" + nextSelect + "']/option"), 1));
        }
    }

    private static void clickAddressButton(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        final WebElement addressButton = driver.findElement(id(ADDRESS_BUTTON_ID));
        addressButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(id(VOIVODESHIP_DROPDOWN_ID)));
    }
}