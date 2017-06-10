package BackendServer.Listings.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import BackendServer.Listings.Entities.ReturningRecreationRequest;

/**This interface is a JpaRepository for the Entity ReturningRecreationRequest with no extra method
 * @author Florian Kutz
 *
 */
public interface ReturningRecreationRequestRepository extends JpaRepository<ReturningRecreationRequest, Long> {

}
