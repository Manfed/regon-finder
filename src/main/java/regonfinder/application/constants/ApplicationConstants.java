package regonfinder.application.constants;

import com.google.common.collect.ImmutableList;

import java.util.List;

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

    public static final List<String> CUSTOM_HEADER = ImmutableList.of(
            "regon9",
            "regon14",
            "nip",
            "nazwisko",
            "imie1",
            "imie2",
            "Nazwa",
            "nazwa",
            "Wojewodztwo",
            "Powiat",
            "Gmina",
            "KodPocztowy",
            "Miejscowosc",
            "Ulica",
            "adSiedzNumerNieruchomosci",
            "adSiedzNumerLokalu",
            "adSiedzNietypoweMiejsceLokalizacji",
            "dataSkresleniazRegon",
            "szczegolnaFormaPrawna_Nazwa",
            "Typ",
            "dataWpisuDoREGON",
            "dataPowstania",
            "dzialalnosci",
            "nazwaSkrocona",
            "numerTelefonu",
            "adresEmail",
            "pkd_Kod",
            "pkd_Nazwa"
    );

    public static final List<String> REPORT_FIELDS = ImmutableList.of(
            //"regon9",
            //"nip",
            //"nazwisko",
            //"imie1",
            //"imie2",
//            "dataZaistnieniaZmiany",
            //"dataSkresleniazRegon",
//            "podstawowaFormaPrawna_Symbol",
//            "szczegolnaFormaPrawna_Symbol",
//            "formaFinansowania_Symbol",
//            "formaWlasnosci_Symbol",
//            "podstawowaFormaPrawna_Nazwa",
            //"szczegolnaFormaPrawna_Nazwa",
//            "formaFinansowania_Nazwa",
//            "formaWlasnosci_Nazwa",
//            "dzialalnosciCeidg",
//            "dzialalnosciRolniczych",
//            "dzialalnosciPozostalych",
//            "dzialalnosciZKrupgn",
            //"regon14",
            //"nazwa",
//            "silos_Symbol",
//            "silos_Nazwa",
//            "adSiedzKraj_Symbol",
//            "adSiedzWojewodztwo_Symbol",
//            "adSiedzPowiat_Symbol",
//            "adSiedzGmina_Symbol",
//            "adSiedzKodPocztowy",
//            "adSiedzMiejscowoscPoczty_Symbol",
//            "adSiedzMiejscowosc_Symbol",
//            "adSiedzUlica_Symbol",
            //"adSiedzNumerNieruchomosci",
            //"adSiedzNumerLokalu",
            //"adSiedzNietypoweMiejsceLokalizacji",
//            "adSiedzKraj_Nazwa",
//            "adSiedzWojewodztwo_Nazwa",
//            "adSiedzPowiat_Nazwa",
//            "adSiedzGmina_Nazwa",
//            "adSiedzMiejscowoscPoczty_Nazwa",
//            "adSiedzMiejscowosc_Nazwa",
//            "adSiedzUlica_Nazwa",
            //"dataPowstania",
            //"dataWpisuDoREGON",
//            "dataRozpoczeciaDzialalnosci",
//            "dataZawieszeniaDzialalnosci",
//            "dataWznowieniaDzialalnosci",
//            "dataZakonczeniaDzialalnosci",
//            "dataWpisuDoRejestruEwidencji",
//            "numerwRejestrzeEwidencji",
//            "organRejestrowy_Symbol",
//            "organRejestrowy_Nazwa",
//            "rodzajRejestru_Symbol",
//            "rodzajRejestru_Nazwa",
            //"dzialalnosci",
//            "numerWrejestrzeEwidencji",
//            "organZalozycielski_Symbol",
//            "rodzajRejestruEwidencji_Symbol",
//            "organZalozycielski_Nazwa",
//            "rodzajRejestruEwidencji_Nazwa",
            //"nazwaSkrocona",
            //"numerTelefonu",
//            "numerWewnetrznyTelefonu",
//            "numerFaksu",
            //"adresEmail"
//            "adresStronyinternetowej",
//            "adresEmail2",
//            "jednostekLokalnych");
    );
    public static final List<String> BASIC_DATA_FIELDS = ImmutableList.of(
            //"Nazwa",
            //"Wojewodztwo",
            //"Powiat",
            //"Gmina",
            //"Miejscowosc",
            //"KodPocztowy",
            //"Ulica",
            //"Typ"
//            "SilosID"
    );

    public static final List<String> PKD_REPORT_FIELDS = ImmutableList.of(
            "pkd_Kod",
            "pkd_Nazwa"
//            "pkd_Przewazajace",
//            "Silos_Symbol");
    );

    private static String getChromedriver() {
        return ApplicationConstants.class.getClassLoader()
                .getResource("chromedriver").getFile();
    }
}
