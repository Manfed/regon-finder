package regonfinder.application.constants;

public class ApplicationConstants {

    public static final String REGON_BROWSER_PAGE_ADDRESS =
            "https://wyszukiwarkaregon.stat.gov.pl/appBIR/index.aspx";

    public static final String CHROME_DRIVER_LOCATION = getChromedriver();

    public static final String BACKUP_FILE_NAME = "voivodeship-backup";

    public final static String ADDRESS_BUTTON_ID = "btnMenuSzukajPoAdresie";
    public final static String VOIVODESHIP_DROPDOWN_ID = "selWojewodztwo";
    public final static String COUNTY_DROPDOWN_ID = "selPowiat";
    public final static String COMMUNE_DROPDOWN_ID = "selGmina";
    public final static String PLACE_DROPDOWN_ID = "selMiejscowosc";
    public final static String SEARCH_BUTTON_ID = "btnSzukajPoAdresie";
    public final static String RESULTS_TABLE_DIV = "divListaJednostek";

    public final static String RECORD_COUND_SPAN_ID = "spanRecCnt";
    public final static String NEXT_PAGE_BUTTON_ID = "btnNextPage";
    public final static String RESULT_TABLE_CLASS = "tabelaZbiorcza";
    public final static String PROGRESS_ICON_ID = "divProgressIcon";

    //Address and Key to the test environment
    public static final String BIR1_ADDRESS = "https://wyszukiwarkaregontest.stat.gov.pl/wsBIR/UslugaBIRzewnPubl.svc";
    public static final String BIR1_WSDL_ADDRESS = "https://wyszukiwarkaregontest.stat.gov.pl/wsBIR/wsdl/UslugaBIRzewnPubl.xsd";
    public static final String BIR1_USER_KEY = "abcde12345abcde12345";


    private static String getChromedriver() {
        return ApplicationConstants.class.getClassLoader()
                .getResource("chromedriver").getFile();
    }
}
