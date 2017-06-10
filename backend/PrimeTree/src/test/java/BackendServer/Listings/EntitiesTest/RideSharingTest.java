package BackendServer.Listings.EntitiesTest;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.Entities.RideSharing;

public class RideSharingTest {
	
	
	
	private LinkedList<String> linkedListForStops;
	private JSONObject obj1;
	private RideSharing rideSharetest;
	private String stop1;
	private String stop2;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
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
	obj1.put(Constants.listingDataFieldFromLocation, "Mannheim");
	obj1.put(Constants.listingDataFieldJourneyStops, linkedListForStops);
	obj1.put(Constants.listingDataFieldFromLocation, "Frankfurt");
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
	@Test
	public void rideSharingFillFieldsTestWithIntFromLocation() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldFromLocation);
		obj1.put(Constants.listingDataFieldFromLocation, 1);
		thrown.expect(WrongFormatException.class);
		rideSharetest.fillFields(obj1, 0);
	}
	
	/**
	 * Test fillFields with boolean fromlocation
	 * @throws WrongFormatException
	 */
	@Test
	public void rideSharingFillFieldsTestWithBooleanFromLocation() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldFromLocation);
		obj1.put(Constants.listingDataFieldFromLocation, true);
		thrown.expect(WrongFormatException.class);
		rideSharetest.fillFields(obj1, 0);
	}

	//----------------------------------------------- journey Stops ------------------------------------------
	
	/**
	 * Test fillFields with int journeyStops
	 * @throws WrongFormatException 
	 */
	@Test
	public void rideSharingFillFieldsTestWithIntJourneyStops() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldJourneyStops);
		obj1.put(Constants.listingDataFieldJourneyStops, 2);
		thrown.expect(WrongFormatException.class);
		rideSharetest.fillFields(obj1, 0);
	}
	
	/**
	 * Test fillFields with String journeyStops
	 * @throws WrongFormatException 
	 */
	@Test
	public void rideSharingFillFieldsTestWithStringJourneyStops() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldJourneyStops);
		obj1.put(Constants.listingDataFieldJourneyStops, "KeinHalt");
		thrown.expect(WrongFormatException.class);
		rideSharetest.fillFields(obj1, 0);
	}
	
	/**
	 * Test fillFields with Boolean journeyStops
	 * @throws WrongFormatException 
	 */
	@Test
	public void rideSharingFillFieldsTestWithBooleanJourneyStops() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldJourneyStops);
		obj1.put(Constants.listingDataFieldJourneyStops, true);
		thrown.expect(WrongFormatException.class);
		rideSharetest.fillFields(obj1, 0);
	}
	
	//--------------------------------------------- toLocation ---------------------------------------------
	
	/**
	 * Test fillFields with int toLocation
	 * @throws WrongFormatException 
	 */
	@Test
	public void rideSharingFillFieldsTestWithIntToLocation() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldToLocation);
		obj1.put(Constants.listingDataFieldToLocation, 20);
		thrown.expect(WrongFormatException.class);
		rideSharetest.fillFields(obj1, 0);
	}
	
	/**
	 * Test fillFields with boolean toLocation
	 * @throws WrongFormatException 
	 */
	@Test
	public void rideSharingFillFieldsTestWithBooleanToLocation() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldToLocation);
		obj1.put(Constants.listingDataFieldToLocation, true);
		thrown.expect(WrongFormatException.class);
		rideSharetest.fillFields(obj1, 0);
	}

	//--------------------------------------------------- availableSeats ----------------------------------
	
	/**
	 * Test fillFields with negative integer availableSeats
	 * @throws WrongFormatException 
	 */
	@Test
	public void rideSharingFillFieldsTestWithNegativInt() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldAvailableSeats);
		obj1.put(Constants.listingDataFieldAvailableSeats, -1);
		thrown.expect(WrongFormatException.class);
		rideSharetest.fillFields(obj1, 0);
	}
	
	/**
	 * Test fillFields with String availableSeats
	 * @throws WrongFormatException 
	 */
	@Test
	public void rideSharingFillFieldsTestWithStringAvailableSeats() throws WrongFormatException {
		obj1.remove(Constants.listingDataFieldAvailableSeats);
		obj1.put(Constants.listingDataFieldAvailableSeats, "Vier");
		thrown.expect(WrongFormatException.class);
		rideSharetest.fillFields(obj1, 0);
	}
	
	/**
	 * Test fillFields with boolean availableSeats
	 * @throws WrongFormatException 
	 */
	@Test
	public void rideSharingFillFieldsTestWithBooleanAvailableSeats() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldAvailableSeats);
		obj1.put(Constants.listingDataFieldAvailableSeats, true);
		thrown.expect(WrongFormatException.class);
		rideSharetest.fillFields(obj1, 0);
	}
	
	//----------------------------------------------travelDateAndTime -------------------------------------
	
	/**
	 * Test fillFields with String travelDateAndTime
	 * @throws WrongFormatException 
	 */
	@Test
	public void rideSharingFillFieldsTestStringTravelDateAndTime() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldTravelDateAndTime);
		obj1.put(Constants.listingDataFieldTravelDateAndTime, "21.3.17");
		thrown.expect(WrongFormatException.class);
		rideSharetest.fillFields(obj1, 0);
	}
	
	/**
	 * Test fiellFields with boolean travelDateAndTime
	 * @throws WrongFormatException 
	 */
	@Test
	public void rideSharingFillFieldsTestBooleanTavelDateAndTime() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldTravelDateAndTime);
		obj1.put(Constants.listingDataFieldTravelDateAndTime, true);
		thrown.expect(WrongFormatException.class);
		rideSharetest.fillFields(obj1, 0);
	}
	
	
	
	

}
