package regonfinder.location.webbrowser;

public class Reports {
    private String generalReport;
    private String pkdReport;
    private String basicData;
    private String additionalReport;

    public Reports(String generalReport, String pkdReport, String basicData) {
        this(generalReport, pkdReport, basicData, null);
    }

    public Reports(String generalReport, String pkdReport, String basicData, String additionalReport) {
        this.generalReport = generalReport;
        this.pkdReport = pkdReport;
        this.basicData = basicData;
        this.additionalReport = additionalReport;
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

    public String getAdditionalReport() {
        return additionalReport;
    }

    public boolean hasAdditionalData() {
        return null != additionalReport && additionalReport.length() > 0;
    }
}
