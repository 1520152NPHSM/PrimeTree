package backendServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import backendServer.listings.GlobalStatistics;
import backendServer.listings.PersistenceAdapter;
import backendServer.user.User;
import backendServer.user.service.UserManager;

/**This class has one method allowing everyone to get the global statistics
 * @author Florian Kutz
 *
 */
@Controller
@RequestMapping(value = "")
public class StatisticsRESTController {
	
	@Autowired
	PersistenceAdapter persistenceAdapter;
	
	@Autowired
	UserManager userManager;
	
	/**This method gets the global statistics over the server and its databases.
	 * This means it gives information about the number of all listings with common
	 * properties, the number of users and admins
	 * @param request
	 * @param response always 200 except the user is not logged in, then it's 401, 
	 * but setting this status is no buissness of this method.
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/statistics", method=RequestMethod.GET)
	public @ResponseBody String getGlobalStatistics(HttpServletRequest request, HttpServletResponse response){
		GlobalStatistics globalStatistics=new GlobalStatistics();
		persistenceAdapter.registerAllListingsInGlobalStatistics(globalStatistics);
		for(User registeredUser : userManager.getAllUsers()){
			globalStatistics.registerUser(registeredUser);
		}
		response.setStatus(HttpServletResponse.SC_OK);
		return globalStatistics.toJSON().toString();
	}

}
