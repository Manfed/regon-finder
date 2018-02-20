package regonfinder.application.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
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
    ResponseEntity<InputStreamResource> getRegons(@ModelAttribute("location") Location location) throws IOException {
        RegonBrowser regonBrowser = new RegonBrowser();
        ReportClient reportClient = new ReportClient();
        ReportParser reportParser = new ReportParser();
        Writer writer = new StringWriter();
        CsvWriter csvWriter = new CsvWriter();

        final Set<String> csvHeader = csvWriter.writeHeader(writer);
        final List<RegonType> regons = regonBrowser.getRegons(location);

        for (RegonType regon : regons) {
            try {
                final Reports report = reportClient.getReport(regon);
                Map<String, String> parsedReport = reportParser.parseReport(report);
                csvWriter.appendMapToFile(writer, csvHeader, parsedReport);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        reportClient.logout();

        final byte[] csvBytes = writer.toString().getBytes();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(csvBytes);
        return ResponseEntity
                .ok()
                .contentLength(csvBytes.length)
                .contentType(
                        MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .header("Content-Disposition", "attachment; filename=" + location.getPlaceName() + ".csv")
                .body(new InputStreamResource(inputStream));
    }
}
