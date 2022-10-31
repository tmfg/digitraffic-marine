package fi.livi.digitraffic.meri.service;

import org.springframework.stereotype.Service;

import com.jcabi.manifests.Manifests;

import fi.livi.digitraffic.meri.annotation.NotTransactionalServiceMethod;

@Service
public class BuildVersionService {

    @NotTransactionalServiceMethod
    public String getAppVersion() {
        if (Manifests.exists("MarineApplication-Version")) {
            return Manifests.read("MarineApplication-Version");
        }
        return "DEV-BUILD";
    }

    @NotTransactionalServiceMethod
    public String getAppBuildRevision() {
        if (Manifests.exists("MarineApplication-Build")) {
            return Manifests.read("MarineApplication-Build");
        }
        return "X";
    }

    @NotTransactionalServiceMethod
    public String getAppFullVersion() {
        return getAppVersion() + "-" + getAppBuildRevision();
    }
}
