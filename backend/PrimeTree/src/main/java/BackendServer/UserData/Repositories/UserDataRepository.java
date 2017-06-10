package BackendServer.UserData.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import BackendServer.UserData.Entities.UserData;

/**This Interface allows database access to the entity "UserData"*/
public interface UserDataRepository extends JpaRepository<UserData, Long> {

}
