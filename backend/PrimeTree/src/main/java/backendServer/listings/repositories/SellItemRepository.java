package backendServer.listings.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backendServer.listings.entities.SellItem;

/**This interface is a JpaRepository for the Entity SellItem with no extra method
 * @author Florian Kutz
 *
 */
public interface SellItemRepository extends JpaRepository<SellItem, Long> {

}