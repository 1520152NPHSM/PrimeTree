package BackendServer.UserData.Configuration;
import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import BackendServer.User.Service.UserManager;
import BackendServer.User.Service.UserManagerImpl;

/**This class defines all beans required for the entity "user"
 * It is necessary because the entity "user lies in a different database than listing and user and needs to
 * get its properties defined like this. This class is inspired by this tutorial for accessing multiple databases 
 * in one Spring-Application using JPA: http://www.baeldung.com/spring-data-jpa-multiple-databases*/
@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(
		basePackages = "BackendServer.UserData.Repositories",
		entityManagerFactoryRef = "userEntityManager", 
	    transactionManagerRef = "userTransactionManager"
		)
public class UserBeanCollection {
	
	@Autowired
    private Environment env;
    
	/**This method is inspired by the tutorial described in the javadoc of this class*/
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean userEntityManager() {
    	
    	System.out.println("userEntityManager() aufgerufen");
    	
        LocalContainerEntityManagerFactoryBean em
          = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(userDataSource());
        em.setPackagesToScan(
          new String[] { "BackendServer.UserData.Entities" });
        em.setPersistenceUnitName("user");
 
        HibernateJpaVendorAdapter vendorAdapter
          = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto",
          env.getProperty("user.hbm2ddl.auto"));
        properties.put("hibernate.dialect",
          env.getProperty("hibernate.dialect"));
        /*The following property is there to solve an error caused by a thrown LazyInitializationException*/ 
        properties.put("hibernate.enable_lazy_load_no_trans",true);
        em.setJpaPropertyMap(properties);
 
        return em;
    }
 
    /**This method is inspired by the tutorial described in the javadoc of this class*/
    @Primary
    @Bean
    public DataSource userDataSource() {
    	
    	System.out.println("userDataSource() aufgerufen");
  
        DriverManagerDataSource dataSource
          = new DriverManagerDataSource();
        dataSource.setDriverClassName(
          env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("user.jdbc.url"));
        dataSource.setUsername(env.getProperty("user.jdbc.user"));
        dataSource.setPassword(env.getProperty("user.jdbc.pass"));
 
        return dataSource;
    }
 
    /**This method is inspired by the tutorial described in the javadoc of this class*/
    @Primary
    @Bean
    public PlatformTransactionManager userTransactionManager() {
    	
    	System.out.println("userTransactionManager() aufgerufen");
  
        JpaTransactionManager transactionManager
          = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
        	userEntityManager().getObject());
        return transactionManager;
    }
	
	@Bean
	public UserManager userManager(){
		return new UserManagerImpl();
	}
    
}
    