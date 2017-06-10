package BackendServer.UserStuffTest;

import static org.junit.Assert.*;

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
import BackendServer.Listings.ListingBeanCollection;
import BackendServer.User.UserRESTController;
import BackendServer.UserData.Configuration.UserBeanCollection;




@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes ={ ListingBeanCollection.class, UserRestControllerTest.class, 
		EmployeeBeanCollection.class, UserBeanCollection.class})
public class UserRestControllerTest {

	
    
    @Autowired
    private UserRESTController testUserRESTController;
    
    private SecurityContext secCon;
    private Authentication auth;
    
    private JSONObject requestBody, commentRequestBody;
 
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
	

    @Before
    public void init(){
    	secCon = new SecurityContextImpl();
    	// login akessler as admin
    	auth = new TestingAuthenticationToken("akessler", "123", "ADMIN");
    	secCon.setAuthentication(auth);
    	SecurityContextHolder.setContext(secCon);
    	
    	response = new MockHttpServletResponse();
    	response.setStatus(HttpServletResponse.SC_OK);
    }
    //----------------------------------getUser()------------------------------------------
    /**
     * Test with everything ok
     */
    @Test
    public void getUserTest(){
    	request = new MockHttpServletRequest("GET", "/user/{id}");
    	String result = testUserRESTController.getUser(0, request, response);
    	assertEquals(response.getStatus(),HttpServletResponse.SC_OK );
    }
    
    /**
     * Test getUser with wrong ID
     */
    @Test
    public void userRESTControllerGetUserTestWithWrongID(){
    	request = new MockHttpServletRequest("GET", "/user/{id}");
    	String result = testUserRESTController.getUser(-1, request, response);
    	assertEquals(response.getStatus(), HttpServletResponse.SC_NOT_FOUND);
    			
    }

	

}
