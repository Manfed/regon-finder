package regonfinder.application.controller;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import regonfinder.location.Location;

import static org.junit.Assert.assertNotNull;

public class HomeControllerTest {
    @Test
    public void getRegons() throws Exception {
        HomeController homeController = new HomeController();
        Location location = new Location("POMORSKIE", "kartuski", "Kartuzy obszar wiejski", "Dzierżążno");

        final StreamingResponseBody regons = homeController.getRegons(location, new MockHttpServletResponse());

        assertNotNull(regons);

    }

}