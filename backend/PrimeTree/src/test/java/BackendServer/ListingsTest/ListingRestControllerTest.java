package BackendServer.ListingsTest;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import BackendServer.ClientDatabaseAccess.Config.EmployeeBeanCollection;
import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.ListingRESTController;
import BackendServer.Listings.PersistenceAdapter;
import BackendServer.Listings.TestConfigRESTController;
import BackendServer.UserData.Configuration.UserBeanCollection;
import BackendServer.Listings.ListingBeanCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes ={ ListingBeanCollection.class, TestConfigRESTController.class,
		EmployeeBeanCollection.class, UserBeanCollection.class})
public class ListingRestControllerTest{
	
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ListingRESTController testRESTController;
    
    
    
    private SecurityContext secCon;
    private Authentication auth;
    
    private JSONObject requestBody, commentRequestBody;
    private String requestBodyString, commentRequestBodyString, query;
    private String[] locationArray, typeArray;
 
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    

    InputStream inputFileStream, inputFileStreamZwei, inputFileStreamDrei, inputFileStreamVier, inputFileStreamFuenf;
    MockMultipartFile file, fileZwei, fileDrei, fileVier, fileFuenf;
    
	private MockMvc mockMvc;
	
    @Before
    public void setup() {
    	// wir haben hier nun unser erstes parameter erstellt
    	requestBody = new JSONObject();
    	requestBody.put(Constants.listingDataFieldListingType, Constants.listingTypeNameServiceOffering);
    	requestBody.put(Constants.listingDataFieldDescription, "gut");
    	requestBody.put(Constants.listingDataFieldCreateDate, new Date().getTime());
    	requestBody.put(Constants.listingDataFieldTitle, "Superangebot");
    	requestBody.put(Constants.listingDataFieldDeadLine, (long) Integer.MAX_VALUE);
    	requestBody.put(Constants.listingDataFieldLocation, "Mannheim");
    	requestBody.put(Constants.listingDataFieldPrice, 20.99);
    	requestBodyString = requestBody.toString();
    	
    	query = "gut";
    	typeArray = new String[1];
    	typeArray[0]= Constants.listingTypeNameServiceOffering;
    	locationArray = new String[1];
    	locationArray[0]="Mannheim";
    	
    	commentRequestBody = new JSONObject();
    	commentRequestBody.put(Constants.listingDataFieldCreateDate, new Date().getTime());
    	commentRequestBody.put(Constants.listingDataFieldComments, "Mein erster Kommentar");
    	commentRequestBodyString = commentRequestBody.toString();
    	// Inputstreams for picture uploads
		try {
			inputFileStream = new BufferedInputStream(new FileInputStream( "/Users/markbeckmann/git/primetree/backend/PrimeTree/src/test/java/BackendServer/ListingsTest/TestImage.png"));
			inputFileStreamZwei = new BufferedInputStream( new FileInputStream("/Users/markbeckmann/git/primetree/backend/PrimeTree/src/test/java/BackendServer/ListingsTest/bild2.jpg"));
			inputFileStreamDrei = new BufferedInputStream( new FileInputStream("/Users/markbeckmann/git/primetree/backend/PrimeTree/src/test/java/BackendServer/ListingsTest/bild3.JPG"));
			inputFileStreamVier = new BufferedInputStream( new FileInputStream("/Users/markbeckmann/git/primetree/backend/PrimeTree/src/test/java/BackendServer/ListingsTest/bild4.PNG"));
			inputFileStreamFuenf = new BufferedInputStream( new FileInputStream("/Users/markbeckmann/git/primetree/backend/PrimeTree/src/test/java/BackendServer/ListingsTest/test.txt"));

			file = new MockMultipartFile("file", "testImage.png", null, inputFileStream);
			fileZwei = new MockMultipartFile("file2", "bild2.jpg", null, inputFileStreamZwei);
			fileDrei = new MockMultipartFile("file3","bild3.JPG", null, inputFileStreamDrei);
			fileVier = new MockMultipartFile("file4","bild4.PNG", null, inputFileStreamVier);
			fileFuenf = new MockMultipartFile("file5", inputFileStreamFuenf);			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
    	secCon = new SecurityContextImpl();
    	// login akessler as admin
    	auth = new TestingAuthenticationToken("akessler", "123", "ADMIN");
    	secCon.setAuthentication(auth);
    	SecurityContextHolder.setContext(secCon);
    	response = new MockHttpServletResponse();
    	response.setStatus(HttpServletResponse.SC_OK);
    }
	
	@Test
	public void createListingTestWithCorrectValues() throws ServletException{
    	request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		JSONObject resultJSON = new JSONObject(result);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertFalse(resultJSON.isNull(Constants.listingDataFieldId));
	}

	/**
	 * Test for createListing with wrong locationfield Expects an Exception
	 */
	@Test
	public void creatListingTestWithWrongLocation(){
    	request = new MockHttpServletRequest();
		requestBody.remove(Constants.listingDataFieldLocation);
		requestBody.put(Constants.listingDataFieldLocation, 123);
		requestBodyString = requestBody.toString();
		String result = testRESTController.createListing(requestBodyString, request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	/**
	 * Test for createListing with null String
	 * We wont test more wrong formats because the tests for that are made in ListingTest
	 */
	@Test
	public void createListingTestWithNullString(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(null, request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	
	//------------------------------ getListing --------------------------------
	/**
	 * Test getListing with correct Values
	 */
	@Test
	public void getListingTestAllCorrect(){
    	// create one listing in the Database
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listing/{id}");
		result = testRESTController.getListing(0, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test getListing with non existing id value
	 */
	@Test
	public void getListingTestWrongId(){
		request = new MockHttpServletRequest("GET", "listing/{id}");
		String result = testRESTController.getListing(Integer.MAX_VALUE, request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	/**
	 * Test getListing with negativ ID
	 */
	@Test
	public void getListingTestNegativeID(){
		request = new MockHttpServletRequest("GET", "listing/{id}");
		String result = testRESTController.getListing(-1, request, response);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	}
	
	
	//------------------------------------------ editListing -----------------------
	/**
	 * Test editListing with all correct values
	 */
	@Test
	public void editListingTestWithCorrectValues(){
    	// create one listing in the Database
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("POST", "listing/{id}");
		testRESTController.editListing(requestBodyString, 0, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test editListing with not existing listing
	 */
	@Test
	public void editListingTestWithNotExistingListing(){
		request = new MockHttpServletRequest("POST", "listing/{id}");
		testRESTController.editListing(requestBodyString, -1, request, response);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	}
	
	/**
	 * Test editListing() with a wrong format in the string
	 */
	@Test
	public void editListingTestWithAWrongFormat(){
    	// create one listing in the Database
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		requestBody.remove(Constants.listingDataFieldDescription);
		requestBody.put(Constants.listingDataFieldDescription, 0);
		requestBodyString = requestBody.toString();
		request = new MockHttpServletRequest("POST", "listing/{id}");
		testRESTController.editListing(requestBodyString, 0, request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	//---------------------------------------- delete ----------------------------------------------
	/**
	 * Test delete with all correct values
	 */
	@Test
	public void deleteTestWithCorrectValues(){
    	// create one listing in the Database
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("DELETE", "listing/delete/{id}");
		testRESTController.delete(0, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test delete with incorrect id
	 */
	@Test
	public void deleteTestWithWrongId(){
		request = new MockHttpServletRequest("POST", "listing/{id}");
		testRESTController.delete(-1, request, response);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	}
	
	
	//--------------------------------- deactivateListing ----------------
	
	/**
	 * Test deactivateListing with correct values
	 */
	@Test
	public void deactivateListing(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("POST", "listing/{id}/deactivate");
		testRESTController.deactivateListing(0, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test deactivateLisitng() with incorrect ID
	 */
	@Test
	public void deactivateListingWithWrongID(){
		request = new MockHttpServletRequest("POST", "listing/{id}/deactivate");
		testRESTController.deactivateListing(-1, request, response);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	}
	
	
	//--------------------------------- activateListing ------------------
	// These test can only run if deactivate is correct !!!
	
	/**
	 * Test activateListing with correct values
	 */
	@Test
	public void activateListingTestWithCorrectValues(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("POST", "listing/{id}/deactivate");
		testRESTController.deactivateListing(0, request, response);
		request = new MockHttpServletRequest("POST", "listing/{id}/activate");
		testRESTController.activateListing(0, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test activateListing with incorrect ID
	 */
	@Test
	public void activateListingTestWithWrongID(){
		request = new MockHttpServletRequest("POST", "listing/{id}/activate");
		testRESTController.activateListing(-1, request, response);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	}
	//--------------------------- postComment ------------------------------------
	
	/**
	 * Test postComment with correct values
	 */
	@Test
	public void postCommentTestWithCorrectValues(){
    	// create one listing in the Database
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
    	
		request = new MockHttpServletRequest("POST", "listing/{id}/comment");
		testRESTController.postComment(0, commentRequestBodyString, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test postComment with wrong id
	 */
	@Test
	public void postCommentTestWithWrongID(){
		request = new MockHttpServletRequest("POST", "listing/{id}(comment");
		testRESTController.postComment(-1, commentRequestBodyString, request, response);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus() );
	}
	
	
	//--------------------------- deleteComment ------------------------------------

	/**
	 * Test deleteComment with correct Values
	 */
	@Test
	public void deleteCommentWithEverythingCorrect(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("POST", "listing/{id}/comment");
		testRESTController.postComment(0, commentRequestBodyString, request, response);
		request = new MockHttpServletRequest("DELETE", "listing/{listingId}/comment/{commentId}");
		testRESTController.deleteComment(0, 0, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test deleteComment with wrong listingID
	 */
	@Test
	public void deleteCommentTestWithWrongListingID(){
		request = new MockHttpServletRequest("DELETE", "listing/{listingId}/comment/{commentId}");
		testRESTController.deleteComment(0, -1, request, response);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	}
	
	/**
	 * Test deleteComment with wrong commentID
	 */
	@Test
	public void deleteCommentTestWithWrongCommentID(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("DELETE", "listing/{listingId}/comment/{commentId}");
		testRESTController.deleteComment(-1, 0, request, response);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	}
	
	//--------------------------- listingMainImageUpload ------------------------------------

	/**
	 * Test listingMainImageUpload with correct Values
	 */
	@Test
	public void listingMainImageUploadTestWithCorrectValues(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.listingMainImageUpload(0, request, response, file);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test listingMainImage with jpg picture{
	 */
	@Test
	public void listingMainImageUploadTestWithJpg(){
		request = new MockHttpServletRequest("POST","listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.listingMainImageUpload(0, request, response, fileZwei);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test listingMainImage with JPG picture{
	 */
	@Test
	public void listingMainImageUploadTestWithJPG(){
		request = new MockHttpServletRequest("POST","listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.listingMainImageUpload(0, request, response, fileDrei);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test listingMainImage with PNG picture{
	 */
	@Test
	public void listingMainImageUploadTestWithPNG(){
		request = new MockHttpServletRequest("POST","listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.listingMainImageUpload(0, request, response, fileVier);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test listingMainImageUpload with wrong listingID
	 */
	@Test
	public void listingMainImageUploadTestWithWrongListingID(){
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.listingMainImageUpload(-1, request, response, file);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	}
	
	/**
	 * Test listingMainImageUpload with wrong file
	 */
	@Test
	public void listingMainImageUploadTestWithWrongFile(){
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.listingMainImageUpload(0, request, response, fileFuenf);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	/**
	 * Test listingMainImageUpload with null file
	 */
	@Test
	public void listingMainImageUploadTestWithNullFile(){
		request = new MockHttpServletRequest("PUT", "listing/upload/main.image/{id}");
		testRESTController.listingMainImageUpload(0, request, response, null);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	//--------------------------- galleryImageUpload ------------------------------------


	/**
	 * Test galleryImageUpload with correct Values
	 */
	@Test
	public void galleryImageUploadTestWithCorrectValues(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.galleryImageUpload(0, 0, request, response, file);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test galleryImageUpload with jpg picture{
	 */
	@Test
	public void galleryImageUploadTestWithJpg(){
		request = new MockHttpServletRequest("POST","listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.galleryImageUpload(0, 0, request, response, fileZwei);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test galleryImageUpload with JPG picture{
	 */
	@Test
	public void galleryImageUploadTestWithJPG(){
		request = new MockHttpServletRequest("POST","listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.galleryImageUpload(0,0, request, response, fileDrei);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test galleryImageUpload with PNG picture{
	 */
	@Test
	public void galleryImageUploadTestWithPNG(){
		request = new MockHttpServletRequest("POST","listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.galleryImageUpload(0, 0,request, response, fileVier);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test galleryImageUpload with wrong listingID
	 */
	@Test
	public void galleryImageUploadTestWithWrongListingID(){
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.galleryImageUpload(-1,0, request, response, file);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	}
	
	/**
	 * Test galleryImageUpload with wrong file
	 */
	@Test
	public void galleryImageUploadTestWithWrongFile(){
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.galleryImageUpload(0,0, request, response, fileFuenf);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	/**
	 * Test galleryImageUpload with null file
	 */
	@Test
	public void galleryImageUploadTestWithNullFile(){
		request = new MockHttpServletRequest("PUT", "listing/upload/main.image/{id}");
		testRESTController.galleryImageUpload(0,0, request, response, null);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	//--------------------------------------- listingGalleryChange ---------------------------------
	
	/**
	 * Test listingGalleryChange with correct values and png file
	 */
	@Test
	public void listingGalleryChangeTestWithCorrectValues(){
    	// create one listing in the Database
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/gallery/{listingId}/{galleryIndex}");
		testRESTController.galleryImageUpload(0, 0, request, response, file);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test listingGalleryChange with .jpg file
	 */
	@Test
	public void listingGalleryChangeTestWithJpgFile(){
    	// create one listing in the Database
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/gallery/{lisitngId}/{galleryIndex}");
		testRESTController.galleryImageUpload(0, 0, request, response, fileZwei);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test listingGalleryChange with .PNG file
	 */
	@Test
	public void listingGalleryChangeTestWithPNGFile(){
    	// create one listing in the Database
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/gallery/{lisitngId}/{galleryIndex}");
		testRESTController.galleryImageUpload(0, 0, request, response, fileDrei);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	
	/**
	 * Test listingGalleryChange with jpg file
	 */
	@Test
	public void listingGalleryChangeTestWithJPGFile(){
    	// create one listing in the Database
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/gallery/{lisitngId}/{galleryIndex}");
		testRESTController.galleryImageUpload(0, 0, request, response, fileVier);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test listingGalleryChange with .txt file
	 */
	@Test
	public void listingGalleryChangeTestWithWrongFile(){
    	// create one listing in the Database
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/gallery/{lisitngId}/{galleryIndex}");
		testRESTController.galleryImageUpload(0, 0, request, response, fileFuenf);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	/**
	 * Test listingGalleryChange with wrong listing id
	 */
	@Test
	public void listingGalleryChangeTestWithWrongListingId(){
    	// create one listing in the Database
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/gallery/{lisitngId}/{galleryIndex}");
		testRESTController.galleryImageUpload(-1, 0, request, response, file);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	}
	
	/**
	 * Test listing GalleryChange with wrong Gallery ID
	 */
	@Test
	public void listingGalleryChangeTestWithWrongGalleryId(){
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/gallery/{lisitngId}/{galleryIndex}");
		testRESTController.galleryImageUpload(0, -1, request, response, file);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	/**
	 * Test listingGalleryChange with null as file
	 */
	@Test
	public void listingGalleryChangeTestWithNullFile(){
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/gallery/{lisitngId}/{galleryIndex}");
		testRESTController.galleryImageUpload(0, 0, request, response, null);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	//-------------------------------------- listingGalleryDelete -------------------------------
	/**
	 * Test listingGalleryDelete with everything correct
	 */
	@Test
	public void listingGalleryDeleteTestEverythingcorrect(){
		// Setup
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.galleryImageUpload(0,0, request, response, file);
		request = new MockHttpServletRequest("DELETE", "listing/upload/gallery/{listingId}/{galleryIndex}");
		testRESTController.listingGalleryDelete(0, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test listingGalleryDelete with wrong Listing id
	 */
	@Test
	public void listingGalleryDeleteTestWithWrongListingId(){
		// Setup
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.galleryImageUpload(0, 0, request, response, file);
		request = new MockHttpServletRequest("DELETE", "listing/upload/gallery/{listingId}/{galleryIndex}");
		testRESTController.listingGalleryDelete(-1, request, response);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	}
	
	/**
	 * Test listingGalleryDelete with wrong Listing id
	 */
	@Test
	public void listingGalleryDeleteTestWithWrongGalleryId(){
		// Setup
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.galleryImageUpload(0, 0, request, response, file);
		request = new MockHttpServletRequest("DELETE", "listing/upload/gallery/{listingId}/{galleryIndex}");
		testRESTController.listingGalleryDelete(0,  request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	//------------------------------------------getListingsBySearch ---------------------------------------
	/**
	 * Test getListingBySearch with correct values
	 */
	@Test
	public void getListingsBySearchTestWithCorrectValues(){
		// Setup
			request = new MockHttpServletRequest("POST", "listing");
			String result = testRESTController.createListing(requestBodyString, request, response);
			request = new MockHttpServletRequest("GET", "listings/search");
			result = testRESTController.getListingsBySearch(query, 1, locationArray, 20, 22, typeArray, Constants.listingKindOffering, Constants.sortOptionAlphabetical_Asc, request, response);
			assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test getListingBySearch with wrong search query
	 */
	@Test
	public void getListingBySearchTestWithWrongQuery(){
		// Setup
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		query = "Staubsauger!!1111einseinself";
		request = new MockHttpServletRequest("GET", "listings/search");
		result = testRESTController.getListingsBySearch(query, 1, locationArray, 20, 22, typeArray, Constants.listingKindOffering, Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	/**
	 * Test getListingBySearch with null query
	 */
	@Test
	public void getListingBySearchTestWithNullQuery(){
		// Setup
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/search");
		result = testRESTController.getListingsBySearch(null, 1, locationArray, 20, 22, typeArray, Constants.listingKindOffering, Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	/**
	 * Test getListingBySearch with wrong MinPrice
	 */
	@Test
	public void getListingBySearchTestWithWrongMinPrice(){
		// Setup
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/search");
		result = testRESTController.getListingsBySearch(query, 1, locationArray, Integer.MAX_VALUE, 22, typeArray, Constants.listingKindOffering, Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	/**
	 * Test getListingBySearch with wrong MaxPrice
	 */
	@Test
	public void getListingBySearchTestWithWrongMaxPrice(){
		// Setup
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/search");
		result = testRESTController.getListingsBySearch(query, 1, locationArray, 20, Integer.MIN_VALUE, typeArray, Constants.listingKindOffering, Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	/**
	 * Test getListingBySearch with null location Array
	 */
	@Test
	public void getListingBySearchTestWithNullLocationArray(){
		// Setup
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/search");
		result = testRESTController.getListingsBySearch(query, 1, null, 20, 22, typeArray, Constants.listingKindOffering, Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test getListingBySearch with null type Array
	 */
	@Test
	public void getListingBySearchTestWithNullTypeArray(){
		// Setup
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/search");
		result = testRESTController.getListingsBySearch(query, 1, locationArray, 20, 22, null, Constants.listingKindOffering, Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test getListingBySearch with null kind
	 */
	@Test
	public void getListingBySearchTestWithNullKind(){
		// Setup
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/search");
		result = testRESTController.getListingsBySearch(query, 1, locationArray, 20, 22, typeArray, null, Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	//------------------------------ getActiveListings -----------------------------------
	
	/**
	 * Test getActiveListings with correct values
	 */
	@Test
	public void getActiveListingsTestWithCorrectValues(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/active");
		result = testRESTController.getActiveListings(1, locationArray, 20, 22, typeArray, Constants.listingKindOffering,
				Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test getActiveListings with null locationArray
	 */
	@Test
	public void getActiveListingsTestWithNullLocations(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/active");
		result = testRESTController.getActiveListings(1, null, 20, 22, typeArray, Constants.listingKindOffering,
				Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test getActiveListings with null typeArray
	 */
	@Test
	public void getActiveListingsTestWithNullTypeArray(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/active");
		result = testRESTController.getActiveListings(1, locationArray, 20, 22, null, Constants.listingKindOffering,
				Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test getActiveListings with null kind
	 */
	@Test
	public void getActiveListingsTestWithNullKind(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/active");
		result = testRESTController.getActiveListings(1, locationArray, 20, 22, typeArray, null,
				Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test getActiveListings with wrong maxPrice
	 */
	@Test
	public void getActiveListingsTestWithWrongMaxPrice(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/active");
		result = testRESTController.getActiveListings(1, locationArray, 20, Integer.MIN_VALUE, typeArray, Constants.listingKindOffering,
				Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(0, result.length());
	}
	
	/**
	 * Test getActiveListings with wrong minPrice
	 */
	@Test
	public void getActiveListingsTestWithMinPrice(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/active");
		result = testRESTController.getActiveListings(1, locationArray, Integer.MAX_VALUE, 22, typeArray, Constants.listingKindOffering,
				Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(0, result.length());
	}
	
	
	//---------------------------------------------------- getInactiveListings -------------------------------------------
	
	
	/**
	 * Test getInactiveListings with correct values
	 */
	@Test
	public void getInactiveListingsTestWithCorrectValues(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/inactive");
		result = testRESTController.getInactiveListings(1, locationArray, 22, 20, typeArray, Constants.listingKindOffering,
				Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test getInactiveListings with null location
	 */
	@Test
	public void getInactiveListingsTestWithNullLocation(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/inactive");
		result = testRESTController.getInactiveListings(1, null, 22, 20, typeArray, Constants.listingKindOffering,
				Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	
	/**
	 * Test getInactiveListings with null type
	 */
	@Test
	public void getInactiveListingsTestWithNullType(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/inactive");
		result = testRESTController.getInactiveListings(1, locationArray, 22, 20, null, Constants.listingKindOffering,
				Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	
	/**
	 * Test getInactiveListings with null kind
	 */
	@Test
	public void getInactiveListingsTestWithNullKind(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/inactive");
		result = testRESTController.getInactiveListings(1, locationArray, 22, 20, typeArray, null,
				Constants.sortOptionAlphabetical_Asc, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	
	
	
}
