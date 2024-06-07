package fi.livi.digitraffic.meri;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
@SpringBootTest(classes = MarineApplication.class,
                webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
        "app.type=daemon",
})
public abstract class AbstractDaemonTestBase extends AbstractTestBase {

}