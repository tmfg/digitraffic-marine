package fi.livi.digitraffic.meri;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "app.type=web",
})
@AutoConfigureMockMvc
public abstract class AbstractWebTestBase extends AbstractTestBase {

    private static final Logger log = LoggerFactory.getLogger(AbstractWebTestBase.class);

    @Autowired(required = false) // not for daemon tests
    protected MockMvc mockMvc;

    protected ResultActions logInfoResponse(final ResultActions result) throws UnsupportedEncodingException {
        return logResponse(result, false);
    }

    protected ResultActions logDebugResponse(final ResultActions result) throws UnsupportedEncodingException {
        return logResponse(result, true);
    }
    private ResultActions logResponse(final ResultActions result, boolean debug) throws UnsupportedEncodingException {
        final String responseStr = result.andReturn().getResponse().getContentAsString();
        if (debug) {
            log.debug("\n" + responseStr);
        } else {
            log.info("\n" + responseStr);
        }
        return result;
    }

    protected ResultActions executeGet(final String url) throws Exception {
        final MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get(url);
        get.contentType(MediaType.APPLICATION_JSON);
        return mockMvc.perform(get);
    }

    protected ResultActions expectOk(final ResultActions rs) throws Exception {
        return rs.andExpect(status().isOk());
    }

}