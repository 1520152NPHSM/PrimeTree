package backendServer.listings.entitiesTest;

import static org.junit.Assert.*;

import java.util.Date;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import backendServer.clientDatabaseAccess.config.EmployeeBeanCollection;
import backendServer.exceptions.WrongFormatException;
import backendServer.listings.Constants;
import backendServer.listings.ListingBeanCollection;
import backendServer.listings.entities.SellItem;
import backendServer.listings.entities.ServiceOffering;
import backendServer.testStuff.TestExclusiveBeans;
import backendServer.userData.configuration.UserBeanCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes ={ ListingBeanCollection.class, TestExclusiveBeans.class,
		EmployeeBeanCollection.class, UserBeanCollection.class})
public class ServiceOfferingTest {

	long createDate, deadLine;
	ServiceOffering testListing;
	JSONObject initialiseJSONObject;
	@Autowired
	Constants.LocationsLoader locationsLoader;

	@Before
	public void init(){
		createDate = new Date().getTime();
		deadLine = new Date().getTime()+20;

		testListing = new ServiceOffering();
		initialiseJSONObject = new JSONObject();
		initialiseJSONObject.put(Constants.listingDataFieldId, 0);
		initialiseJSONObject.put(Constants.listingDataFieldListingType, Constants.listingTypeNameServiceOffering);
		initialiseJSONObject.put(Constants.listingDataFieldCreateDate, createDate);
		initialiseJSONObject.put(Constants.listingDataFieldDeadLine, deadLine);
		initialiseJSONObject.put(Constants.listingDataFieldCreator, 0);
		initialiseJSONObject.put(Constants.listingDataFieldDescription, "test");
		initialiseJSONObject.put(Constants.listingDataFieldDeadLine, 1);
		initialiseJSONObject.put(Constants.listingDataFieldLocation, "Koeln");
		initialiseJSONObject.put(Constants.listingDataFieldTitle, "test1");
		initialiseJSONObject.put(Constants.listingDataFieldPrice, 0);
		initialiseJSONObject.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		initialiseJSONObject.put(Constants.listingDataFieldActive, true);
		initialiseJSONObject.put(Constants.listingDataFieldPrice, 20);

	}
	
	/**
	 * Test fillFields with correct values
	 */
	@Test
	public void ServiceOfferingFillFieldsTest(){
		testListing.fillFields(initialiseJSONObject, 0);
		assertEquals(20, testListing.getPrice(), 0);
	}
	
	/**
	 * Test fillFields with no price
	 */
	@Test(expected = WrongFormatException.class)
	public void ServiceOfferingFillFieldsTestWithNoPrice(){
		initialiseJSONObject.remove(Constants.listingDataFieldPrice);
		testListing.fillFields(initialiseJSONObject, 0);
	}
}
