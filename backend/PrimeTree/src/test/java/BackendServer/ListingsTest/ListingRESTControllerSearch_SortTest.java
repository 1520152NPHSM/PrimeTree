package BackendServer.ListingsTest;

import static org.junit.Assert.*;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import BackendServer.ClientDatabaseAccess.Config.EmployeeBeanCollection;
import BackendServer.Listings.Constants;
import BackendServer.Listings.ListingBeanCollection;
import BackendServer.Listings.ListingRESTController;
import BackendServer.TestStuff.TestExclusiveBeans;
import BackendServer.UserData.Configuration.UserBeanCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { ListingBeanCollection.class, TestExclusiveBeans.class,
		EmployeeBeanCollection.class, UserBeanCollection.class })
public class ListingRESTControllerSearch_SortTest {

	@Autowired
	private ListingRESTController testRESTController;

	private SecurityContext secCon;
	private Authentication auth;
	long createDate, expiryDate, travelDateAndTime, fromDate, toDate;

	private JSONObject listing1, listing2, listing3, listing4, listing5, listing6, listing7, listing8, listing9,
			listing10, listing11, listing12, listing13, listing14, listing15, listing16, listing17, listing18,
			listing19, listing20, listing21, listing22, listing23, listing24, listing25, listing26, listing27,
			listing28, listing29, listing30, listing31, listing32, listing33, listing34, listing35, listing36,
			listing37, listing38, listing39, listing40, listing41, listing42, listing43, listing44, listing45,
			listing46, listing47, listing48, listing49, listing50, listing51;

	private String stringListing1, stringListing2, stringListing3, stringListing4, stringListing5, stringListing6,
			stringListing7, stringListing8, stringListing9, stringListing10, stringListing11, stringListing12,
			stringListing13, stringListing14, stringListing15, stringListing16, stringListing17, stringListing18,
			stringListing19, stringListing20, stringListing21, stringListing22, stringListing23, stringListing24,
			stringListing25, stringListing26, stringListing27, stringListing28, stringListing29, stringListing30,
			stringListing31, stringListing32, stringListing33, stringListing34, stringListing35, stringListing36,
			stringListing37, stringListing38, stringListing39, stringListing40, stringListing41, stringListing42,
			stringListing43, stringListing44, stringListing45, stringListing46, stringListing47, stringListing48,
			stringListing49, stringListing50, stringListing51;

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@Before
	public void setup() {
		createDate = new Date().getTime();
		expiryDate = new Date().getTime();
		travelDateAndTime = new Date().getTime();
		fromDate = new Date().getTime();
		toDate = new Date().getTime();

		secCon = new SecurityContextImpl();
		// login akessler as admin
		auth = new TestingAuthenticationToken("akessler", "123", "ADMIN");
		secCon.setAuthentication(auth);
		SecurityContextHolder.setContext(secCon);
		request = new MockHttpServletRequest("POST", "listing");
		response = new MockHttpServletResponse();
		response.setStatus(HttpServletResponse.SC_OK);

		// -----------------------SaleOffer------------------------------------------

		listing1 = new JSONObject();
		listing1.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listing1.put(Constants.listingDataFieldTitle, "Fahrrad");
		listing1.put(Constants.listingDataFieldDescription, "Ein neues Rad");
		listing1.put(Constants.listingDataFieldLocation, "Mannheim");
		listing1.put(Constants.listingDataFieldCreateDate, createDate);
		listing1.put(Constants.listingDataFieldDeadLine, expiryDate + 1000);
		listing1.put(Constants.listingDataFieldPrice, 150);
		listing1.put(Constants.listingDataFieldCondition, "used");

		listing2 = new JSONObject();
		listing2.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listing2.put(Constants.listingDataFieldTitle, "Topf");
		listing2.put(Constants.listingDataFieldDescription, "Gebrauchter Topf");
		listing2.put(Constants.listingDataFieldLocation, "Mannheim");
		listing2.put(Constants.listingDataFieldCreateDate, createDate);
		listing2.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing2.put(Constants.listingDataFieldPrice, 21.99);
		listing2.put(Constants.listingDataFieldCondition, "used");

		listing3 = new JSONObject();
		listing3.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listing3.put(Constants.listingDataFieldTitle, "Hemd");
		listing3.put(Constants.listingDataFieldDescription, "Hemd");
		listing3.put(Constants.listingDataFieldLocation, "Mannheim");
		listing3.put(Constants.listingDataFieldCreateDate, createDate + 10);
		listing3.put(Constants.listingDataFieldDeadLine, expiryDate + 120);
		listing3.put(Constants.listingDataFieldPrice, 27.99);
		listing3.put(Constants.listingDataFieldCondition, "new");

		listing4 = new JSONObject();
		listing4.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listing4.put(Constants.listingDataFieldTitle, "Hund");
		listing4.put(Constants.listingDataFieldDescription, "alter Hund");
		listing4.put(Constants.listingDataFieldLocation, "Mannheim");
		listing4.put(Constants.listingDataFieldCreateDate, createDate);
		listing4.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing4.put(Constants.listingDataFieldPrice, 10378.99);
		listing4.put(Constants.listingDataFieldCondition, "used");

		listing5 = new JSONObject();
		listing5.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listing5.put(Constants.listingDataFieldTitle, "Katze");
		listing5.put(Constants.listingDataFieldDescription, "Baby Katze");
		listing5.put(Constants.listingDataFieldLocation, "Frankfurt");
		listing5.put(Constants.listingDataFieldCreateDate, createDate);
		listing5.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing5.put(Constants.listingDataFieldPrice, 12);
		listing5.put(Constants.listingDataFieldCondition, "new");

		listing6 = new JSONObject();
		listing6.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listing6.put(Constants.listingDataFieldTitle, "Topf");
		listing6.put(Constants.listingDataFieldDescription, "Gebrauchter Topf");
		listing6.put(Constants.listingDataFieldLocation, "Stuttgart");
		listing6.put(Constants.listingDataFieldCreateDate, createDate);
		listing6.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing6.put(Constants.listingDataFieldPrice, 25.99);
		listing6.put(Constants.listingDataFieldCondition, "new");

		listing7 = new JSONObject();
		listing7.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listing7.put(Constants.listingDataFieldTitle, "Topf");
		listing7.put(Constants.listingDataFieldDescription, "Gebrauchter Topf");
		listing7.put(Constants.listingDataFieldLocation, "Karlsruhe");
		listing7.put(Constants.listingDataFieldCreateDate, createDate);
		listing7.put(Constants.listingDataFieldDeadLine, expiryDate + 120);
		listing7.put(Constants.listingDataFieldPrice, 21.99);
		listing7.put(Constants.listingDataFieldCondition, "used");

		listing8 = new JSONObject();
		listing8.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listing8.put(Constants.listingDataFieldTitle, "Bürste");
		listing8.put(Constants.listingDataFieldDescription, "Neue Bürste");
		listing8.put(Constants.listingDataFieldLocation, "Mannheim");
		listing8.put(Constants.listingDataFieldCreateDate, createDate);
		listing8.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing8.put(Constants.listingDataFieldPrice, 1.99);
		listing8.put(Constants.listingDataFieldCondition, "used");
		
		//------------------------------------------------- RideSharing-----------------------------------------------

		listing9 = new JSONObject();
		listing9.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
		listing9.put(Constants.listingDataFieldTitle, "Einfache Fahrt");
		listing9.put(Constants.listingDataFieldDescription, "Mannheim nach Köln");
		listing9.put(Constants.listingDataFieldLocation, "Mannheim");
		listing9.put(Constants.listingDataFieldCreateDate, createDate);
		listing9.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing9.put(Constants.listingDataFieldFromLocation, "Mannheim");
		listing9.put(Constants.listingDataFieldToLocation, "Köln");
		listing9.put(Constants.listingDataFieldTravelDateAndTime, travelDateAndTime + 700);

		listing10 = new JSONObject();
		listing10.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
		listing10.put(Constants.listingDataFieldTitle, "Einfache Fahrt");
		listing10.put(Constants.listingDataFieldDescription, "München nach Karlsi");
		listing10.put(Constants.listingDataFieldLocation, "München");
		listing10.put(Constants.listingDataFieldCreateDate, createDate);
		listing10.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing10.put(Constants.listingDataFieldFromLocation, "München");
		listing10.put(Constants.listingDataFieldToLocation, "Karlsruhe");
		listing10.put(Constants.listingDataFieldTravelDateAndTime, travelDateAndTime + 100);

		listing11 = new JSONObject();
		listing11.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
		listing11.put(Constants.listingDataFieldTitle, "Einfache Fahrt");
		listing11.put(Constants.listingDataFieldDescription, "München nach Köln");
		listing11.put(Constants.listingDataFieldLocation, "München");
		listing11.put(Constants.listingDataFieldCreateDate, createDate);
		listing11.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing11.put(Constants.listingDataFieldFromLocation, "München");
		listing11.put(Constants.listingDataFieldToLocation, "Köln");
		listing11.put(Constants.listingDataFieldTravelDateAndTime, travelDateAndTime + 900);

		listing12 = new JSONObject();
		listing12.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
		listing12.put(Constants.listingDataFieldTitle, "Einfache Fahrt");
		listing12.put(Constants.listingDataFieldDescription, "Köln nach München");
		listing12.put(Constants.listingDataFieldLocation, "Köln");
		listing12.put(Constants.listingDataFieldCreateDate, createDate);
		listing12.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing12.put(Constants.listingDataFieldFromLocation, "Köln");
		listing12.put(Constants.listingDataFieldToLocation, "München");
		listing12.put(Constants.listingDataFieldTravelDateAndTime, travelDateAndTime + 900);

		listing13 = new JSONObject();
		listing13.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
		listing13.put(Constants.listingDataFieldTitle, "Einfache Fahrt");
		listing13.put(Constants.listingDataFieldDescription, "Köln nach Mannheim");
		listing13.put(Constants.listingDataFieldLocation, "Köln");
		listing13.put(Constants.listingDataFieldCreateDate, createDate);
		listing13.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing13.put(Constants.listingDataFieldFromLocation, "Köln");
		listing13.put(Constants.listingDataFieldToLocation, "Mannheim");
		listing13.put(Constants.listingDataFieldTravelDateAndTime, travelDateAndTime + 900);

		listing14 = new JSONObject();
		listing14.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
		listing14.put(Constants.listingDataFieldTitle, "Einfache Fahrt");
		listing14.put(Constants.listingDataFieldDescription, "München nach Mannheim");
		listing14.put(Constants.listingDataFieldLocation, "München");
		listing14.put(Constants.listingDataFieldCreateDate, createDate);
		listing14.put(Constants.listingDataFieldDeadLine, expiryDate + 12010);
		listing14.put(Constants.listingDataFieldFromLocation, "München");
		listing14.put(Constants.listingDataFieldToLocation, "Mannheim");
		listing14.put(Constants.listingDataFieldTravelDateAndTime, travelDateAndTime + 9010);

		listing15 = new JSONObject();
		listing15.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
		listing15.put(Constants.listingDataFieldTitle, "Einfache Fahrt");
		listing15.put(Constants.listingDataFieldDescription, "München nach Stuttgart");
		listing15.put(Constants.listingDataFieldLocation, "München");
		listing15.put(Constants.listingDataFieldCreateDate, createDate);
		listing15.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing15.put(Constants.listingDataFieldFromLocation, "München");
		listing15.put(Constants.listingDataFieldToLocation, "Stuttgart");
		listing15.put(Constants.listingDataFieldTravelDateAndTime, travelDateAndTime + 900);

		listing16 = new JSONObject();
		listing16.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
		listing16.put(Constants.listingDataFieldTitle, "Einfache Fahrt");
		listing16.put(Constants.listingDataFieldDescription, "Stuttgart nach München");
		listing16.put(Constants.listingDataFieldLocation, "Stuttgard");
		listing16.put(Constants.listingDataFieldCreateDate, createDate);
		listing16.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing16.put(Constants.listingDataFieldFromLocation, "Stuttgard");
		listing16.put(Constants.listingDataFieldToLocation, "München");
		listing16.put(Constants.listingDataFieldTravelDateAndTime, travelDateAndTime + 900);
		
		//------------------------------------------------ServiceOffering--------------------------------------------

		listing17 = new JSONObject();
		listing17.put(Constants.listingDataFieldListingType, Constants.listingTypeNameServiceOffering);
		listing17.put(Constants.listingDataFieldTitle, "Rasenmähen");
		listing17.put(Constants.listingDataFieldDescription, "Mein Sohn mäht rasen für Geld");
		listing17.put(Constants.listingDataFieldLocation, "München");
		listing17.put(Constants.listingDataFieldCreateDate, createDate);
		listing17.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing17.put(Constants.listingDataFieldPrice, 200.10);

		listing18 = new JSONObject();
		listing18.put(Constants.listingDataFieldListingType, Constants.listingTypeNameServiceOffering);
		listing18.put(Constants.listingDataFieldTitle, "Rasenmähen");
		listing18.put(Constants.listingDataFieldDescription, "Mein Sohn mäht rasen für Geld");
		listing18.put(Constants.listingDataFieldLocation, "Mannheim");
		listing18.put(Constants.listingDataFieldCreateDate, createDate);
		listing18.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing18.put(Constants.listingDataFieldPrice, 200.10);

		listing19 = new JSONObject();
		listing19.put(Constants.listingDataFieldListingType, Constants.listingTypeNameServiceOffering);
		listing19.put(Constants.listingDataFieldTitle, "Rasenmähen");
		listing19.put(Constants.listingDataFieldDescription, "Mein Sohn mäht rasen für Geld");
		listing19.put(Constants.listingDataFieldLocation, "Köln");
		listing19.put(Constants.listingDataFieldCreateDate, createDate);
		listing19.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing19.put(Constants.listingDataFieldPrice, 200.10);

		listing20 = new JSONObject();
		listing20.put(Constants.listingDataFieldListingType, Constants.listingTypeNameServiceOffering);
		listing20.put(Constants.listingDataFieldTitle, "Rasenmähen");
		listing20.put(Constants.listingDataFieldDescription, "Mein Sohn mäht rasen für Geld");
		listing20.put(Constants.listingDataFieldLocation, "Stuttgard");
		listing20.put(Constants.listingDataFieldCreateDate, createDate);
		listing20.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing20.put(Constants.listingDataFieldPrice, 200.10);

		listing21 = new JSONObject();
		listing21.put(Constants.listingDataFieldListingType, Constants.listingTypeNameServiceOffering);
		listing21.put(Constants.listingDataFieldTitle, "Steuererklärung");
		listing21.put(Constants.listingDataFieldDescription, "Ich mache eure Steuererklärung");
		listing21.put(Constants.listingDataFieldLocation, "München");
		listing21.put(Constants.listingDataFieldCreateDate, createDate);
		listing21.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing21.put(Constants.listingDataFieldPrice, 9.99);

		listing22 = new JSONObject();
		listing22.put(Constants.listingDataFieldListingType, Constants.listingTypeNameServiceOffering);
		listing22.put(Constants.listingDataFieldTitle, "Java Kurs");
		listing22.put(Constants.listingDataFieldDescription, "Ihr seid schlecht in Java? Ich kann es euch bebringen");
		listing22.put(Constants.listingDataFieldLocation, "Mannheim");
		listing22.put(Constants.listingDataFieldCreateDate, createDate);
		listing22.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing22.put(Constants.listingDataFieldPrice, 999);

		listing23 = new JSONObject();
		listing23.put(Constants.listingDataFieldListingType, Constants.listingTypeNameServiceOffering);
		listing23.put(Constants.listingDataFieldTitle, "Steuererklärung");
		listing23.put(Constants.listingDataFieldDescription, "Ich mache eure Steuererklärung");
		listing23.put(Constants.listingDataFieldLocation, "Mannheim");
		listing23.put(Constants.listingDataFieldCreateDate, createDate);
		listing23.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing23.put(Constants.listingDataFieldPrice, 20.10);

		listing24 = new JSONObject();
		listing24.put(Constants.listingDataFieldListingType, Constants.listingTypeNameServiceOffering);
		listing24.put(Constants.listingDataFieldTitle, "Pc bauen");
		listing24.put(Constants.listingDataFieldDescription, "ich baue euch euren Pc zusammen");
		listing24.put(Constants.listingDataFieldLocation, "");
		listing24.put(Constants.listingDataFieldCreateDate, createDate);
		listing24.put(Constants.listingDataFieldDeadLine, expiryDate + 1200);
		listing24.put(Constants.listingDataFieldPrice, 79);
		
		//-----------------------------------------RideShareRequest ---------------------------------------

		listing25 = new JSONObject();
		listing25.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideShareRequest);
		listing25.put(Constants.listingDataFieldTitle, "Köln Mannheim");
		listing25.put(Constants.listingDataFieldDescription,
				"Ich suche eine Mitfarhgelegenheit von Köln nach Mannheim.");
		listing25.put(Constants.listingDataFieldActivityLocation, "Köln");
		listing25.put(Constants.listingDataFieldCreateDate, createDate);
		listing25.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing25.put(Constants.listingDataFieldFromLocation, "Köln");
		listing25.put(Constants.listingDataFieldToLocation, "Mannheim");

		listing26 = new JSONObject();
		listing26.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideShareRequest);
		listing26.put(Constants.listingDataFieldTitle, "Köln Stuttgard");
		listing26.put(Constants.listingDataFieldDescription,
				"Ich suche eine Mitfarhgelegenheit von Köln nach Mannheim.");
		listing26.put(Constants.listingDataFieldActivityLocation, "Köln");
		listing26.put(Constants.listingDataFieldCreateDate, createDate);
		listing26.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing26.put(Constants.listingDataFieldFromLocation, "Köln");
		listing26.put(Constants.listingDataFieldToLocation, "Stuttgard");

		listing27 = new JSONObject();
		listing27.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideShareRequest);
		listing27.put(Constants.listingDataFieldTitle, "Köln München");
		listing27.put(Constants.listingDataFieldDescription,
				"Ich suche eine Mitfarhgelegenheit von Köln nach Mannheim.");
		listing27.put(Constants.listingDataFieldActivityLocation, "Köln");
		listing27.put(Constants.listingDataFieldCreateDate, createDate);
		listing27.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing27.put(Constants.listingDataFieldFromLocation, "Köln");
		listing27.put(Constants.listingDataFieldToLocation, "München");

		listing28 = new JSONObject();
		listing28.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideShareRequest);
		listing28.put(Constants.listingDataFieldTitle, "Köln Karlsruhe");
		listing28.put(Constants.listingDataFieldDescription,
				"Ich suche eine Mitfarhgelegenheit von Köln nach Mannheim.");
		listing28.put(Constants.listingDataFieldActivityLocation, "Köln");
		listing28.put(Constants.listingDataFieldCreateDate, createDate);
		listing28.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing28.put(Constants.listingDataFieldFromLocation, "Köln");
		listing28.put(Constants.listingDataFieldToLocation, "Karlsruhe");

		listing29 = new JSONObject();
		listing29.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideShareRequest);
		listing29.put(Constants.listingDataFieldTitle, "Mannheim Karlsruhe");
		listing29.put(Constants.listingDataFieldDescription,
				"Ich suche eine Mitfarhgelegenheit von Mannheim nach Karlsruhe.");
		listing29.put(Constants.listingDataFieldActivityLocation, "Mannheim");
		listing29.put(Constants.listingDataFieldCreateDate, createDate);
		listing29.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing29.put(Constants.listingDataFieldFromLocation, "Mannheim");
		listing29.put(Constants.listingDataFieldToLocation, "Karlsruhe");

		listing30 = new JSONObject();
		listing30.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideShareRequest);
		listing30.put(Constants.listingDataFieldTitle, "Karlsruhe Mannheim");
		listing30.put(Constants.listingDataFieldDescription,
				"Ich suche eine Mitfarhgelegenheit von Karlsruhe nach Mannheim.");
		listing30.put(Constants.listingDataFieldActivityLocation, "Karlsruhe");
		listing30.put(Constants.listingDataFieldCreateDate, createDate);
		listing30.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing30.put(Constants.listingDataFieldFromLocation, "Karlsruhe");
		listing30.put(Constants.listingDataFieldToLocation, "Mannheim");

		listing31 = new JSONObject();
		listing31.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideShareRequest);
		listing31.put(Constants.listingDataFieldTitle, "München Mannheim");
		listing31.put(Constants.listingDataFieldDescription,
				"Ich suche eine Mitfarhgelegenheit von München nach Mannheim.");
		listing31.put(Constants.listingDataFieldActivityLocation, "München");
		listing31.put(Constants.listingDataFieldCreateDate, createDate);
		listing31.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing31.put(Constants.listingDataFieldFromLocation, "München");
		listing31.put(Constants.listingDataFieldToLocation, "Mannheim");

		listing32 = new JSONObject();
		listing32.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideShareRequest);
		listing32.put(Constants.listingDataFieldTitle, "Karlsruhe München");
		listing32.put(Constants.listingDataFieldDescription,
				"Ich suche eine Mitfarhgelegenheit von Karlsruhe nach München.");
		listing32.put(Constants.listingDataFieldActivityLocation, "Karlsruhe");
		listing32.put(Constants.listingDataFieldCreateDate, createDate);
		listing32.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing32.put(Constants.listingDataFieldFromLocation, "Karlsruhe");
		listing32.put(Constants.listingDataFieldToLocation, "München");

		// ----------------------------PurchaseRequests------------------------------------

		listing33 = new JSONObject();
		listing33.put(Constants.listingDataFieldListingType, Constants.listingTypeNamePurchaseRequest);
		listing33.put(Constants.listingDataFieldTitle, "Suche Fernseher");
		listing33.put(Constants.listingDataFieldDescription, "Ich suche einen neuen Fernseher.");
		listing33.put(Constants.listingDataFieldActivityLocation, "Karlsruhe");
		listing33.put(Constants.listingDataFieldCreateDate, createDate);
		listing33.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing33.put(Constants.listingDataFieldCondition, "new");

		listing34 = new JSONObject();
		listing34.put(Constants.listingDataFieldListingType, Constants.listingTypeNamePurchaseRequest);
		listing34.put(Constants.listingDataFieldTitle, "Suche Fernseher");
		listing34.put(Constants.listingDataFieldDescription, "Ich suche einen neuen Fernseher.");
		listing34.put(Constants.listingDataFieldActivityLocation, "München");
		listing34.put(Constants.listingDataFieldCreateDate, createDate);
		listing34.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing34.put(Constants.listingDataFieldCondition, "new");
		listing34 = new JSONObject();

		listing35.put(Constants.listingDataFieldListingType, Constants.listingTypeNamePurchaseRequest);
		listing35.put(Constants.listingDataFieldTitle, "Suche Fernseher");
		listing35.put(Constants.listingDataFieldDescription, "Ich suche einen neuen Fernseher.");
		listing35.put(Constants.listingDataFieldActivityLocation, "Mannheim");
		listing35.put(Constants.listingDataFieldCreateDate, createDate);
		listing35.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing35.put(Constants.listingDataFieldCondition, "new");

		listing36 = new JSONObject();
		listing36.put(Constants.listingDataFieldListingType, Constants.listingTypeNamePurchaseRequest);
		listing36.put(Constants.listingDataFieldTitle, "Suche Hund");
		listing36.put(Constants.listingDataFieldDescription, "Ich suche einen neuen Hund.");
		listing36.put(Constants.listingDataFieldActivityLocation, "Karlsruhe");
		listing36.put(Constants.listingDataFieldCreateDate, createDate);
		listing36.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing36.put(Constants.listingDataFieldCondition, "used");

		listing37 = new JSONObject();
		listing37.put(Constants.listingDataFieldListingType, Constants.listingTypeNamePurchaseRequest);
		listing37.put(Constants.listingDataFieldTitle, "Suche Fernseher");
		listing37.put(Constants.listingDataFieldDescription, "Ich suche einen neuen Fernseher.");
		listing37.put(Constants.listingDataFieldActivityLocation, "Karlsruhe");
		listing37.put(Constants.listingDataFieldCreateDate, createDate);
		listing37.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing37.put(Constants.listingDataFieldCondition, "new");

		listing38 = new JSONObject();
		listing38.put(Constants.listingDataFieldListingType, Constants.listingTypeNamePurchaseRequest);
		listing38.put(Constants.listingDataFieldTitle, "Suche Fernseher");
		listing38.put(Constants.listingDataFieldDescription, "Ich suche einen gebrauchten Fernseher.");
		listing38.put(Constants.listingDataFieldActivityLocation, "Karlsruhe");
		listing38.put(Constants.listingDataFieldCreateDate, createDate);
		listing38.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing38.put(Constants.listingDataFieldCondition, "used");

		listing39 = new JSONObject();
		listing39.put(Constants.listingDataFieldListingType, Constants.listingTypeNamePurchaseRequest);
		listing39.put(Constants.listingDataFieldTitle, "Suche Kinderspielzeug");
		listing39.put(Constants.listingDataFieldDescription, "Ich suche Kinderspielzeug.");
		listing39.put(Constants.listingDataFieldActivityLocation, "Köln");
		listing39.put(Constants.listingDataFieldCreateDate, createDate);
		listing39.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing39.put(Constants.listingDataFieldCondition, "used");

		listing40 = new JSONObject();
		listing40.put(Constants.listingDataFieldListingType, Constants.listingTypeNamePurchaseRequest);
		listing40.put(Constants.listingDataFieldTitle, "Suche Fernseher");
		listing40.put(Constants.listingDataFieldDescription, "Ich suche einen neuen Fernseher.");
		listing40.put(Constants.listingDataFieldActivityLocation, "Köln");
		listing40.put(Constants.listingDataFieldCreateDate, createDate);
		listing40.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing40.put(Constants.listingDataFieldCondition, "used");

		// --------------------------------
		// BorrowRequest------------------------#

		listing41 = new JSONObject();
		listing41.put(Constants.listingDataFieldListingType, Constants.listingTypeNameBorrowRequest);
		listing41.put(Constants.listingDataFieldTitle, "Auto");
		listing41.put(Constants.listingDataFieldDescription, "Ich möchte mir ein Auto leihen.");
		listing41.put(Constants.listingDataFieldActivityLocation, "Köln");
		listing41.put(Constants.listingDataFieldCreateDate, createDate);
		listing41.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing41.put(Constants.listingDataFieldBorrowFromDate, fromDate + 200);
		listing41.put(Constants.listingDataFieldBorrowToDate, toDate + 1000);

		listing42 = new JSONObject();
		listing42.put(Constants.listingDataFieldListingType, Constants.listingTypeNameBorrowRequest);
		listing42.put(Constants.listingDataFieldTitle, "Auto");
		listing42.put(Constants.listingDataFieldDescription, "Ich möchte mir ein Auto leihen.");
		listing42.put(Constants.listingDataFieldActivityLocation, "Mannheim");
		listing42.put(Constants.listingDataFieldCreateDate, createDate);
		listing42.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing42.put(Constants.listingDataFieldBorrowFromDate, fromDate + 200);
		listing42.put(Constants.listingDataFieldBorrowToDate, toDate + 1000);

		listing43 = new JSONObject();
		listing43.put(Constants.listingDataFieldListingType, Constants.listingTypeNameBorrowRequest);
		listing43.put(Constants.listingDataFieldTitle, "Auto");
		listing43.put(Constants.listingDataFieldDescription, "Ich möchte mir ein Auto leihen.");
		listing43.put(Constants.listingDataFieldActivityLocation, "München");
		listing43.put(Constants.listingDataFieldCreateDate, createDate);
		listing43.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing43.put(Constants.listingDataFieldBorrowFromDate, fromDate + 200);
		listing43.put(Constants.listingDataFieldBorrowToDate, toDate + 1000);

		listing44 = new JSONObject();
		listing44.put(Constants.listingDataFieldListingType, Constants.listingTypeNameBorrowRequest);
		listing44.put(Constants.listingDataFieldTitle, "Auto");
		listing44.put(Constants.listingDataFieldDescription, "Ich möchte mir ein Auto leihen.");
		listing44.put(Constants.listingDataFieldActivityLocation, "Karlsruhe");
		listing44.put(Constants.listingDataFieldCreateDate, createDate);
		listing44.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing44.put(Constants.listingDataFieldBorrowFromDate, fromDate + 200);
		listing44.put(Constants.listingDataFieldBorrowToDate, toDate + 1000);

		listing45 = new JSONObject();
		listing45.put(Constants.listingDataFieldListingType, Constants.listingTypeNameBorrowRequest);
		listing45.put(Constants.listingDataFieldTitle, "Auto");
		listing45.put(Constants.listingDataFieldDescription, "Ich möchte mir ein Auto leihen.");
		listing45.put(Constants.listingDataFieldActivityLocation, "Stuttgard");
		listing45.put(Constants.listingDataFieldCreateDate, createDate);
		listing45.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing45.put(Constants.listingDataFieldBorrowFromDate, fromDate + 200);
		listing45.put(Constants.listingDataFieldBorrowToDate, toDate + 1000);

		listing46 = new JSONObject();
		listing46.put(Constants.listingDataFieldListingType, Constants.listingTypeNameBorrowRequest);
		listing46.put(Constants.listingDataFieldTitle, "Auto");
		listing46.put(Constants.listingDataFieldDescription, "Ich möchte mir ein Auto leihen.");
		listing46.put(Constants.listingDataFieldActivityLocation, "Nürnberg");
		listing46.put(Constants.listingDataFieldCreateDate, createDate);
		listing46.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing46.put(Constants.listingDataFieldBorrowFromDate, fromDate + 200);
		listing46.put(Constants.listingDataFieldBorrowToDate, toDate + 1000);

		listing47 = new JSONObject();
		listing47.put(Constants.listingDataFieldListingType, Constants.listingTypeNameBorrowRequest);
		listing47.put(Constants.listingDataFieldTitle, "Auto");
		listing47.put(Constants.listingDataFieldDescription, "Ich möchte mir ein Auto leihen.");
		listing47.put(Constants.listingDataFieldActivityLocation, "Zug");
		listing47.put(Constants.listingDataFieldCreateDate, createDate);
		listing47.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing47.put(Constants.listingDataFieldBorrowFromDate, fromDate + 200);
		listing47.put(Constants.listingDataFieldBorrowToDate, toDate + 1000);

		listing48 = new JSONObject();
		listing48.put(Constants.listingDataFieldListingType, Constants.listingTypeNameBorrowRequest);
		listing48.put(Constants.listingDataFieldTitle, "Stifte");
		listing48.put(Constants.listingDataFieldDescription, "Ich möchte mir Stifte leihen.");
		listing48.put(Constants.listingDataFieldActivityLocation, "Köln");
		listing48.put(Constants.listingDataFieldCreateDate, createDate);
		listing48.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing48.put(Constants.listingDataFieldBorrowFromDate, fromDate + 200);
		listing48.put(Constants.listingDataFieldBorrowToDate, toDate + 1000);

		listing49 = new JSONObject();
		listing49.put(Constants.listingDataFieldListingType, Constants.listingTypeNameBorrowRequest);
		listing49.put(Constants.listingDataFieldTitle, "Hemd");
		listing49.put(Constants.listingDataFieldDescription, "Ich möchte mir ein Hemd leihen.");
		listing49.put(Constants.listingDataFieldActivityLocation, "Mannheim");
		listing49.put(Constants.listingDataFieldCreateDate, createDate);
		listing49.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing49.put(Constants.listingDataFieldBorrowFromDate, fromDate + 200);
		listing49.put(Constants.listingDataFieldBorrowToDate, toDate + 1000);

		listing50 = new JSONObject();
		listing50.put(Constants.listingDataFieldListingType, Constants.listingTypeNameBorrowRequest);
		listing50.put(Constants.listingDataFieldTitle, "Grill");
		listing50.put(Constants.listingDataFieldDescription, "Ich möchte mir einen Grill leihen.");
		listing50.put(Constants.listingDataFieldActivityLocation, "Karlsruhe");
		listing50.put(Constants.listingDataFieldCreateDate, createDate);
		listing50.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing50.put(Constants.listingDataFieldBorrowFromDate, fromDate + 200);
		listing50.put(Constants.listingDataFieldBorrowToDate, toDate + 1000);

		listing51 = new JSONObject();
		listing51.put(Constants.listingDataFieldListingType, Constants.listingTypeNameBorrowRequest);
		listing51.put(Constants.listingDataFieldTitle, "Decke");
		listing51.put(Constants.listingDataFieldDescription, "Ich möchte mir eine Decke leihen.");
		listing51.put(Constants.listingDataFieldActivityLocation, "Köln");
		listing51.put(Constants.listingDataFieldCreateDate, createDate);
		listing51.put(Constants.listingDataFieldDeadLine, expiryDate + 2300);
		listing51.put(Constants.listingDataFieldBorrowFromDate, fromDate + 200);
		listing51.put(Constants.listingDataFieldBorrowToDate, toDate + 1000);

		stringListing1 = testRESTController.createListing(stringListing1, request, response);
		stringListing2 = testRESTController.createListing(stringListing2, request, response);
		stringListing3 = testRESTController.createListing(stringListing3, request, response);
		stringListing4 = testRESTController.createListing(stringListing4, request, response);
		stringListing5 = testRESTController.createListing(stringListing5, request, response);
		stringListing6 = testRESTController.createListing(stringListing6, request, response);
		stringListing7 = testRESTController.createListing(stringListing7, request, response);
		stringListing8 = testRESTController.createListing(stringListing8, request, response);
		stringListing9 = testRESTController.createListing(stringListing9, request, response);
		stringListing10 = testRESTController.createListing(stringListing10, request, response);
		stringListing11 = testRESTController.createListing(stringListing11, request, response);
		stringListing12 = testRESTController.createListing(stringListing12, request, response);
		stringListing13 = testRESTController.createListing(stringListing13, request, response);
		stringListing14 = testRESTController.createListing(stringListing14, request, response);
		stringListing15 = testRESTController.createListing(stringListing15, request, response);
		stringListing16 = testRESTController.createListing(stringListing16, request, response);
		stringListing17 = testRESTController.createListing(stringListing17, request, response);
		stringListing18 = testRESTController.createListing(stringListing18, request, response);
		stringListing19 = testRESTController.createListing(stringListing19, request, response);
		stringListing20 = testRESTController.createListing(stringListing20, request, response);
		stringListing21 = testRESTController.createListing(stringListing21, request, response);
		stringListing22 = testRESTController.createListing(stringListing22, request, response);
		stringListing23 = testRESTController.createListing(stringListing23, request, response);
		stringListing24 = testRESTController.createListing(stringListing24, request, response);
		stringListing25 = testRESTController.createListing(stringListing25, request, response);
		stringListing26 = testRESTController.createListing(stringListing26, request, response);
		stringListing27 = testRESTController.createListing(stringListing27, request, response);
		stringListing28 = testRESTController.createListing(stringListing28, request, response);
		stringListing29 = testRESTController.createListing(stringListing29, request, response);
		stringListing30 = testRESTController.createListing(stringListing30, request, response);
		stringListing31 = testRESTController.createListing(stringListing31, request, response);
		stringListing32 = testRESTController.createListing(stringListing32, request, response);
		stringListing33 = testRESTController.createListing(stringListing34, request, response);
		stringListing34 = testRESTController.createListing(stringListing34, request, response);
		stringListing35 = testRESTController.createListing(stringListing35, request, response);
		stringListing36 = testRESTController.createListing(stringListing36, request, response);
		stringListing37 = testRESTController.createListing(stringListing37, request, response);
		stringListing38 = testRESTController.createListing(stringListing38, request, response);
		stringListing39 = testRESTController.createListing(stringListing39, request, response);
		stringListing40 = testRESTController.createListing(stringListing40, request, response);
		stringListing41 = testRESTController.createListing(stringListing41, request, response);
		stringListing42 = testRESTController.createListing(stringListing42, request, response);
		stringListing43 = testRESTController.createListing(stringListing43, request, response);
		stringListing44 = testRESTController.createListing(stringListing44, request, response);
		stringListing45 = testRESTController.createListing(stringListing45, request, response);
		stringListing46 = testRESTController.createListing(stringListing46, request, response);
		stringListing47 = testRESTController.createListing(stringListing47, request, response);
		stringListing48 = testRESTController.createListing(stringListing48, request, response);
		stringListing48 = testRESTController.createListing(stringListing49, request, response);
		stringListing50 = testRESTController.createListing(stringListing50, request, response);
		stringListing51 = testRESTController.createListing(stringListing51, request, response);
	}

}
