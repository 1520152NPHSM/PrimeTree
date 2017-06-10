package BackendServer.ClientDatabaseAccess.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import BackendServer.ClientDatabaseAccess.Entities.Location;

/**This Interface allows database access to the entity "Location"
 * 
 * @author Florian Kutz
 * */
public interface StandOrtRepository extends JpaRepository<Location, Long> {

}
