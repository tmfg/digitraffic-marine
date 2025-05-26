package fi.livi.digitraffic.meri.dao;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.SSE_DATA;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.common.util.TimeUtil;
import fi.livi.digitraffic.meri.AbstractDaemonTestBase;
import fi.livi.digitraffic.test.util.AssertUtil;

public class UpdatedTimestampRepositoryTest extends AbstractDaemonTestBase {
    @Autowired
    private UpdatedTimestampRepository updatedTimestampRepository;

    @Test
    @Transactional
    public void testUpdate() {
        final Instant aika = Instant.now();

        updatedTimestampRepository.setUpdated(SSE_DATA, aika, "testi");

        final Instant response = updatedTimestampRepository.findLastUpdated(SSE_DATA);

        // might have different resolution
        AssertUtil.assertTimesEqual(TimeUtil.withoutMillis(aika), TimeUtil.withoutMillis(response));
    }
}
