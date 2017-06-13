package backendServer.listings.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backendServer.listings.entities.RideSharing;

/**This interface is a JpaRepository for the Entity RideSharing with no extra method
 * @author Florian Kutz
 *
 */
public interface RideSharingRepository extends JpaRepository<RideSharing, Long> {

}
