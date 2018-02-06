package regonfinder.application.constants;

public class ApplicationConstants {

    public static final String REGON_BROWSER_PAGE_ADDRESS =
            "https://wyszukiwarkaregon.stat.gov.pl/appBIR/index.aspx";

    public static final String CHROME_DRIVER_LOCATION = getChromedriver();

    private static String getChromedriver() {
        return ApplicationConstants.class.getClassLoader()
                .getResource("chromedriver").getFile();
    }
}
