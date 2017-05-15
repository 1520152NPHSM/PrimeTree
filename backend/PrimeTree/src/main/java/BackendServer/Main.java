package BackendServer;

import org.apache.catalina.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import BackendServer.Listings.Repositories.SellItemRepository;
import BackendServer.Security.Configuration.WebSecurityConfig;

/**This Main Class launches the BackendServer and customizes HttpOnly on true*/
@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class Main implements EmbeddedServletContainerCustomizer{

    public static void main(String[] args) {
    	System.out.println("Main gestartet");
        SpringApplication.run(Main.class, args);
    }

    /**This method customizes httpOnly on true. True is also the default-value of httpOnly,
     *  but we don't know, whether the user of this Server stays with requiring true on httpOnly and if not, 
     *  we can easily change it in this code segment.*/
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		{
		    ((TomcatEmbeddedServletContainerFactory) container).addContextCustomizers(new TomcatContextCustomizer()
		    {
		        @Override
		        public void customize(Context context)
		        {
		            context.setUseHttpOnly(true);
		        }
		    });
		}
	}
}