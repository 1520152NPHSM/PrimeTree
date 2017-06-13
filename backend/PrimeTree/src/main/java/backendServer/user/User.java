package backendServer.user;

import java.util.Collection;
import java.util.LinkedList;

import org.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import backendServer.clientDatabaseAccess.entities.EmployeeData;
import backendServer.clientDatabaseAccess.entities.Location;
import backendServer.listings.Constants;
import backendServer.userData.entities.UserData;

/**This class representing a User and implementing UserDetails stores its Data in two persistent Objects in 
 * two different databases: employeeData in employeedb and userData in userdb
 * @author Florian Kutz
 *
 */
public class User implements UserDetails {
	
	private EmployeeData employeeData;
	private UserData userData;
	
	/** The data have to be created first and given to this constructor to create a User
	 * @param employeeData the data in employeedb which are brought by the client bridging IT
	 * @param userData the userData in userdb which are exclusvely relevant for this application
	 */
	public User(EmployeeData employeeData,UserData userData){
		this.employeeData=employeeData;
		this.userData=userData;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new LinkedList<GrantedAuthority>();
		collection.add(new SimpleGrantedAuthority("USER"));
		if(isAdmin()){
			collection.add(new SimpleGrantedAuthority("ADMIN"));
		}
		return collection;
	}

	@Override
	public String getPassword() {
		return "123";
	}

	@Override
	public String getUsername() {
		return employeeData.getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public boolean isAdmin(){
		return userData.isInAdminRole();
	}

	public long getId() {
		return employeeData.getId();
	}

	public String getPicture() {
		return employeeData.getFoto();
	}

	/**This method creates a JSONObject with all data both in employeeData and userData without any hint about 
	 * which data are in which entities
	 * @return a JSONObject with those fields:
	 * id: id of both userData and employeeData
	 * picture: the path to the userPicture
	 * firstName: The first name of the user
	 * lastName: the last name of the user
	 * isAdmin: true, if the user is an dadmin of bIT Kleinanzeigen
	 * eMail: the mail-address of the user
	 * phone: the phone number of the user
	 * location: the location where the employee works
	 * position: the position in the company of the employee
	 */
	public JSONObject toJSON() {
		JSONObject userAsJSON=new JSONObject();
		userAsJSON.put(Constants.userFieldId, this.getId());
		userAsJSON.put(Constants.userFieldPicture, this.getPicture());
		userAsJSON.put(Constants.userFieldFirstName, this.getFirstName());
		userAsJSON.put(Constants.userFieldLastName, this.getLastName());
		userAsJSON.put(Constants.userFieldIsAdmin, this.isAdmin());
		userAsJSON.put(Constants.userFieldEMail, this.getEMail());
		userAsJSON.put(Constants.userFieldPhone, this.getPhoneNumber());
		if(this.getLocation()!=null){
			userAsJSON.put(Constants.userFieldLocation, this.getLocation());
		}else{
			userAsJSON.put(Constants.userFieldLocation, "NotKnown");
		}
		userAsJSON.put(Constants.userFieldPosition, this.getPosition());
		
		return userAsJSON;
	}

	private String getPosition() {
		return employeeData.getSkillLevel();
	}

	private Location getLocation() {
		return employeeData.getStandort();
	}

	private String getPhoneNumber() {
		return employeeData.getTelefonNummer();
	}

	private String getEMail() {
		return employeeData.geteMail();
	}

	public String getLastName() {
		return employeeData.getNachname();
	}

	public String getFirstName() {
		return employeeData.getVorname();
	}

}
