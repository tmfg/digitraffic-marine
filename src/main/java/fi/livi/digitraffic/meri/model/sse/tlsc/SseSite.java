
package fi.livi.digitraffic.meri.model.sse.tlsc;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.util.StringUtil;

public class SseSite {

    private String siteName;
    private Integer siteNumber;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public SseSite() {
    }

    public SseSite(final String siteName, final Integer siteNumber, final Map<String, Object> additionalProperties) {
        this(siteName, siteNumber);
        this.additionalProperties = additionalProperties;
    }

    public SseSite(final String siteName, final Integer siteNumber) {
        this.siteName = siteName;
        this.siteNumber = siteNumber;
    }

    public String getSiteName() {
        return siteName;
    }
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    public Integer getSiteNumber() {
        return siteNumber;
    }
    public void setSiteNumber(Integer siteNumber) {
        this.siteNumber = siteNumber;
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
