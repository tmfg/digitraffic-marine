
package fi.livi.digitraffic.meri.model.sse.tlsc;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import fi.livi.digitraffic.meri.model.sse.SseProperties.SiteType;
import fi.livi.digitraffic.meri.util.StringUtil;

public class SseSite {

    private String siteName;
    private Integer siteNumber;
    private Map<String, Object> additionalProperties = new HashMap<>();
    private SiteType siteType;

    public SseSite() {
    }

    public SseSite(final String siteName, final Integer siteNumber, final SiteType siteType, final Map<String, Object> additionalProperties) {
        this(siteName, siteNumber, siteType);
        this.additionalProperties = additionalProperties;
    }

    public SseSite(final String siteName, final Integer siteNumber, SiteType siteType) {
        this.siteName = siteName;
        this.siteNumber = siteNumber;
        this.siteType = siteType;
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

    public SiteType getSiteType() {
        return siteType;
    }
}
