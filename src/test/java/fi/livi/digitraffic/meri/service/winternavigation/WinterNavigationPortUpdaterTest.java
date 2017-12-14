package fi.livi.digitraffic.meri.service.winternavigation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.xml.transform.StringSource;

import fi.livi.digitraffic.meri.AbstractIntegrationTest;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationPortRepository;
import fi.livi.digitraffic.meri.domain.winternavigation.PortRestriction;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationPort;
import ibnet_baltice_ports.Ports;
import ibnet_baltice_schema.PortsResponseType;

public class WinterNavigationPortUpdaterTest extends AbstractIntegrationTest {

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

    @Before
    public void before() {
        winterNavigationPortUpdater = new WinterNavigationPortUpdater(winterNavigationClient, winterNavigationPortRepository, updatedTimestampRepository);
        winterNavigationPortRepository.deleteAll();
    }

    @Test
    @Transactional
    @Rollback
    public void updateWinterNavigationPortsSucceeds() throws IOException {

        when(winterNavigationClient.getWinterNavigationPorts()).thenReturn(getResponse("winterNavigationPortsResponse.xml"));

        winterNavigationPortUpdater.updateWinterNavigationPorts();

        List<WinterNavigationPort> ports = winterNavigationPortRepository.findAll();
        assertEquals(156, ports.size());
        final WinterNavigationPort port = ports.stream().filter(p -> p.getLocode().equals("FIPUH")).findFirst().get();
        assertEquals("FIPUH", port.getLocode());
        assertEquals("PUHOS", port.getName());
        assertEquals("FI", port.getNationality());
        assertEquals(Double.valueOf("29.9167"), port.getLongitude(), 0.00001);
        assertEquals(Double.valueOf("62.1"), port.getLatitude(), 0.01);
        assertEquals("Baltic Sea", port.getSeaArea());
        assertEquals(1, port.getPortRestrictions().size());

        final PortRestriction restriction = port.getPortRestrictions().get(0);
        assertEquals(1, restriction.getOrderNumber().intValue());
        assertFalse(restriction.getCurrent());
        assertTrue(restriction.getPortRestricted());
        assertFalse(restriction.getPortClosed());
        assertEquals(Timestamp.valueOf("2017-12-11 10:10:28.217"), restriction.getIssueTime());
        assertEquals(Timestamp.valueOf("2017-12-11 11:10:28.217"), restriction.getLastModified());
        assertEquals(Date.valueOf("2017-12-16"), restriction.getValidFrom());
        assertNull(restriction.getValidUntil());
        assertEquals("I,II 2000", restriction.getRawText());
        assertEquals("I,II 2000", restriction.getFormattedText());
    }

    private Ports getResponse(final String filename) throws IOException {
        final JAXBElement<PortsResponseType> unmarshal =
            ((JAXBElement<PortsResponseType>) jaxb2Marshaller.unmarshal(new StringSource(readFile(filename))));
        return unmarshal.getValue().getPorts();
    }
}