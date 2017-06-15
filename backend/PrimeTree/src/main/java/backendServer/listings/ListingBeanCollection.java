package backendServer.listings;

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
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.transaction.PlatformTransactionManager;

import backendServer.clientDatabaseAccess.repositories.StandOrtRepository;
import backendServer.listings.Constants.LocationsLoader;
import backendServer.listings.objectControllers.BorrowRequestObjectController;
import backendServer.listings.objectControllers.ListingObjectController;
import backendServer.listings.objectControllers.PurchaseRequestObjectController;
import backendServer.listings.objectControllers.ReturningRecreationRequestObjectController;
import backendServer.listings.objectControllers.RideShareRequestObjectController;
import backendServer.listings.objectControllers.RideSharingObjectController;
import backendServer.listings.objectControllers.SellItemObjectController;
import backendServer.listings.objectControllers.ServiceOfferingObjectController;
import backendServer.listings.objectControllers.SingleRecreationRequestObjectController;
import backendServer.user.service.MyPermissionEvaluator;
import backendServer.user.service.UserManager;
import backendServer.user.service.UserManagerImpl;

/**This class defines all beans relevant for the implementation of the Listing-REST-methods 
 * and enables the JpaRepositories of listingdb
 * The first part of this class is inspired by this tutorial for accessing multiple databases 
 * in one Spring-Application using JPA: http://www.baeldung.com/spring-data-jpa-multiple-databases
 * 
 * @author Florian Kutz
 * */
@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(
		basePackages = "backendServer.listings.repositories",
		entityManagerFactoryRef = "listingEntityManager", 
	    transactionManagerRef = "listingTransactionManager"
		)
public class ListingBeanCollection {
	
	@Autowired
    private Environment env;
    
	/**This method is inspired by the tutorial described in the javadoc of this class*/
    @Bean
    public LocalContainerEntityManagerFactoryBean listingEntityManager() {
    	
    	System.out.println("listingEntityManager() aufgerufen");
    	
        LocalContainerEntityManagerFactoryBean em
          = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(listingDataSource());
        em.setPackagesToScan(
          new String[] { "backendServer.listings.entities" });
        em.setPersistenceUnitName("Listing");
 
        HibernateJpaVendorAdapter vendorAdapter
          = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto",
          env.getProperty("listings.hbm2ddl.auto"));
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
    public DataSource listingDataSource() {
    	
    	System.out.println("listingDataSource() aufgerufen");
  
        DriverManagerDataSource dataSource
          = new DriverManagerDataSource();
        dataSource.setDriverClassName(
          env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("listings.jdbc.url"));
        dataSource.setUsername(env.getProperty("listings.jdbc.user"));
        dataSource.setPassword(env.getProperty("listings.jdbc.pass"));
 
        return dataSource;
    }
 
    /**This method is inspired by the tutorial described in the javadoc of this class*/
    @Bean
    public PlatformTransactionManager listingTransactionManager() {
    	
    	System.out.println("listingTransactionManager() aufgerufen");
  
        JpaTransactionManager transactionManager
          = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
          listingEntityManager().getObject());
        return transactionManager;
    }
	
    /**This bean gives us an instance of the interface PersistenceAdapter*/
	@Bean 
	public PersistenceAdapter persistenceAdapter(){
	   return new PersistenceAdapterImpl();
	}
	
	/**This bean gives us an array of non-abstract instances of ListingObjectController responsible for 
	 * all required listingTypes*/
	@Bean
	public ListingObjectController[] getAnArrayOfAllTypesOfListingObjectController(){
		ListingObjectController[] returnValue={
				sellItemObjectController(),
				serviceOfferingObjectController(),
				rideSharingObjectController(),
				rideShareRequestObjectController(),
				purchaseRequestObjectController(),
				borrowRequestObjectController(),
				singleRecreationRequestObjectController(),
				returningRecreationRequestObjectController()
		};
		return returnValue;
	}
	
	@Bean
	public SellItemObjectController sellItemObjectController() {
		return new SellItemObjectController();
	}
	
	@Bean
	public ServiceOfferingObjectController serviceOfferingObjectController(){
		return new ServiceOfferingObjectController();
	}
	
	@Bean
	public RideSharingObjectController rideSharingObjectController(){
		return new RideSharingObjectController();
	}
	
	@Bean
	public RideShareRequestObjectController rideShareRequestObjectController(){
		return new RideShareRequestObjectController();
	}
	
	@Bean 
	public PurchaseRequestObjectController purchaseRequestObjectController(){
		return new PurchaseRequestObjectController();
	}
	
	@Bean
	public BorrowRequestObjectController borrowRequestObjectController(){
		return new BorrowRequestObjectController();
	}
	
	@Bean
	public SingleRecreationRequestObjectController singleRecreationRequestObjectController(){
		return new SingleRecreationRequestObjectController();
	}
	
	@Bean
	public ReturningRecreationRequestObjectController returningRecreationRequestObjectController(){
		return new ReturningRecreationRequestObjectController();
	}

	@Bean
	public PermissionEvaluator permissionEvaluator(){
		return new MyPermissionEvaluator();
	}
	
	@Bean
	public Constants.LocationsLoader constantsLocationLoader(StandOrtRepository standortRepository){
		Constants.LocationsLoader locationsLoader= new LocationsLoader(standortRepository);
		locationsLoader.setAllLocations();
		return locationsLoader;
	}

}