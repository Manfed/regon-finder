package regonfinder.location.webbrowser;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class RegonType {

    private final Map<String, ReportNames> REPORTS_DEPENDENCE = ImmutableMap.of(
            "F", new ReportNames("PublDaneRaportFizycznaOsoba", "PublDaneRaportDzialalnosciFizycznej"),
            "LF", new ReportNames("PublDaneRaportLokalnaFizycznej", "PublDaneRaportDzialalnosciLokalnejFizycznej"),
            "P", new ReportNames("PublDaneRaportPrawna", "PublDaneRaportDzialalnosciPrawnej"),
            "LP", new ReportNames("PublDaneRaportLokalnaPrawnej", "PublDaneRaportDzialalnosciLokalnejPrawnej")
    );

    private String regon;
    private String reportType;
    private ReportNames reportName;

    public RegonType(String regon, String reportType) {
        this.regon = regon;
        this.reportType = reportType;
        this.reportName = REPORTS_DEPENDENCE.get(reportType);
    }

    public String getRegon() {
        return regon;
    }

    public String getReportType() {
        return reportType;
    }

    public ReportNames getReportName() {
        return reportName;
    }
}
