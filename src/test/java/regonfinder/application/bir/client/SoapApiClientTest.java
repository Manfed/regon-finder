package regonfinder.application.bir.client;

import cis.bir.publ._2014._07.IUslugaBIRzewnPubl;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SoapApiClientTest {
    @Test
    public void prepareApiTest() throws Exception {
        SoapApiClient soapApiClient = new SoapApiClient();

        final IUslugaBIRzewnPubl port = soapApiClient.prepareApi();

        assertNotNull(port);
        assertNotNull(soapApiClient.getSoapMessageHandler().getSessionCookie());
    }

}