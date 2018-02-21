package regonfinder.application.controller;

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
import regonfinder.application.csv.CsvWriter;
import regonfinder.location.Commune;
import regonfinder.location.County;
import regonfinder.location.Location;
import regonfinder.location.Place;
import regonfinder.location.webbrowser.RegonBrowser;
import regonfinder.location.webbrowser.RegonOptionsFactory;
import regonfinder.location.webbrowser.RegonType;
import regonfinder.location.webbrowser.Reports;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("voivodeships", RegonOptionsFactory.VOIVODESHIPS.keySet());
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
                                                   @RequestParam(value = "countyName") String countyName) {
        return RegonOptionsFactory.VOIVODESHIPS.getOrDefault(voivodeshipName, null)
                .getCounties().stream()
                    .filter(county -> county.getName().equals(countyName))
                    .findFirst().orElse(null).getCommunes();
    }

    @GetMapping("/place")
    public @ResponseBody List<Place> getPlaces(@RequestParam(value = "voivodeshipName") String voivodeshipName,
                                               @RequestParam(value = "countyName") String countyName,
                                               @RequestParam(value = "communeName") String communeName) {
        return RegonOptionsFactory.VOIVODESHIPS.getOrDefault(voivodeshipName, null)
                .getCounties().stream()
                .filter(county -> county.getName().equals(countyName))
                .findFirst().orElse(null).getCommunes().stream()
                        .filter(commune -> commune.getName().equals(communeName))
                        .findFirst().orElse(null).getPlaces();
    }

    @PostMapping(value = "/address", produces = "text/csv")
    public @ResponseBody
    StreamingResponseBody getRegons(@ModelAttribute("location") Location location,
                                    HttpServletResponse response) throws IOException {

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=" + location.getPlaceName() + LocalDateTime.now() + ".csv");

        return outputStream -> {
            RegonBrowser regonBrowser = new RegonBrowser();
            ReportClient reportClient = new ReportClient();
            ReportParser reportParser = new ReportParser();
            CsvWriter csvWriter = new CsvWriter();

            final Set<String> csvHeader = csvWriter.writeHeader(outputStream);
            final List<RegonType> regons = regonBrowser.getRegons(location);

            for (RegonType regon : regons) {
                try {
                    final Reports report = reportClient.getReport(regon);
                    Map<String, String> parsedReport = reportParser.parseReport(report);
                    csvWriter.appendMapToFile(outputStream, csvHeader, parsedReport);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            reportClient.logout();
        };
    }
}
