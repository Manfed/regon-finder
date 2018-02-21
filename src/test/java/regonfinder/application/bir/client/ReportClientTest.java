package regonfinder.application.bir.client;

import org.junit.Before;
import org.junit.Test;
import regonfinder.location.webbrowser.RegonType;
import regonfinder.location.webbrowser.Reports;

import static org.junit.Assert.assertNotNull;

public class ReportClientTest {

    private ReportClient reportClient;

    @Before
    public void setUp() throws Exception {
        reportClient = new ReportClient();
    }

    @Test
    public void getReportTest() throws Exception {
        RegonType regonType = new RegonType("771413050", "F");
        final Reports report = reportClient.getReport(regonType);

        assertNotNull(report);
    }

}