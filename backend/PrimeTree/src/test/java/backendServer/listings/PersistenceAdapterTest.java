package backendServer.listings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import backendServer.exceptions.CommentNotFoundException;
import backendServer.exceptions.ListingNotFoundException;
import backendServer.exceptions.MainImageNotSupportedException;
import backendServer.exceptions.WrongFormatException;
import backendServer.listings.Constants;
import backendServer.listings.PersistenceAdapter;
import backendServer.listings.entities.BorrowRequest;
import backendServer.listings.entities.Comment;
import backendServer.listings.entities.Listing;
import backendServer.listings.entities.PurchaseRequest;
import backendServer.listings.entities.ReturningRecreationRequest;
import backendServer.listings.entities.RideShareRequest;
import backendServer.listings.entities.RideSharing;
import backendServer.listings.entities.SellItem;
import backendServer.listings.entities.ServiceOffering;
import backendServer.listings.entities.SingleRecreationRequest;
import backendServer.listings.repositories.SellItemRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={backendServer.userData.configuration.UserBeanCollection.class,
backendServer.listings.ListingBeanCollection.class, backendServer.clientDatabaseAccess.config.EmployeeBeanCollection.class})
public class PersistenceAdapterTest {
	
	@Autowired
	PersistenceAdapter persistenceAdapter;
	@Autowired
	Constants.LocationsLoader locationsLoader;
	
	//These are all JSONObjects for testing persistNewListing with valid input:
	JSONObject 
	correctListingDataForSellItemCreateWithoutExtraFields,
	correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored,
	correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored,
	correctListingDataForRideShareOfferCreateWithNoOptionalFields,
	correctListingDataForRideShareOfferCreateWithAllOptionalFields,
	correctListingDataForBorrowRequestCreateWithNoOptionalFields,
	correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest,
	correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture,
	correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture,
	correctListingDataForReturningRecreationRequestCreateWithNoOptionalField,
	correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields,
	correctListingDataForSingleRecreationRequestCreateWithNoOptionalField,
	correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields,
	correctListingDataForRideShareRequestCreateWithNoOptionalField,
	correctListingDataForRideShareRequestCreateWithAllOptionalFields;
	
	//And those are the ones for testing persistNewListing with invalid input:
	JSONObject
	listingDataForSellItemWithAnExistingListingTypeWhichIsNotSellItem,
	listingDataForSellItemWithAnNotExistingListingType,
	listingDataForSellItemWithMissingAttributeListingType,
	listingDataForSellItemWithMissingAttributeCreateDate,
	listingDataForSellItemWithMissingAttributeDescription,
	listingDataForSellItemWithMissingAttributeLocation,
	listingDataForSellItemWithMissingAttributeTitle,
	listingDataForSellItemWithMissingAttributePrice,
	listingDataForSellItemWithMissingAttributeCondition,
	listingDataForSellItemWithNotExistingCondition,
	listingDataForSellItemWithWrongTypeOfOneRequiredField,
	listingDataForSellItemWithWrongTypeOfOneOptionalField,
	listingDataForServiceOfferWithMissingAttributePrice,
	listingDataForRideShareOfferWithMissingAttributeFromLocation,
	listingDataForRideShareOfferWithMissingAttributeToLocation,
	listingDataForRideShareOfferWithMissingAttributeTravelDateAndTime,
	listingDataForPurchaseRequestWithMissingAttributeCondition,
	listingDataForPurchaseRequestWithNotExistingCondition,
	listingDataForReturningRecreationRequestWithMissingAttributeRegularity,
	listingDataForReturningRecreationRequestWithNotExistingRegularity,
	listingDataForReturningRecreationRequestWithMissingAttributeCategory,
	listingDataForReturningRecreationRequestWithNotExistingCategory,
	listingDataForRideShareRequestWithMissingAttributeFromLocation,
	listingDataForRideShareRequestWithMissingAttributeToLocation;
	
	//Those are the JSONObjects used to test comment-Methods
	JSONObject 
	validCommentData,
	commentDataWithMissingCreateDate,
	commentDataWithMissingMessage,
	commentDataWithintMessage,
	commentDataWithStringCreateDate;
	
	//Those are the imageDatas for uploadTests
	byte[] 
	fileDataOfAPointpng,
	fileDataOfAPointjpg,
//	fileDataOfAPointjpeg,
	fileDataOfAPointPNG,
//	fileDataOfAPointJPG,
//	fileDataOfAPointJPEG,
	fileDataOfAPointtxt;
	
	//Those are the names of the origin files for the imageDatas above
	String
	NameOfAPointpngFile,
	NameOfAPointjpgFile,
//	NameOfAPointjpegFile,
	NameOfAPointPNGFile,
//	NameOfAPointJPGFile,
//	NameOfAPointJPEGFile,
	NameOfAPointtxtFile;
	
	FileInputStream inputStream;
	
	@Before
	public void setup() throws IOException{
		
		//These are all JSONObjects for testing persistNewListing with valid input
		correctListingDataForSellItemCreateWithoutExtraFields=new JSONObject();
		correctListingDataForSellItemCreateWithoutExtraFields.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		correctListingDataForSellItemCreateWithoutExtraFields.put(Constants.listingDataFieldCreateDate, (long) 0);
		correctListingDataForSellItemCreateWithoutExtraFields.put(Constants.listingDataFieldDescription, "1");
		correctListingDataForSellItemCreateWithoutExtraFields.put(Constants.listingDataFieldTitle, "2");
		correctListingDataForSellItemCreateWithoutExtraFields.put(Constants.listingDataFieldLocation, "Mannheim");
		correctListingDataForSellItemCreateWithoutExtraFields.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		correctListingDataForSellItemCreateWithoutExtraFields.put(Constants.listingDataFieldPrice, (double) 0 );
		
		correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored=new JSONObject();
		correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.put(Constants.listingDataFieldDescription, "4");
		correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.put(Constants.listingDataFieldTitle, "5");
		correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.put(Constants.listingDataFieldLocation, "Mannheim");
		correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(1));
		correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.put(Constants.listingDataFieldPrice, (double) 0 );
		correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.put(Constants.listingDataFieldActive, true);
		correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.put(Constants.listingDataFieldDeadLine, new Date().getTime()+100000);
		correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.put(Constants.listingDataFieldImageGallery, new JSONArray().put("7"));
		correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.put(Constants.listingDataFieldPicture, "8");
		correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.put(Constants.listingDataFieldActivityLocation, "ShouldBeIgnored");
		
		correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored=new JSONObject();
		correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored.put(Constants.listingDataFieldListingType, Constants.listingTypeNameServiceOffering);
		correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored.put(Constants.listingDataFieldDescription, "9");
		correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored.put(Constants.listingDataFieldTitle, "10");
		correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored.put(Constants.listingDataFieldLocation, "Mannheim");
		correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored.put(Constants.listingDataFieldPrice, (double) 0 );
		correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored.put(Constants.listingDataFieldActive, false );
		
		correctListingDataForRideShareOfferCreateWithNoOptionalFields=new JSONObject();
		correctListingDataForRideShareOfferCreateWithNoOptionalFields.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
		correctListingDataForRideShareOfferCreateWithNoOptionalFields.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		correctListingDataForRideShareOfferCreateWithNoOptionalFields.put(Constants.listingDataFieldDescription, "12");
		correctListingDataForRideShareOfferCreateWithNoOptionalFields.put(Constants.listingDataFieldTitle, "13");
		correctListingDataForRideShareOfferCreateWithNoOptionalFields.put(Constants.listingDataFieldLocation, "Mannheim");
		correctListingDataForRideShareOfferCreateWithNoOptionalFields.put(Constants.listingDataFieldFromLocation, "15");
		correctListingDataForRideShareOfferCreateWithNoOptionalFields.put(Constants.listingDataFieldToLocation, "16");
		correctListingDataForRideShareOfferCreateWithNoOptionalFields.put(Constants.listingDataFieldTravelDateAndTime, new Date().getTime()+100000);
		
		correctListingDataForRideShareOfferCreateWithAllOptionalFields=new JSONObject();
		correctListingDataForRideShareOfferCreateWithAllOptionalFields.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
		correctListingDataForRideShareOfferCreateWithAllOptionalFields.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		correctListingDataForRideShareOfferCreateWithAllOptionalFields.put(Constants.listingDataFieldDescription, "17");
		correctListingDataForRideShareOfferCreateWithAllOptionalFields.put(Constants.listingDataFieldTitle, "18");
		correctListingDataForRideShareOfferCreateWithAllOptionalFields.put(Constants.listingDataFieldLocation, "Mannheim");
		correctListingDataForRideShareOfferCreateWithAllOptionalFields.put(Constants.listingDataFieldFromLocation, "20");
		correctListingDataForRideShareOfferCreateWithAllOptionalFields.put(Constants.listingDataFieldToLocation, "21");
		correctListingDataForRideShareOfferCreateWithAllOptionalFields.put(Constants.listingDataFieldTravelDateAndTime, new Date().getTime()+100000);
		correctListingDataForRideShareOfferCreateWithAllOptionalFields.put(Constants.listingDataFieldAvailableSeats, 1);
		correctListingDataForRideShareOfferCreateWithAllOptionalFields.put(Constants.listingDataFieldJourneyStops, new JSONArray().put("22").put("23"));
		
		correctListingDataForBorrowRequestCreateWithNoOptionalFields=new JSONObject();
		correctListingDataForBorrowRequestCreateWithNoOptionalFields.put(Constants.listingDataFieldListingType, Constants.listingTypeNameBorrowRequest);
		correctListingDataForBorrowRequestCreateWithNoOptionalFields.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		correctListingDataForBorrowRequestCreateWithNoOptionalFields.put(Constants.listingDataFieldDescription, "24");
		correctListingDataForBorrowRequestCreateWithNoOptionalFields.put(Constants.listingDataFieldTitle, "25");
		correctListingDataForBorrowRequestCreateWithNoOptionalFields.put(Constants.listingDataFieldLocation, "Mannheim");
		
		correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest=new JSONObject();
		correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.put(Constants.listingDataFieldListingType, Constants.listingTypeNameBorrowRequest);
		correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.put(Constants.listingDataFieldDescription, "27");
		correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.put(Constants.listingDataFieldTitle, "28");
		correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.put(Constants.listingDataFieldLocation, "Mannheim");
		correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.put(Constants.listingDataFieldBorrowFromDate, new Date().getTime()+100000);
		correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.put(Constants.listingDataFieldBorrowToDate, new Date().getTime()+200000);
		correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.put(Constants.listingDataFieldPicture, "30");
		
		correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture=new JSONObject();
		correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture.put(Constants.listingDataFieldListingType, Constants.listingTypeNamePurchaseRequest);
		correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture.put(Constants.listingDataFieldDescription, "31");
		correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture.put(Constants.listingDataFieldTitle, "32");
		correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture.put(Constants.listingDataFieldLocation, "Mannheim");
		correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		
		correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture=new JSONObject();
		correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture.put(Constants.listingDataFieldListingType, Constants.listingTypeNamePurchaseRequest);
		correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture.put(Constants.listingDataFieldDescription, "34");
		correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture.put(Constants.listingDataFieldTitle, "35");
		correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture.put(Constants.listingDataFieldLocation, "Mannheim");
		correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(1));
		correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture.put(Constants.listingDataFieldPicture, "37");
		
		correctListingDataForReturningRecreationRequestCreateWithNoOptionalField=new JSONObject();
		correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.put(Constants.listingDataFieldListingType, Constants.listingTypeNameReturningRecreationRequest);
		correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.put(Constants.listingDataFieldDescription, "38");
		correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.put(Constants.listingDataFieldTitle, "39");
		correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.put(Constants.listingDataFieldLocation, "Mannheim");
		correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.put(Constants.listingDataFieldFreeTimeActivityCategory, Constants.allFreeTimeActivityCategories.get(0));
		correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.put(Constants.listingDataFieldActivityLocation, "41");
		correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.put(Constants.listingDataFieldRegularity, Constants.allRegularityOptions.get(0));
		
		correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields=new JSONObject();
		correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldListingType, Constants.listingTypeNameReturningRecreationRequest);
		correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldDescription, "42");
		correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldTitle, "43");
		correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldLocation, "Mannheim");
		correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldFreeTimeActivityCategory, Constants.allFreeTimeActivityCategories.get(0));
		correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldActivityLocation, "45");
		correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldPicture, "46");
		correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldStartDate, new Date().getTime()+1000000);
		correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldEndDate, new Date().getTime()+2000000);
		correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldRegularity, Constants.allRegularityOptions.get(1));
		
		correctListingDataForSingleRecreationRequestCreateWithNoOptionalField=new JSONObject();
		correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSingleRecreationRequest);
		correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.put(Constants.listingDataFieldDescription, "47");
		correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.put(Constants.listingDataFieldTitle, "48");
		correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.put(Constants.listingDataFieldLocation, "Mannheim");
		correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.put(Constants.listingDataFieldFreeTimeActivityCategory, Constants.allFreeTimeActivityCategories.get(0));
		correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.put(Constants.listingDataFieldActivityLocation, "50");
		
		correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields=new JSONObject();
		correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSingleRecreationRequest);
		correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldDescription, "51");
		correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldTitle, "52");
		correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldLocation, "Mannheim");
		correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldFreeTimeActivityCategory, Constants.allFreeTimeActivityCategories.get(0));
		correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldActivityLocation, "54");
		correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldPicture, "55");
		correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldHappeningDate, new Date().getTime()+100000);
		
		correctListingDataForRideShareRequestCreateWithNoOptionalField=new JSONObject();
		correctListingDataForRideShareRequestCreateWithNoOptionalField.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideShareRequest);
		correctListingDataForRideShareRequestCreateWithNoOptionalField.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		correctListingDataForRideShareRequestCreateWithNoOptionalField.put(Constants.listingDataFieldDescription, "56");
		correctListingDataForRideShareRequestCreateWithNoOptionalField.put(Constants.listingDataFieldTitle, "57");
		correctListingDataForRideShareRequestCreateWithNoOptionalField.put(Constants.listingDataFieldLocation, "Mannheim");
		correctListingDataForRideShareRequestCreateWithNoOptionalField.put(Constants.listingDataFieldFromLocation, "59");
		correctListingDataForRideShareRequestCreateWithNoOptionalField.put(Constants.listingDataFieldToLocation, "60");
		
		correctListingDataForRideShareRequestCreateWithAllOptionalFields=new JSONObject();
		correctListingDataForRideShareRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideShareRequest);
		correctListingDataForRideShareRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		correctListingDataForRideShareRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldDescription, "61");
		correctListingDataForRideShareRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldTitle, "62");
		correctListingDataForRideShareRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldLocation, "Mannheim");
		correctListingDataForRideShareRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldFromLocation, "64");
		correctListingDataForRideShareRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldToLocation, "65");
		correctListingDataForRideShareRequestCreateWithAllOptionalFields.put(Constants.listingDataFieldTravelDateAndTime, new Date().getTime()+1000000);
		
		//These are all JSONObjects for testing persistNewListing with invalid input:
		
		listingDataForSellItemWithAnExistingListingTypeWhichIsNotSellItem=new JSONObject();
		listingDataForSellItemWithAnExistingListingTypeWhichIsNotSellItem.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
		listingDataForSellItemWithAnExistingListingTypeWhichIsNotSellItem.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForSellItemWithAnExistingListingTypeWhichIsNotSellItem.put(Constants.listingDataFieldDescription, "67");
		listingDataForSellItemWithAnExistingListingTypeWhichIsNotSellItem.put(Constants.listingDataFieldTitle, "68");
		listingDataForSellItemWithAnExistingListingTypeWhichIsNotSellItem.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForSellItemWithAnExistingListingTypeWhichIsNotSellItem.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		listingDataForSellItemWithAnExistingListingTypeWhichIsNotSellItem.put(Constants.listingDataFieldPrice, (double) 0 );
		
		listingDataForSellItemWithAnNotExistingListingType=new JSONObject();
		listingDataForSellItemWithAnNotExistingListingType.put(Constants.listingDataFieldListingType, "Gibt Es Nicht");
		listingDataForSellItemWithAnNotExistingListingType.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForSellItemWithAnNotExistingListingType.put(Constants.listingDataFieldDescription, "70");
		listingDataForSellItemWithAnNotExistingListingType.put(Constants.listingDataFieldTitle, "71");
		listingDataForSellItemWithAnNotExistingListingType.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForSellItemWithAnNotExistingListingType.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		listingDataForSellItemWithAnNotExistingListingType.put(Constants.listingDataFieldPrice, (double) 0 );
		
		listingDataForSellItemWithMissingAttributeListingType=new JSONObject();
		listingDataForSellItemWithMissingAttributeListingType.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForSellItemWithMissingAttributeListingType.put(Constants.listingDataFieldDescription, "73");
		listingDataForSellItemWithMissingAttributeListingType.put(Constants.listingDataFieldTitle, "74");
		listingDataForSellItemWithMissingAttributeListingType.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForSellItemWithMissingAttributeListingType.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		listingDataForSellItemWithMissingAttributeListingType.put(Constants.listingDataFieldPrice, (double) 0 );
		
		listingDataForSellItemWithMissingAttributeCreateDate=new JSONObject();
		listingDataForSellItemWithMissingAttributeCreateDate.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listingDataForSellItemWithMissingAttributeCreateDate.put(Constants.listingDataFieldDescription, "76");
		listingDataForSellItemWithMissingAttributeCreateDate.put(Constants.listingDataFieldTitle, "77");
		listingDataForSellItemWithMissingAttributeCreateDate.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForSellItemWithMissingAttributeCreateDate.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		listingDataForSellItemWithMissingAttributeCreateDate.put(Constants.listingDataFieldPrice, (double) 0 );
		
		listingDataForSellItemWithMissingAttributeDescription=new JSONObject();
		listingDataForSellItemWithMissingAttributeDescription.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listingDataForSellItemWithMissingAttributeDescription.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForSellItemWithMissingAttributeDescription.put(Constants.listingDataFieldTitle, "79");
		listingDataForSellItemWithMissingAttributeDescription.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForSellItemWithMissingAttributeDescription.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		listingDataForSellItemWithMissingAttributeDescription.put(Constants.listingDataFieldPrice, (double) 0 );
		
		listingDataForSellItemWithMissingAttributeLocation=new JSONObject();
		listingDataForSellItemWithMissingAttributeLocation.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listingDataForSellItemWithMissingAttributeLocation.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForSellItemWithMissingAttributeLocation.put(Constants.listingDataFieldDescription, "81");
		listingDataForSellItemWithMissingAttributeLocation.put(Constants.listingDataFieldTitle, "82");
		listingDataForSellItemWithMissingAttributeLocation.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		listingDataForSellItemWithMissingAttributeLocation.put(Constants.listingDataFieldPrice, (double) 0 );
		
		listingDataForSellItemWithMissingAttributeTitle=new JSONObject();
		listingDataForSellItemWithMissingAttributeTitle.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listingDataForSellItemWithMissingAttributeTitle.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForSellItemWithMissingAttributeTitle.put(Constants.listingDataFieldDescription, "83");
		listingDataForSellItemWithMissingAttributeTitle.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForSellItemWithMissingAttributeTitle.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		listingDataForSellItemWithMissingAttributeTitle.put(Constants.listingDataFieldPrice, (double) 0 );
		
		listingDataForSellItemWithMissingAttributePrice=new JSONObject();
		listingDataForSellItemWithMissingAttributePrice.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listingDataForSellItemWithMissingAttributePrice.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForSellItemWithMissingAttributePrice.put(Constants.listingDataFieldDescription, "85");
		listingDataForSellItemWithMissingAttributePrice.put(Constants.listingDataFieldTitle, "86");
		listingDataForSellItemWithMissingAttributePrice.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForSellItemWithMissingAttributePrice.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		
		listingDataForSellItemWithMissingAttributeCondition=new JSONObject();
		listingDataForSellItemWithMissingAttributeCondition.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listingDataForSellItemWithMissingAttributeCondition.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForSellItemWithMissingAttributeCondition.put(Constants.listingDataFieldDescription, "88");
		listingDataForSellItemWithMissingAttributeCondition.put(Constants.listingDataFieldTitle, "89");
		listingDataForSellItemWithMissingAttributeCondition.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForSellItemWithMissingAttributeCondition.put(Constants.listingDataFieldPrice, (double) 0 );
		
		listingDataForSellItemWithNotExistingCondition=new JSONObject();
		listingDataForSellItemWithNotExistingCondition.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listingDataForSellItemWithNotExistingCondition.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForSellItemWithNotExistingCondition.put(Constants.listingDataFieldDescription, "91");
		listingDataForSellItemWithNotExistingCondition.put(Constants.listingDataFieldTitle, "92");
		listingDataForSellItemWithNotExistingCondition.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForSellItemWithNotExistingCondition.put(Constants.listingDataFieldCondition, "Gibt es nicht");
		listingDataForSellItemWithNotExistingCondition.put(Constants.listingDataFieldPrice, (double) 0 );
		
		listingDataForSellItemWithWrongTypeOfOneRequiredField=new JSONObject();
		listingDataForSellItemWithWrongTypeOfOneRequiredField.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listingDataForSellItemWithWrongTypeOfOneRequiredField.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForSellItemWithWrongTypeOfOneRequiredField.put(Constants.listingDataFieldDescription, "94");
		listingDataForSellItemWithWrongTypeOfOneRequiredField.put(Constants.listingDataFieldTitle, 1);
		listingDataForSellItemWithWrongTypeOfOneRequiredField.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForSellItemWithWrongTypeOfOneRequiredField.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		listingDataForSellItemWithWrongTypeOfOneRequiredField.put(Constants.listingDataFieldPrice, (double) 0 );
		
		listingDataForSellItemWithWrongTypeOfOneOptionalField=new JSONObject();
		listingDataForSellItemWithWrongTypeOfOneOptionalField.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		listingDataForSellItemWithWrongTypeOfOneOptionalField.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForSellItemWithWrongTypeOfOneOptionalField.put(Constants.listingDataFieldDescription, "96");
		listingDataForSellItemWithWrongTypeOfOneOptionalField.put(Constants.listingDataFieldTitle, "97");
		listingDataForSellItemWithWrongTypeOfOneOptionalField.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForSellItemWithWrongTypeOfOneOptionalField.put(Constants.listingDataFieldCondition, Constants.allItemConditions.get(0));
		listingDataForSellItemWithWrongTypeOfOneOptionalField.put(Constants.listingDataFieldPrice, (double) 0 );
		listingDataForSellItemWithWrongTypeOfOneOptionalField.put(Constants.listingDataFieldDeadLine, "Kein Long");
		
		listingDataForServiceOfferWithMissingAttributePrice=new JSONObject();
		listingDataForServiceOfferWithMissingAttributePrice.put(Constants.listingDataFieldListingType, Constants.listingTypeNameServiceOffering);
		listingDataForServiceOfferWithMissingAttributePrice.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForServiceOfferWithMissingAttributePrice.put(Constants.listingDataFieldDescription, "99");
		listingDataForServiceOfferWithMissingAttributePrice.put(Constants.listingDataFieldTitle, "100");
		listingDataForServiceOfferWithMissingAttributePrice.put(Constants.listingDataFieldLocation, "Mannheim");
		
		listingDataForRideShareOfferWithMissingAttributeFromLocation=new JSONObject();
		listingDataForRideShareOfferWithMissingAttributeFromLocation.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
		listingDataForRideShareOfferWithMissingAttributeFromLocation.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForRideShareOfferWithMissingAttributeFromLocation.put(Constants.listingDataFieldDescription, "102");
		listingDataForRideShareOfferWithMissingAttributeFromLocation.put(Constants.listingDataFieldTitle, "103");
		listingDataForRideShareOfferWithMissingAttributeFromLocation.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForRideShareOfferWithMissingAttributeFromLocation.put(Constants.listingDataFieldToLocation, "105");
		listingDataForRideShareOfferWithMissingAttributeFromLocation.put(Constants.listingDataFieldTravelDateAndTime, new Date().getTime()+100000);
		
		listingDataForRideShareOfferWithMissingAttributeToLocation=new JSONObject();
		listingDataForRideShareOfferWithMissingAttributeToLocation.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
		listingDataForRideShareOfferWithMissingAttributeToLocation.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForRideShareOfferWithMissingAttributeToLocation.put(Constants.listingDataFieldDescription, "106");
		listingDataForRideShareOfferWithMissingAttributeToLocation.put(Constants.listingDataFieldTitle, "107");
		listingDataForRideShareOfferWithMissingAttributeToLocation.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForRideShareOfferWithMissingAttributeToLocation.put(Constants.listingDataFieldFromLocation, "109");
		listingDataForRideShareOfferWithMissingAttributeToLocation.put(Constants.listingDataFieldTravelDateAndTime, new Date().getTime()+100000);
		
		listingDataForRideShareOfferWithMissingAttributeTravelDateAndTime=new JSONObject();
		listingDataForRideShareOfferWithMissingAttributeTravelDateAndTime.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
		listingDataForRideShareOfferWithMissingAttributeTravelDateAndTime.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForRideShareOfferWithMissingAttributeTravelDateAndTime.put(Constants.listingDataFieldDescription, "110");
		listingDataForRideShareOfferWithMissingAttributeTravelDateAndTime.put(Constants.listingDataFieldTitle, "111");
		listingDataForRideShareOfferWithMissingAttributeTravelDateAndTime.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForRideShareOfferWithMissingAttributeTravelDateAndTime.put(Constants.listingDataFieldFromLocation, "113");
		listingDataForRideShareOfferWithMissingAttributeTravelDateAndTime.put(Constants.listingDataFieldToLocation, "114");
		
		listingDataForPurchaseRequestWithMissingAttributeCondition=new JSONObject();
		listingDataForPurchaseRequestWithMissingAttributeCondition.put(Constants.listingDataFieldListingType, Constants.listingTypeNamePurchaseRequest);
		listingDataForPurchaseRequestWithMissingAttributeCondition.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForPurchaseRequestWithMissingAttributeCondition.put(Constants.listingDataFieldDescription, "115");
		listingDataForPurchaseRequestWithMissingAttributeCondition.put(Constants.listingDataFieldTitle, "116");
		listingDataForPurchaseRequestWithMissingAttributeCondition.put(Constants.listingDataFieldLocation, "Mannheim");
		
		listingDataForPurchaseRequestWithNotExistingCondition=new JSONObject();
		listingDataForPurchaseRequestWithNotExistingCondition.put(Constants.listingDataFieldListingType, Constants.listingTypeNamePurchaseRequest);
		listingDataForPurchaseRequestWithNotExistingCondition.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForPurchaseRequestWithNotExistingCondition.put(Constants.listingDataFieldDescription, "118");
		listingDataForPurchaseRequestWithNotExistingCondition.put(Constants.listingDataFieldTitle, "119");
		listingDataForPurchaseRequestWithNotExistingCondition.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForPurchaseRequestWithNotExistingCondition.put(Constants.listingDataFieldCondition, "Gibt es nicht");
		
		listingDataForReturningRecreationRequestWithMissingAttributeRegularity=new JSONObject();
		listingDataForReturningRecreationRequestWithMissingAttributeRegularity.put(Constants.listingDataFieldListingType, Constants.listingTypeNameReturningRecreationRequest);
		listingDataForReturningRecreationRequestWithMissingAttributeRegularity.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForReturningRecreationRequestWithMissingAttributeRegularity.put(Constants.listingDataFieldDescription, "121");
		listingDataForReturningRecreationRequestWithMissingAttributeRegularity.put(Constants.listingDataFieldTitle, "122");
		listingDataForReturningRecreationRequestWithMissingAttributeRegularity.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForReturningRecreationRequestWithMissingAttributeRegularity.put(Constants.listingDataFieldFreeTimeActivityCategory, Constants.allFreeTimeActivityCategories.get(0));
		listingDataForReturningRecreationRequestWithMissingAttributeRegularity.put(Constants.listingDataFieldActivityLocation, "124");
		
		listingDataForReturningRecreationRequestWithNotExistingRegularity=new JSONObject();
		listingDataForReturningRecreationRequestWithMissingAttributeRegularity.put(Constants.listingDataFieldListingType, Constants.listingTypeNameReturningRecreationRequest);
		listingDataForReturningRecreationRequestWithMissingAttributeRegularity.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForReturningRecreationRequestWithMissingAttributeRegularity.put(Constants.listingDataFieldDescription, "125");
		listingDataForReturningRecreationRequestWithMissingAttributeRegularity.put(Constants.listingDataFieldTitle, "126");
		listingDataForReturningRecreationRequestWithMissingAttributeRegularity.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForReturningRecreationRequestWithMissingAttributeRegularity.put(Constants.listingDataFieldFreeTimeActivityCategory, Constants.allFreeTimeActivityCategories.get(0));
		listingDataForReturningRecreationRequestWithMissingAttributeRegularity.put(Constants.listingDataFieldActivityLocation, "128");
		listingDataForReturningRecreationRequestWithMissingAttributeRegularity.put(Constants.listingDataFieldRegularity, "Gibt es nicht");
		
		listingDataForReturningRecreationRequestWithMissingAttributeCategory=new JSONObject();
		listingDataForReturningRecreationRequestWithMissingAttributeCategory.put(Constants.listingDataFieldListingType, Constants.listingTypeNameReturningRecreationRequest);
		listingDataForReturningRecreationRequestWithMissingAttributeCategory.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForReturningRecreationRequestWithMissingAttributeCategory.put(Constants.listingDataFieldDescription, "129");
		listingDataForReturningRecreationRequestWithMissingAttributeCategory.put(Constants.listingDataFieldTitle, "130");
		listingDataForReturningRecreationRequestWithMissingAttributeCategory.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForReturningRecreationRequestWithMissingAttributeCategory.put(Constants.listingDataFieldActivityLocation, "132");
		listingDataForReturningRecreationRequestWithMissingAttributeCategory.put(Constants.listingDataFieldRegularity, Constants.allRegularityOptions.get(0));
		
		listingDataForReturningRecreationRequestWithNotExistingCategory=new JSONObject();
		listingDataForReturningRecreationRequestWithNotExistingCategory.put(Constants.listingDataFieldListingType, Constants.listingTypeNameReturningRecreationRequest);
		listingDataForReturningRecreationRequestWithNotExistingCategory.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForReturningRecreationRequestWithNotExistingCategory.put(Constants.listingDataFieldDescription, "133");
		listingDataForReturningRecreationRequestWithNotExistingCategory.put(Constants.listingDataFieldTitle, "134");
		listingDataForReturningRecreationRequestWithNotExistingCategory.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForReturningRecreationRequestWithNotExistingCategory.put(Constants.listingDataFieldFreeTimeActivityCategory, "Gibt es nicht");
		listingDataForReturningRecreationRequestWithNotExistingCategory.put(Constants.listingDataFieldActivityLocation, "136");
		listingDataForReturningRecreationRequestWithNotExistingCategory.put(Constants.listingDataFieldRegularity, Constants.allRegularityOptions.get(0));
		
		listingDataForRideShareRequestWithMissingAttributeFromLocation=new JSONObject();
		listingDataForRideShareRequestWithMissingAttributeFromLocation.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
		listingDataForRideShareRequestWithMissingAttributeFromLocation.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForRideShareRequestWithMissingAttributeFromLocation.put(Constants.listingDataFieldDescription, "137");
		listingDataForRideShareRequestWithMissingAttributeFromLocation.put(Constants.listingDataFieldTitle, "138");
		listingDataForRideShareRequestWithMissingAttributeFromLocation.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForRideShareRequestWithMissingAttributeFromLocation.put(Constants.listingDataFieldToLocation, "4");
		listingDataForRideShareRequestWithMissingAttributeFromLocation.put(Constants.listingDataFieldTravelDateAndTime, new Date().getTime()+100000);
		
		listingDataForRideShareRequestWithMissingAttributeToLocation=new JSONObject();
		listingDataForRideShareRequestWithMissingAttributeToLocation.put(Constants.listingDataFieldListingType, Constants.listingTypeNameRideSharing);
		listingDataForRideShareRequestWithMissingAttributeToLocation.put(Constants.listingDataFieldCreateDate, new Date().getTime());
		listingDataForRideShareRequestWithMissingAttributeToLocation.put(Constants.listingDataFieldDescription, "139");
		listingDataForRideShareRequestWithMissingAttributeToLocation.put(Constants.listingDataFieldTitle, "140");
		listingDataForRideShareRequestWithMissingAttributeToLocation.put(Constants.listingDataFieldLocation, "Mannheim");
		listingDataForRideShareRequestWithMissingAttributeToLocation.put(Constants.listingDataFieldFromLocation, "142");
		listingDataForRideShareRequestWithMissingAttributeToLocation.put(Constants.listingDataFieldTravelDateAndTime, new Date().getTime()+100000);
		
		NameOfAPointpngFile="D:/Users/Florian Kutz/Pictures/TestImage.png";
		inputStream=new FileInputStream(NameOfAPointpngFile);
		fileDataOfAPointpng=new byte[inputStream.available()];
		inputStream.read(fileDataOfAPointpng);

		NameOfAPointjpgFile="D:/Users/Florian Kutz/Pictures/testImage.jpg";
		inputStream=new FileInputStream(NameOfAPointjpgFile);
		fileDataOfAPointjpg=new byte[inputStream.available()];
		inputStream.read(fileDataOfAPointjpg);

//		NameOfAPointjpegFile="test.jpeg";
//		inputStream=new FileInputStream(NameOfAPointjpegFile);
//		fileDataOfAPointjpeg=new byte[inputStream.available()];
//		inputStream.read(fileDataOfAPointjpeg);
		
		NameOfAPointPNGFile="D:/Users/Florian Kutz/Pictures/testImage (2).PNG";
		inputStream=new FileInputStream(NameOfAPointPNGFile);
		fileDataOfAPointPNG=new byte[inputStream.available()];
		inputStream.read(fileDataOfAPointPNG);

//		NameOfAPointJPGFile="test.JPG";
//		inputStream=new FileInputStream(NameOfAPointJPGFile);
//		fileDataOfAPointJPG=new byte[inputStream.available()];
//		inputStream.read(fileDataOfAPointJPG);
//
//		NameOfAPointJPEGFile="test.JPEG";
//		inputStream=new FileInputStream(NameOfAPointJPEGFile);
//		fileDataOfAPointJPEG=new byte[inputStream.available()];
//		inputStream.read(fileDataOfAPointJPEG);

		NameOfAPointtxtFile="D:/Users/Florian Kutz/Pictures/test.txt";
		inputStream=new FileInputStream(NameOfAPointtxtFile);
		fileDataOfAPointtxt=new byte[inputStream.available()];
		inputStream.read(fileDataOfAPointtxt);
		
		validCommentData=new JSONObject();
		validCommentData.put(Constants.commentDataFieldMessage, "Ein Kommentar");
		validCommentData.put(Constants.commentDataFieldDate, new Date().getTime());
		
		commentDataWithMissingCreateDate=new JSONObject();
		commentDataWithMissingCreateDate.put(Constants.commentDataFieldMessage, "Ein Kommentar");

		commentDataWithMissingMessage=new JSONObject();
		commentDataWithMissingMessage.put(Constants.commentDataFieldDate, new Date().getTime());
		
		commentDataWithintMessage=new JSONObject();
		commentDataWithintMessage.put(Constants.commentDataFieldMessage, 5);
		commentDataWithintMessage.put(Constants.commentDataFieldDate, new Date().getTime());
		
		commentDataWithStringCreateDate=new JSONObject();
		commentDataWithintMessage.put(Constants.commentDataFieldMessage, "Ein Kommentar");
		commentDataWithintMessage.put(Constants.commentDataFieldDate, "heute");
	}
	
	//-------------------------------------persistNewListing--------------------------------------------
	
	
	//These are all Test that try out persistNewListing with valid input
	@Test
	public void createNewWithCorrectValuesAndNoExtraFields() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		SellItem newListing=(SellItem) persistenceAdapter.getListingById(newId);
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameSellItem,newListing.getType());
		assertEquals(Constants.allItemConditions.get(0),newListing.getItemCondition());
		assertEquals(correctListingDataForSellItemCreateWithoutExtraFields.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForSellItemCreateWithoutExtraFields.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForSellItemCreateWithoutExtraFields.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForSellItemCreateWithoutExtraFields.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals((double) 0,newListing.getPrice(),0.001);
		assertEquals(Constants.listingKindOffering,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void createNewWithCorrectValuesAndAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored, 0);
		SellItem newListing=(SellItem) persistenceAdapter.getListingById(newId);
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameSellItem,newListing.getType());
		assertEquals(Constants.allItemConditions.get(1),newListing.getItemCondition());
		assertEquals(correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals((double) 0,newListing.getPrice(),0.001);
		assertEquals(Constants.listingKindOffering,newListing.getKind());
		assertTrue(newListing.isActive());
		assertEquals(correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.getLong(Constants.listingDataFieldDeadLine), newListing.getExpiryDate().getTime(),60000);
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void createNewWithCorrectValuesAndNoOptionalFieldExceptIsActiveAndPictureWhichShouldBeIgnored() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored, 0);
		ServiceOffering newListing=(ServiceOffering) persistenceAdapter.getListingById(newId);
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameServiceOffering,newListing.getType());
		assertEquals(correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals((double) 0,newListing.getPrice(),0.001);
		assertEquals(Constants.listingKindOffering,newListing.getKind());
		assertFalse(newListing.isActive());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void createNewWithcorrectListingDataForRideShareOfferCreateWithNoOptionalFields() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForRideShareOfferCreateWithNoOptionalFields, 0);
		RideSharing newListing=(RideSharing) persistenceAdapter.getListingById(newId);
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameRideSharing,newListing.getType());
		assertEquals(correctListingDataForRideShareOfferCreateWithNoOptionalFields.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),600000);
		assertEquals(correctListingDataForRideShareOfferCreateWithNoOptionalFields.getLong(Constants.listingDataFieldTravelDateAndTime),newListing.getTravelDateAndTime().getTime(),60000);
		assertEquals(correctListingDataForRideShareOfferCreateWithNoOptionalFields.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForRideShareOfferCreateWithNoOptionalFields.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForRideShareOfferCreateWithNoOptionalFields.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(correctListingDataForRideShareOfferCreateWithNoOptionalFields.getString(Constants.listingDataFieldFromLocation),newListing.getFromLocation());
		assertEquals(correctListingDataForRideShareOfferCreateWithNoOptionalFields.getString(Constants.listingDataFieldToLocation),newListing.getToLocation());
		assertEquals(Constants.listingKindOffering,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertEquals(0,newListing.getJourneyStops().size());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void createNewWithcorrectListingDataForRideShareOfferCreateWithAllOptionalFields() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForRideShareOfferCreateWithAllOptionalFields, 0);
		RideSharing newListing=(RideSharing) persistenceAdapter.getListingById(newId);
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameRideSharing,newListing.getType());
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),600000);
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getLong(Constants.listingDataFieldTravelDateAndTime),newListing.getTravelDateAndTime().getTime(),600000);
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getString(Constants.listingDataFieldFromLocation),newListing.getFromLocation());
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getString(Constants.listingDataFieldToLocation),newListing.getToLocation());
		assertEquals(Constants.listingKindOffering,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getJSONArray(Constants.listingDataFieldJourneyStops).get(0),newListing.getJourneyStops().get(0));
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getJSONArray(Constants.listingDataFieldJourneyStops).get(1),newListing.getJourneyStops().get(1));
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getJSONArray(Constants.listingDataFieldJourneyStops).length(),newListing.getJourneyStops().size());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void createNewWithCorrectListingDataForBorrowRequestCreateWithNoOptionalFields() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForBorrowRequestCreateWithNoOptionalFields, 0);
		BorrowRequest newListing=(BorrowRequest) persistenceAdapter.getListingById(newId);
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameBorrowRequest,newListing.getType());
		assertEquals(correctListingDataForBorrowRequestCreateWithNoOptionalFields.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForBorrowRequestCreateWithNoOptionalFields.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForBorrowRequestCreateWithNoOptionalFields.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForBorrowRequestCreateWithNoOptionalFields.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertNull(newListing.getBorrowFromDate());
		assertNull(newListing.getBorrowToDate());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void createNewWithCorrectListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest, 0);
		BorrowRequest newListing=(BorrowRequest) persistenceAdapter.getListingById(newId);
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameBorrowRequest,newListing.getType());
		assertEquals(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.getLong(Constants.listingDataFieldBorrowFromDate),newListing.getBorrowFromDate().getTime(),60000);
		assertEquals(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.getLong(Constants.listingDataFieldBorrowToDate),newListing.getBorrowToDate().getTime(),60000);
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void createNewWithCorrectListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture, 0);
		PurchaseRequest newListing=(PurchaseRequest) persistenceAdapter.getListingById(newId);
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNamePurchaseRequest,newListing.getType());
		assertEquals(Constants.allItemConditions.get(0),newListing.getItemCondition());
		assertEquals(correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void createNewWithCorrectListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture, 0);
		PurchaseRequest newListing=(PurchaseRequest) persistenceAdapter.getListingById(newId);
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNamePurchaseRequest,newListing.getType());
		assertEquals(Constants.allItemConditions.get(1),newListing.getItemCondition());
		assertEquals(correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void createNewWithCorrectListingDataForReturningRecreationRequestCreateWithNoOptionalField() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForReturningRecreationRequestCreateWithNoOptionalField, 0);
		ReturningRecreationRequest newListing=(ReturningRecreationRequest) persistenceAdapter.getListingById(newId);
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameReturningRecreationRequest,newListing.getType());
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertEquals(Constants.allFreeTimeActivityCategories.get(0),newListing.getCategory());
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldActivityLocation),newListing.getActivityLocation());
		assertEquals(Constants.allRegularityOptions.get(0),newListing.getRegularity());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void createNewWithCorrectListingDataForReturningRecreationRequestCreateWithAllOptionalFields() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields, 0);
		ReturningRecreationRequest newListing=(ReturningRecreationRequest) persistenceAdapter.getListingById(newId);
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameReturningRecreationRequest,newListing.getType());
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.getLong(Constants.listingDataFieldStartDate),newListing.getStartDate().getTime(),60000);
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.getLong(Constants.listingDataFieldEndDate),newListing.getEndDate().getTime(),60000);
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertEquals(Constants.allFreeTimeActivityCategories.get(0),newListing.getCategory());
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldActivityLocation),newListing.getActivityLocation());
		assertEquals(Constants.allRegularityOptions.get(1),newListing.getRegularity());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void createNewWithCorrectListingDataForSingleRecreationRequestCreateWithNoOptionalField() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSingleRecreationRequestCreateWithNoOptionalField, 0);
		SingleRecreationRequest newListing=(SingleRecreationRequest) persistenceAdapter.getListingById(newId);
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameSingleRecreationRequest,newListing.getType());
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertEquals(Constants.allFreeTimeActivityCategories.get(0),newListing.getCategory());
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldActivityLocation),newListing.getActivityLocation());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void createNewWithCorrectListingDataForSingleRecreationRequestCreateWithAllOptionalFields() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields, 0);
		SingleRecreationRequest newListing=(SingleRecreationRequest) persistenceAdapter.getListingById(newId);
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameSingleRecreationRequest,newListing.getType());
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.getLong(Constants.listingDataFieldHappeningDate),newListing.getHappeningDate().getTime(),60000);
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertEquals(Constants.allFreeTimeActivityCategories.get(0),newListing.getCategory());
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldActivityLocation),newListing.getActivityLocation());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void createNewWithcorrectListingDataForRideShareRequestCreateWithNoOptionalFields() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForRideShareRequestCreateWithNoOptionalField, 0);
		RideShareRequest newListing=(RideShareRequest) persistenceAdapter.getListingById(newId);
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameRideShareRequest,newListing.getType());
		assertEquals(correctListingDataForRideShareRequestCreateWithNoOptionalField.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),600000);
		assertEquals(correctListingDataForRideShareRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForRideShareRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForRideShareRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(correctListingDataForRideShareRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldFromLocation),newListing.getFromLocation());
		assertEquals(correctListingDataForRideShareRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldToLocation),newListing.getToLocation());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void createNewWithcorrectListingDataForRideShareRequestCreateWithAllOptionalFields() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForRideShareRequestCreateWithAllOptionalFields, 0);
		RideShareRequest newListing=(RideShareRequest) persistenceAdapter.getListingById(newId);
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameRideShareRequest,newListing.getType());
		assertEquals(correctListingDataForRideShareRequestCreateWithAllOptionalFields.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),600000);
		assertEquals(correctListingDataForRideShareRequestCreateWithAllOptionalFields.getLong(Constants.listingDataFieldTravelDateAndTime),newListing.getTravelDateAndTime().getTime(),600000);
		assertEquals(correctListingDataForRideShareRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForRideShareRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForRideShareRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(correctListingDataForRideShareRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldFromLocation),newListing.getFromLocation());
		assertEquals(correctListingDataForRideShareRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldToLocation),newListing.getToLocation());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertEquals((long)0,newListing.getOwner());
	}
	
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataWithWrongTypeOfOneRequiredField() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForSellItemWithWrongTypeOfOneRequiredField, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForSellItemWithAnNotExistingListingType() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForSellItemWithAnNotExistingListingType, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForSellItemWithMissingAttributeListingType() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForSellItemWithMissingAttributeListingType, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForSellItemWithMissingAttributeCreateDate() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForSellItemWithMissingAttributeCreateDate, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForSellItemWithAnExistingListingTypeWhichIsNotSellItem() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForSellItemWithAnExistingListingTypeWhichIsNotSellItem, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForSellItemWithMissingAttributeDescription() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForSellItemWithMissingAttributeDescription, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForSellItemWithMissingAttributeLocation() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForSellItemWithMissingAttributeLocation, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForSellItemWithMissingAttributeTitle() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForSellItemWithMissingAttributeTitle, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForSellItemWithMissingAttributePrice() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForSellItemWithMissingAttributePrice, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForSellItemWithMissingAttributeCondition() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForSellItemWithMissingAttributeCondition, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForSellItemWithNotExistingCondition() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForSellItemWithNotExistingCondition, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataWithWrongTypeOfOneOptionalField() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForSellItemWithWrongTypeOfOneOptionalField, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForServiceOfferWithMissingAttributePrice() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForServiceOfferWithMissingAttributePrice, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForRideShareOfferWithMissingAttributeFromLocation() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForRideShareOfferWithMissingAttributeFromLocation, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForRideShareOfferWithMissingAttributeToLocation() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForRideShareOfferWithMissingAttributeToLocation, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForRideShareOfferWithMissingAttributeTravelDateAndTime() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForRideShareOfferWithMissingAttributeTravelDateAndTime, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForPurchaseRequestWithMissingAttributeCondition() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForPurchaseRequestWithMissingAttributeCondition, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForPurchaseRequestWithNotExistingCondition() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForPurchaseRequestWithNotExistingCondition, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForReturningRecreationRequestWithMissingAttributeRegularity() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForReturningRecreationRequestWithMissingAttributeRegularity, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForReturningRecreationRequestWithNotExistingRegularity() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForReturningRecreationRequestWithNotExistingRegularity, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForReturningRecreationRequestWithMissingAttributeCategory() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForReturningRecreationRequestWithMissingAttributeCategory, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForReturningRecreationRequestWithNotExistingCategory() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForReturningRecreationRequestWithNotExistingCategory, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForRideShareRequestWithMissingAttributeFromLocation() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForRideShareRequestWithMissingAttributeFromLocation, 0);
	}
	
	@Test(expected=WrongFormatException.class)
	public void createNewWithListingDataForRideShareRequestWithMissingAttributeToLocation() throws WrongFormatException{
		persistenceAdapter.persistNewListing(listingDataForRideShareRequestWithMissingAttributeToLocation, 0);
	}
	
	//-------------------------------------getListingById--------------------------------------------
	
	//Because getListingById is very well used in the persistNewListingTest it's basically tested with valid inputs
	
	@Test(expected=ListingNotFoundException.class)
	public void testGetListingByIdWithNotExistingId() throws ListingNotFoundException{
		persistenceAdapter.getListingById(-1);
	}
	
	//-------------------------------------deleteListingById--------------------------------------------
	
	@Test(expected=ListingNotFoundException.class)
	public void testDeleteListingWithValidId() throws ListingNotFoundException, WrongFormatException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		try{
			persistenceAdapter.deleteListingById(newId);
		}catch(ListingNotFoundException e){
			fail();
		}
		persistenceAdapter.getListingById(newId);
	}
	
	@Test(expected=ListingNotFoundException.class)
	public void testDeleteListingByIdWithNotExistingId() throws ListingNotFoundException{
		persistenceAdapter.deleteListingById(-1);
	}
	
	//-------------------------------------isOwnerOfListing--------------------------------------------
	
	@Test
	public void testIsOwnerOfListingWithExpectedResultTrue() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		assertTrue(persistenceAdapter.isOwnerOfListing(newId, 0));
	}
	
	@Test
	public void testIsOwnerOfListingWithExpectedResultFalse() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		assertFalse(persistenceAdapter.isOwnerOfListing(newId, 1));
	}
	
	//-------------------------------------uploadMainImage--------------------------------------------
	
	@Test
	public void testUploadPictureWithPointpngAndThenChangeThePictureToAPointjpg() throws IOException, ListingNotFoundException, WrongFormatException, MainImageNotSupportedException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.uploadMainImage(fileDataOfAPointpng, newId, NameOfAPointpngFile);
		
		SellItem ListingWithUploadedImage=(SellItem) persistenceAdapter.getListingById(newId);
		String publicPathOfUploadedPicture=ListingWithUploadedImage.getPicture();
		assertEquals(publicPathOfUploadedPicture.substring(publicPathOfUploadedPicture.length()-4),
				NameOfAPointpngFile.substring(NameOfAPointpngFile.length()-4));
		
		persistenceAdapter.uploadMainImage(fileDataOfAPointjpg, newId, NameOfAPointjpgFile);
		
		ListingWithUploadedImage=(SellItem) persistenceAdapter.getListingById(newId);
		publicPathOfUploadedPicture=ListingWithUploadedImage.getPicture();
		assertEquals(publicPathOfUploadedPicture.substring(publicPathOfUploadedPicture.length()-4),
				NameOfAPointjpgFile.substring(NameOfAPointjpgFile.length()-4));
	}
	
//	@Test
//	public void testUploadMainImageWithPointjpeg() throws WrongFormatException, IOException, ListingNotFoundException, MainImageNotSupportedException{
//		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
//		persistenceAdapter.uploadMainImage(fileDataOfAPointjpeg, newId, NameOfAPointjpegFile);
//		
//		SellItem ListingWithUploadedImage=(SellItem) persistenceAdapter.getListingById(newId);
//		String publicPathOfUploadedPicture=ListingWithUploadedImage.getPicture();
//		assertEquals(publicPathOfUploadedPicture.substring(publicPathOfUploadedPicture.length()-5),
//				NameOfAPointjpegFile.substring(NameOfAPointjpegFile.length()-5));
//	}
	
	@Test
	public void testUploadPictureWithPointPNG() throws WrongFormatException, IOException, ListingNotFoundException, MainImageNotSupportedException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.uploadMainImage(fileDataOfAPointPNG, newId, NameOfAPointPNGFile);
		
		SellItem ListingWithUploadedImage=(SellItem) persistenceAdapter.getListingById(newId);
		String publicPathOfUploadedPicture=ListingWithUploadedImage.getPicture();
		assertEquals(publicPathOfUploadedPicture.substring(publicPathOfUploadedPicture.length()-4).toLowerCase(),
				NameOfAPointPNGFile.substring(NameOfAPointPNGFile.length()-4).toLowerCase());
	}
	
//	@Test
//	public void testUploadPictureWithPointJPG() throws WrongFormatException, IOException, ListingNotFoundException, MainImageNotSupportedException{
//		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
//		persistenceAdapter.uploadMainImage(fileDataOfAPointJPG, newId, NameOfAPointJPGFile);
//		
//		SellItem ListingWithUploadedImage=(SellItem) persistenceAdapter.getListingById(newId);
//		String publicPathOfUploadedPicture=ListingWithUploadedImage.getPicture();
//		assertEquals(publicPathOfUploadedPicture.substring(publicPathOfUploadedPicture.length()-4),
//				NameOfAPointJPGFile.substring(NameOfAPointJPGFile.length()-4));
//	}
//	
//	@Test
//	public void testUploadPictureWithPointJPEG() throws WrongFormatException, IOException, ListingNotFoundException, MainImageNotSupportedException{
//		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
//		persistenceAdapter.uploadMainImage(fileDataOfAPointJPEG, newId, NameOfAPointJPEGFile);
//		
//		SellItem ListingWithUploadedImage=(SellItem) persistenceAdapter.getListingById(newId);
//		String publicPathOfUploadedPicture=ListingWithUploadedImage.getPicture();
//		assertEquals(publicPathOfUploadedPicture.substring(publicPathOfUploadedPicture.length()-5),
//				NameOfAPointJPEGFile.substring(NameOfAPointJPEGFile.length()-5));
//	}
	
	@Test(expected=ListingNotFoundException.class)
	public void testUploadPictureWithNotExistingListingId() throws IOException, ListingNotFoundException, MainImageNotSupportedException{
		persistenceAdapter.uploadMainImage(fileDataOfAPointpng, -1, NameOfAPointpngFile);
	}
	
	@Test(expected=IOException.class)
	public void testUploadPictureWithTextFile() throws IOException, ListingNotFoundException, WrongFormatException, MainImageNotSupportedException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.uploadMainImage(fileDataOfAPointtxt, newId, NameOfAPointtxtFile);
	}
	
	@Test(expected=IOException.class)
	public void testUploadPictureWithTextFile2() throws IOException, ListingNotFoundException, WrongFormatException, MainImageNotSupportedException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.uploadMainImage(fileDataOfAPointtxt, newId, NameOfAPointtxtFile);
	}

	//-------------------------------------uploadMainImage--------------------------------------------
	
	@Test
	public void editWithCorrectValuesAndNoExtraFieldsAndNoChanges() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.edit(newId, correctListingDataForSellItemCreateWithoutExtraFields);
		SellItem newListing=(SellItem) persistenceAdapter.getListingById(newId);
		
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameSellItem,newListing.getType());
		assertEquals(Constants.allItemConditions.get(0),newListing.getItemCondition());
		assertEquals(correctListingDataForSellItemCreateWithoutExtraFields.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForSellItemCreateWithoutExtraFields.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForSellItemCreateWithoutExtraFields.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForSellItemCreateWithoutExtraFields.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals((double) 0,newListing.getPrice(),0.001);
		assertEquals(Constants.listingKindOffering,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void editWithCorrectValuesAndAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.edit(newId, correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored);
		SellItem newListing=(SellItem) persistenceAdapter.getListingById(newId);
		
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameSellItem,newListing.getType());
		assertEquals(Constants.allItemConditions.get(1),newListing.getItemCondition());
		assertEquals(correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals((double) 0,newListing.getPrice(),0.001);
		assertEquals(Constants.listingKindOffering,newListing.getKind());
		assertTrue(newListing.isActive());
		assertEquals(correctListingDataForSellItemCreateWithAllOptionalFieldsPlusOneAdditionalFieldFromTheFieldsListOfAnotherListingTypeThatShouldBeIgnored.getLong(Constants.listingDataFieldDeadLine), newListing.getExpiryDate().getTime(),60000);
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void editWithCorrectValuesAndNoOptionalFieldExceptIsActiveAndPictureWhichShouldBeIgnored() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored, 0);
		persistenceAdapter.edit(newId, correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored);
		ServiceOffering newListing=(ServiceOffering) persistenceAdapter.getListingById(newId);
		
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameServiceOffering,newListing.getType());
		assertEquals(correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals((double) 0,newListing.getPrice(),0.001);
		assertEquals(Constants.listingKindOffering,newListing.getKind());
		assertFalse(newListing.isActive());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void editWithcorrectListingDataForRideShareOfferCreateWithNoOptionalFields() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForRideShareOfferCreateWithAllOptionalFields, 0);
		persistenceAdapter.edit(newId, correctListingDataForRideShareOfferCreateWithNoOptionalFields);
		RideSharing newListing=(RideSharing) persistenceAdapter.getListingById(newId);
		
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameRideSharing,newListing.getType());
		assertEquals(correctListingDataForRideShareOfferCreateWithNoOptionalFields.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),600000);
		assertEquals(correctListingDataForRideShareOfferCreateWithNoOptionalFields.getLong(Constants.listingDataFieldTravelDateAndTime),newListing.getTravelDateAndTime().getTime(),60000);
		assertEquals(correctListingDataForRideShareOfferCreateWithNoOptionalFields.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForRideShareOfferCreateWithNoOptionalFields.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForRideShareOfferCreateWithNoOptionalFields.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(correctListingDataForRideShareOfferCreateWithNoOptionalFields.getString(Constants.listingDataFieldFromLocation),newListing.getFromLocation());
		assertEquals(correctListingDataForRideShareOfferCreateWithNoOptionalFields.getString(Constants.listingDataFieldToLocation),newListing.getToLocation());
		assertEquals(Constants.listingKindOffering,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertEquals(0,newListing.getJourneyStops().size());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void editWithcorrectListingDataForRideShareOfferCreateWithAllOptionalFields() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForRideShareOfferCreateWithNoOptionalFields, 0);
		persistenceAdapter.edit(newId, correctListingDataForRideShareOfferCreateWithAllOptionalFields);
		RideSharing newListing=(RideSharing) persistenceAdapter.getListingById(newId);
		
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameRideSharing,newListing.getType());
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),600000);
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getLong(Constants.listingDataFieldTravelDateAndTime),newListing.getTravelDateAndTime().getTime(),600000);
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getString(Constants.listingDataFieldFromLocation),newListing.getFromLocation());
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getString(Constants.listingDataFieldToLocation),newListing.getToLocation());
		assertEquals(Constants.listingKindOffering,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getJSONArray(Constants.listingDataFieldJourneyStops).get(0),newListing.getJourneyStops().get(0));
		assertEquals(correctListingDataForRideShareOfferCreateWithAllOptionalFields.getJSONArray(Constants.listingDataFieldJourneyStops).get(1),newListing.getJourneyStops().get(1));
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void editWithCorrectListingDataForBorrowRequestCreateWithNoOptionalFields() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest, 0);
		persistenceAdapter.edit(newId, correctListingDataForBorrowRequestCreateWithNoOptionalFields);
		BorrowRequest newListing=(BorrowRequest) persistenceAdapter.getListingById(newId);
		
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameBorrowRequest,newListing.getType());
		assertEquals(correctListingDataForBorrowRequestCreateWithNoOptionalFields.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForBorrowRequestCreateWithNoOptionalFields.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForBorrowRequestCreateWithNoOptionalFields.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForBorrowRequestCreateWithNoOptionalFields.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertNull(newListing.getBorrowFromDate());
		assertNull(newListing.getBorrowToDate());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void editWithCorrectListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForBorrowRequestCreateWithNoOptionalFields, 0);
		persistenceAdapter.edit(newId, correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest);
		BorrowRequest newListing=(BorrowRequest) persistenceAdapter.getListingById(newId);
		
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameBorrowRequest,newListing.getType());
		assertEquals(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.getLong(Constants.listingDataFieldBorrowFromDate),newListing.getBorrowFromDate().getTime(),60000);
		assertEquals(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest.getLong(Constants.listingDataFieldBorrowToDate),newListing.getBorrowToDate().getTime(),60000);
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void editWithCorrectListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture, 0);
		persistenceAdapter.edit(newId, correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture);
		PurchaseRequest newListing=(PurchaseRequest) persistenceAdapter.getListingById(newId);
		
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNamePurchaseRequest,newListing.getType());
		assertEquals(Constants.allItemConditions.get(0),newListing.getItemCondition());
		assertEquals(correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void editWithCorrectListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture, 0);
		persistenceAdapter.edit(newId, correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture);
		PurchaseRequest newListing=(PurchaseRequest) persistenceAdapter.getListingById(newId);
		
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNamePurchaseRequest,newListing.getType());
		assertEquals(Constants.allItemConditions.get(1),newListing.getItemCondition());
		assertEquals(correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForPurchaseRequestCreateWithUsedConditionAndAPicture.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void editWithCorrectListingDataForReturningRecreationRequestCreateWithNoOptionalField() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields, 0);
		persistenceAdapter.edit(newId, correctListingDataForReturningRecreationRequestCreateWithNoOptionalField);
		ReturningRecreationRequest newListing=(ReturningRecreationRequest) persistenceAdapter.getListingById(newId);
		
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameReturningRecreationRequest,newListing.getType());
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertEquals(Constants.allFreeTimeActivityCategories.get(0),newListing.getCategory());
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldActivityLocation),newListing.getActivityLocation());
		assertEquals(Constants.allRegularityOptions.get(0),newListing.getRegularity());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void editWithCorrectListingDataForReturningRecreationRequestCreateWithAllOptionalFields() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForReturningRecreationRequestCreateWithNoOptionalField, 0);
		persistenceAdapter.edit(newId, correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields);
		ReturningRecreationRequest newListing=(ReturningRecreationRequest) persistenceAdapter.getListingById(newId);
		
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameReturningRecreationRequest,newListing.getType());
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.getLong(Constants.listingDataFieldStartDate),newListing.getStartDate().getTime(),60000);
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.getLong(Constants.listingDataFieldEndDate),newListing.getEndDate().getTime(),60000);
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertEquals(Constants.allFreeTimeActivityCategories.get(0),newListing.getCategory());
		assertEquals(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldActivityLocation),newListing.getActivityLocation());
		assertEquals(Constants.allRegularityOptions.get(1),newListing.getRegularity());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void editWithCorrectListingDataForSingleRecreationRequestCreateWithNoOptionalField() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields, 0);
		persistenceAdapter.edit(newId, correctListingDataForSingleRecreationRequestCreateWithNoOptionalField);
		SingleRecreationRequest newListing=(SingleRecreationRequest) persistenceAdapter.getListingById(newId);
		
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameSingleRecreationRequest,newListing.getType());
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertEquals(Constants.allFreeTimeActivityCategories.get(0),newListing.getCategory());
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldActivityLocation),newListing.getActivityLocation());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void editWithCorrectListingDataForSingleRecreationRequestCreateWithAllOptionalFields() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSingleRecreationRequestCreateWithNoOptionalField, 0);
		persistenceAdapter.edit(newId, correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields);
		SingleRecreationRequest newListing=(SingleRecreationRequest) persistenceAdapter.getListingById(newId);
		
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameSingleRecreationRequest,newListing.getType());
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),60000);
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.getLong(Constants.listingDataFieldHappeningDate),newListing.getHappeningDate().getTime(),60000);
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertEquals(Constants.allFreeTimeActivityCategories.get(0),newListing.getCategory());
		assertEquals(correctListingDataForSingleRecreationRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldActivityLocation),newListing.getActivityLocation());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertNull(newListing.getPicture());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void editWithcorrectListingDataForRideShareRequestCreateWithNoOptionalFields() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForRideShareRequestCreateWithAllOptionalFields, 0);
		persistenceAdapter.edit(newId, correctListingDataForRideShareRequestCreateWithNoOptionalField);
		RideShareRequest newListing=(RideShareRequest) persistenceAdapter.getListingById(newId);
		
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameRideShareRequest,newListing.getType());
		assertEquals(correctListingDataForRideShareRequestCreateWithNoOptionalField.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),600000);
		assertEquals(correctListingDataForRideShareRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForRideShareRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForRideShareRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(correctListingDataForRideShareRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldFromLocation),newListing.getFromLocation());
		assertEquals(correctListingDataForRideShareRequestCreateWithNoOptionalField.getString(Constants.listingDataFieldToLocation),newListing.getToLocation());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertEquals((long)0,newListing.getOwner());
	}
	
	@Test
	public void editWithcorrectListingDataForRideShareRequestCreateWithAllOptionalFields() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForRideShareRequestCreateWithNoOptionalField, 0);
		persistenceAdapter.edit(newId, correctListingDataForRideShareRequestCreateWithAllOptionalFields);
		RideShareRequest newListing=(RideShareRequest) persistenceAdapter.getListingById(newId);
		
		assertEquals(newId,newListing.getListingId());
		assertEquals(Constants.listingTypeNameRideShareRequest,newListing.getType());
		assertEquals(correctListingDataForRideShareRequestCreateWithAllOptionalFields.getLong(Constants.listingDataFieldCreateDate),newListing.getCreateDate().getTime(),600000);
		assertEquals(correctListingDataForRideShareRequestCreateWithAllOptionalFields.getLong(Constants.listingDataFieldTravelDateAndTime),newListing.getTravelDateAndTime().getTime(),600000);
		assertEquals(correctListingDataForRideShareRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldDescription),newListing.getDescription());
		assertEquals(correctListingDataForRideShareRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldTitle),newListing.getTitle());
		assertEquals(correctListingDataForRideShareRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldLocation),newListing.getLocation());
		assertEquals(correctListingDataForRideShareRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldFromLocation),newListing.getFromLocation());
		assertEquals(correctListingDataForRideShareRequestCreateWithAllOptionalFields.getString(Constants.listingDataFieldToLocation),newListing.getToLocation());
		assertEquals(Constants.listingKindRequest,newListing.getKind());
		assertTrue(newListing.isActive());
		assertNull(newListing.getExpiryDate());
		assertEquals((long)0,newListing.getOwner());
	}
	
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataWithWrongTypeOfOneRequiredField() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.edit(newId, listingDataForSellItemWithWrongTypeOfOneRequiredField);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForSellItemWithAnNotExistingListingType() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.edit(newId, listingDataForSellItemWithAnNotExistingListingType);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForSellItemWithMissingAttributeListingType() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.edit(newId, listingDataForSellItemWithMissingAttributeListingType);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForSellItemWithMissingAttributeCreateDate() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.edit(newId, listingDataForSellItemWithMissingAttributeCreateDate);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForSellItemWithAnExistingListingTypeWhichIsNotSellItem() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.edit(newId, listingDataForSellItemWithAnExistingListingTypeWhichIsNotSellItem);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForSellItemWithMissingAttributeDescription() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.edit(newId, listingDataForSellItemWithMissingAttributeDescription);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForSellItemWithMissingAttributeLocation() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.edit(newId, listingDataForSellItemWithMissingAttributeLocation);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForSellItemWithMissingAttributeTitle() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.edit(newId, listingDataForSellItemWithMissingAttributeTitle);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForSellItemWithMissingAttributePrice() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.edit(newId, listingDataForSellItemWithMissingAttributePrice);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForSellItemWithMissingAttributeCondition() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.edit(newId, listingDataForSellItemWithMissingAttributeCondition);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForSellItemWithNotExistingCondition() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.edit(newId, listingDataForSellItemWithNotExistingCondition);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataWithWrongTypeOfOneOptionalField() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForSellItemCreateWithoutExtraFields, 0);
		persistenceAdapter.edit(newId, listingDataForSellItemWithWrongTypeOfOneOptionalField);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForServiceOfferWithMissingAttributePrice() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored, 0);
		persistenceAdapter.edit(newId, listingDataForServiceOfferWithMissingAttributePrice);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForRideShareOfferWithMissingAttributeFromLocation() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForRideShareOfferCreateWithAllOptionalFields, 0);
		persistenceAdapter.edit(newId, listingDataForRideShareOfferWithMissingAttributeFromLocation);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForRideShareOfferWithMissingAttributeToLocation() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForRideShareOfferCreateWithAllOptionalFields, 0);
		persistenceAdapter.edit(newId, listingDataForRideShareOfferWithMissingAttributeToLocation);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForRideShareOfferWithMissingAttributeTravelDateAndTime() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForRideShareOfferCreateWithAllOptionalFields, 0);
		persistenceAdapter.edit(newId, listingDataForRideShareOfferWithMissingAttributeTravelDateAndTime);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForPurchaseRequestWithMissingAttributeCondition() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture, 0);
		persistenceAdapter.edit(newId, listingDataForPurchaseRequestWithMissingAttributeCondition);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForPurchaseRequestWithNotExistingCondition() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForPurchaseRequestCreateWithNewConditionAndNoPicture, 0);
		persistenceAdapter.edit(newId, listingDataForPurchaseRequestWithNotExistingCondition);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForReturningRecreationRequestWithMissingAttributeRegularity() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields, 0);
		persistenceAdapter.edit(newId, listingDataForReturningRecreationRequestWithMissingAttributeRegularity);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForReturningRecreationRequestWithNotExistingRegularity() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields, 0);
		persistenceAdapter.edit(newId, listingDataForReturningRecreationRequestWithNotExistingRegularity);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForReturningRecreationRequestWithMissingAttributeCategory() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields, 0);
		persistenceAdapter.edit(newId, listingDataForReturningRecreationRequestWithMissingAttributeCategory);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForReturningRecreationRequestWithNotExistingCategory() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForReturningRecreationRequestCreateWithAllOptionalFields, 0);
		persistenceAdapter.edit(newId, listingDataForReturningRecreationRequestWithNotExistingCategory);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForRideShareRequestWithMissingAttributeFromLocation() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForRideShareRequestCreateWithAllOptionalFields, 0);
		persistenceAdapter.edit(newId, listingDataForRideShareRequestWithMissingAttributeFromLocation);
	}
	
	@Test(expected=WrongFormatException.class)
	public void editWithListingDataForRideShareRequestWithMissingAttributeToLocation() throws WrongFormatException, ListingNotFoundException{
		long newId=persistenceAdapter.persistNewListing(correctListingDataForRideShareRequestCreateWithAllOptionalFields, 0);
		persistenceAdapter.edit(newId, listingDataForRideShareRequestWithMissingAttributeToLocation);
	}
	
	@Test(expected=WrongFormatException.class)
	public void testEditWithChangingListingType() throws WrongFormatException, ListingNotFoundException{
		//We test this with an ServiceOffer to another listingType because ServiceOffer has only optional field so no WrongFormatException is thrown because of missing attributes, but only because of the changing listingType
		long newId=persistenceAdapter.persistNewListing(correctListingDataForServiceOfferCreateWithNoOptionalFieldsExceptIsActiveAndPictureWhichShouldBeIgnored, 0);
		persistenceAdapter.edit(newId, correctListingDataForRideShareRequestCreateWithAllOptionalFields);
	}
	
	@Test(expected=ListingNotFoundException.class)
	public void testEditWithNotExistingListingId() throws WrongFormatException, ListingNotFoundException{
		persistenceAdapter.edit(-1, correctListingDataForRideShareRequestCreateWithAllOptionalFields);
	}
	
	//---------------------------------------------------comment------------------------------------------------------------------------------
	
	@Test
	public void testCommentWithValidInput() throws ListingNotFoundException{
		long listingId=persistenceAdapter.persistNewListing(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest, 0);
		persistenceAdapter.comment(validCommentData, 1, listingId);
		Listing commentedListing=persistenceAdapter.getListingById(listingId);
		
		assertEquals(1,commentedListing.getComments().size());
		Comment createdComment=commentedListing.getComments().iterator().next();
		assertEquals(validCommentData.getString(Constants.commentDataFieldMessage),
				createdComment.getText());
		assertEquals(validCommentData.getLong(Constants.commentDataFieldDate), createdComment.getCreateDate().getTime(), 60000);
		assertEquals(1, createdComment.getAuthorId());
	}
	
	@Test(expected=WrongFormatException.class)
	public void testCommentWithMissingCreateDate() throws ListingNotFoundException{
		long listingId=persistenceAdapter.persistNewListing(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest, 0);
		persistenceAdapter.comment(commentDataWithMissingCreateDate, 1, listingId);
	}
	
	@Test(expected=WrongFormatException.class)
	public void testCommentWithMissingMessage() throws ListingNotFoundException{
		long listingId=persistenceAdapter.persistNewListing(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest, 0);
		persistenceAdapter.comment(commentDataWithMissingMessage, 1, listingId);
	}
	
	@Test(expected=WrongFormatException.class)
	public void testCommentWithIntMessage() throws ListingNotFoundException{
		long listingId=persistenceAdapter.persistNewListing(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest, 0);
		persistenceAdapter.comment(commentDataWithintMessage, 1, listingId);
	}
	
	@Test(expected=WrongFormatException.class)
	public void testCommentWithStringCreateDate() throws ListingNotFoundException{
		long listingId=persistenceAdapter.persistNewListing(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest, 0);
		persistenceAdapter.comment(commentDataWithStringCreateDate, 1, listingId);
	}
	
	@Test(expected=ListingNotFoundException.class)
	public void testCommentWithNotExistingListingId() throws ListingNotFoundException{
		persistenceAdapter.comment(validCommentData, 1, -1);
	}
	
	//-------------------------------------------deleteComment----------------------------------------------------------------
	
	@Test
	public void testDeleteCommentWithValidCommentId() throws ListingNotFoundException, CommentNotFoundException{
		long listingId=persistenceAdapter.persistNewListing(correctListingDataForBorrowRequestCreateWithAllOptionalFieldsInBorrowRequest, 0);
		persistenceAdapter.comment(validCommentData, 1, listingId);
		long commentId=persistenceAdapter.getListingById(listingId).getComments().iterator().next().getId();
		persistenceAdapter.deleteComment(commentId);
		assertEquals(0,persistenceAdapter.getListingById(listingId).getComments().size());
	}
	
	@Test(expected=CommentNotFoundException.class)
	public void testDeleteCommentWithNotExistingCommentId() throws CommentNotFoundException{
		persistenceAdapter.deleteComment(-1);
	}
	
	

}
