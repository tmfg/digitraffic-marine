package fi.livi.digitraffic.meri.dao.v2;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.model.v2.V2CodeDescription;
import fi.livi.digitraffic.meri.util.dao.SqlRepository;

@Repository
@ConditionalOnWebApplication
public interface V2CodeDescriptionRepository extends SqlRepository {
    @Query(value = "select code, description_fi descriptionFi, description_en descriptionEn from code_description where domain = 'CARGO'",
        nativeQuery = true)
    List<V2CodeDescription> listAllCargoTypes();

    @Query(value = "select code, description_fi descriptionFi, description_en descriptionEn from code_description where domain = 'VESSEL'"
        , nativeQuery = true)
    List<V2CodeDescription> listAllVesselTypes();

    @Query(value = "select code, description_fi descriptionFi, description_en descriptionEn from code_description where domain = 'AGENT'",
        nativeQuery = true)
    List<V2CodeDescription> listAllAgentTypes();
}
