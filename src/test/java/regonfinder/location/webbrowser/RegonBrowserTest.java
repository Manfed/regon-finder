package regonfinder.location.webbrowser;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import regonfinder.application.constants.ApplicationConstants;
import regonfinder.location.Location;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class RegonBrowserTest {
    @Test
    public void getRegons() throws Exception {
        RegonBrowser regonBrowser = new RegonBrowser();
        Location location = new Location("POMORSKIE", "kartuski", "Kartuzy obszar wiejski", "Dzierżążno", "Dzierżążno");

        final List<RegonType> regons = regonBrowser.getRegons(location);

        assertEquals(regons.size(), 443);
    }

    @Test
    public void selectStreetTestWhenAlertPopsOut() throws Exception {
        RegonBrowser regonBrowser = new RegonBrowser();
        System.setProperty("webdriver.chrome.driver", ApplicationConstants.CHROME_DRIVER_LOCATION);
        WebDriver driver = new ChromeDriver();

        regonBrowser.prepareLocation(driver, new Location("22", "63", "011", "0977278", "Słupsk"));
        regonBrowser.selectStreet(driver, "13246");
    }
}