package fi.livi.digitraffic.meri.service;

import org.springframework.stereotype.Service;

import com.jcabi.manifests.Manifests;

@Service
public class BuildVersionService {
    public String getAppVersion() {
        if (Manifests.exists("AisApplication-Version")) {
            return Manifests.read("AisApplication-Version");
        }
        return "DEV-BUILD";
    }

    public String getAppBuildRevision() {
        if (Manifests.exists("AisApplication-Build")) {
            return Manifests.read("AisApplication-Build");
        }
        return "X";
    }

    public String getAppFullVersion() {
        return getAppVersion() + "-" + getAppBuildRevision();
    }
}
