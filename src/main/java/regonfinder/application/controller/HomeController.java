package regonfinder.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import regonfinder.location.Commune;
import regonfinder.location.County;
import regonfinder.location.Location;
import regonfinder.location.Place;
import regonfinder.location.webbrowser.RegonBrowser;
import regonfinder.location.webbrowser.RegonOptionsFactory;
import regonfinder.location.webbrowser.RegonType;

import java.util.List;

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

    @PostMapping("/address")
    public @ResponseBody String getRegons(@ModelAttribute("location") Location location,
                                          Model model) {
        RegonBrowser regonBrowser = new RegonBrowser();
        final List<RegonType> regons = regonBrowser.getRegons(location);
        return "home";
    }
}
