package BackendServer.Listings.EntitiesTest;

import static org.junit.Assert.*;

import java.util.Date;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.Entities.ReturningRecreationRequest;
import BackendServer.Listings.Entities.SingleRecreationRequest;

public class SingleRecreatingRequestTest {


	long createDate, deadLine, startDate, endDate, happeningDate;
	JSONObject JSONObjectforTesting;
	SingleRecreationRequest testListing;
	
	@Before
	public void init(){
		testListing = new SingleRecreationRequest();
		createDate = new Date().getTime();
		deadLine = new Date().getTime()+20;
		startDate = new Date().getTime()+5;
		endDate = new Date().getTime()+10;
		happeningDate = new Date().getTime()+9;
		
		
		JSONObjectforTesting = new JSONObject();
		JSONObjectforTesting.put(Constants.listingDataFieldId, 0);
		JSONObjectforTesting.put(Constants.listingDataFieldCreateDate, createDate);
		JSONObjectforTesting.put(Constants.listingDataFieldDeadLine, deadLine);
		JSONObjectforTesting.put(Constants.listingDataFieldCreator, 0);
		JSONObjectforTesting.put(Constants.listingDataFieldDescription, "test");
		JSONObjectforTesting.put(Constants.listingDataFieldDeadLine, 1);
		JSONObjectforTesting.put(Constants.listingDataFieldLocation, "Koeln");
		JSONObjectforTesting.put(Constants.listingDataFieldTitle, "test1");
		JSONObjectforTesting.put(Constants.listingDataFieldPrice, 0);
		JSONObjectforTesting.put(Constants.listingDataFieldCondition, "new");
		JSONObjectforTesting.put(Constants.listingDataFieldStartDate, startDate);
		JSONObjectforTesting.put(Constants.listingDataFieldEndDate, endDate);
		JSONObjectforTesting.put(Constants.listingDataFieldFreeTimeActivityCategory, "Sport");
		JSONObjectforTesting.put(Constants.listingDataFieldActivityLocation, "Mannheim");
		JSONObjectforTesting.put(Constants.listingDataFieldRegularity, "Daily");
		JSONObjectforTesting.put(Constants.listingDataFieldHappeningDate, happeningDate);
	}
	
	/**
	 * Test fillFields with correctValues
	 */
	@Test
	public void SingleRecreationRequestFillFieldsTestWithCorrectValues(){
		testListing.fillFields(JSONObjectforTesting, 0);
		assertEquals(happeningDate, testListing.getHappeningDate().getTime());
	}
	
	/**
	 * Test fillFields with nor happenignDate
	 */
	@Test
	public void singleRecreationRequestFillFieldsTestWithNoHappeningDate(){
		JSONObjectforTesting.remove(Constants.listingDataFieldHappeningDate);
		testListing.fillFields(JSONObjectforTesting, 0);
		assertEquals(null, testListing.getHappeningDate());
	}

}
