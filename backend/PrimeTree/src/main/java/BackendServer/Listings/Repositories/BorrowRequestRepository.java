package BackendServer.Listings.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import BackendServer.Listings.Entities.BorrowRequest;

/**This interface is a JpaRepository for the Entity BorrowRequest with no extra method
 * @author Florian Kutz
 *
 */
public interface BorrowRequestRepository extends JpaRepository<BorrowRequest, Long> {

}
