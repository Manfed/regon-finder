package regonfinder.application.configuration;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import regonfinder.location.webbrowser.RegonOptionsFactory;

@Component
public class LocationInitializer implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        RegonOptionsFactory.getVoivodeshipsData();
    }
}
