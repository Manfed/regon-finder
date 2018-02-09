package regonfinder.location.webbrowser;

public class RegonType {

    private String regon;
    private String reportType;
    private String reportName;

    public RegonType(String regon, String reportType, String reportName) {
        this.regon = regon;
        this.reportType = reportType;
        this.reportName = reportName;
    }

    public String getRegon() {
        return regon;
    }

    public void setRegon(String regon) {
        this.regon = regon;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }
}
