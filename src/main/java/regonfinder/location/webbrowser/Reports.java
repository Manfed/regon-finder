package regonfinder.location.webbrowser;

public class Reports {
    private String generalReport;
    private String pkdReport;

    public Reports(String generalReport, String pkdReport) {
        this.generalReport = generalReport;
        this.pkdReport = pkdReport;
    }

    public String getGeneralReport() {
        return generalReport;
    }

    public String getPkdReport() {
        return pkdReport;
    }
}
