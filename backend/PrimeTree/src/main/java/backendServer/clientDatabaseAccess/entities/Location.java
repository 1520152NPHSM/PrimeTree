package backendServer.clientDatabaseAccess.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**This class represents the Standort-Entity in the employeedb
 * 
 * @author Florian Kutz
 * */
@Entity
@Table(name = "standort")
public class Location {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String ortName;

	public String getOrtName() {
		return ortName;
	}

	public void setOrtName(String ortName) {
		this.ortName = ortName;
	}

}
