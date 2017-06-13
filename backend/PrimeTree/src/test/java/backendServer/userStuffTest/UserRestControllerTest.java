package backendServer.userStuffTest;

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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import backendServer.clientDatabaseAccess.config.EmployeeBeanCollection;
import backendServer.exceptions.UserNotFoundException;
import backendServer.listings.Constants;
import backendServer.listings.ListingBeanCollection;
import backendServer.listings.ListingRESTController;
import backendServer.testStuff.TestExclusiveBeans;
import backendServer.user.UserRESTController;
import backendServer.userData.configuration.UserBeanCollection;




@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes ={ ListingBeanCollection.class, UserRestControllerTest.class, 
		EmployeeBeanCollection.class, UserBeanCollection.class,TestExclusiveBeans.class})
public class UserRestControllerTest {

	
    
    @Autowired
    private UserRESTController testUserRESTController;
    
    @Autowired
    private ListingRESTController testRESTController;
    
	@Autowired
	Constants.LocationsLoader locationsLoader;
    
    private SecurityContext secCon;
    private Authentication auth;
    
    private JSONObject requestBody;
 
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private String requestBodyString;
	

    @Before
    public void init(){
    	secCon = new SecurityContextImpl();
    	// login akessler as admin
    	auth = new TestingAuthenticationToken("akessler", "123", "ADMIN");
    	secCon.setAuthentication(auth);
    	SecurityContextHolder.setContext(secCon);
    	
    	response = new MockHttpServletResponse();
    	response.setStatus(HttpServletResponse.SC_OK);
    	
    	
    	
    	requestBody = new JSONObject();
    	requestBody.put(Constants.listingDataFieldListingType, Constants.listingTypeNameServiceOffering);
    	requestBody.put(Constants.listingDataFieldDescription, "gut");
    	requestBody.put(Constants.listingDataFieldCreateDate, new Date().getTime());
    	requestBody.put(Constants.listingDataFieldTitle, "Superangebot");
    	requestBody.put(Constants.listingDataFieldDeadLine, (long) Integer.MAX_VALUE);
    	requestBody.put(Constants.listingDataFieldLocation, "Mannheim");
    	requestBody.put(Constants.listingDataFieldPrice, 20.99);
    	requestBodyString = requestBody.toString();
    	
    	
    	//create one listing for favorites test
    	request = new MockHttpServletRequest("POST", "listing");
    	testRESTController.createListing(requestBodyString, request, response);
    }
    //----------------------------------getUser()------------------------------------------
    /**
     * Test with everything ok
     */
    @Test
    public void getUserTest(){
    	request = new MockHttpServletRequest("GET", "/user/{id}");
    	String result = testUserRESTController.getUser(2, request, response);
    	assertEquals(HttpServletResponse.SC_OK, response.getStatus() );
    }
    
    /**
     * Test getUser with wrong ID
     */
    @Test
    public void userRESTControllerGetUserTestWithWrongID(){
    	request = new MockHttpServletRequest("GET", "/user/{id}");
    	String result = testUserRESTController.getUser(-1, request, response);
    	assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
    			
    }

    
    //--------------------------------getUsers()---------------------------------------------
    
    /**
     * Test getUsers with everything ok
     */
    @Test
    public void userRESTControllerGetUsersTest(){
    	request = new MockHttpServletRequest("GET", "/users");
    	response.setStatus(HttpServletResponse.SC_OK);
    	String result = testUserRESTController.getUsers(request, response);
    	assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }
	

    
    //-------------------------------postFavourites()------------------------------
    
    /**
     * Test postFavourites with correct values
     * @throws UserNotFoundException 
     * @throws UsernameNotFoundException 
     */
    @Test
    public void userRESTControllerpostFavouritesTest() throws UsernameNotFoundException, UserNotFoundException{
    	request = new MockHttpServletRequest("POST", "/user/favourites");
    	testUserRESTController.postFavourite(0, request, response);
    	assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
    }
    
    /**
     * Test postFavourites with 2 times same listing
     * @throws UserNotFoundException 
     * @throws UsernameNotFoundException 
     */
    @Test
    public void userRESTControllerPostFavsTestWith2timesSameListing() throws UsernameNotFoundException, UserNotFoundException{
    	request = new MockHttpServletRequest("POST", "/user/favourites");
    	testUserRESTController.postFavourite(0, request, response);
    	testUserRESTController.postFavourite(0, request, response);
    	assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }
    
    //--------------------------------------deleteFavourites
    
    /**
     * Test deleteFavorite this test can only run if postFavourite is running
     * @throws UserNotFoundException 
     * @throws UsernameNotFoundException 
     */
    @Test
    public void userRESTControllerDeleteFavourites() throws UsernameNotFoundException, UserNotFoundException{
    	request = new MockHttpServletRequest("POST", "/user/favourites");
    	testUserRESTController.postFavourite(0, request, response);
    	request = new MockHttpServletRequest("DELETE", "/user/favourites");
    	testUserRESTController.deleteFavourite(0, request, response);
    	assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }
    
    /**
     * Test deleteFavourites with wrong listingID
     * @throws UserNotFoundException 
     * @throws UsernameNotFoundException 
     */
    @Test
    public void userRESTControllerDeleteFavouritesWithWrongID() throws UsernameNotFoundException, UserNotFoundException{
    	request = new MockHttpServletRequest("DELETE", "/user/favourites");
    	testUserRESTController.deleteFavourite(-1, request, response);
    	assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
    }
    
    //--------------------------------------getFavourites---------------------------------------
    /**
     * Test getFavourites with everthing ok
     * @throws UserNotFoundException 
     * @throws UsernameNotFoundException 
     */
    @Test
    public void userRESTControllerGetFavouritesTest() throws UsernameNotFoundException, UserNotFoundException{
    	request = new MockHttpServletRequest("GET", "/user/favourites");
    	testUserRESTController.getFavourites(request, response);
    	assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    	
    }
    
    //----------------------------------------declareAdmin-------------------------------------
    /**
     * Test declareAdmin
     */
    @Test
    public void userRESTControllerDeclareAdminTest(){
    	request = new MockHttpServletRequest("POST", "/user/{id}/admin");
    	//ToDo find user that is no admin
    	testUserRESTController.declareAdmin(2, request, response);
    	assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }
    
    /**
     * Test delcare admin with worng id
     */
    @Test
    public void userRESTControllerDeclareAdminTestWithWrongID(){
    	request = new MockHttpServletRequest("POST", "/user/y{id}/admin");
    	testUserRESTController.declareAdmin(-1, request, response);
    	assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
    }
    
    /**
     * Test declareAdmin with admin user
     */
    @Test
    public void userRESTControllerDeclareAdminTestWithAdminUser(){
    	request = new MockHttpServletRequest("POST", "/user/{id}/admin");
    	//ToDo find user that is no admin
    	testUserRESTController.declareAdmin(2, request, response);
    	testUserRESTController.declareAdmin(2, request, response);
    	assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }
    
    //----------------------------------DeleteAdmin--------------------------------
    
    /**
     * Test deleteAdmin with everything ok
     * this test can only run if declareadmin is correct
     */
    @Test
    public void userRESTControllerDeleteAdminTest(){
    	request = new MockHttpServletRequest("POST", "/user/{id}/admin");
    	//ToDo find user that is no admin
    	testUserRESTController.declareAdmin(2, request, response);
    	request = new MockHttpServletRequest("DELETE", "/user/{id}/admin");
    	testUserRESTController.deleteAdmin(2, request, response);
    	assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }
    
    /**
     * test deleteAdmin with wrong user ID
     */
    @Test
    public void userRESTControllerDeleteAdminTestWithWrongUserID(){
    	request = new MockHttpServletRequest("DELETE", "/user/{id}/admin");
    	testUserRESTController.deleteAdmin(3, request, response);
    	assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
    }
    
    /**
     * test deleteAdmin with no admin user
     */
    @Test
    public void userRESTControllerDeleteAdminTestWithNoAdminUer(){
    	request = new MockHttpServletRequest("DELETE", "/user/{id}/admin");
    	//ToDo find User thats no admin
    	testUserRESTController.deleteAdmin(2, request, response);
    	assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }
    
}
