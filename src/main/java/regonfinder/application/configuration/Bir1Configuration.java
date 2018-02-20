package regonfinder.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import regonfinder.application.bir.client.SoapApiClient;

@Configuration
public class Bir1Configuration {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPaths("cis.bir._2014._07",
                "cis.bir.publ._2014._07",
                "cis.bir.publ._2014._07.datacontract");
        return marshaller;
    }

    @Bean
    public SoapApiClient quoteClient(Jaxb2Marshaller marshaller) {
        SoapApiClient client = new SoapApiClient();
        client.setDefaultUri("http://www.webservicex.com/stockquote.asmx"); //TODO
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
