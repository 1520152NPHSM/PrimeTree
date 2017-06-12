package BackendServer.Listings.EntitiesTest;

import static org.junit.Assert.*;

import java.util.Date;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.Entities.RideShareRequest;
import BackendServer.Listings.Entities.SellItem;

public class RideShareRequestTest {

	JSONObject JSONObjectforFillFields;
	long createDate, deadLine, travelDateandTime;
	RideShareRequest testListing;
	
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
	@Test(expected = WrongFormatException.class)
	public void rideShareRequestFillFieldsTestWithNoTravelDateAndTime(){
		JSONObjectforFillFields.remove(Constants.listingDataFieldTravelDateAndTime);
		testListing.fillFields(JSONObjectforFillFields, 0);
	}

}
