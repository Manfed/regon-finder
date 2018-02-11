package regonfinder.location.webbrowser;

public class ReportNames {
    private String generalReportName;
    private String pkdReportName;

    public ReportNames(String generalReportName, String pkdReportName) {
        this.generalReportName = generalReportName;
        this.pkdReportName = pkdReportName;
    }

    public String getGeneralReportName() {
        return generalReportName;
    }

    public String getPkdReportName() {
        return pkdReportName;
    }
}
