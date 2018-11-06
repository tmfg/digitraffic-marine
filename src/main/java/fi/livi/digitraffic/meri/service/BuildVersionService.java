package fi.livi.digitraffic.meri.service;

import org.springframework.stereotype.Service;

import com.jcabi.manifests.Manifests;

@Service
public class BuildVersionService {
    public String getAppVersion() {
        if (Manifests.exists("MarineApplication-Version")) {
            return Manifests.read("MarineApplication-Version");
        }
        return "DEV-BUILD";
    }

    public String getAppBuildRevision() {
        if (Manifests.exists("MarineApplication-Build")) {
            return Manifests.read("MarineApplication-Build");
        }
        return "X";
    }

    public String getAppFullVersion() {
        return getAppVersion() + "-" + getAppBuildRevision();
    }
}
