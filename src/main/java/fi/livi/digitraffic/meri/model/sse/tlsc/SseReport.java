
package fi.livi.digitraffic.meri.model.sse.tlsc;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import fi.livi.digitraffic.meri.util.StringUtil;

public class SseReport {

    private SseSite sseSite;
    private SseFields sseFields;
    private SseExtraFields sseExtraFields;
    private Map<String, Object> additionalProperties = new HashMap<>();

    public SseReport() {
    }

    public SseReport(final SseSite sseSite, final SseFields sseFields, final SseExtraFields sseExtraFields, final Map<String, Object> additionalProperties) {
        this(sseSite, sseFields, sseExtraFields);
        this.additionalProperties = additionalProperties;
    }

    public SseReport(SseSite sseSite, SseFields sseFields, SseExtraFields sseExtraFields) {
        this.sseSite = sseSite;
        this.sseFields = sseFields;
        this.sseExtraFields = sseExtraFields;
    }

    public SseSite getSseSite() {
        return sseSite;
    }
    public void setSseSite(SseSite sseSite) {
        this.sseSite = sseSite;
    }
    public SseFields getSseFields() {
        return sseFields;
    }

    public void setSseFields(SseFields sseFields) {
        this.sseFields = sseFields;
    }
    public SseExtraFields getSseExtraFields() {
        return sseExtraFields;
    }
    public void setSseExtraFields(SseExtraFields sseExtraFields) {
        this.sseExtraFields = sseExtraFields;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return StringUtil.toJsonString(this);
    }
}
