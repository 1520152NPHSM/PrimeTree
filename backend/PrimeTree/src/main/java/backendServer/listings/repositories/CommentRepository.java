package backendServer.listings.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backendServer.listings.entities.Comment;

/**This interface is a JpaRepository for the Entity Comment with no extra method
 * @author Florian Kutz
 *
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
