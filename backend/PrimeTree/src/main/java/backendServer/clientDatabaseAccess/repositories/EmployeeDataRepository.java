package backendServer.clientDatabaseAccess.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backendServer.clientDatabaseAccess.entities.EmployeeData;

/**This Interface allows database access to the entity "Employee"
 * 
 * @author Florian Kutz
 * */
@Repository
public interface EmployeeDataRepository extends JpaRepository<EmployeeData, Long>{
	
	EmployeeData findByLogin(String login);

}