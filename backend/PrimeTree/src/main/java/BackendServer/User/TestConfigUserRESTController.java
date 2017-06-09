package BackendServer.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TestConfigUserRESTController {

	@Bean
	public UserRESTController userRESTcontroller(){
		return new UserRESTController();
	}
}
