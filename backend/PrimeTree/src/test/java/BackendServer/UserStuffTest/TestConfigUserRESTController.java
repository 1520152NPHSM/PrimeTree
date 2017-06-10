package BackendServer.UserStuffTest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import BackendServer.User.UserRESTController;

@Configuration
public class TestConfigUserRESTController {

	@Bean
	public UserRESTController userRESTcontroller(){
		return new UserRESTController();
	}
}

