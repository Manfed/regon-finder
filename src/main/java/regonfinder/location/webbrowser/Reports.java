package regonfinder.location.webbrowser;

public class Reports {
    private String generalReport;
    private String pkdReport;
    private String basicData;

    public Reports(String generalReport, String pkdReport, String basicData) {
        this.generalReport = generalReport;
        this.pkdReport = pkdReport;
        this.basicData = basicData;
    }

    public String getGeneralReport() {
        return generalReport;
    }

    public String getPkdReport() {
        return pkdReport;
    }

    public String getBasicData() {
        return basicData;
    }
}
