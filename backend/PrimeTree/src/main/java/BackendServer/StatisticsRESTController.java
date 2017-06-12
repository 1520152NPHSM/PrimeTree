package BackendServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BackendServer.Listings.GlobalStatistics;
import BackendServer.Listings.PersistenceAdapter;
import BackendServer.User.User;
import BackendServer.User.Service.UserManager;

@Controller
@RequestMapping(value = "")
public class StatisticsRESTController {
	
	@Autowired
	PersistenceAdapter persistenceAdapter;
	
	@Autowired
	UserManager userManager;
	
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
