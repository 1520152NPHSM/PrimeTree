package BackendServer.Listings.EntitiesTest;

import static org.assertj.core.api.Assertions.in;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
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
	private long createDate, deadLine;
	private Collection comments;

	
	@Rule
	
	public ExpectedException thrown = ExpectedException.none();
	// type is SellItem/Ridesharing
	// kind is offering or request
	/**
	 * Setup the JSONObject for the tests Setup only the fields for Listing
	 */
	@Before
	public void initObjects() {
		createDate = new Date().getTime();
		deadLine = new Date().getTime()+20;
		comments = new ArrayList();

		testListing = new SellItem();
		initialiseJSONObject = new JSONObject();
		initialiseJSONObject.put(Constants.listingDataFieldId, 0);
		initialiseJSONObject.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		initialiseJSONObject.put(Constants.listingDataFieldCreateDate, createDate);
		initialiseJSONObject.put(Constants.listingDataFieldDeadLine, deadLine);
		initialiseJSONObject.put(Constants.listingDataFieldCreator, 0);
		initialiseJSONObject.put(Constants.listingDataFieldDescription, "test");
		initialiseJSONObject.put(Constants.listingDataFieldDeadLine, 1);
		initialiseJSONObject.put(Constants.listingDataFieldLocation, "Koeln");
		initialiseJSONObject.put(Constants.listingDataFieldTitle, "test1");
		initialiseJSONObject.put(Constants.listingDataFieldPrice, 0);
		initialiseJSONObject.put(Constants.listingDataFieldCondition, "new");
		initialiseJSONObject.put(Constants.listingDataFieldActive, true);
		initialiseJSONObject.put(Constants.listingDataFieldComments, "");
		initialiseJSONObject.put(Constants.listingDataFieldComments, comments);
		

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
		testListing.fillFields(initialiseJSONObject, 0);
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
	@Test(expected = WrongFormatException.class)
	public void fillFieldsTestWithNull() throws WrongFormatException {
		testListing.fillFields(null, 0);
	}
	
	/**
	 * TestFillFields with no createDate
	 */
	@Test(expected = WrongFormatException.class)
	public void fillFieldsTestWithNoCreateDate(){
		initialiseJSONObject.remove(Constants.listingDataFieldCreateDate);
		testListing.fillFields(initialiseJSONObject, 0);
	}
	
	/**
	 * Test fillFields with no description
	 */
	@Test(expected = WrongFormatException.class)
	public void fillFieldsTestWithNoDescription(){
		initialiseJSONObject.remove(Constants.listingDataFieldDescription);
		testListing.fillFields(initialiseJSONObject, 0);
	}
	
	/**
	 * Test fillFields with no location
	 */
	@Test(expected = WrongFormatException.class)
	public void fillFieldsTestWithNoLocation(){
		initialiseJSONObject.remove(Constants.listingDataFieldLocation);
		testListing.fillFields(initialiseJSONObject, 0);
	}
	
	/**
	 * Test fillFields with no title
	 */
	@Test(expected = WrongFormatException.class)
	public void fillFieldsTestWithNoTitle(){
		initialiseJSONObject.remove(Constants.listingDataFieldTitle);
		testListing.fillFields(initialiseJSONObject, 0);
	}
	
	@Test
	public void fillFieldsTestWithNoActiveField(){
		initialiseJSONObject.remove(Constants.listingDataFieldActive);
		testListing.fillFields(initialiseJSONObject, 0);
		assertEquals(true, testListing.isActive());
	}
	
	@Test
	public void fillFieldsTestWithNoDeadLine(){
		initialiseJSONObject.remove(Constants.listingDataFieldDeadLine);
		testListing.fillFields(initialiseJSONObject, 0);
		assertEquals(null, testListing.getExpiryDate());
	}

	/**
	 * Test toJSON with correct values the new JSON object should have the same
	 * values as obj1
	 */
	@Test
	public void toJSONTest() {
		testListing.fillFields(initialiseJSONObject, 0);
		testJSON = testListing.toJSON();

		assertEquals(testJSON.get(Constants.listingDataFieldCreator), testListing.getOwner());
		assertEquals(testJSON.get(Constants.listingDataFieldDescription), testListing.getDescription());
		assertEquals(testJSON.get(Constants.listingDataFieldTitle), testListing.getTitle());
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
		testListing.fillFields(initialiseJSONObject, 0);
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
		testListing.fillFields(initialiseJSONObject, 0);
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
		testListing.fillFields(initialiseJSONObject, 0);
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
		testListing.fillFields(initialiseJSONObject, 0);
		test = testListing.matchFilterOptions(null, true, 0, 0, typeArray, "Offering");
		assertEquals(true, test);
	}

	/**
	 * Test matchFilterOptions with type = null
	 */
	@Test
	public void matchFilterOptionsTestWithNullType() {
		testListing.fillFields(initialiseJSONObject, 0);
		test = testListing.matchFilterOptions(locations, true, 0, 0, null, "Offering");
		assertEquals(true, test);
	}

	/**
	 * Test matchFilterOptions with kind = null
	 */
	@Test
	public void matchFilterOptionsTestWithNullKind() {
		testListing.fillFields(initialiseJSONObject, 0);
		test = testListing.matchFilterOptions(locations, true, 0, 0, typeArray, null);
		assertEquals(true, test);
	}

	/**
	 * Test matchFilterOptions with shallbeactive = false
	 */
	@Test
	public void matchFilterOptionsTestWithFalseShallBeActive() {
		testListing.fillFields(initialiseJSONObject, 0);
		test = testListing.matchFilterOptions(locations, false, 0, 0, typeArray, "Offering");
		assertEquals(false, test);
	}

	/**
	 * Test matchFilterOptions with wrong kind
	 */
	@Test
	public void matchFilterOptionsTestWithWrongKind() {
		testListing.fillFields(initialiseJSONObject, 0);
		test = testListing.matchFilterOptions(locations, true, 0, 0, typeArray, "Request");
		assertEquals(false, test);
	}

	/**
	 * Test matchFilterOptions with wrong minPrice
	 */
	@Test
	public void matchFilterOptionsTestWithWrongMinPrice() {
		testListing.fillFields(initialiseJSONObject, 0);
		test = testListing.matchFilterOptions(locations, true, 1, 0, typeArray, "Offering");
		assertEquals(false, test);
	}

	/**
	 * Test matchFilterOptions with wrong maxPrice
	 */
	@Test
	public void matchFilterOptionsWithWrongMaxPrice() {
		testListing.fillFields(initialiseJSONObject, 0);
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
		testListing.fillFields(initialiseJSONObject, 0);
		test = testListing.matchFilterOptions("test1", locations, true, 0, 0, typeArray, "Offering");
		assertEquals(true, test);
	}

	/**
	 * Test matchFilterOptions with correct Querry (query = description)
	 */
	@Test
	public void matchFilterOptionsTestWithCorrectQueryDescription() {
		testListing.fillFields(initialiseJSONObject, 0);
		test = testListing.matchFilterOptions("test", locations, true, 0, 0, typeArray, "Offering");
		assertEquals(true, test);
	}

	/**
	 * Test matchFilterOptions with query = null
	 */
	@Test
	public void matchFilterOptionsTestWithQuerryNull() {
		testListing.fillFields(initialiseJSONObject, 0);
		test = testListing.matchFilterOptions(null, locations, true, 0, 0, typeArray, "Offering");
		assertEquals(false, test);
	}

	/**
	 * Test matchFilterOptions with wrong query
	 */
	@Test
	public void matchFilterOptionsTestWithWrongQuery() {
		testListing.fillFields(initialiseJSONObject, 0);
		test = testListing.matchFilterOptions("test123", locations, true, 0, 0, typeArray, "Offering");
		assertEquals(false, test);
	}

}
