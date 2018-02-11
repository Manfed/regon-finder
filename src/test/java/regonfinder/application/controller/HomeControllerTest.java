package regonfinder.application.controller;

import org.junit.Test;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import regonfinder.location.Location;

import static org.junit.Assert.assertNotNull;

public class HomeControllerTest {
    @Test
    public void getRegons() throws Exception {
        HomeController homeController = new HomeController();
        Location location = new Location("POMORSKIE", "kartuski", "Kartuzy obszar wiejski", "Dzierżążno");

        final ResponseEntity<InputStreamResource> regons = homeController.getRegons(location, null);

        assertNotNull(regons);

    }

}