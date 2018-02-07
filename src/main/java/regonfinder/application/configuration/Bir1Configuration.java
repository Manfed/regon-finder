package regonfinder.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import regonfinder.application.bir.client.FullReportClient;

@Configuration
public class Bir1Configuration {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("bir1.wsdl");
        return marshaller;
    }

    @Bean
    public FullReportClient quoteClient(Jaxb2Marshaller marshaller) {
        FullReportClient client = new FullReportClient();
        client.setDefaultUri("http://www.webservicex.com/stockquote.asmx"); //TODO
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
