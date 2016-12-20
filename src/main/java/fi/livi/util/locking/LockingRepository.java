package fi.livi.util.locking;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LockingRepository extends SqlRepository {
    @Query(nativeQuery = true, value = "select lock_name from locking_table where lock_name = ?1 for update")
    String acquireLock(final String lockName);
}
