package BackendServer.Listings.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import BackendServer.Listings.Entities.PurchaseRequest;

/**This interface is a JpaRepository for the Entity RequestListingRequest with no extra method
 * @author Florian Kutz
 *
 */
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {

}
