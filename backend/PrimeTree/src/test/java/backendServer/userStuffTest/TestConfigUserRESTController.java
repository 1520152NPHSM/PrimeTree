package backendServer.userStuffTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import backendServer.listings.Constants;
import backendServer.user.UserRESTController;

@Configuration
public class TestConfigUserRESTController {

	@Bean
	public UserRESTController userRESTcontroller(){
		return new UserRESTController();
	}
	@Autowired
	Constants.LocationsLoader locationsLoader;
}

