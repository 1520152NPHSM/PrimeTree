package BackendServer.Listings;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BackendServer.UserData.Configuration.UserBeanCollection.class,
BackendServer.Listings.ListingBeanCollection.class, BackendServer.ClientDatabaseAccess.Config.EmployeeBeanCollection.class})
public class PersistenceAdapterSearchAndFilterTest {
	
	@Autowired
	PersistenceAdapter persistenceAdapter;
	
	String
	query1;
	
	String
	location1,
	location2;
	
	String[]
	locationArray1;
	
	int
	minPrice1;
	
	int
	maxPrice1;
	
	String[]
	typeArray1;
	
	@Before
	public void setUp(){
		query1="istDrin";
		location1="Drinnenstadt";
		location2="NiemandErstelltHierEinInseratHausen";
		locationArray1=new String[]{location1, location2};
		minPrice1=0;
		maxPrice1=100;
		typeArray1=new String[]{Constants.type};
	}
	
//	@Test
//	public void searchWith{
//		persistenceAdapter.getListingsBySearch(query, page, location, b, price_min, price_max, type, kind, sort, statistics)
//	}
	

}
