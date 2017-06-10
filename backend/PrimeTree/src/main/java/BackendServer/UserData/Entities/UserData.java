package BackendServer.UserData.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import BackendServer.Exceptions.FavouriteNotFoundException;

/**This class represents the UserData-Entity in the userdb*/
@Entity
@Table(name = "UserData")
public class UserData {
	
	@Id
	private Long id;
	
	private boolean inAdminRole;
	
	 @ElementCollection
	 private List<Long> favouriteList;
	
	public UserData(){}
	public UserData(Long id, boolean admin) {
		this.setId(id);
		this.setInAdminRole(admin);
		this.favouriteList = new ArrayList<Long>();
	}

	public boolean isInAdminRole() {
		return inAdminRole;
	}

	public void setInAdminRole(boolean inAdminRole) {
		this.inAdminRole = inAdminRole;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public List<Long> getFavouriteList() {
		return favouriteList;
	}

	public void setFavouriteList(List<Long> favouriteList) {
		this.favouriteList = favouriteList;
	}

	public void addFavourite(Long newFavourite){
		this.favouriteList.add(newFavourite);
	}
	
	public List<Long> getFavourites(){
		return favouriteList;
	}

	public void removeFavourite(long listingId) throws FavouriteNotFoundException {
		if(!this.favouriteList.remove(listingId)){
			throw new FavouriteNotFoundException();
		}
	}
}