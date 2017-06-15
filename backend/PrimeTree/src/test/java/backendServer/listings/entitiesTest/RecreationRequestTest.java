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
import backendServer.listings.entities.RecreationRequest;
import backendServer.listings.entities.ReturningRecreationRequest;
import backendServer.testStuff.TestExclusiveBeans;
import backendServer.userData.configuration.UserBeanCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes ={ ListingBeanCollection.class, TestExclusiveBeans.class,
		EmployeeBeanCollection.class, UserBeanCollection.class})
public class RecreationRequestTest {
	
	JSONObject JSONObjectWithCorrectValues, JSONObjectWithWrongFreeTimeActivityCategory;
	private long createOne, deadLine, startDate, endDate;
	RecreationRequest testListing;
	@Autowired
	Constants.LocationsLoader locationsLoader;
	
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
		
		JSONObjectWithWrongFreeTimeActivityCategory = new JSONObject();
		JSONObjectWithWrongFreeTimeActivityCategory.put(Constants.listingDataFieldId, 0);
		JSONObjectWithWrongFreeTimeActivityCategory.put(Constants.listingDataFieldListingType, Constants.listingTypeNameReturningRecreationRequest);
		JSONObjectWithWrongFreeTimeActivityCategory.put(Constants.listingDataFieldCreateDate, createOne);
		JSONObjectWithWrongFreeTimeActivityCategory.put(Constants.listingDataFieldDeadLine, deadLine);
		JSONObjectWithWrongFreeTimeActivityCategory.put(Constants.listingDataFieldCreator, 0);
		JSONObjectWithWrongFreeTimeActivityCategory.put(Constants.listingDataFieldDescription, "test");
		JSONObjectWithWrongFreeTimeActivityCategory.put(Constants.listingDataFieldDeadLine, 1);
		JSONObjectWithWrongFreeTimeActivityCategory.put(Constants.listingDataFieldLocation, "Koeln");
		JSONObjectWithWrongFreeTimeActivityCategory.put(Constants.listingDataFieldTitle, "test1");
		JSONObjectWithWrongFreeTimeActivityCategory.put(Constants.listingDataFieldPrice, 0);
		JSONObjectWithWrongFreeTimeActivityCategory.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		JSONObjectWithWrongFreeTimeActivityCategory.put(Constants.listingDataFieldStartDate, startDate);
		JSONObjectWithWrongFreeTimeActivityCategory.put(Constants.listingDataFieldEndDate, endDate);
		JSONObjectWithWrongFreeTimeActivityCategory.put(Constants.listingDataFieldFreeTimeActivityCategory, "falsch");
		JSONObjectWithWrongFreeTimeActivityCategory.put(Constants.listingDataFieldActivityLocation, "Mannheim");
		JSONObjectWithWrongFreeTimeActivityCategory.put(Constants.listingDataFieldRegularity, "Daily");
		
	}


	/**
	 * Test fillFields with all correct Values
	 */
	@Test
	public void recreationRequestFillFieldsTestWithCorrectValues(){
		testListing.fillFields(JSONObjectWithCorrectValues, 0);
		assertEquals(createOne,testListing.getCreateDate().getTime() );
		assertEquals("test", testListing.getDescription());
		assertEquals("Koeln", testListing.getLocation());
	}
	
	/**
	 * Test fillFields with Wrong freeTimeactivityCategory
	 */
	@Test(expected = WrongFormatException.class)
	public void recreationRequestFillFieldsTestWithWrongFreeTimeActivityCategory(){
		testListing.fillFields(JSONObjectWithWrongFreeTimeActivityCategory, 0);
	}
	

}
