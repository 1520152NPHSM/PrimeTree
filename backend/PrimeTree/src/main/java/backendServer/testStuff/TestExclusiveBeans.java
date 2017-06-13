package backendServer.testStuff;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import backendServer.StatisticsRESTController;
import backendServer.listings.ListingRESTController;
import backendServer.user.UserRESTController;

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
