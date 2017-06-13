package backendServer.listings.entitiesTest;

import static org.junit.Assert.*;

import java.util.Date;

import org.json.JSONException;
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
import backendServer.listings.entities.BorrowRequest;
import backendServer.listings.entities.RequestListing;
import backendServer.testStuff.TestExclusiveBeans;
import backendServer.userData.configuration.UserBeanCollection;
import backendServer.userStuffTest.UserRestControllerTest;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes ={ ListingBeanCollection.class, TestExclusiveBeans.class,
		EmployeeBeanCollection.class, UserBeanCollection.class})
public class BorrowRequestTest {

	private BorrowRequest borrowRequestTest;
	private JSONObject initialiseJSONOject;
	private long dateOne, dateTwo;
	@Autowired
	Constants.LocationsLoader locationsLoader;

	@Before
	public void init() {
		dateOne = new Date().getTime();
		dateTwo = new Date().getTime() + 20;
		borrowRequestTest = new BorrowRequest();
		initialiseJSONOject = new JSONObject();
		initialiseJSONOject.put(Constants.listingDataFieldId, 0);
		initialiseJSONOject.put(Constants.listingDataFieldListingType, Constants.listingTypeNameBorrowRequest);
		initialiseJSONOject.put(Constants.listingDataFieldActive, true);
		initialiseJSONOject.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		initialiseJSONOject.put(Constants.listingDataFieldCreator, 0);
		initialiseJSONOject.put(Constants.listingDataFieldDescription, "test");
		initialiseJSONOject.put(Constants.listingDataFieldDeadLine, 1);
		initialiseJSONOject.put(Constants.listingDataFieldLocation, "Koeln");
		initialiseJSONOject.put(Constants.listingDataFieldTitle, "test1");
		initialiseJSONOject.put(Constants.listingDataFieldPrice, 0);
		initialiseJSONOject.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		initialiseJSONOject.put(Constants.listingDataFieldBorrowFromDate, dateOne);
		initialiseJSONOject.put(Constants.listingDataFieldBorrowToDate, dateTwo);
	}

	// ---------------------------- fillFields
	// ---------------------------------------

	/**
	 * Test fillFields with correct values
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void borrowRequestFillFieldsTestWithCorrectValues() throws WrongFormatException {
		borrowRequestTest.fillFields(initialiseJSONOject, 0);

		Date date1 = new Date(dateTwo);
		Date date2 = new Date(dateOne);
		assertEquals(date1, borrowRequestTest.getBorrowToDate());
		assertEquals(date2, borrowRequestTest.getBorrowFromDate());

	}

	// ------------------------------------ borrowFromDate
	// ------------------------------

	/**
	 * Test fillFields with String borrowFromDate
	 * 
	 * @throws WrongFormatException
	 */
	@Test(expected = JSONException.class)
	public void borrowRequestFillFieldsWithStringBorrowFromDate() throws WrongFormatException {
		initialiseJSONOject.remove(Constants.listingDataFieldBorrowFromDate);
		initialiseJSONOject.put(Constants.listingDataFieldBorrowFromDate, "21.04.2017");
		borrowRequestTest.fillFields(initialiseJSONOject, 0);
	}

	/**
	 * Test fillFields with boolean borrowFromDate
	 * 
	 * @throws WrongFormatException
	 */
	@Test(expected = JSONException.class)
	public void borrowRequestFillFieldsWithBooleanBorrowFromDate() throws WrongFormatException {
		initialiseJSONOject.remove(Constants.listingDataFieldBorrowFromDate);
		initialiseJSONOject.put(Constants.listingDataFieldBorrowFromDate, true);
		borrowRequestTest.fillFields(initialiseJSONOject, 0);
	}

	/**
	 * Test fillFields with no borrowFromDate
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void borrowRequestFillFieldsTestWithNoBorrowFromDate() throws WrongFormatException {
		initialiseJSONOject.remove(Constants.listingDataFieldBorrowFromDate);
		borrowRequestTest.fillFields(initialiseJSONOject, 0);
		assertEquals(null, borrowRequestTest.getBorrowFromDate());
	}

	// -------------------------------------- borrowToDate
	// ---------------------------------------

	/**
	 * Test fillFields with no borrowToDate
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void borrowRequestFillFieldsTestWithNoBorrowToDate() throws WrongFormatException {
		initialiseJSONOject.remove(Constants.listingDataFieldBorrowToDate);
		borrowRequestTest.fillFields(initialiseJSONOject, 0);
		assertEquals(null, borrowRequestTest.getBorrowToDate());
	}

	/**
	 * Test fillFields with wrong format in borrowToDate
	 * 
	 * @throws WrongFormatException
	 */
	@Test(expected = JSONException.class)
	public void borrowRequestFillFielsWithWrongFormat() throws WrongFormatException {
		initialiseJSONOject.remove(Constants.listingDataFieldBorrowToDate);
		initialiseJSONOject.put(Constants.listingDataFieldBorrowToDate, true);
		borrowRequestTest.fillFields(initialiseJSONOject, 0);
	}

	// -------------------------------------- borrowToDate
	// ---------------------------------------

	/**
	 * Test fillFields with no imageLocation
	 * 
	 * @throws WrongFormatException
	 */
	@Test
	public void borrowRequestFillFieldsTestWithNoPicture() throws WrongFormatException {
		initialiseJSONOject.remove(Constants.listingDataFieldPicture);
		borrowRequestTest.fillFields(initialiseJSONOject, 0);
		assertEquals(null, borrowRequestTest.getPicture());
	}

	/**
	 * Test fillFields with wrong imageLocation
	 * @throws WrongFormatException 
	 */
	@Test
	public void borrowFillFieldsTestWithWrongPicture() throws WrongFormatException {
		initialiseJSONOject.remove(Constants.listingDataFieldPicture);
		initialiseJSONOject.put(Constants.listingDataFieldPicture, 123);
		borrowRequestTest.fillFields(initialiseJSONOject, 0);
		assertNull(borrowRequestTest.getPicture());
	}

}
