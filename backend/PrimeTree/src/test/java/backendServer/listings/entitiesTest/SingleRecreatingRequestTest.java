package backendServer.listings.entitiesTest;

import static org.junit.Assert.*;

import java.util.Date;

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
import backendServer.listings.entities.ReturningRecreationRequest;
import backendServer.listings.entities.SingleRecreationRequest;
import backendServer.testStuff.TestExclusiveBeans;
import backendServer.userData.configuration.UserBeanCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes ={ ListingBeanCollection.class, TestExclusiveBeans.class,
		EmployeeBeanCollection.class, UserBeanCollection.class})
public class SingleRecreatingRequestTest {


	long createDate, deadLine, startDate, endDate, happeningDate;
	JSONObject JSONObjectforTesting;
	SingleRecreationRequest testListing;
	@Autowired
	Constants.LocationsLoader locationsLoader;
	
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
		JSONObjectforTesting.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSingleRecreationRequest);
		JSONObjectforTesting.put(Constants.listingDataFieldCreateDate, createDate);
		JSONObjectforTesting.put(Constants.listingDataFieldDeadLine, deadLine);
		JSONObjectforTesting.put(Constants.listingDataFieldCreator, 0);
		JSONObjectforTesting.put(Constants.listingDataFieldDescription, "test");
		JSONObjectforTesting.put(Constants.listingDataFieldDeadLine, 1);
		JSONObjectforTesting.put(Constants.listingDataFieldLocation, "Koeln");
		JSONObjectforTesting.put(Constants.listingDataFieldTitle, "test1");
		JSONObjectforTesting.put(Constants.listingDataFieldPrice, 0);
		JSONObjectforTesting.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
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
