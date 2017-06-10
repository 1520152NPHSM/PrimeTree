package BackendServer.TestStuff;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import BackendServer.StatisticsRESTController;
import BackendServer.Listings.ListingRESTController;
import BackendServer.User.UserRESTController;

@Configuration
public class TestExclusiveBeans {
	
	@Bean
	public ListingRESTController listingRESTController(){
		return new ListingRESTController();
	}
	
	@Bean
	public UserRESTController userRESTController(){
		return new UserRESTController();
	}
	
	@Bean
	public StatisticsRESTController statisticsRESTController(){
		return new StatisticsRESTController();
	}

}
