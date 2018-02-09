package regonfinder.application.bir.client;

import cis.bir.publ._2014._07.IUslugaBIRzewnPubl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.tempuri.UslugaBIRzewnPubl;
import regonfinder.application.bir.client.handler.SoapHandlerResolver;
import regonfinder.application.bir.client.handler.SoapMessageHandler;
import regonfinder.application.constants.ApplicationConstants;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.AddressingFeature;
import java.net.MalformedURLException;
import java.net.URL;

public class FullReportClient extends WebServiceGatewaySupport {

    private final String SERVICE_STATUS = "StatusUslugi";

    private SoapMessageHandler soapMessageHandler;
    private SoapHandlerResolver soapHandlerResolver;

    public FullReportClient() {
        this.soapMessageHandler = new SoapMessageHandler();
        this.soapHandlerResolver = new SoapHandlerResolver(soapMessageHandler);
    }

    private static final Logger log = LoggerFactory.getLogger(FullReportClient.class);

    public void prepareApi() throws MalformedURLException {
        UslugaBIRzewnPubl service  = new UslugaBIRzewnPubl(new URL(ApplicationConstants.BIR1_WSDL_ADDRESS));
        service.setHandlerResolver(this.soapHandlerResolver);

        IUslugaBIRzewnPubl port = service.getE3(new AddressingFeature());

        BindingProvider bindingProvider = (BindingProvider) port;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ApplicationConstants.BIR1_ADDRESS);

        final String value = port.getValue(SERVICE_STATUS);

        return;
    }

}
