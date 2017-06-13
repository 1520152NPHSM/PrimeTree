package backendServer.listingsTest;

import static org.junit.Assert.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import backendServer.clientDatabaseAccess.config.EmployeeBeanCollection;
import backendServer.exceptions.WrongFormatException;
import backendServer.listings.*;
import backendServer.listings.entities.Listing;
import backendServer.listings.entities.Offering;
import backendServer.listings.entities.SellItem;


public class ConstantsAndSimpleMethodsTest {
	
	Integer[] integerArray = new Integer[5];
	int[] intArray=new int[5];
	String[] stringArray = new String[5];
	JSONArray jSONArray = new JSONArray();
	
	Object[] objectArray = new Object[2];
	Listing[] listingArrayResult = new Listing[2];
	Listing[] listingArray = new Listing[2];
	
	Listing listing1 = new SellItem();
	Listing listing2 = new SellItem();
	
	Object object1;
	Object object2;

	
	JSONObject obj1 = new JSONObject();
	JSONObject obj2 = new JSONObject();


	@Test
	public void parseCastIntegerArrayToIntArray() {
		for(int i =0;i<integerArray.length;i++){
			integerArray[i]=i;
			intArray[i]=i;
		}
		assertArrayEquals(intArray, SimpleMethods.parseIntegerArrayToIntArray(integerArray));

	}
	
//	@Test
//	public void testparseJSONArrayToStringArray(){
//		for(int i=0;i<stringArray.length;i++){
//			stringArray[i] = i+"";
//			jSONArray.put(i+"");
//		}
//		assertArrayEquals(stringArray, SimpleMethods.parseJSONArrayToStringArray(jSONArray));
//	}
//	
	@Test
	public void testparseObjectArrayToListingArray(){
		fillJsonObjects();
		try {
			listing1.fillFields(obj1, 0);
			listing2.fillFields(obj2, 1);
		} catch (WrongFormatException e) {
			e.printStackTrace();
		}
		
		object1 = (Object) listing1;
		object2 = (Object) listing2;
		
		objectArray[0]=object1;
		objectArray[1]=object2;
		
		listingArray[0]=listing1;
		listingArray[1]=listing2;	
		
		listingArrayResult = SimpleMethods.parseObjectArrayToListingArray(objectArray);
		assertArrayEquals(listingArrayResult, listingArray);

	}
	
	
	
	public void fillJsonObjects(){
		obj1.put("id", 0);
		obj1.put("active", true);
		obj1.put("createDate", 1);
		obj1.put("creator", 0);
		obj1.put("description", "test");
		obj1.put("expiryDate", 2.9);
		obj1.put("location", "Koeln");
		obj1.put("mainImage", "Null");
		obj1.put("title", "test1");
		obj1.put("condition", "gut");
		obj1.put("price", 2);

		obj2.put("id", 0);
		obj2.put("active", true);
		obj2.put("createDate", 1);
		obj2.put("creator", 1);
		obj2.put("description", "test");
		obj2.put("expiryDate", 3.9);
		obj2.put("location", "Mannheim");
		obj2.put("mainImage", "Null");
		obj2.put("title", "test1");
		obj2.put("condition", "schlecht");
		obj2.put("price", 3);
	}
	
}
