package regonfinder.application.bir.client;

import org.junit.Test;
import regonfinder.location.webbrowser.RegonType;

public class FullReportClientTest {
    @Test
    public void getFullReport() throws Exception {
        FullReportClient fullReportClient = new FullReportClient();
        RegonType regonType = new RegonType("080207121", "P", "DaneRaportPrawnaPubl");

        fullReportClient.getFullReport(regonType);
    }

}