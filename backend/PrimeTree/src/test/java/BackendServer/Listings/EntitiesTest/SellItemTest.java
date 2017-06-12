package BackendServer.Listings.EntitiesTest;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.Entities.SellItem;

public class SellItemTest {

	private SellItem testListing;
	private JSONObject obj1;
	private List<String> imageGallery;
	private String imagepath1;
	private String imagepath2;

	// Only price and condition will be tested here because the other fields
	// were tested in ListingTest
	/**
	 * Setup the JSONObject for the tests
	 */
	@Before
	public void initObjects() {
		imageGallery = new LinkedList<String>();
		testListing = new SellItem();
		imageGallery.add(imagepath1);
		imageGallery.add(imagepath2);
		obj1 = new JSONObject();
		obj1.put(Constants.listingDataFieldId, 0);
		obj1.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		obj1.put(Constants.listingDataFieldActive, true);
		obj1.put(Constants.listingDataFieldCreateDate, 1);
		obj1.put(Constants.listingDataFieldCreator, 0);
		obj1.put(Constants.listingDataFieldDescription, "test");
		obj1.put(Constants.listingDataFieldDeadLine, 2.9);
		obj1.put(Constants.listingDataFieldLocation, "Koeln");
		obj1.put(Constants.listingDataFieldTitle, "test1");
		obj1.put(Constants.listingDataFieldPrice, 2.1);
		obj1.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		obj1.put(Constants.listingDataFieldImageGallery, imageGallery);

	}

	// ----------------------------------
	// fillFieldsTest---------------------------------------------------

	/**
	 * Test filliFedls with correct values
	 */
	@Test
	public void fillFieldsTestWithCorrectValues() {
		try {
			testListing.fillFields(obj1, 0);
		} catch (WrongFormatException e) {
			e.printStackTrace();
		}
	}

	// ---------------------------------- fillFieldsTest for price -----------------------------------------

	/**
	 * Test fillFields with no price
	 * @throws WrongFormatException 
	 */
	@Test(expected = WrongFormatException.class)
	public void fillFieldsTestWithNoPrice() throws WrongFormatException{
		obj1.remove(Constants.listingDataFieldPrice);
		testListing.fillFields(obj1, 0);
	}

	/**
	 * Test fillfields with a negativ price
	 * 
	 * @throws WrongFormatException
	 * @result Exception
	 */
	@Test(expected = JSONException.class)
	public void fillFieldsTestNegativePrice() throws WrongFormatException {
		obj1.remove(Constants.listingDataFieldPrice);
		obj1.put(Constants.listingDataFieldPrice, -2.1);
		testListing.fillFields(obj1, 0);
	}

	/**
	 * Test fillFields with an Int instead of Float in price
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void fillFieldsTestWithIntPrice() throws WrongFormatException {
		obj1.remove(Constants.listingDataFieldPrice);
		obj1.put(Constants.listingDataFieldPrice, 100);
		testListing.fillFields(obj1, 0);
		assertEquals(100, testListing.getPrice(), 0);
	}

	/**
	 * Test fillFields with a String instead of float in price
	 * 
	 * @throws WrongFormatException
	 */
	@Test(expected = JSONException.class)
	public void fillFieldsTestWithStringPrice() throws WrongFormatException {
		obj1.remove(Constants.listingDataFieldPrice);
		obj1.put(Constants.listingDataFieldPrice, "zwei");
		testListing.fillFields(obj1, 0);
	}

	/**
	 * Test fillFields with a boolean instead of float in price
	 * 
	 * @throws WrongFormatException
	 */
	@Test(expected = JSONException.class)
	public void fillFieldsTestWithbooleanPrice() throws WrongFormatException {
		obj1.remove(Constants.listingDataFieldPrice);
		obj1.put(Constants.listingDataFieldPrice, true);
		testListing.fillFields(obj1, 0);

	}

	// ------------------------------------- fillFields for condition
	// --------------------------------

	/**
	 * Test fillFields with a boolean in Condition
	 * 
	 * @throws WrongFormatException
	 */
	@Test(expected = JSONException.class)
	public void fillFieldsTestWithBooleanCondition() throws WrongFormatException {
		obj1.remove(Constants.listingDataFieldCondition);
		obj1.put(Constants.listingDataFieldCondition, true);
		testListing.fillFields(obj1, 0);

	}

	/**
	 * Test fillFields with an int in Condition
	 * 
	 * @throws WrongFormatException
	 */
	@Test(expected = JSONException.class)
	public void fillFieldsTestWithIntCondition() throws WrongFormatException {
		obj1.remove(Constants.listingDataFieldCondition);
		obj1.put(Constants.listingDataFieldCondition, 2);
		testListing.fillFields(obj1, 0);

	}
	// ------------------------------------- fillFields for imagelocation
	// ------------------------------------

	/**
	 * Test fillFields with a boolean instead of a String in imagelocation
	 * 
	 * @throws WrongFormatException
	 */
	@Test(expected = JSONException.class)
	public void fillFieldsTestWithBooleanImagelocation() throws WrongFormatException {
		obj1.remove(Constants.listingDataFieldPicture);
		obj1.put(Constants.listingDataFieldPicture, true);
		testListing.fillFields(obj1, 0);
	}

	/**
	 * Test fillFields with an int instead of a String in imagelocation
	 * 
	 * @throws WrongFormatException
	 */
	@Test(expected = JSONException.class)
	public void fillFieldsTestWithIntImagelocation() throws WrongFormatException {
		obj1.remove(Constants.listingDataFieldPicture);
		obj1.put(Constants.listingDataFieldPicture, 21);
		testListing.fillFields(obj1, 0);
	}
}
