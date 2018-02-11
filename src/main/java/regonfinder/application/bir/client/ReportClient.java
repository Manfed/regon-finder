package regonfinder.application.bir.client;

import cis.bir.publ._2014._07.IUslugaBIRzewnPubl;
import regonfinder.location.webbrowser.RegonType;
import regonfinder.location.webbrowser.Reports;

import java.net.MalformedURLException;

public class ReportClient {

    private static final long DELAY_BETWEEN_REQUESTS_IN_MS = 660;

    private static long lastRequestTime;
    private SoapApiClient soapApiClient;

    public ReportClient() {
        this.soapApiClient = new SoapApiClient();
    }

    public synchronized Reports getReport(RegonType regonType) {
        final long lastRequestDiff = System.currentTimeMillis() - lastRequestTime;
        Reports reports = null;

        if (lastRequestDiff < DELAY_BETWEEN_REQUESTS_IN_MS) {
            try {
                Thread.sleep(DELAY_BETWEEN_REQUESTS_IN_MS - lastRequestDiff);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            final IUslugaBIRzewnPubl port = soapApiClient.prepareApi();
            String generalReport = port.danePobierzPelnyRaport(regonType.getRegon(),
                    regonType.getReportName().getGeneralReportName());
            String pkdReport = port.danePobierzPelnyRaport(regonType.getRegon(),
                    regonType.getReportName().getPkdReportName());
            reports = new Reports(generalReport, pkdReport);
        } catch (Exception e) {
            e.printStackTrace();
        }
        lastRequestTime = System.currentTimeMillis();
        return reports;
    }

    public void logout() {
        try {
            soapApiClient.logout();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
