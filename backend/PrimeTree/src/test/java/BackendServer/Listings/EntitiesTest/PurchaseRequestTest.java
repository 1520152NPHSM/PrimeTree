package BackendServer.Listings.EntitiesTest;

import static org.junit.Assert.*;

import java.util.Date;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.Entities.PurchaseRequest;
import BackendServer.Listings.Entities.RecreationRequest;
import BackendServer.Listings.Entities.ReturningRecreationRequest;

public class PurchaseRequestTest {

	
	JSONObject JSONObjectWithCorrectValues, JSONObjectWithWrongCondition;
	private long createOne, deadLine, startDate, endDate;
	PurchaseRequest testListing;
	
	@Before
	public void init(){
		testListing = new PurchaseRequest();
		createOne = new Date().getTime();
		deadLine = new Date().getTime()+20;
		startDate = new Date().getTime()+5;
		endDate = new Date().getTime()+10;
		
		
		JSONObjectWithCorrectValues = new JSONObject();
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldId, 0);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldCreateDate, createOne);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldDeadLine, deadLine);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldCreator, 0);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldDescription, "test");
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldDeadLine, 1);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldLocation, "Koeln");
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldTitle, "test1");
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldPrice, 0);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldCondition, "new");
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldStartDate, startDate);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldEndDate, endDate);
		
		JSONObjectWithWrongCondition = new JSONObject();
		JSONObjectWithWrongCondition.put(Constants.listingDataFieldId, 0);
		JSONObjectWithWrongCondition.put(Constants.listingDataFieldCreateDate, createOne);
		JSONObjectWithWrongCondition.put(Constants.listingDataFieldDeadLine, deadLine);
		JSONObjectWithWrongCondition.put(Constants.listingDataFieldCreator, 0);
		JSONObjectWithWrongCondition.put(Constants.listingDataFieldDescription, "test");
		JSONObjectWithWrongCondition.put(Constants.listingDataFieldDeadLine, 1);
		JSONObjectWithWrongCondition.put(Constants.listingDataFieldLocation, "Koeln");
		JSONObjectWithWrongCondition.put(Constants.listingDataFieldTitle, "test1");
		JSONObjectWithWrongCondition.put(Constants.listingDataFieldPrice, 0);
		JSONObjectWithWrongCondition.put(Constants.listingDataFieldStartDate, startDate);
		JSONObjectWithWrongCondition.put(Constants.listingDataFieldEndDate, endDate);
		JSONObjectWithWrongCondition.put(Constants.listingDataFieldCondition, "falsch");
		
	}

	
	/**
	 * Test fillFields with correct values
	 */
	@Test
	public void purchaseRequestFillFieldsTestWithCorrectValues(){
		testListing.fillFields(JSONObjectWithCorrectValues, 0);
		assertEquals("new", testListing.getItemCondition());
	}

	/**
	 * Test fillFields with no condition
	 */
	@Test(expected = WrongFormatException.class)
	public void purchaseRequestFillFieldsTestWithNoCondition(){
		JSONObjectWithWrongCondition.remove(Constants.listingDataFieldCondition);
		testListing.fillFields(JSONObjectWithWrongCondition, 0);
	}
	
	/**
	 * Test fillFields with wrong condition
	 */
	@Test (expected = WrongFormatException.class)
	public void purchaseRequestFillFieldsWithWrongCondition(){
		testListing.fillFields(JSONObjectWithWrongCondition, 0);
	}
}
