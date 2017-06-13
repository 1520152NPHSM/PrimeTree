package backendServer.user.service;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import backendServer.exceptions.ListingNotFoundException;
import backendServer.listings.PersistenceAdapter;
import backendServer.listings.repositories.CommentRepository;

/**This class checks whether a user has a specific access to a specific resource
 * @author Florian Kutz
 *
 */
public class MyPermissionEvaluator implements PermissionEvaluator {
	
	@Autowired
	PersistenceAdapter persistenceAdapter;

	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	UserManager userManager;
	
	@Override
	public boolean hasPermission(Authentication arg0, Object arg1, Object arg2) {
		return false;
	}

	@Override
	public boolean hasPermission(Authentication arg0, Serializable arg1, String arg2, Object arg3) {
		if(arg2.equals("listing")&&arg3.equals("owner")){
			try {
				return persistenceAdapter.isOwnerOfListing((Integer) arg1, getUserIdFromAuthenticationObject(arg0));
			} catch (ListingNotFoundException e) {
				return true;
			}
		}else if(arg2.equals("listing")&&arg3.equals("reader")){
			try {
				return persistenceAdapter.getListingById((Long) arg1).isActive() ||
						persistenceAdapter.isOwnerOfListing((Integer) arg1, getUserIdFromAuthenticationObject(arg0));
			} catch (ListingNotFoundException e) {
				return true;
			}
		}else if(arg2.equals("comment")&&arg3.equals("author")){
			return commentRepository.findOne((Long) arg1).getAuthorId()==getUserIdFromAuthenticationObject(arg0);
		}else{
			return false;
		}
	}
	
	/**This method gets the uderId
	 * @param auth the authentication-Object from the user
	 * @return the user's id
	 */
	private long getUserIdFromAuthenticationObject(Authentication auth){
		return userManager.loadUserByUsername(auth.getName()).getId();
	}

}
