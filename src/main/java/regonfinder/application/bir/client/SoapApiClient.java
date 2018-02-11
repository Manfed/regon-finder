package regonfinder.application.bir.client;

import cis.bir.publ._2014._07.IUslugaBIRzewnPubl;
import org.tempuri.UslugaBIRzewnPubl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import regonfinder.application.bir.client.handler.SoapHandlerResolver;
import regonfinder.application.bir.client.handler.SoapMessageHandler;
import regonfinder.application.constants.ApplicationConstants;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.AddressingFeature;
import java.net.MalformedURLException;
import java.net.URL;

public class SoapApiClient extends WebServiceGatewaySupport {

    private final String SESSION_STATUS = "StatusSesji";

    private SoapMessageHandler soapMessageHandler;
    private SoapHandlerResolver soapHandlerResolver;

    public SoapApiClient() {
        this.soapMessageHandler = new SoapMessageHandler();
        this.soapHandlerResolver = new SoapHandlerResolver(soapMessageHandler);
    }

    private static final Logger log = LoggerFactory.getLogger(SoapApiClient.class);

    public IUslugaBIRzewnPubl prepareApi() throws MalformedURLException {
        IUslugaBIRzewnPubl port = preparePort();

        final String sessionStatus = port.getValue(SESSION_STATUS);

        if (this.soapMessageHandler.getSessionCookie().equals("") || sessionStatus.equals("1")) {
            String sid = port.zaloguj(ApplicationConstants.BIR1_USER_KEY);
            this.soapMessageHandler.setSessionCookie(sid);
        }

        return port;
    }



    public void logout() throws MalformedURLException {
        final IUslugaBIRzewnPubl port = preparePort();
        port.wyloguj(soapMessageHandler.getSessionCookie());
    }

    public SoapMessageHandler getSoapMessageHandler() {
        return soapMessageHandler;
    }

    public SoapHandlerResolver getSoapHandlerResolver() {
        return soapHandlerResolver;
    }

    private IUslugaBIRzewnPubl preparePort() throws MalformedURLException {
        UslugaBIRzewnPubl service  = new UslugaBIRzewnPubl(new URL(ApplicationConstants.BIR1_WSDL_ADDRESS));
        service.setHandlerResolver(this.soapHandlerResolver);

        IUslugaBIRzewnPubl port = service.getE3(new AddressingFeature());

        BindingProvider bindingProvider = (BindingProvider) port;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ApplicationConstants.BIR1_ADDRESS);
        return port;
    }
}
