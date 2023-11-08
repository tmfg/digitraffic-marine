package fi.livi.digitraffic.meri.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.info.GitProperties;
import org.springframework.stereotype.Component;

@Component
public class ActuatorInfoCommonGit extends ActuatorInfoGit {

    @Autowired
    public ActuatorInfoCommonGit(@Qualifier("commonGitProperties") final GitProperties properties) {
        super(properties);
    }

    /**
     * @return the detail key where git info will be located
     */
    protected String getDetailKey() {
        return "common.git";
    }
}