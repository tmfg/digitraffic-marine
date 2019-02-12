package fi.livi.digitraffic.meri.dao;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.model.CodeDescriptionJson;
import fi.livi.digitraffic.meri.util.dao.SqlRepository;

@Repository
@ConditionalOnWebApplication
public interface CodeDescriptionRepository extends SqlRepository {
    @Query(value = "select code, description from code_description where domain = 'CARGO'", nativeQuery = true)
    List<CodeDescriptionJson> listAllCargoTypes();

    @Query(value = "select code, description from code_description where domain = 'VESSEL'", nativeQuery = true)
    List<CodeDescriptionJson> listAllVesselTypes();

    @Query(value = "select code, description from code_description where domain = 'AGENT'", nativeQuery = true)
    List<CodeDescriptionJson> listAllAgentTypes();
}
