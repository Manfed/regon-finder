package regonfinder.application.controller;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import regonfinder.application.bir.client.ReportClient;
import regonfinder.application.bir.client.ReportParser;
import regonfinder.application.constants.ApplicationConstants;
import regonfinder.application.csv.CsvWriter;
import regonfinder.location.Commune;
import regonfinder.location.County;
import regonfinder.location.Location;
import regonfinder.location.Place;
import regonfinder.location.webbrowser.LocationType;
import regonfinder.location.webbrowser.RegonBrowser;
import regonfinder.location.webbrowser.RegonOptionsFactory;
import regonfinder.location.webbrowser.RegonType;
import regonfinder.location.webbrowser.Reports;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class HomeController {
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("voivodeships", RegonOptionsFactory.VOIVODESHIPS.values());
        model.addAttribute("location", new Location());
        return "home";
    }

    @GetMapping("/county")
    public @ResponseBody List<County> getCounties(@RequestParam(value = "voivodeshipName") String voivodeshipName) {
        return RegonOptionsFactory.VOIVODESHIPS.getOrDefault(voivodeshipName, null)
                .getCounties();
    }

    @GetMapping("/commune")
    public @ResponseBody List<Commune> getCommunes(@RequestParam(value = "voivodeshipName") String voivodeshipName,
                                                   @RequestParam(value = "countyId") String countyId) {
        return RegonOptionsFactory.VOIVODESHIPS.getOrDefault(voivodeshipName, null)
                .getCounties().stream()
                    .filter(county -> county.getId().equals(countyId))
                    .findFirst().orElse(null).getCommunes();
    }

    @GetMapping("/place")
    public @ResponseBody List<Place> getPlaces(@RequestParam(value = "voivodeshipName") String voivodeshipName,
                                               @RequestParam(value = "countyId") String countyId,
                                               @RequestParam(value = "communeId") String communeId) {
        return RegonOptionsFactory.VOIVODESHIPS.getOrDefault(voivodeshipName, null)
                .getCounties().stream()
                .filter(county -> county.getId().equals(countyId))
                .findFirst().orElse(null).getCommunes().stream()
                        .filter(commune -> commune.getId().equals(communeId))
                        .findFirst().orElse(null).getPlaces();
    }

    @PostMapping(value = "/address", produces = "text/csv")
    public @ResponseBody
    StreamingResponseBody getRegons(@ModelAttribute("location") Location location,
                                    HttpServletResponse response) throws IOException {

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=" + location.getPlaceName() + LocalDateTime.now() + ".csv");

        return outputStream -> {
            System.setProperty("webdriver.chrome.driver", ApplicationConstants.CHROME_DRIVER_LOCATION);
            WebDriver driver = new ChromeDriver();
            RegonBrowser regonBrowser = new RegonBrowser();

            final LocationType locationType = regonBrowser.prepareLocation(driver, location);

            switch (locationType) {
                case BIG:
                    fetchBigLocationData(driver, regonBrowser, outputStream);
                    break;
                case EMPTY:
                    LOG.debug("Empty location");
                    response.setStatus(204);
                    driver.quit();
                    return;
                case REGULAR:
                    fetchRegularLocationData(driver, regonBrowser, outputStream);
                    break;
                default:
                    driver.quit();
                    throw new UnsupportedOperationException();
            }

            driver.quit();
            LOG.info("Fetching of " + location.getPlaceName() + " finished successfully");
        };
    }

    private void fetchRegularLocationData(WebDriver driver, RegonBrowser regonBrowser, OutputStream outputStream) {
        ReportClient reportClient = new ReportClient();
        ReportParser reportParser = new ReportParser();
        CsvWriter csvWriter = new CsvWriter();

        try {
            int pagesCount = regonBrowser.getPagesCount(driver);
            final Set<String> csvHeader = csvWriter.writeHeader(outputStream);

            for (int i = 0; i < pagesCount; i++) {
                final List<RegonType> regonsFromPage = regonBrowser.getRegonsFromPage(driver);

                for (RegonType regon : regonsFromPage) {
                    final Reports report = reportClient.getReport(regon);
                    final Map<String, String> parsedReport = reportParser.parseReport(report);
                    csvWriter.appendMapToFile(outputStream, csvHeader, parsedReport);
                }

                if (i < pagesCount - 1) {
                    regonBrowser.goToNextPage(driver);
                }
            }

            reportClient.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchBigLocationData(WebDriver driver, RegonBrowser regonBrowser, OutputStream outputStream) {
        final List<String> streetIds = RegonOptionsFactory.getStreetIds(driver);

        streetIds.forEach(streetId -> {
            final LocationType locationType = regonBrowser.selectStreet(driver, streetId);
            if (locationType.equals(LocationType.REGULAR)) {
                fetchRegularLocationData(driver, regonBrowser, outputStream);
            }
        });
    }
}
