package regonfinder.application.bir.client.handler;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.ArrayList;
import java.util.List;

public class SoapHandlerResolver implements HandlerResolver {
    private SoapMessageHandler soapMessageHandler;

    public SoapHandlerResolver(SoapMessageHandler soapMessageHandler) {
        this.soapMessageHandler = soapMessageHandler;
    }
    @Override
    public List<Handler> getHandlerChain(PortInfo portInfo) {
        List<Handler> handlers = new ArrayList<>();
        handlers.add(soapMessageHandler);
        return handlers;
    }
}
