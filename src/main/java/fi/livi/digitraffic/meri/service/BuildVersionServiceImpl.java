package fi.livi.digitraffic.meri.service;

import com.jcabi.manifests.Manifests;
import org.springframework.stereotype.Service;

@Service
public class BuildVersionServiceImpl implements BuildVersionService{

    @Override
    public String getAppVersion() {
        if (Manifests.exists("AisApplication-Version")) {
            return Manifests.read("AisApplication-Version");
        }
        return "DEV-BUILD";
    }

    @Override
    public String getAppBuildRevision() {
        if (Manifests.exists("AisApplication-Build")) {
            return Manifests.read("AisApplication-Build");
        }
        return "X";
    }

    @Override
    public String getAppFullVersion() {
        return getAppVersion() + "-" + getAppBuildRevision();
    }
}
