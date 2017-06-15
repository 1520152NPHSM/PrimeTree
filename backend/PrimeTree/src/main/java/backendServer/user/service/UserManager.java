package backendServer.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import backendServer.exceptions.FavouriteAlreadyExistsException;
import backendServer.exceptions.FavouriteNotFoundException;
import backendServer.exceptions.UserHadAlreadyTheRightAdminStatusException;
import backendServer.exceptions.UserNotFoundException;
import backendServer.user.User;

/**An implementaion of this interface gives read- and write-access to persisstent users
 * @author Florian Kutz
 *
 */
public interface UserManager extends UserDetailsService{
	
	/**This method gets the favourite list of a sinngle user
	 * @param userId the id of the user
	 * @return the favourite list of the user
	 * @throws UserNotFoundException if no user with id userId exists
	 */
	public Long[] getFavouriteList(long userId) throws UserNotFoundException;

	/**This method adds a favourite to the list of a user
	 * @param userId the id of the user
	 * @param listingId the id of the new favourite
	 * @throws UserNotFoundException if no user with the id userId exists
	 * @throws FavouriteAlreadyExistsException if a favourite with the id listingId is already in the database
	 */
	public void addFavourite(long userId, long listingId) throws UserNotFoundException, FavouriteAlreadyExistsException;
	
	/**This method removes a favourite from the list of a user
	 * @param userId the id of the user
	 * @param listingId the id of the new favourite
	 * @throws UserNotFoundException if no user with the id userId exists
	 * @throws FavouriteNotFoundException if no favourite with the id listingId exists
	 */
	public void removeFavourite(long userId, long listingId) throws UserNotFoundException, FavouriteNotFoundException;
	
	/**This method loads a user by his/her id
	 * @param userId id of the user that should be loaded
	 * @return the user-object with all datas
	 * @throws UserNotFoundException if no user with the id userId exists
	 */
	public User loadUserById(long userId) throws UserNotFoundException;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 * By overriding this method definition you can be sure that not only an object is returned that implements 
	 * UserDetails but that the returned object is a user
	 */
	public User loadUserByUsername(String username) throws UsernameNotFoundException ;

	/**This method loads all users that have both userData as well as employeeData
	 * @return an array with all users that have both userData as well as employeeData
	 */
	public User[] getAllUsers();

	/**This method sets the isAdmin-boolean of a user
	 * @param l the id of the user
	 * @param true, if the user should be an admin
	 * @throws UserNotFoundException if no user with the id userId exists
	 * @throws UserHadAlreadyTheRightAdminStatusException if the boolean was already in the same state as the parameter shouldBeActive
	 */
	public void setIsAdminOnUser(long userId, boolean shouldBeAdmin) throws UserNotFoundException, UserHadAlreadyTheRightAdminStatusException;
}
