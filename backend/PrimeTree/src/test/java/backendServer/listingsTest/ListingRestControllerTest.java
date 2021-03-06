package backendServer.listingsTest;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Optional;

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

import backendServer.clientDatabaseAccess.config.EmployeeBeanCollection;
import backendServer.exceptions.WrongFormatException;
import backendServer.listings.Constants;
import backendServer.listings.ListingBeanCollection;
import backendServer.listings.ListingRESTController;
import backendServer.listings.PersistenceAdapter;
import backendServer.testStuff.TestExclusiveBeans;
import backendServer.userData.configuration.UserBeanCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes ={ ListingBeanCollection.class, TestExclusiveBeans.class,
		EmployeeBeanCollection.class, UserBeanCollection.class})
public class ListingRestControllerTest{
	
    @Autowired
    private ListingRESTController testRESTController;
	@Autowired
	Constants.LocationsLoader locationsLoader;
    
    
    
    private SecurityContext secCon;
    private Authentication auth;
    
    private JSONObject requestBody, commentRequestBody, resultJSON, listingJSON;
    private String requestBodyString, commentRequestBodyString, query;
    private String[] locationArray, typeArray;
    private int listingID;
 
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private Optional<Integer> pageOptional, price_minOptional, price_maxOptional;
    private Optional<String> sortOptional, kindOptional;
    private Optional<String[]> locationOptional, typeOptional;
    
    
    
    InputStream inputFileStream, inputFileStreamZwei, inputFileStreamDrei, inputFileStreamVier, inputFileStreamFuenf;
    MockMultipartFile file, fileZwei, fileDrei, fileVier, fileFuenf;
    
	
    @Before
    public void setup() {
    	// wir haben hier nun unser erstes parameter erstellt
    	requestBody = new JSONObject();
    	requestBody.put(Constants.listingDataFieldListingType, Constants.listingTypeNameServiceOffering);
    	requestBody.put(Constants.listingDataFieldDescription, Constants.allFreeTimeActivityCategories.get(0));
    	requestBody.put(Constants.listingDataFieldCreateDate, new Date().getTime());
    	requestBody.put(Constants.listingDataFieldTitle, "Superangebot");
    	requestBody.put(Constants.listingDataFieldDeadLine, (long) Integer.MAX_VALUE);
    	requestBody.put(Constants.listingDataFieldLocation, "Mannheim");
    	requestBody.put(Constants.listingDataFieldPrice, 20.99);
    	requestBodyString = requestBody.toString();
    	
    	query = Constants.allFreeTimeActivityCategories.get(0);
    	// Setup Otionals
    	typeArray = new String[1];
    	typeArray[0]= Constants.listingTypeNameServiceOffering;
    	typeOptional = Optional.of(typeArray);
    	locationArray = new String[1];
    	locationArray[0]="Mannheim";
    	locationOptional = Optional.of(locationArray);
    	pageOptional = Optional.of(1);
    	price_maxOptional = Optional.of(22);
    	price_minOptional = Optional.of(20);
    	kindOptional = Optional.of(Constants.listingKindOffering);
    	sortOptional = Optional.of(Constants.sortOptionAlphabetical_Asc);
    	
    	
    	commentRequestBody = new JSONObject();
    	commentRequestBody.put(Constants.commentDataFieldDate, new Date().getTime());
    	commentRequestBody.put(Constants.commentDataFieldMessage, "Mein erster Kommentar");
    	commentRequestBodyString = commentRequestBody.toString();
    	// Inputstreams for picture uploads
		try {
			inputFileStream = new BufferedInputStream(new FileInputStream( ConstantsForTests.fileEins));
			inputFileStreamZwei = new BufferedInputStream( new FileInputStream(ConstantsForTests.fileZwei));
			inputFileStreamDrei = new BufferedInputStream( new FileInputStream(ConstantsForTests.fileDrei));
			inputFileStreamVier = new BufferedInputStream( new FileInputStream(ConstantsForTests.fileVier));
			inputFileStreamFuenf = new BufferedInputStream( new FileInputStream(ConstantsForTests.fileFünf));

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
		assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
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
	
	
	
	//------------------------------ getListing --------------------------------
	/**
	 * Test getListing with correct Values
	 */
	@Test
	public void getListingTestAllCorrect(){
    	// create one listing in the Database
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("GET", "listing/{id}");
		result = testRESTController.getListing(listingID, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	/**
	 * Test getListing with non existing id value
	 */
	@Test
	public void getListingTestWrongId(){
		request = new MockHttpServletRequest("GET", "listing/{id}");
		String result = testRESTController.getListing(Integer.MAX_VALUE, request, response);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
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
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("POST", "listing/{id}");
		testRESTController.editListing(requestBodyString, listingID, request, response);
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
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		requestBody.remove(Constants.listingDataFieldDescription);
		requestBody.put(Constants.listingDataFieldDescription, 0);
		requestBodyString = requestBody.toString();
		request = new MockHttpServletRequest("POST", "listing/{id}");
		testRESTController.editListing(requestBodyString, listingID, request, response);
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
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("DELETE", "listing/delete/{id}");
		testRESTController.delete(listingID, request, response);
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
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("POST", "listing/{id}/deactivate");
		testRESTController.deactivateListing(listingID, request, response);
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
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("POST", "listing/{id}/deactivate");
		testRESTController.deactivateListing(listingID, request, response);
		request = new MockHttpServletRequest("POST", "listing/{id}/activate");
		testRESTController.activateListing(listingID, request, response);
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
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("POST", "listing/{id}/comment");
		testRESTController.postComment(listingID, commentRequestBodyString, request, response);
		assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
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
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("POST", "listing/{id}/comment");
		testRESTController.postComment(listingID, commentRequestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listing/{id}");
		int commentID = new JSONObject(testRESTController.getListing(listingID, request, response)).getJSONArray(Constants.listingDataFieldComments).getJSONObject(0).getInt(Constants.commentDataFieldCommentId);
		request = new MockHttpServletRequest("DELETE", "listing/{listingId}/comment/{commentId}");
		testRESTController.deleteComment(commentID, listingID, request, response);
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
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("DELETE", "listing/{listingId}/comment/{commentId}");
		testRESTController.deleteComment(-1, listingID, request, response);
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
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		request.addHeader("fileType", file.getOriginalFilename());
		testRESTController.listingMainImageUpload(listingID, request, response, file);
		assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
	}
	
	/**
	 * Test listingMainImage with jpg picture{
	 */
	@Test
	public void listingMainImageUploadTestWithJpg(){
		request = new MockHttpServletRequest("POST","listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		request.addHeader("fileType", fileZwei.getOriginalFilename());
		testRESTController.listingMainImageUpload(listingID, request, response, fileZwei);
		assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
	}
	
	/**
	 * Test listingMainImage with JPG picture{
	 */
	@Test
	public void listingMainImageUploadTestWithJPG(){
		request = new MockHttpServletRequest("POST","listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		request.addHeader("fileType", fileDrei.getOriginalFilename());
		testRESTController.listingMainImageUpload(listingID, request, response, fileDrei);
		assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
	}
	
	/**
	 * Test listingMainImage with PNG picture{
	 */
	@Test
	public void listingMainImageUploadTestWithPNG(){
		request = new MockHttpServletRequest("POST","listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		request.addHeader("fileType", fileVier.getOriginalFilename());
		testRESTController.listingMainImageUpload(listingID, request, response, fileVier);
		assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
	}
	
	/**
	 * Test listingMainImageUpload with wrong listingID
	 */
	@Test
	public void listingMainImageUploadTestWithWrongListingID(){
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		request.addHeader("fileType", file.getOriginalFilename());
		testRESTController.listingMainImageUpload(-1, request, response, file);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	}
	
	/**
	 * Test listingMainImageUpload with wrong file
	 */
	@Test
	public void listingMainImageUploadTestWithWrongFile(){
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		request.addHeader("fileType", fileFuenf.getOriginalFilename());
		testRESTController.listingMainImageUpload(0, request, response, fileFuenf);
		assertEquals(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, response.getStatus());
	}
	/**
	 * Test listingMainImageUpload with null file
	 */
	@Test
	public void listingMainImageUploadTestWithNullFile(){
		request = new MockHttpServletRequest("PUT", "listing/upload/main.image/{id}");
		testRESTController.listingMainImageUpload(0, request, response, null);
		assertEquals(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, response.getStatus());
	}
	
	//--------------------------- galleryImageUpload ------------------------------------


	/**
	 * Test galleryImageUpload with correct Values
	 */
	@Test
	public void galleryImageUploadTestWithCorrectValues(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.galleryImageUpload(listingID, 0, request, response, file);
		assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
	}
	
	/**
	 * Test galleryImageUpload with jpg picture{
	 */
	@Test
	public void galleryImageUploadTestWithJpg(){
		request = new MockHttpServletRequest("POST","listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.galleryImageUpload(listingID, 0, request, response, fileZwei);
		assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
	}
	
	/**
	 * Test galleryImageUpload with JPG picture{
	 */
	@Test
	public void galleryImageUploadTestWithJPG(){
		request = new MockHttpServletRequest("POST","listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.galleryImageUpload(listingID,0, request, response, fileDrei);
		assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
	}
	
	/**
	 * Test galleryImageUpload with PNG picture{
	 */
	@Test
	public void galleryImageUploadTestWithPNG(){
		request = new MockHttpServletRequest("POST","listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.galleryImageUpload(listingID, 0,request, response, fileVier);
		assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
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
		assertEquals(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, response.getStatus());
	}
	/**
	 * Test galleryImageUpload with null file
	 */
	@Test
	public void galleryImageUploadTestWithNullFile(){
		request = new MockHttpServletRequest("PUT", "listing/upload/main.image/{id}");
		testRESTController.galleryImageUpload(0,0, request, response, null);
		assertEquals(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, response.getStatus());
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
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/gallery/{listingId}/{galleryIndex}");
		testRESTController.galleryImageUpload(listingID, 0, request, response, file);
		assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
	}
	
	/**
	 * Test listingGalleryChange with .jpg file
	 */
	@Test
	public void listingGalleryChangeTestWithJpgFile(){
    	// create one listing in the Database
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
    	resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/gallery/{lisitngId}/{galleryIndex}");
		testRESTController.galleryImageUpload(listingID, 0, request, response, fileZwei);
		assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
	}
	
	/**
	 * Test listingGalleryChange with .PNG file
	 */
	@Test
	public void listingGalleryChangeTestWithPNGFile(){
    	// create one listing in the Database
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/gallery/{lisitngId}/{galleryIndex}");
		testRESTController.galleryImageUpload(listingID, 0, request, response, fileDrei);
		assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
	}
	
	
	/**
	 * Test listingGalleryChange with jpg file
	 */
	@Test
	public void listingGalleryChangeTestWithJPGFile(){
    	// create one listing in the Database
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/gallery/{lisitngId}/{galleryIndex}");
		testRESTController.galleryImageUpload(listingID, 0, request, response, fileVier);
		assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
	}
	
	/**
	 * Test listingGalleryChange with .txt file
	 */
	@Test
	public void listingGalleryChangeTestWithWrongFile(){
    	// create one listing in the Database
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/gallery/{lisitngId}/{galleryIndex}");
		testRESTController.galleryImageUpload(listingID, 0, request, response, fileFuenf);
		assertEquals(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, response.getStatus());
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
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/gallery/{lisitngId}/{galleryIndex}");
		testRESTController.galleryImageUpload(listingID, -1, request, response, file);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	/**
	 * Test listingGalleryChange with null as file
	 */
	@Test
	public void listingGalleryChangeTestWithNullFile(){
       	request = new MockHttpServletRequest("POST", "listing");
    	String result = testRESTController.createListing(requestBodyString, request, response);
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/gallery/{lisitngId}/{galleryIndex}");
		testRESTController.galleryImageUpload(listingID, 0, request, response, null);
		assertEquals(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, response.getStatus());
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
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.galleryImageUpload(listingID,0, request, response, file);
		request = new MockHttpServletRequest("DELETE", "listing/upload/gallery/{listingId}/{galleryIndex}");
		testRESTController.listingGalleryDelete(listingID, request, response);
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
		resultJSON = new JSONObject(result);
		listingID =  resultJSON.getInt(Constants.listingDataFieldId);
		request = new MockHttpServletRequest("PUT", "listing/upload/main-image/{id}");
		testRESTController.galleryImageUpload(listingID, 0, request, response, file);
		request = new MockHttpServletRequest("DELETE", "listing/upload/gallery/{listingId}/{galleryIndex}");
		testRESTController.listingGalleryDelete(-1, request, response);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	
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
			result = testRESTController.getListingsBySearch(query, pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional, sortOptional, request, response);
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
		result = testRESTController.getListingsBySearch(query, pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional, sortOptional, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
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
		result = testRESTController.getListingsBySearch(null, pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional, sortOptional, request, response);
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
		price_minOptional = Optional.of(Integer.MAX_VALUE);
		result = testRESTController.getListingsBySearch(query, pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional, sortOptional, request, response);
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
		price_maxOptional = Optional.of(Integer.MIN_VALUE);
		result = testRESTController.getListingsBySearch(query, pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional, sortOptional, request, response);
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
		locationOptional = Optional.empty();
		result = testRESTController.getListingsBySearch(query, pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional, sortOptional, request, response);
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
		typeOptional = Optional.empty();
		result = testRESTController.getListingsBySearch(query, pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional, sortOptional, request, response);
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
		kindOptional = Optional.empty();
		result = testRESTController.getListingsBySearch(query, pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional, sortOptional, request, response);
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
		result = testRESTController.getActiveListings(pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional,
				sortOptional, request, response);
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
		locationOptional = Optional.empty();
		result = testRESTController.getActiveListings(pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional,
				sortOptional, request, response);
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
		typeOptional = Optional.empty();
		result = testRESTController.getActiveListings(pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional,
				sortOptional, request, response);
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
		kindOptional = Optional.empty();
		result = testRESTController.getActiveListings(pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional,
				sortOptional, request, response);
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
		price_maxOptional = Optional.of(Integer.MIN_VALUE);
		result = testRESTController.getActiveListings(pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional,
				sortOptional, request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
		assertEquals(0, result.length());
	}
	
	/**
	 * Test getActiveListings with wrong minPrice
	 */
	@Test
	public void getActiveListingsTestWithWrongMinPrice(){
		request = new MockHttpServletRequest("POST", "listing");
		String result = testRESTController.createListing(requestBodyString, request, response);
		request = new MockHttpServletRequest("GET", "listings/active");
		price_minOptional = Optional.of(Integer.MAX_VALUE);
		result = testRESTController.getActiveListings(pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional,
				sortOptional, request, response);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
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
		result = testRESTController.getInactiveListings(pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional,
				sortOptional, request, response);
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
		locationOptional = Optional.empty();
		result = testRESTController.getInactiveListings(pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional,
				sortOptional, request, response);
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
		typeOptional = Optional.empty();
		result = testRESTController.getInactiveListings(pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional,
				sortOptional, request, response);
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
		kindOptional = Optional.empty();
		result = testRESTController.getInactiveListings(pageOptional, locationOptional, price_minOptional, price_maxOptional, typeOptional, kindOptional,
				sortOptional, request, response);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
	}
	
	
	
	
}
