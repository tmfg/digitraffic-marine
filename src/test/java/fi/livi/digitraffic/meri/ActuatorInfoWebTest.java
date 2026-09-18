package fi.livi.digitraffic.meri;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

public class ActuatorInfoWebTest extends AbstractWebTestBase {

    @Test
    public void info() throws Exception {
        final ResultActions response =
            logDebugResponse(executeGet("/actuator/info"));
        
        expectOk(response)
            .andExpect(jsonPath("$['common.git'].branch").exists())
            .andExpect(jsonPath("$['common.git'].branch").value(not(startsWith("${"))))
            .andExpect(jsonPath("$['common.git'].commit.id").exists())
            .andExpect(jsonPath("$['common.git'].commit.time").exists())
            .andExpect(jsonPath("$['common.git'].buildTime").exists())
            .andExpect(jsonPath("$['common.git'].buildTime").value(not(startsWith("${"))))
            .andExpect(jsonPath("$.git.branch").exists())
            .andExpect(jsonPath("$.git.branch").value(not(startsWith("${"))))
            .andExpect(jsonPath("$.git.commit.id").exists())
            .andExpect(jsonPath("$.git.commit.time").exists())
            .andExpect(jsonPath("$.git.buildTime").exists())
            .andExpect(jsonPath("$.git.buildTime").value(not(startsWith("${"))))
            .andExpect(jsonPath("$.db.version").exists())
            .andExpect(jsonPath("$.db.success").exists())
            .andExpect(jsonPath("$.db.installedOn").exists());
    }
}
