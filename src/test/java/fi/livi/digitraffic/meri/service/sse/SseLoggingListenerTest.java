package fi.livi.digitraffic.meri.service.sse;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.controller.CachedLocker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SseLoggingListenerTest extends AbstractTestBase {
    @Mock
    private CachedLocker cachedLocker;

    @Mock
    private SseMqttSender sseMqttSender;

    @Captor
    private ArgumentCaptor<SseLoggingListener.StatusMessage> statusCaptor;

    @Test
    public void test() {
        final SseLoggingListener sseLoggingListener = new SseLoggingListener(cachedLocker, sseMqttSender);
        // log two messages, one successful and one failed
        sseLoggingListener.addSendSseMessagesStatistics(true);
        sseLoggingListener.addSendStatusMessagesStatistics(false);

        when(cachedLocker.hasLock()).thenReturn(true);
        ReflectionTestUtils.invokeMethod(sseLoggingListener,  "sendStatusAndLogSentStatistics");
        sseLoggingListener.destroy();

        verify(sseMqttSender).sendStatusMessage(statusCaptor.capture());
        // assert two messages, one failed
        assertEquals(statusCaptor.getValue().messages, 2);
        assertEquals(statusCaptor.getValue().failures, 1);
        assertNotNull(statusCaptor.getValue().getTimeStamp());
    }
}
