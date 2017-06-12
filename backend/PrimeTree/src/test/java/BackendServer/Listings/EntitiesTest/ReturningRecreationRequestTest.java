package BackendServer.Listings.EntitiesTest;

import static org.junit.Assert.*;

import java.util.Date;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.Entities.RecreationRequest;
import BackendServer.Listings.Entities.ReturningRecreationRequest;

public class ReturningRecreationRequestTest {

	JSONObject JSONObjectWithCorrectValues, JSONObjectWithBadRegularity, JSONObjectWithNoRegularity,
	JSONObjectWithNoStartDate, JSONObjectWithNoEndDate;
	private long createOne, deadLine, startDate, endDate;
	ReturningRecreationRequest testListing;
	
	@Before
	public void init(){
		testListing = new ReturningRecreationRequest();
		createOne = new Date().getTime();
		deadLine = new Date().getTime()+20;
		startDate = new Date().getTime()+5;
		endDate = new Date().getTime()+10;
		
		
		JSONObjectWithCorrectValues = new JSONObject();
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldId, 0);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldListingType, Constants.listingTypeNameReturningRecreationRequest);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldCreateDate, createOne);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldDeadLine, deadLine);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldCreator, 0);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldDescription, "test");
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldDeadLine, 1);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldLocation, "Koeln");
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldTitle, "test1");
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldPrice, 0);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldStartDate, startDate);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldEndDate, endDate);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldFreeTimeActivityCategory, "Sport");
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldActivityLocation, "Mannheim");
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldRegularity, "Daily");
		
		JSONObjectWithBadRegularity = new JSONObject();
		JSONObjectWithBadRegularity.put(Constants.listingDataFieldListingType, Constants.listingTypeNameReturningRecreationRequest);
		JSONObjectWithBadRegularity.put(Constants.listingDataFieldId, 0);
		JSONObjectWithBadRegularity.put(Constants.listingDataFieldCreateDate, createOne);
		JSONObjectWithBadRegularity.put(Constants.listingDataFieldDeadLine, deadLine);
		JSONObjectWithBadRegularity.put(Constants.listingDataFieldCreator, 0);
		JSONObjectWithBadRegularity.put(Constants.listingDataFieldDescription, "test");
		JSONObjectWithBadRegularity.put(Constants.listingDataFieldDeadLine, 1);
		JSONObjectWithBadRegularity.put(Constants.listingDataFieldLocation, "Koeln");
		JSONObjectWithBadRegularity.put(Constants.listingDataFieldTitle, "test1");
		JSONObjectWithBadRegularity.put(Constants.listingDataFieldPrice, 0);
		JSONObjectWithBadRegularity.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		JSONObjectWithBadRegularity.put(Constants.listingDataFieldStartDate, startDate);
		JSONObjectWithBadRegularity.put(Constants.listingDataFieldEndDate, endDate);
		JSONObjectWithBadRegularity.put(Constants.listingDataFieldFreeTimeActivityCategory, "Sport");
		JSONObjectWithBadRegularity.put(Constants.listingDataFieldActivityLocation, "Mannheim");
		JSONObjectWithBadRegularity.put(Constants.listingDataFieldRegularity, "falsch");
		
		JSONObjectWithNoStartDate = new JSONObject();
		JSONObjectWithNoStartDate.put(Constants.listingDataFieldId, 0);
		JSONObjectWithNoStartDate.put(Constants.listingDataFieldListingType, Constants.listingTypeNameReturningRecreationRequest);
		JSONObjectWithNoStartDate.put(Constants.listingDataFieldCreateDate, createOne);
		JSONObjectWithNoStartDate.put(Constants.listingDataFieldDeadLine, deadLine);
		JSONObjectWithNoStartDate.put(Constants.listingDataFieldCreator, 0);
		JSONObjectWithNoStartDate.put(Constants.listingDataFieldDescription, "test");
		JSONObjectWithNoStartDate.put(Constants.listingDataFieldDeadLine, 1);
		JSONObjectWithNoStartDate.put(Constants.listingDataFieldLocation, "Koeln");
		JSONObjectWithNoStartDate.put(Constants.listingDataFieldTitle, "test1");
		JSONObjectWithNoStartDate.put(Constants.listingDataFieldPrice, 0);
		JSONObjectWithNoStartDate.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		JSONObjectWithNoStartDate.put(Constants.listingDataFieldEndDate, endDate);
		JSONObjectWithNoStartDate.put(Constants.listingDataFieldFreeTimeActivityCategory, "Sport");
		JSONObjectWithNoStartDate.put(Constants.listingDataFieldActivityLocation, "Mannheim");
		JSONObjectWithNoStartDate.put(Constants.listingDataFieldRegularity, "Daily");
		
		JSONObjectWithNoEndDate = new JSONObject();
		JSONObjectWithNoEndDate.put(Constants.listingDataFieldId, 0);
		JSONObjectWithNoEndDate.put(Constants.listingDataFieldListingType, Constants.listingTypeNameReturningRecreationRequest);
		JSONObjectWithNoEndDate.put(Constants.listingDataFieldCreateDate, createOne);
		JSONObjectWithNoEndDate.put(Constants.listingDataFieldDeadLine, deadLine);
		JSONObjectWithNoEndDate.put(Constants.listingDataFieldCreator, 0);
		JSONObjectWithNoEndDate.put(Constants.listingDataFieldDescription, "test");
		JSONObjectWithNoEndDate.put(Constants.listingDataFieldDeadLine, 1);
		JSONObjectWithNoEndDate.put(Constants.listingDataFieldLocation, "Koeln");
		JSONObjectWithNoEndDate.put(Constants.listingDataFieldTitle, "test1");
		JSONObjectWithNoEndDate.put(Constants.listingDataFieldPrice, 0);
		JSONObjectWithNoEndDate.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		JSONObjectWithNoEndDate.put(Constants.listingDataFieldStartDate, startDate);
		JSONObjectWithNoEndDate.put(Constants.listingDataFieldFreeTimeActivityCategory, "Sport");
		JSONObjectWithNoEndDate.put(Constants.listingDataFieldActivityLocation, "Mannheim");
		JSONObjectWithNoEndDate.put(Constants.listingDataFieldRegularity, "Daily");
		
		JSONObjectWithNoRegularity = new JSONObject();
		JSONObjectWithNoRegularity.put(Constants.listingDataFieldListingType, Constants.listingTypeNameReturningRecreationRequest);
		JSONObjectWithNoRegularity.put(Constants.listingDataFieldId, 0);
		JSONObjectWithNoRegularity.put(Constants.listingDataFieldCreateDate, createOne);
		JSONObjectWithNoRegularity.put(Constants.listingDataFieldDeadLine, deadLine);
		JSONObjectWithNoRegularity.put(Constants.listingDataFieldCreator, 0);
		JSONObjectWithNoRegularity.put(Constants.listingDataFieldDescription, "test");
		JSONObjectWithNoRegularity.put(Constants.listingDataFieldDeadLine, 1);
		JSONObjectWithNoRegularity.put(Constants.listingDataFieldLocation, "Koeln");
		JSONObjectWithNoRegularity.put(Constants.listingDataFieldTitle, "test1");
		JSONObjectWithNoRegularity.put(Constants.listingDataFieldPrice, 0);
		JSONObjectWithNoRegularity.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		JSONObjectWithNoRegularity.put(Constants.listingDataFieldStartDate, startDate);
		JSONObjectWithNoRegularity.put(Constants.listingDataFieldEndDate, endDate);
		JSONObjectWithNoRegularity.put(Constants.listingDataFieldFreeTimeActivityCategory, "Sport");
		JSONObjectWithNoRegularity.put(Constants.listingDataFieldActivityLocation, "Mannheim");
	}
	
	
	@Test
	public void ReturningRecreationRequestFillFieldsTestWithCorrectValues(){
		testListing.fillFields(JSONObjectWithCorrectValues, 0);
		assertEquals("Koeln", testListing.getLocation());
		assertEquals("Daily", testListing.getRegularity());
		assertEquals(startDate, testListing.getStartDate().getTime());
		assertEquals(endDate, testListing.getEndDate().getTime());
	}
	
	/**
	 * Test fillFields with wrong regularity
	 */
	@Test(expected = WrongFormatException.class)
	public void returningRecreationRequestFillFieldsTestWithBadRegularity(){
		testListing.fillFields(JSONObjectWithBadRegularity, 0);
	}
	
	/**
	 * Test fillFields with no regularity
	 */
	@Test(expected = WrongFormatException.class)
	public void returningRecreationRequestFillFieldsTestWithNoRegularity(){
		testListing.fillFields(JSONObjectWithNoRegularity, 0);
	}
	
	/**
	 * Test fillFields with no startDate
	 */
	@Test
	public void returningRecreationRequestFillFieldsTestWithNoStartDate(){
		testListing.fillFields(JSONObjectWithNoStartDate, 0);
		assertEquals(endDate, testListing.getEndDate().getTime());
		assertEquals(null, testListing.getStartDate());
	}
	
	/**
	 * Test fillFields with no endDate
	 */
	@Test
	public void returningRecreationRequestFillFieldsTestWithNoEndDate(){
		testListing.fillFields(JSONObjectWithNoEndDate, 0);
		assertEquals(null, testListing.getEndDate());
	}

}
