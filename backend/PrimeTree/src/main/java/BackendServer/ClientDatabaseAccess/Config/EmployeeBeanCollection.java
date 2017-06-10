package BackendServer.ClientDatabaseAccess.Config;
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

/**This class defines all beans required for the entity "Employee"
 * It is necessary because the entity "employee lies in a different database than listing and user and needs to
 * get its properties defined like this. This class is inspired by this tutorial for accessing multiple databases 
 * in one Spring-Application using JPA: http://www.baeldung.com/spring-data-jpa-multiple-databases
 * 
 * @author Florian Kutz
 * */
@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(
		basePackages = "BackendServer.ClientDatabaseAccess.Repositories",
		entityManagerFactoryRef = "employeeEntityManager", 
	    transactionManagerRef = "employeeTransactionManager"
		)
public class EmployeeBeanCollection {
	
	@Autowired
    private Environment env;
    
	/**This method is inspired by the tutorial described in the javadoc of this class*/
    @Bean
    public LocalContainerEntityManagerFactoryBean employeeEntityManager() {
    	
    	System.out.println("employeeEntityManager() aufgerufen");
    	
        LocalContainerEntityManagerFactoryBean em
          = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(employeeDataSource());
        em.setPackagesToScan(
          new String[] { "BackendServer.ClientDatabaseAccess.Entities" });
        em.setPersistenceUnitName("Employee");
 
        HibernateJpaVendorAdapter vendorAdapter
          = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto",
          env.getProperty("employee.hbm2ddl.auto"));
        properties.put("hibernate.dialect",
          env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);
        /*The following property is there to solve an error caused by a thrown LazyInitializationException*/ 
        properties.put("hibernate.enable_lazy_load_no_trans",true);
        em.setJpaPropertyMap(properties);
 
        return em;
    }
 
    /**This method is inspired by the tutorial described in the javadoc of this class*/
    @Bean
    public DataSource employeeDataSource() {
    	
    	System.out.println("employeeDataSource() aufgerufen");
  
        DriverManagerDataSource dataSource
          = new DriverManagerDataSource();
        dataSource.setDriverClassName(
          env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("employee.jdbc.url"));
        dataSource.setUsername(env.getProperty("employee.jdbc.user"));
        dataSource.setPassword(env.getProperty("employee.jdbc.pass"));
 
        return dataSource;
    }
 
    /**This method is inspired by the tutorial described in the javadoc of this class*/
    @Bean
    public PlatformTransactionManager employeeTransactionManager() {
    	
    	System.out.println("employeeTransactionManager() aufgerufen");
  
        JpaTransactionManager transactionManager
          = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
        	employeeEntityManager().getObject());
        return transactionManager;
    }
    
    
    
}
    