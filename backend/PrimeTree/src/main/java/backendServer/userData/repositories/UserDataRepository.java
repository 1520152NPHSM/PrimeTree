package backendServer.userData.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import backendServer.userData.entities.UserData;

/**This Interface allows database access to the entity "UserData"*/
public interface UserDataRepository extends JpaRepository<UserData, Long> {

}
