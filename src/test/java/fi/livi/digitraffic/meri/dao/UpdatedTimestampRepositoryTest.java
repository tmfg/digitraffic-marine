package fi.livi.digitraffic.meri.dao;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.SSE_DATA;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
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

        updatedTimestampRepository.setUpdated(SSE_DATA, aika, "testi");

        final ZonedDateTime response = updatedTimestampRepository.findLastUpdated(SSE_DATA);

        // might have different resolution
        assertTimesEqual(aika.withNano(0), response.withNano(0));
    }
}
