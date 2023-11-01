package fi.livi.digitraffic.meri;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
        "app.type=daemon",
})
public abstract class AbstractDaemonTestBase extends AbstractTestBase {

}