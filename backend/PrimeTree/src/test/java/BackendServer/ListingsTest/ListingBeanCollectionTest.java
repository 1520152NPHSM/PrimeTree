
package BackendServer.ListingsTest;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import BackendServer.Listings.ListingBeanCollection;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ListingBeanCollection.class, loader=AnnotationConfigContextLoader.class)
public class ListingBeanCollectionTest {

	
	@Autowired
	LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;
	@Test
	public void LocalContainerEntityManagerFactoryBeanTest() {
		
		
	}

}