package regonfinder.application.bir.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import org.tempuri.UslugaBIRzewnPubl;

import cis.bir.publ._2014._07.IUslugaBIRzewnPubl;

import javax.xml.ws.soap.AddressingFeature;

public class FullReportClient extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(FullReportClient.class);

    public void getFullReport(String regon) {
        UslugaBIRzewnPubl service  = new UslugaBIRzewnPubl();
        IUslugaBIRzewnPubl port = service.getE3(new AddressingFeature());

        port.danePobierzPelnyRaport() //TODO
    }

}
