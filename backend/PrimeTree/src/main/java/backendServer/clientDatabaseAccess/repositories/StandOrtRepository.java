package backendServer.clientDatabaseAccess.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import backendServer.clientDatabaseAccess.entities.Location;

/**This Interface allows database access to the entity "Location"
 * 
 * @author Florian Kutz
 * */
public interface StandOrtRepository extends JpaRepository<Location, Long> {

}
