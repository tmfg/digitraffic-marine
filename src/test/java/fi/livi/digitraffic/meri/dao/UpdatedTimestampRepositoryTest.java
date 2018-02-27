package fi.livi.digitraffic.meri.dao;

import java.time.ZonedDateTime;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.AbstractTestBase;

public class UpdatedTimestampRepositoryTest extends AbstractTestBase {
    @Autowired
    private UpdatedTimestampRepository updatedTimestampRepository;

    @Test
    @Transactional
    public void testUpdate() {
        final ZonedDateTime aika = ZonedDateTime.now();

        updatedTimestampRepository.setUpdated("test", aika, "testi");

        final ZonedDateTime response = updatedTimestampRepository.findLastUpdated("test");

        assertTimesEqual(aika, response);
    }
}
