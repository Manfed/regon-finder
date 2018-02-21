package regonfinder.application.bir.client;

import cis.bir.publ._2014._07.IUslugaBIRzewnPubl;
import cis.bir.publ._2014._07.datacontract.ObjectFactory;
import cis.bir.publ._2014._07.datacontract.ParametryWyszukiwania;
import regonfinder.location.webbrowser.RegonType;
import regonfinder.location.webbrowser.Reports;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReportClient {

    private SoapApiClient soapApiClient;

    public ReportClient() {
        this.soapApiClient = new SoapApiClient();
    }

    public synchronized Reports getReport(RegonType regonType) {
        Reports reports = null;

        try {
            final IUslugaBIRzewnPubl port = soapApiClient.prepareApi();

            ObjectFactory objectFactory = new ObjectFactory();
            ParametryWyszukiwania searchParams = objectFactory.createParametryWyszukiwania();
            searchParams.setRegon(objectFactory.createParametryWyszukiwaniaRegon(regonType.getRegon()));

            String generalReport = port.danePobierzPelnyRaport(regonType.getRegon(),
                    regonType.getReportName().getGeneralReportName());
            String pkdReport = port.danePobierzPelnyRaport(regonType.getRegon(),
                    regonType.getReportName().getPkdReportName());
            String basicData = port.daneSzukaj(searchParams);

            final String silosId = getSilosId(basicData);
            String additionalReport = (silosId != null && regonType.getReportType().equals("F")) ?
                    port.danePobierzPelnyRaport(regonType.getRegon(), getReportNameForGivenSilosId(silosId)) : null;

            reports = new Reports(generalReport, pkdReport, basicData, additionalReport);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reports;
    }

    public void logout() {
        try {
            soapApiClient.logout();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private String getSilosId(final String basicDataReport) {
        Pattern pattern = Pattern.compile("<SilosID>(.*)</SilosID>");
        Matcher matcher = pattern.matcher(basicDataReport);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String getReportNameForGivenSilosId(final String silosId) {
        switch (silosId) {
            case "1":
                return "PublDaneRaportDzialalnoscFizycznejCeidg";
            case "2":
                return "PublDaneRaportDzialalnoscFizycznejRolnicza";
            case "3":
                return "PublDaneRaportDzialalnoscFizycznejPozostala";
            case "4":
                return "PublDaneRaportDzialalnoscFizycznejWKrupgn";
            default:
                return "PublDaneRaportLokalneFizycznej";
        }
    }
}
