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
import backendServer.listings.entities.RideShareRequest;
import backendServer.listings.entities.SellItem;
import backendServer.testStuff.TestExclusiveBeans;
import backendServer.userData.configuration.UserBeanCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes ={ ListingBeanCollection.class, TestExclusiveBeans.class,
		EmployeeBeanCollection.class, UserBeanCollection.class})
public class RideShareRequestTest {

	JSONObject JSONObjectforFillFields;
	long createDate, deadLine, travelDateandTime;
	RideShareRequest testListing;
	@Autowired
	Constants.LocationsLoader locationsLoader;
	
	@Before
	public void init(){
		createDate = new Date().getTime();
		deadLine = new Date().getTime()+20;
		travelDateandTime = new Date().getTime()+10;

		testListing = new RideShareRequest();
		JSONObjectforFillFields = new JSONObject();
		JSONObjectforFillFields.put(Constants.listingDataFieldId, 0);
		JSONObjectforFillFields.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideShareRequest);
		JSONObjectforFillFields.put(Constants.listingDataFieldCreateDate, createDate);
		JSONObjectforFillFields.put(Constants.listingDataFieldDeadLine, deadLine);
		JSONObjectforFillFields.put(Constants.listingDataFieldCreator, 0);
		JSONObjectforFillFields.put(Constants.listingDataFieldDescription, "test");
		JSONObjectforFillFields.put(Constants.listingDataFieldDeadLine, 1);
		JSONObjectforFillFields.put(Constants.listingDataFieldLocation, "Koeln");
		JSONObjectforFillFields.put(Constants.listingDataFieldTitle, "test1");
		JSONObjectforFillFields.put(Constants.listingDataFieldPrice, 0);
		JSONObjectforFillFields.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		JSONObjectforFillFields.put(Constants.listingDataFieldActive, true);
		JSONObjectforFillFields.put(Constants.listingDataFieldFromLocation, "Mannheim");
		JSONObjectforFillFields.put(Constants.listingDataFieldToLocation, "Koeln");
		JSONObjectforFillFields.put(Constants.listingDataFieldTravelDateAndTime, travelDateandTime);
	}
	
	/**
	 * Test fillFields with correct Values
	 */
	@Test
	public void rideShareRequestFillFieldsTestWithCorrectValues(){
		testListing.fillFields(JSONObjectforFillFields, 0);
		assertEquals("Mannheim", testListing.getFromLocation());
		assertEquals("Koeln", testListing.getToLocation());
		assertEquals(travelDateandTime, testListing.getTravelDateAndTime().getTime());
	}
	
	/**
	 * Test fillFields with no fromlocation
	 */
	@Test(expected = WrongFormatException.class)
	public void rideShareRequestFillFieldsTestWithNoFromLocation(){
		JSONObjectforFillFields.remove(Constants.listingDataFieldFromLocation);
		testListing.fillFields(JSONObjectforFillFields, 0);
	}
	
	/**
	 * Test fillFields with no fromtoLocation
	 */
	@Test(expected = WrongFormatException.class)
	public void rideShareRequestFillFieldsTestWithNoToLocation(){
		JSONObjectforFillFields.remove(Constants.listingDataFieldToLocation);
		testListing.fillFields(JSONObjectforFillFields, 0);
	}
	
	/**
	 * Test fillFields with no fromlocation
	 */
	@Test
	public void rideShareRequestFillFieldsTestWithNoTravelDateAndTime(){
		JSONObjectforFillFields.remove(Constants.listingDataFieldTravelDateAndTime);
		testListing.fillFields(JSONObjectforFillFields, 0);
		assertNull(testListing.getTravelDateAndTime());
	}

}
