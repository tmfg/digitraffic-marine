package fi.livi.digitraffic.meri.service.winternavigation;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import fi.livi.digitraffic.meri.annotation.NotTransactionalServiceMethod;
import ibnet_baltice_ports.Ports;
import ibnet_baltice_schema.ObjectFactory;
import ibnet_baltice_schema.PortsRequestType;
import ibnet_baltice_schema.PortsResponseType;
import ibnet_baltice_schema.WaypointsRequestType;
import ibnet_baltice_schema.WaypointsResponseType;
import ibnet_baltice_schema.WinterShipsRequestType;
import ibnet_baltice_schema.WinterShipsResponseType;
import ibnet_baltice_waypoints.DirWaysType;
import ibnet_baltice_winterships.WinterShips;

@Service
@ConditionalOnNotWebApplication
public class WinterNavigationClient extends WebServiceGatewaySupport {
    private static final Logger log = LoggerFactory.getLogger(WinterNavigationClient.class);

    private final ObjectFactory objectFactory = new ObjectFactory();

    @Autowired
    public WinterNavigationClient(@Value("${winter.navigation.url:}") final String winterNavigationUrl,
                                  final Jaxb2Marshaller marshaller) {
        setDefaultUri(winterNavigationUrl);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        final HttpComponentsMessageSender sender = new HttpComponentsMessageSender();
        sender.setConnectionTimeout(60000);
        sender.setReadTimeout(60000);
        setMessageSender(sender);
    }

    @NotTransactionalServiceMethod
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 30000))
    public Ports getWinterNavigationPorts() {
        final JAXBElement<PortsResponseType> portsResponseTypeJAXBElement =
            (JAXBElement<PortsResponseType>) getWebServiceTemplate().marshalSendAndReceive(objectFactory.createPortsRequest(new PortsRequestType()));

        return portsResponseTypeJAXBElement.getValue().getPorts();
    }

    @NotTransactionalServiceMethod
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 30000))
    public WinterShips getWinterNavigationShips() {
        final JAXBElement<WinterShipsResponseType> winterShipsResponseTypeJAXBElement =
            (JAXBElement<WinterShipsResponseType>) getWebServiceTemplate().marshalSendAndReceive(objectFactory.createWinterShipsRequest(new WinterShipsRequestType()));

        return winterShipsResponseTypeJAXBElement.getValue().getWinterShips();
    }

    @NotTransactionalServiceMethod
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 30000))
    public DirWaysType getWinterNavigationWaypoints() {
        final JAXBElement<WaypointsResponseType> waypointsResponseTypeJAXBElement =
            (JAXBElement<WaypointsResponseType>) getWebServiceTemplate().marshalSendAndReceive(objectFactory.createWaypointsRequest(new WaypointsRequestType()));

        return waypointsResponseTypeJAXBElement.getValue().getWaypoints();
    }
}
