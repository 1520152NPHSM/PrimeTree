package BackendServer.Listings.EntitiesTest;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.Entities.SellItem;

public class ListingTest {
	// We create a Sellitem to test Listing because we cannot create a Listing
	// because it is abstract
	private SellItem testListing;
	private JSONObject initialiseJSONObject;
	private JSONObject testJSON;
	private String[] locations;
	private String[] typeArray;
	private boolean test;
	private List<String> imageGallery;
	private String imagepath1;
	private String imagepath2;
	private long dateOne, dateTwo;

	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	// type is SellItem/Ridesharing
	// kind is offering or request
	/**
	 * Setup the JSONObject for the tests Setup only the fields for Listing
	 */
	@Before
	public void initObjects() {
		dateOne = new Date().getTime();
		dateTwo = new Date().getTime()+20;
		testListing = new SellItem();
		initialiseJSONObject = new JSONObject();
		initialiseJSONObject.put(Constants.listingDataFieldId, 0);
		initialiseJSONObject.put(Constants.listingDataFieldCreateDate, dateOne);
		initialiseJSONObject.put(Constants.listingDataFieldEndDate, dateTwo);
		initialiseJSONObject.put(Constants.listingDataFieldCreator, 0);
		initialiseJSONObject.put(Constants.listingDataFieldDescription, "test");
		initialiseJSONObject.put(Constants.listingDataFieldDeadLine, 1);
		initialiseJSONObject.put(Constants.listingDataFieldLocation, "Koeln");
		initialiseJSONObject.put(Constants.listingDataFieldTitle, "test1");
		initialiseJSONObject.put(Constants.listingDataFieldPrice, 0);
		initialiseJSONObject.put(Constants.listingDataFieldCondition, "new");

		locations = new String[2];
		locations[0] = "Mannheim";
		locations[1] = "Koeln";

		typeArray = new String[2];
		typeArray[0] = "SellItem";
		typeArray[1] = "RideSharing";
	}

	// ---------------------------------------- Correct tests
	// ---------------------------------------------------

	// You don`t need to test with null because the put method doesn`t allow
	// null
	/**
	 * Test fillFields with correct values in SellItem
	 * 
	 * @Result a correct Listing
	 */
	@Test
	public void fillfieldsTestAllCorrectSellItem() {
		try {
			testListing.fillFields(initialiseJSONObject, 0);
		} catch (WrongFormatException e) {
			e.printStackTrace();
		}
		// assertEquals(testListing.getListingId(), 0);
		assertEquals(testListing.getOwner(), 0);
		assertEquals(testListing.getDescription(), "test");
		assertEquals(testListing.getTitle(), "test1");
		assertEquals(testListing.getLocation(), "Koeln");
		assertEquals(testListing.isActive(), true);
	}

	/**
	 * Test fillfields with null
	 * 
	 * @throws WrongFormatException
	 */
	@Test(expected = Exception.class)
	public void fillFieldsTestWithNull() throws WrongFormatException {
		testListing.fillFields(null, 0);
	}

	/**
	 * Test toJSON with correct values the new JSON object should have the same
	 * values as obj1
	 */
	@Test
	public void toJSONTest() {
		testJSON = testListing.toJSON();

		// assertEquals(testJSON.get("id"), testListing.getListingId());
		assertEquals(testJSON.get(Constants.listingDataFieldCreator), testListing.getOwner());
		assertEquals(testJSON.get(Constants.listingDataFieldDescription), testListing.getDescription());
		assertEquals(testJSON.get(Constants.listingDataFieldTitle), testListing.getTitle());
		assertEquals(testJSON.get(Constants.listingDataFieldPicture), testListing.getPicture());
		assertEquals(testJSON.get(Constants.listingDataFieldLocation), testListing.getLocation());
		assertEquals(testJSON.get(Constants.listingDataFieldActive), testListing.isActive());
	}

	// ---------------------------------- fillFields for ID
	// -------------------------------------------

	// /**
	// * Test fillfields with a String instead of Int in ID
	// */
	// @Test(expected = Exception.class)
	// public void fillFieldsTestStringID(){
	// initialiseJSONOject.remove("id");
	// initialiseJSONOject.put("id", "zwei");
	// testListing.fillFields(initialiseJSONOject, 0);
	// }
	//
	// /**
	// * Test fillFields with a double instead of an Int in ID
	// */
	// @Test(expected = Exception.class)
	// public void fillFieldsTestWithDoubleID(){
	// initialiseJSONOject.remove("id");
	// initialiseJSONOject.put("id", 20.09);
	// testListing.fillFields(initialiseJSONOject, 0);
	// }
	// /**
	// * Test fillFields with a boolean instead of an Int in ID
	// */
	// @Test(expected = Exception.class)
	// public void fillFieldsTestWithBolleanID(){
	// initialiseJSONOject.remove("id");
	// initialiseJSONOject.put("id", true);
	// testListing.fillFields(initialiseJSONOject, 0);
	// }

	// ------------------------------------- fillFields for active
	// --------------------------------

	/**
	 * Test fillFields with a String instead of boolean in active
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithStringActive() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldActive);
		initialiseJSONObject.put(Constants.listingDataFieldActive, "test");
		thrown.expect(WrongFormatException.class);
		testListing.fillFields(initialiseJSONObject, 0);
	}

	/**
	 * Test fillFields with an int instead of boolean in active
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithIntActive() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldActive);
		initialiseJSONObject.put(Constants.listingDataFieldActive, 3);
		thrown.expect(WrongFormatException.class);
		testListing.fillFields(initialiseJSONObject, 0);
	}

	// ------------------------------------- fillFields for CreateDate
	// -------------------------------

	/**
	 * Test fillFields with a boolean in createDate
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithBooleanCreateDate() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldCreateDate);
		initialiseJSONObject.put(Constants.listingDataFieldCreateDate, true);
		thrown.expect(WrongFormatException.class);
		testListing.fillFields(initialiseJSONObject, 0);
	}

	/**
	 * Test fillFields with a String in createDate
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithStringCreateDate() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldCreateDate);
		initialiseJSONObject.put(Constants.listingDataFieldCreateDate, "20.05.2017");
		thrown.expect(WrongFormatException.class);
		testListing.fillFields(initialiseJSONObject, 0);
	}

	// ------------------------------------- fillFields for creator
	// --------------------------------

	/**
	 * Test fillFields with a String instead of int in creator
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithStringCreator() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldCreator);
		initialiseJSONObject.put(Constants.listingDataFieldCreator, "Mark");
		thrown.expect(WrongFormatException.class);
		testListing.fillFields(initialiseJSONObject, 0);
	}

	/**
	 * Test fillFields with a boolean instead of int in creator
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithBooleanCreator() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldCreator);
		initialiseJSONObject.put(Constants.listingDataFieldCreator, true);
		thrown.expect(WrongFormatException.class);
		testListing.fillFields(initialiseJSONObject, 0);
		//assertEquals(0, testListing.getOwner());
	}

	/**
	 * Test fillFields with a doubleinstead of int in creator
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithDoubleCreator() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldCreator);
		initialiseJSONObject.put(Constants.listingDataFieldCreator, 2.1);
		thrown.expect(WrongFormatException.class);
		testListing.fillFields(initialiseJSONObject, 0);
	}

	// ------------------------------------- fillFields for description
	// -------------------------------

	/**
	 * Test fillfields with a boolean instead of String in description
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithBooleanDesription() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldDescription);
		initialiseJSONObject.put(Constants.listingDataFieldDescription, true);
		thrown.expect(WrongFormatException.class);
		testListing.fillFields(initialiseJSONObject, 0);

	}

	/**
	 * Test fillfields with a number instead of String in description
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithIntDesription() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldDescription);
		initialiseJSONObject.put(Constants.listingDataFieldDescription, 12);
		thrown.expect(WrongFormatException.class);
		testListing.fillFields(initialiseJSONObject, 0);
	}

	// ------------------------------------- fillFields for expiryDate
	// -------------------------------

	/**
	 * Test fillFields with a boolean in createDate
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithBooleanExpiryDate() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldDeadLine);
		initialiseJSONObject.put(Constants.listingDataFieldDeadLine, true);
		thrown.expect(WrongFormatException.class);
		testListing.fillFields(initialiseJSONObject, 0);
	}

	/**
	 * Test fillFields with a String in createDate
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithStringExpiryDate() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldDeadLine);
		initialiseJSONObject.put(Constants.listingDataFieldDeadLine, "20.05.2017");
		thrown.expect(WrongFormatException.class);
		testListing.fillFields(initialiseJSONObject, 0);
	}

	// ------------------------------------- fillFields for location
	// --------------------------------

	/**
	 * Test fillFields with a wrong location in location
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithWrongLocation() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldLocation);
		initialiseJSONObject.put(Constants.listingDataFieldLocation, "Vossenack");
		thrown.expect(WrongFormatException.class);
		testListing.fillFields(initialiseJSONObject, 0);
	}

	/**
	 * Test fillFields with a int in location
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithIntLocation() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldLocation);
		initialiseJSONObject.put(Constants.listingDataFieldLocation, 2);
		thrown.expect(WrongFormatException.class);
		testListing.fillFields(initialiseJSONObject, 0);
	}

	/**
	 * Test fillFields with a boolean in location
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithBooleanLocation() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldLocation);
		initialiseJSONObject.put(Constants.listingDataFieldLocation, true);
		thrown.expect(WrongFormatException.class);
		testListing.fillFields(initialiseJSONObject, 0);
	}

	/**
	 * Test fillFields with an empty String in location
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithEmptyStringLocation() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldLocation);
		initialiseJSONObject.put(Constants.listingDataFieldLocation, "");
		thrown.expect(WrongFormatException.class);
		testListing.fillFields(initialiseJSONObject, 0);
	}

	// ------------------------------------- fillFields for title
	// ----------------------------------------

	/**
	 * Test fillFields with empty string in title
	 * 
	 * @throws WrongFormatException
	 */
	@Test(expected = WrongFormatException.class)
	public void fillFieldsTestEmptyStringTitle() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldTitle);
		initialiseJSONObject.put(Constants.listingDataFieldTitle, "");
		testListing.fillFields(initialiseJSONObject, 0);
	}

	/**
	 * Test fillFields with an Int instead of String in title
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithIntTitle() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldTitle);
		initialiseJSONObject.put(Constants.listingDataFieldTitle, 10);
		thrown.expect(WrongFormatException.class);
		testListing.fillFields(initialiseJSONObject, 0);
	}

	/**
	 * Test fillFields with an boolean instead of String in title
	 * @throws WrongFormatException 
	 */
	@Test
	public void fillFieldsTestWithBooleanTitle() throws WrongFormatException {
		initialiseJSONObject.remove(Constants.listingDataFieldTitle);
		initialiseJSONObject.put(Constants.listingDataFieldTitle, true);
		thrown.expect(WrongFormatException.class);
			testListing.fillFields(initialiseJSONObject, 0);
	}

	// ---------------------------------------- matchFilterOptions
	// --------------------------------------------------------

	/**
	 * Test matchFilterOptions with correct Values
	 * 
	 * @Result true
	 */
	@Test
	public void matchFilterOptionsTestWithCorrectValues() {
		test = testListing.matchFilterOptions(locations, true, 0, 0, typeArray, "Offering");
		assertEquals(true, test);
		// other correct values for min/maxPrice price is set to 0
		test = testListing.matchFilterOptions(locations, true, -2, 1, typeArray, "Offering");
		assertEquals(true, test);
	}

	/**
	 * Test matchFilterOptions with wrong location
	 */
	@Test
	public void matchFilterOptionsWithWrongLocation() {
		testListing.setLocation("Vossenack");
		test = testListing.matchFilterOptions(locations, true, 0, 0, typeArray, "Offering");
		assertEquals(false, test);
		testListing.setLocation("Koeln");
	}

	/**
	 * Test matchFilterOptions with wrong type
	 */
	@Test
	public void matchFilterOptionsTestWithWrongType() {
		testListing.setType("ServiceOffering");
		test = testListing.matchFilterOptions(locations, true, 0, 0, typeArray, "Offering");
		assertEquals(false, test);
		testListing.setType("SellItem");
	}

	/**
	 * Test matchFilterOptions with location = null
	 */
	@Test
	public void matchFilterOptionsTestWithNullLocation() {
		test = testListing.matchFilterOptions(null, true, 0, 0, typeArray, "Offering");
		assertEquals(true, test);
	}

	/**
	 * Test matchFilterOptions with type = null
	 */
	@Test
	public void matchFilterOptionsTestWithNullType() {
		test = testListing.matchFilterOptions(locations, true, 0, 0, null, "Offering");
		assertEquals(true, test);
	}

	/**
	 * Test matchFilterOptions with kind = null
	 */
	@Test
	public void matchFilterOptionsTestWithNullKind() {
		test = testListing.matchFilterOptions(locations, true, 0, 0, typeArray, null);
		assertEquals(true, test);
	}

	/**
	 * Test matchFilterOptions with shallbeactive = false
	 */
	@Test
	public void matchFilterOptionsTestWithFalseShallBeActive() {
		test = testListing.matchFilterOptions(locations, false, 0, 0, typeArray, "Offering");
		assertEquals(false, test);
	}

	/**
	 * Test matchFilterOptions with wrong kind
	 */
	@Test
	public void matchFilterOptionsTestWithWrongKind() {
		test = testListing.matchFilterOptions(locations, true, 0, 0, typeArray, "Request");
		assertEquals(false, test);
	}

	/**
	 * Test matchFilterOptions with wrong minPrice
	 */
	@Test
	public void matchFilterOptionsTestWithWrongMinPrice() {
		test = testListing.matchFilterOptions(locations, true, 1, 0, typeArray, "Offering");
		assertEquals(false, test);
	}

	/**
	 * Test matchFilterOptions with wrong maxPrice
	 */
	@Test
	public void matchFilterOptionsWithWrongMaxPrice() {
		test = testListing.matchFilterOptions(locations, true, 0, -2, typeArray, "Offering");
		assertEquals(false, test);
	}

	// ------------------------------------- matchFilterOptions with query
	// ------------------------------------
	/**
	 * Test matchFilterOptions with correct Querry (query = title)
	 */
	@Test
	public void matchFilterOptionsTestWithCorrectQueryTitle() {
		test = testListing.matchFilterOptions("test1", locations, true, 0, 0, typeArray, "Offering");
		assertEquals(true, test);
	}

	/**
	 * Test matchFilterOptions with correct Querry (query = description)
	 */
	@Test
	public void matchFilterOptionsTestWithCorrectQueryDescription() {
		test = testListing.matchFilterOptions("test", locations, true, 0, 0, typeArray, "Offering");
		assertEquals(true, test);
	}

	/**
	 * Test matchFilterOptions with query = null
	 */
	@Test
	public void matchFilterOptionsTestWithQuerryNull() {
		test = testListing.matchFilterOptions(null, locations, true, 0, 0, typeArray, "Offering");
		assertEquals(false, test);
	}

	/**
	 * Test matchFilterOptions with wrong query
	 */
	@Test
	public void matchFilterOptionsTestWithWrongQuery() {
		test = testListing.matchFilterOptions("test123", locations, true, 0, 0, typeArray, "Offering");
		assertEquals(false, test);
	}

}
