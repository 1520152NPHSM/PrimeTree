package BackendServer.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import BackendServer.Exceptions.FavouriteAlreadyExistsException;
import BackendServer.Exceptions.FavouriteNotFoundException;
import BackendServer.Exceptions.UserHadAlreadyTheRightAdminStatusException;
import BackendServer.Exceptions.UserNotFoundException;
import BackendServer.Listings.Constants;
import BackendServer.User.Service.UserManager;

/**This class implements all methods beginning with /user or /users except /user/login and /user/logout
 * @author Florian Kutz
 *
 */
@Controller
@RequestMapping(value = "")
public class UserRESTController {
	
	@Autowired
	UserManager userManager;
	
	/**This method gives accesss to all public data of a single user
	 * @param userId the id of the user whose data should be returned
	 * @param request
	 * @param response The status is 200 if everything went well, 404 
	 * if o user with the id userId exists and 403 if the user is not logged in.
	 * @return a stringified JSONObject containing all datas of the requested user
	 */
	@CrossOrigin
	@RequestMapping(value = "/user/{id}", method=RequestMethod.GET)
    public @ResponseBody String getUser(@PathVariable(value="id") final int userId, HttpServletRequest request, HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			User foundUser=userManager.loadUserById(userId);
			result=foundUser.toJSON();
		} catch (UserNotFoundException thrownException) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return result.toString();
	}
	
	/**This method gives an admin an overview over all users
	 * @param request
	 * @param response The status is 200 if everything went well, 404 
	 * if o user with the id userId exists and 403 if the user is not logged in or not an admin.
	 * @return a stringified JSONObject hving a JSONArray with all user-Objects as JSONObjects
	 */
	@CrossOrigin
	@RequestMapping(value = "/users", method=RequestMethod.GET)
    public @ResponseBody String getUsers( HttpServletRequest request, HttpServletResponse response){
		JSONObject result=new JSONObject();
		User[] allUsers=userManager.getAllUsers();
		JSONArray allUsersAsJSON=new JSONArray(allUsers.length);
		for(int index=0;index<allUsers.length;index++){
			allUsersAsJSON.put(index, allUsers[index].toJSON());
		}
		result.put(Constants.responseFieldUsers, allUsersAsJSON);
		return result.toString();
	}
	
	/**This method adds a favourite to the favourite-list of the user. It does not check, whether a listing with that id exists.
	 * @param listingId the id of the listing which should be added to the favourite list
	 * @param request
	 * @param response The status is 200 if everything went well, 401 
	 * if the listing already exists in the favourite list and 403 if the user is not logged in.
	 * @throws UsernameNotFoundException if something is badly implemented, shouldn't happen
	 * @throws UserNotFoundException if something is badly implemented, shouldn't happen
	 */
	@CrossOrigin
	@RequestMapping(value = "/user/favourites", method=RequestMethod.POST)
    public @ResponseBody void postFavourite(@RequestParam final int listingId, HttpServletRequest request, HttpServletResponse response) throws UsernameNotFoundException, UserNotFoundException{
		try {
			userManager.addFavourite(getRequestersUserId(), listingId);
		} catch (FavouriteAlreadyExistsException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} 
	}
	
	/**This method deletes one favourite from the favourite-list of the user who sent the request
	 * @param listingId id of the listing which should be removed from the favourite list
	 * @param request
	 * @param response The status is 200 if everything went well, 404 
	 * if the listing doesn't exist in the favourite list and 403 if the user is not logged in.
	 * @throws UsernameNotFoundException if something is badly implemented, shouldn't happen
	 * @throws UserNotFoundException if something is badly implemented, shouldn't happen
	 */
	@CrossOrigin
	@RequestMapping(value = "/user/favourites", method=RequestMethod.DELETE)
    public @ResponseBody void deleteFavourite(@RequestParam final int listingId, HttpServletRequest request, HttpServletResponse response) throws UsernameNotFoundException, UserNotFoundException{
		try {
			userManager.removeFavourite(getRequestersUserId(), listingId); 
			} catch (FavouriteNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	/**This method gives an user his/her favourite-list
	 * @param request
	 * @param response The status is 200 if everything went well and 403 if the user is not logged in.
	 * @return a stringified JSONObject with a JSONArray with all favourite ids
	 * @throws UsernameNotFoundException if something is badly implemented, shouldn't happen
	 * @throws UserNotFoundException if something is badly implemented, shouldn't happen
	 */
	@CrossOrigin
	@RequestMapping(value = "/user/favourites", method=RequestMethod.GET)
    public @ResponseBody String getFavourites(HttpServletRequest request, HttpServletResponse response) throws UsernameNotFoundException, UserNotFoundException{
		JSONObject result=new JSONObject();
		result.put(Constants.responseFieldFavouriteList, userManager.getFavouriteList(getRequestersUserId()));
		return result.toString();		
	}
	
	/**This method allows admins to make someone else an admin
	 * @param userId id of the user that should become an admin
	 * @param request
	 * @param response The status is 200 if everything went well, 404  if no user with the id userId exists, 
	 * 401 if the declared user was already an admin and 403 if the user is not logged in or not an admin.
	 */
	@CrossOrigin
	@RequestMapping(value = "/user/{id}/admin", method=RequestMethod.POST)
	@PreAuthorize("hasAuthority('ADMIN')")
    public @ResponseBody void declareAdmin(@PathVariable(value="id") final int userId, HttpServletRequest request, HttpServletResponse response){
		try {
			userManager.setIsAdminOnUser(userId, true);
		} catch (UserNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (UserHadAlreadyTheRightAdminStatusException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	/**This method allows admins to take the admin state away from an admin
	 * @param userId id of the user that should lose the admin status
	 * @param request
	 * @param response The status is 200 if everything went well, 404  if no user with the id userId exists, 
	 * 401 if the declared user was already no admin and 403 if the user is not logged in or not an admin.
	 */
	@CrossOrigin
	@RequestMapping(value = "/user/{id}/admin", method=RequestMethod.DELETE)
	@PreAuthorize("hasAuthority('ADMIN')")
    public @ResponseBody void deleteAdmin(@PathVariable(value="id") final int userId, HttpServletRequest request, HttpServletResponse response){
		try {
			userManager.setIsAdminOnUser(userId, false);
		} catch (UserNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (UserHadAlreadyTheRightAdminStatusException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	/**This method gets the id of the user who sent the request
	 * @return the id of the user who sent the request
	 */
	private long getRequestersUserId(){
		return userManager.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
	}

}
