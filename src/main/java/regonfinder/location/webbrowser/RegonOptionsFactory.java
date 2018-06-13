package regonfinder.location.webbrowser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import regonfinder.application.constants.ApplicationConstants;
import regonfinder.location.Commune;
import regonfinder.location.County;
import regonfinder.location.Place;
import regonfinder.location.Voivodeship;

import javax.annotation.Nullable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfElementsToBeMoreThan;
import static regonfinder.application.constants.ApplicationConstants.COMMUNE_DROPDOWN_ID;
import static regonfinder.application.constants.ApplicationConstants.COUNTY_DROPDOWN_ID;
import static regonfinder.application.constants.ApplicationConstants.PLACE_DROPDOWN_ID;
import static regonfinder.application.constants.ApplicationConstants.REGON_BROWSER_PAGE_ADDRESS;
import static regonfinder.application.constants.ApplicationConstants.STREET_DROPDOWN_ID;
import static regonfinder.application.constants.ApplicationConstants.VOIVODESHIP_DROPDOWN_ID;

public class RegonOptionsFactory {

    private final static String DROPDOWN_OPTION_VALUE_ARGUMENT_NAME = "value";

    public static Map<String, Voivodeship> VOIVODESHIPS;

    public static void getVoivodeshipsData() {
        final Path path = Paths.get(ApplicationConstants.BACKUP_FILE_NAME);

        if (Files.exists(path)) {
            try (FileInputStream fileInputStream = new FileInputStream(path.toString());
                 ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

                VOIVODESHIPS = (Map<String, Voivodeship>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try (FileOutputStream fileOutputStream = new FileOutputStream(path.toString());
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                System.setProperty("webdriver.chrome.driver", ApplicationConstants.CHROME_DRIVER_LOCATION);
                WebDriver driver = new ChromeDriver();
                driver.get(REGON_BROWSER_PAGE_ADDRESS);

                clickAddressButton(driver);

                VOIVODESHIPS = getVoivodeships(driver);

                objectOutputStream.writeObject(VOIVODESHIPS);

                driver.close();
                driver.quit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> getStreetIds(WebDriver driver) {
        final List<WebElement> dropdownItems = getDropdownItems(driver, STREET_DROPDOWN_ID,
                ExpectedConditions.numberOfElementsToBeMoreThan(xpath("//select[@id='" +
                        STREET_DROPDOWN_ID + "']/option"), 1));
        return dropdownItems.stream()
                .map(item -> item.getAttribute(DROPDOWN_OPTION_VALUE_ARGUMENT_NAME))
                .collect(Collectors.toList());
    }

    private static Map<String, Voivodeship> getVoivodeships(WebDriver driver) {
        return getDropdownItems(driver, VOIVODESHIP_DROPDOWN_ID, null).stream()
                .collect(toMap(
                        WebElement::getText,
                        option -> fetchVoivodeship(driver, option.getText(), option.getAttribute(DROPDOWN_OPTION_VALUE_ARGUMENT_NAME))
                ));
    }

    private static Voivodeship fetchVoivodeship(WebDriver driver, String voivodeshipName, String voivodeshipId) {
        selectOption(driver, VOIVODESHIP_DROPDOWN_ID, voivodeshipId, COUNTY_DROPDOWN_ID);
        List<County> counties = getDropdownItems(driver, COUNTY_DROPDOWN_ID, null).stream()
                .map(county -> fetchCounty(driver, county.getText(), county.getAttribute(DROPDOWN_OPTION_VALUE_ARGUMENT_NAME)))
                .collect(Collectors.toList());
        return new Voivodeship(voivodeshipName, Integer.parseInt(voivodeshipId), counties);
    }

    private static County fetchCounty(WebDriver driver, String countyName, String countyId) {
        selectOption(driver, COUNTY_DROPDOWN_ID, countyId, COMMUNE_DROPDOWN_ID);
        List<Commune> communes = getDropdownItems(driver, COMMUNE_DROPDOWN_ID, null).stream()
                .map(commune -> fetchCommune(driver, commune.getText(), commune.getAttribute(DROPDOWN_OPTION_VALUE_ARGUMENT_NAME)))
                .collect(Collectors.toList());
        return new County(countyName, countyId, communes);
    }

    private static Commune fetchCommune(WebDriver driver, String communeName, String communeId) {
        selectOption(driver, COMMUNE_DROPDOWN_ID, communeId, PLACE_DROPDOWN_ID);
        List<Place> places = getDropdownItems(driver, PLACE_DROPDOWN_ID, null).stream()
                .map(place -> new Place(place.getText(), place.getAttribute(DROPDOWN_OPTION_VALUE_ARGUMENT_NAME)))
                .collect(Collectors.toList());
        return new Commune(communeName, communeId, places);
    }

    private static List<WebElement> getDropdownItems(WebDriver driver, String dropdownId, @Nullable ExpectedCondition waitFor) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        if (waitFor != null) {
            wait.until(waitFor);
        } else {
            wait.until(numberOfElementsToBeMoreThan(id(dropdownId), 1));
        }

        final WebElement webElement = driver.findElement(id(dropdownId));
        Select dropdown = new Select(webElement);
        return dropdown.getOptions().stream()
                .filter(option -> !option.getAttribute(DROPDOWN_OPTION_VALUE_ARGUMENT_NAME).isEmpty() &&
                        Integer.parseInt(option.getAttribute(DROPDOWN_OPTION_VALUE_ARGUMENT_NAME)) > 0)
                .collect(Collectors.toList());
    }

    /**
     * Select option in chosen regon browser dropdown
     * @param driver Selenium Webdriver
     * @param dropdownId id of the select element
     * @param value value of the dropdown option to select
     * @param nextSelect next dropdown which should be populated after selecting option in first
     */
    private static void selectOption(WebDriver driver, String dropdownId, String value, String nextSelect) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        final WebElement webElement = driver.findElement(id(dropdownId));
        Select dropdown = new Select(webElement);
        dropdown.selectByValue(value);
        if (nextSelect != null) {
            wait.until(numberOfElementsToBeMoreThan(xpath("//select[@id='" + nextSelect + "']/option"), 1));
        }
    }

    private static void clickAddressButton(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        final WebElement addressButton = driver.findElement(id(ApplicationConstants.ADDRESS_BUTTON_ID));
        addressButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(id(VOIVODESHIP_DROPDOWN_ID)));
    }
}
