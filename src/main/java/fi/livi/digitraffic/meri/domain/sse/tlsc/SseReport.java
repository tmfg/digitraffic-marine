
package fi.livi.digitraffic.meri.domain.sse.tlsc;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.util.StringUtil;

/**
 * The Items Schema
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "site",
    "sseFields",
    "extraFields",
    "additionalProperties"
})
public class SseReport {

    private Site site;
    private SseFields sseFields;
    private ExtraFields extraFields;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public SseReport() {
    }

    public SseReport(final Site site, final SseFields sseFields, final ExtraFields extraFields, final Map<String, Object> additionalProperties) {
        this(site, sseFields, extraFields);
        this.additionalProperties = additionalProperties;
    }

    public SseReport(Site site, SseFields sseFields, ExtraFields extraFields) {
        this.site = site;
        this.sseFields = sseFields;
        this.extraFields = extraFields;
    }

    public Site getSite() {
        return site;
    }
    public void setSite(Site site) {
        this.site = site;
    }
    public SseFields getSseFields() {
        return sseFields;
    }

    public void setSseFields(SseFields sseFields) {
        this.sseFields = sseFields;
    }
    public ExtraFields getExtraFields() {
        return extraFields;
    }
    public void setExtraFields(ExtraFields extraFields) {
        this.extraFields = extraFields;
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
