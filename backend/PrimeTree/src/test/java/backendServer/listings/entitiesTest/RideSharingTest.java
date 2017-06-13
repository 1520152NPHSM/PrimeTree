package backendServer.listings.entitiesTest;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import backendServer.clientDatabaseAccess.config.EmployeeBeanCollection;
import backendServer.exceptions.WrongFormatException;
import backendServer.listings.Constants;
import backendServer.listings.ListingBeanCollection;
import backendServer.listings.entities.RideSharing;
import backendServer.testStuff.TestExclusiveBeans;
import backendServer.userData.configuration.UserBeanCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes ={ ListingBeanCollection.class, TestExclusiveBeans.class,
		EmployeeBeanCollection.class, UserBeanCollection.class})
public class RideSharingTest {
	
	
	
	private LinkedList<String> linkedListForStops;
	private JSONObject obj1;
	private RideSharing rideSharetest;
	private String stop1;
	private String stop2;
	@Autowired
	Constants.LocationsLoader locationsLoader;
	
	
	@Before
	public void initObjects(){
	obj1 = new JSONObject();
	rideSharetest = new RideSharing();
	linkedListForStops=new LinkedList<String>();
	stop1 = "Koeln";
	stop2 = "Dortmund";
	linkedListForStops.add(stop1);
	linkedListForStops.add(stop2);
	
	obj1.put(Constants.responseFieldNewListingId, 0);
	obj1.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
	obj1.put(Constants.listingDataFieldActive, true);
	obj1.put(Constants.listingDataFieldCreateDate, 1);
	obj1.put(Constants.listingDataFieldCreator, 0);
	obj1.put(Constants.listingDataFieldDescription, "test");
	obj1.put(Constants.listingDataFieldEndDate, 2.9);
	obj1.put(Constants.listingDataFieldLocation, "Koeln");
	obj1.put(Constants.listingDataFieldFromLocation, "Koeln");
	obj1.put(Constants.listingDataFieldPicture, "Null");
	obj1.put(Constants.listingDataFieldTitle, "test1");
	obj1.put(Constants.listingDataFieldJourneyStops, linkedListForStops);
	obj1.put(Constants.listingDataFieldToLocation, "Frankfurt");
	obj1.put(Constants.listingDataFieldAvailableSeats, 3);
	obj1.put(Constants.listingDataFieldTravelDateAndTime, 10);
	}


	//--------------------------- fillFields -----------------------------------------
	
	/**
	 * Test fill field with correct parameters
	 */
	@Test
	public void rideSharingFillFieldsTestWithCorrectValues(){
		try {
			rideSharetest.fillFields(obj1, 0);
		} catch (WrongFormatException e) {
			e.printStackTrace();
		}
	}
	
	//-------------------------------------- fromlocation ---------------------------
	
	/**
	 * Test fillFields with int fromlocation
	 * @throws WrongFormatException 
	 */
	@Test(expected = JSONException.class)
	public void rideSharingFillFieldsTestWithIntFromLocation() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldFromLocation);
		obj1.put(Constants.listingDataFieldFromLocation, 1);
		rideSharetest.fillFields(obj1, 0);
	}
	
	/**
	 * Test fillFields with boolean fromlocation
	 * @throws WrongFormatException
	 */
	@Test(expected = JSONException.class)
	public void rideSharingFillFieldsTestWithBooleanFromLocation() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldFromLocation);
		obj1.put(Constants.listingDataFieldFromLocation, true);
		rideSharetest.fillFields(obj1, 0);
	}

	//----------------------------------------------- journey Stops ------------------------------------------
	
	/**
	 * Test fillFields with int journeyStops
	 * @throws WrongFormatException 
	 */
	@Test(expected = JSONException.class)
	public void rideSharingFillFieldsTestWithIntJourneyStops() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldJourneyStops);
		obj1.put(Constants.listingDataFieldJourneyStops, 2);
		rideSharetest.fillFields(obj1, 0);
	}
	
	/**
	 * Test fillFields with String journeyStops
	 * @throws WrongFormatException 
	 */
	@Test(expected = JSONException.class)
	public void rideSharingFillFieldsTestWithStringJourneyStops() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldJourneyStops);
		obj1.put(Constants.listingDataFieldJourneyStops, "KeinHalt");
		rideSharetest.fillFields(obj1, 0);
	}
	
	/**
	 * Test fillFields with Boolean journeyStops
	 * @throws WrongFormatException 
	 */
	@Test(expected = JSONException.class)
	public void rideSharingFillFieldsTestWithBooleanJourneyStops() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldJourneyStops);
		obj1.put(Constants.listingDataFieldJourneyStops, true);
		rideSharetest.fillFields(obj1, 0);
	}
	
	//--------------------------------------------- toLocation ---------------------------------------------
	
	/**
	 * Test fillFields with int toLocation
	 * @throws WrongFormatException 
	 */
	@Test(expected = JSONException.class)
	public void rideSharingFillFieldsTestWithIntToLocation() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldToLocation);
		obj1.put(Constants.listingDataFieldToLocation, 20);
		rideSharetest.fillFields(obj1, 0);
	}
	
	/**
	 * Test fillFields with boolean toLocation
	 * @throws WrongFormatException 
	 */
	@Test(expected = JSONException.class)
	public void rideSharingFillFieldsTestWithBooleanToLocation() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldToLocation);
		obj1.put(Constants.listingDataFieldToLocation, true);
		rideSharetest.fillFields(obj1, 0);
	}

	//--------------------------------------------------- availableSeats ----------------------------------
	
	/**
	 * Test fillFields with negative integer availableSeats
	 * @throws WrongFormatException 
	 */
	@Test(expected = JSONException.class)
	public void rideSharingFillFieldsTestWithNegativInt() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldAvailableSeats);
		obj1.put(Constants.listingDataFieldAvailableSeats, -1);
		rideSharetest.fillFields(obj1, 0);
	}
	
	/**
	 * Test fillFields with String availableSeats
	 * @throws WrongFormatException 
	 */
	@Test(expected = JSONException.class)
	public void rideSharingFillFieldsTestWithStringAvailableSeats() throws WrongFormatException {
		obj1.remove(Constants.listingDataFieldAvailableSeats);
		obj1.put(Constants.listingDataFieldAvailableSeats, "Vier");
		rideSharetest.fillFields(obj1, 0);
	}
	
	/**
	 * Test fillFields with boolean availableSeats
	 * @throws WrongFormatException 
	 */
	@Test(expected = JSONException.class)
	public void rideSharingFillFieldsTestWithBooleanAvailableSeats() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldAvailableSeats);
		obj1.put(Constants.listingDataFieldAvailableSeats, true);
		rideSharetest.fillFields(obj1, 0);
	}
	
	//----------------------------------------------travelDateAndTime -------------------------------------
	
	/**
	 * Test fillFields with String travelDateAndTime
	 * @throws WrongFormatException 
	 */
	@Test(expected = JSONException.class)
	public void rideSharingFillFieldsTestStringTravelDateAndTime() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldTravelDateAndTime);
		obj1.put(Constants.listingDataFieldTravelDateAndTime, "21.3.17");
		rideSharetest.fillFields(obj1, 0);
	}
	
	/**
	 * Test fiellFields with boolean travelDateAndTime
	 * @throws WrongFormatException 
	 */
	@Test(expected = JSONException.class)
	public void rideSharingFillFieldsTestBooleanTavelDateAndTime() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldTravelDateAndTime);
		obj1.put(Constants.listingDataFieldTravelDateAndTime, true);
		rideSharetest.fillFields(obj1, 0);
	}
	
	
	
	

}
