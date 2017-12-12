package fi.livi.digitraffic.meri.service.winternavigation;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import fi.livi.digitraffic.meri.service.winternavigation.dto.ShipsDto;
import ibnet_baltice_ports.Ports;
import ibnet_baltice_schema.ObjectFactory;
import ibnet_baltice_schema.PortsRequestType;
import ibnet_baltice_schema.PortsResponseType;

@Service
public class WinterNavigationClient extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(WinterNavigationClient.class);

    private final ObjectFactory objectFactory = new ObjectFactory();

    @Autowired
    public WinterNavigationClient(@Value("${winter.navigation.url}") final String winterNavigationUrl,
                                  final Jaxb2Marshaller marshaller) {
        setDefaultUri(winterNavigationUrl);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        HttpComponentsMessageSender sender = new HttpComponentsMessageSender();
        sender.setConnectionTimeout(30000);
        sender.setReadTimeout(30000);
        setMessageSender(sender);
    }

    public Ports getWinterNavigationPorts() {
        final JAXBElement<PortsResponseType> portsResponseTypeJAXBElement =
            (JAXBElement<PortsResponseType>) getWebServiceTemplate().marshalSendAndReceive(objectFactory.createPortsRequest(new PortsRequestType()));

        return portsResponseTypeJAXBElement.getValue().getPorts();
    }

    public ShipsDto getWinterNavigationShips() {
        return null;
    }
}
