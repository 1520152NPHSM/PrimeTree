package BackendServer.Listings.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import BackendServer.Listings.Entities.ServiceOffering;

/**This interface is a JpaRepository for the Entity ServiceOffering with no extra method
 * @author Florian Kutz
 *
 */
public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {

}
