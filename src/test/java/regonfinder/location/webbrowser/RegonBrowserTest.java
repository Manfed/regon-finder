package regonfinder.location.webbrowser;

import org.junit.Test;
import regonfinder.location.Location;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class RegonBrowserTest {
    @Test
    public void getRegons() throws Exception {
        RegonBrowser regonBrowser = new RegonBrowser();
        Location location = new Location("POMORSKIE", "kartuski", "Kartuzy obszar wiejski", "Dzierżążno");

        final List<RegonType> regons = regonBrowser.getRegons(location);

        assertEquals(regons.size(), 443);
    }

}