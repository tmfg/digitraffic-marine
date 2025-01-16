package fi.livi.digitraffic.meri.service.winternavigation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Date;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.xml.transform.StringSource;

import fi.livi.digitraffic.meri.AbstractDaemonTestBase;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationPortRepository;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationPortFeatureCollectionV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationPortFeatureV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationPortRestrictionV1;
import fi.livi.digitraffic.meri.service.winternavigation.v1.WinterNavigationWebServiceV1;
import ibnet_baltice_ports.Ports;
import ibnet_baltice_schema.PortsResponseType;
import jakarta.xml.bind.JAXBElement;

public class WinterNavigationPortUpdaterTest extends AbstractDaemonTestBase {

    @MockBean
    private WinterNavigationClient winterNavigationClient;

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private WinterNavigationPortUpdater winterNavigationPortUpdater;

    @Autowired
    private WinterNavigationPortRepository winterNavigationPortRepository;

    @Autowired
    private UpdatedTimestampRepository updatedTimestampRepository;

    @Autowired
    private Jaxb2Marshaller jaxb2Marshaller;

    private WinterNavigationWebServiceV1 winterNavigationWebServiceV1;

    private static final String LOCODE_PUHOS = "FIPUH";

    @BeforeEach
    public void before() {
        winterNavigationWebServiceV1 = loadBean(WinterNavigationWebServiceV1.class);
        winterNavigationPortUpdater = new WinterNavigationPortUpdater(winterNavigationClient, winterNavigationPortRepository, updatedTimestampRepository);
        winterNavigationPortRepository.deleteAll();
    }

    @Test
    @Transactional
    @Rollback
    public void updateWinterNavigationPortsSucceeds() throws IOException {
        when(winterNavigationClient.getWinterNavigationPorts()).thenReturn(getResponse("winternavigation/winterNavigationPortsResponse.xml"));

        winterNavigationPortUpdater.updateWinterNavigationPorts();

        final WinterNavigationPortFeatureCollectionV1 collection = winterNavigationWebServiceV1.getWinterNavigationPorts();
        assertEquals(156, collection.getFeatures().size());
        final WinterNavigationPortFeatureV1 feature = getPort(collection, LOCODE_PUHOS);
        assertEquals(LOCODE_PUHOS, feature.getProperties().locode);
        assertEquals("PUHOS", feature.getProperties().name);
        assertEquals("FI", feature.getProperties().nationality);
        assertEquals(Double.parseDouble("29.9167"), feature.getGeometry().getCoordinates().get(0), 0.00001);
        assertEquals(Double.parseDouble("62.1"), feature.getGeometry().getCoordinates().get(1), 0.01);
        assertEquals("Baltic Sea", feature.getProperties().seaArea);
        assertEquals(1, feature.getProperties().portRestrictions.size());

        final WinterNavigationPortRestrictionV1 restriction = feature.getProperties().portRestrictions.get(0);
        assertFalse(restriction.isCurrent);
        assertTrue(restriction.portRestricted);
        assertFalse(restriction.portClosed);
        assertEquals(ZonedDateTime.parse("2017-12-11T08:10:28.217+00:00").toEpochSecond(), restriction.issueTime.toEpochSecond());
        assertEquals(ZonedDateTime.parse("2017-12-11T09:10:28.217+00:00").toEpochSecond(), restriction.lastModified.toEpochSecond());
        assertEquals(Date.valueOf("2017-12-16"), restriction.validFrom);
        assertNull(restriction.validUntil);
        assertEquals("I,II 2000", restriction.rawText);
        assertEquals("I,II 2000", restriction.formattedText);
    }

    private WinterNavigationPortFeatureV1 getPort(final WinterNavigationPortFeatureCollectionV1 collection, final String locode) {
        return collection.getFeatures().stream().filter(f -> f.locode.equals(locode)).findFirst().get();
    }

    private Ports getResponse(final String filename) throws IOException {
        final JAXBElement<PortsResponseType> unmarshal =
            ((JAXBElement<PortsResponseType>) jaxb2Marshaller.unmarshal(new StringSource(readFile(filename))));
        return unmarshal.getValue().getPorts();
    }
}