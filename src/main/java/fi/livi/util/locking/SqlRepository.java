package fi.livi.util.locking;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * You need to add the following for this to work:
 *
 * @EntityScan(basePackages = "fi.livi.util")
 */
@NoRepositoryBean
public interface SqlRepository extends Repository<SqlRepository.DummyEntity, Long> {
    @Entity
    class DummyEntity {
        @Id
        public long id;
    }

}
