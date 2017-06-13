package backendServer;

import org.apache.catalina.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import backendServer.listings.Constants;

/**This Main Class launches the BackendServer and customizes HttpOnly on true
 * 
 * @author Florian Kutz
 * */
@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class Main extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer{
	
	@Autowired
	Constants.LocationsLoader locationLoader;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Main.class);
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