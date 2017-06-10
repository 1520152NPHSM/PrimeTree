package BackendServer.Listings.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import BackendServer.Listings.Entities.SingleRecreationRequest;

/**This interface is a JpaRepository for the Entity SingleRecreationRequest with no extra method
 * @author Florian Kutz
 *
 */
public interface SingleRecreationRequestRepository extends JpaRepository<SingleRecreationRequest, Long> {

}
