package fi.livi.digitraffic.meri.controller.info;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import fi.livi.digitraffic.meri.AbstractWebTestBase;
import fi.livi.digitraffic.meri.model.DataSource;
import fi.livi.digitraffic.meri.service.DataStatusService;

public class InfoControllerV1Test extends AbstractWebTestBase {

    @Autowired
    private DataStatusService dataStatusService;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    // Parameterless GET apis that are intentionally not in update-times
    private static final Set<String> NOT_IN_UPDATE_TIMES = Set.of(
        "/api/info/v1/update-times"
    );

    @Test
    public void updateTimes() throws Exception {

        final String portCallInterval = dataStatusService.getDataSourceUpdateInterval(DataSource.PORT_CALL);

        final ResultActions response =
            logDebugResponse(
                executeGet(InfoControllerV1.API_INFO_V1 + InfoControllerV1.UPDATE_TIMES));
        expectOk(response)
            .andExpect(jsonPath("updateTimes[*].api").exists())
            .andExpect(jsonPath("updateTimes[*].subtype").exists())
            .andExpect(jsonPath("updateTimes[*].dataUpdatedTime").exists())
            .andExpect(jsonPath("updateTimes[*].dataCheckedTime").exists())
            .andExpect(jsonPath("updateTimes[*].dataUpdateInterval").exists())
            .andExpect(jsonPath("$.updateTimes[*].api", Matchers.not(Matchers.hasItem("/api/sse/v1/measurements"))))
            .andExpect(jsonPath("$.updateTimes[?(@.api=='/api/port-call/v1/port-calls')].dataUpdateInterval", Matchers.hasItem(portCallInterval)))
            ;
    }

    /**
     * All GET apis without path variables and required request parameters must be in update-times, so that clients
     * don't need to fetch the data to find out if it is up to date. Deprecated apis may be left out.
     */
    @Test
    public void updateTimesContainsAllApisWithoutRequiredParameters() throws Exception {
        final String json = executeGet(InfoControllerV1.API_INFO_V1 + InfoControllerV1.UPDATE_TIMES)
            .andReturn().getResponse().getContentAsString();
        final Map<String, String> skipped = new TreeMap<>();
        final Set<String> checked = new TreeSet<>();
        final Set<String> missing = new TreeSet<>();
        findGetApis().forEach((api, info) -> {
            final boolean inUpdateTimes = json.contains("\"" + api + "\"");
            if (info.skipReason() != null) {
                skipped.put(api, info.skipReason());
            } else if (NOT_IN_UPDATE_TIMES.contains(api)) {
                skipped.put(api, "excluded in test");
            } else if (!inUpdateTimes && info.deprecated()) {
                skipped.put(api, "deprecated and not in update-times");
            } else {
                checked.add(api);
                if (!inUpdateTimes) {
                    missing.add(api);
                }
            }
        });

        final StringBuilder summary = new StringBuilder("update-times coverage: checked ").append(checked.size())
            .append(" apis, missing ").append(missing.size()).append(", skipped ").append(skipped.size());
        summary.append("\nChecked:");
        checked.forEach(api -> summary.append("\n  ").append(missing.contains(api) ? "MISSING " : "ok      ").append(api));
        summary.append("\nSkipped:");
        skipped.forEach((api, reason) -> summary.append("\n  ").append(api).append("  (").append(reason).append(")"));
        System.out.println(summary);

        Assertions.assertTrue(missing.isEmpty(), "Missing from update-times (add them or exclude in test): " + missing);
    }

    private record ApiInfo(String skipReason, boolean deprecated) {}

    /** GET apis under /api. skipReason tells why the api is not expected in update-times, null if it is expected. */
    private Map<String, ApiInfo> findGetApis() {
        final Map<String, ApiInfo> apis = new TreeMap<>();
        requestMappingHandlerMapping.getHandlerMethods().forEach((final RequestMappingInfo info, final HandlerMethod method) -> {
            if (!info.getMethodsCondition().getMethods().contains(RequestMethod.GET)) {
                return;
            }
            final boolean requiredParams = Arrays.stream(method.getMethodParameters())
                .map(p -> p.getParameterAnnotation(RequestParam.class))
                .anyMatch(a -> a != null && a.required() && ValueConstants.DEFAULT_NONE.equals(a.defaultValue()));
            final boolean deprecated = method.hasMethodAnnotation(Deprecated.class)
                || method.getBeanType().isAnnotationPresent(Deprecated.class);
            info.getPatternValues().stream()
                .filter(path -> path.startsWith("/api/"))
                .forEach(path -> apis.put(path,
                    new ApiInfo(path.contains("{") ? "path variable" : requiredParams ? "required request parameter" : null, deprecated)));
        });
        return apis;
    }
}
