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
import backendServer.listings.entities.PurchaseRequest;
import backendServer.listings.entities.RecreationRequest;
import backendServer.listings.entities.ReturningRecreationRequest;
import backendServer.testStuff.TestExclusiveBeans;
import backendServer.userData.configuration.UserBeanCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes ={ ListingBeanCollection.class, TestExclusiveBeans.class,
		EmployeeBeanCollection.class, UserBeanCollection.class})
public class PurchaseRequestTest {

	
	JSONObject JSONObjectWithCorrectValues, JSONObjectWithWrongCondition;
	private long createOne, deadLine, startDate, endDate;
	PurchaseRequest testListing;
	@Autowired
	Constants.LocationsLoader locationsLoader;
	
	@Before
	public void init(){
		testListing = new PurchaseRequest();
		createOne = new Date().getTime();
		deadLine = new Date().getTime()+20;
		startDate = new Date().getTime()+5;
		endDate = new Date().getTime()+10;
		
		
		JSONObjectWithCorrectValues = new JSONObject();
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldId, 0);
		JSONObjectWithCorrectValues.put(Constants.listingDataFieldListingType, Constants.listingTypeNamePurchaseRequest);
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
		
		JSONObjectWithWrongCondition = new JSONObject();
		JSONObjectWithWrongCondition.put(Constants.listingDataFieldId, 0);
		JSONObjectWithWrongCondition.put(Constants.listingDataFieldListingType, Constants.listingTypeNamePurchaseRequest);
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
		assertEquals(Constants.allItemConditions.get(0), testListing.getItemCondition());
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
