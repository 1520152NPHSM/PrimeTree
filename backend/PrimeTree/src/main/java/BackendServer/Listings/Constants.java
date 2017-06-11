package BackendServer.Listings;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;

import BackendServer.Listings.Entities.Listing;

/**This class defines all Constants relevant for the BackendServer as static fields
 * 
 * @author Florian Kutz
 * */
public class Constants {
	
	//The following Strings are the names of the required fields in all JSONObjects representing listingData
	public static final String
	listingDataFieldCondition="condition",
	listingDataFieldComments="comments",
	listingDataFieldDescription="description",
	listingDataFieldDeadLine="expiryDate",
	listingDataFieldLocation="location",
	listingDataFieldPrice="price",
	listingDataFieldTitle="title",
	listingDataFieldListingType="type",
	listingDataFieldCreateDate="createDate",
	listingDataFieldActive="isActive",
	listingDataFieldCreator="creatorID",
	listingDataFieldId="id",
	listingDataFieldImageGallery="imageGallery",
	listingDataFieldPicture="mainImage",
	listingDataFieldFromLocation="fromLocation",
	listingDataFieldJourneyStops="journeyStops",
	listingDataFieldToLocation="toLocation",
	listingDataFieldAvailableSeats="availableSeats",
	listingDataFieldTravelDateAndTime="travelDateAndTime",
	listingDataFieldBorrowFromDate="borrowFromDate",
	listingDataFieldBorrowToDate="borrowToDate",
	listingDataFieldActivityLocation="activityLocation",
	listingDataFieldStartDate="startDate",
	listingDataFieldEndDate="endDate",
	listingDataFieldHappeningDate="happeningDate",
	listingDataFieldFreeTimeActivityCategory="freeTimeActivityCategory",
	listingDataFieldRegularity="regularity";
	
	public static final String
	listingKindOffering="Offering",
	listingKindRequest="Request";
	
	//The following list define enumerations
	public static final List<String>
	allItemConditions=allItemConditions(),
	allRegularityOptions=allRegularityOptions(),
	allFreeTimeActivityCategories=allFreeTimeActivityCategories();
	
	//The following Strings are the names of all listingTypes
	public static final String 
	listingTypeNameSellItem="SaleOffer",
	listingTypeNameRideSharing="RideShareOffer",
	listingTypeNameServiceOffering="ServiceOffer",
	listingTypeNameBorrowRequest="BorrowRequest",
	listingTypeNameRideShareRequest="RideShareRequest",
	listingTypeNamePurchaseRequest="PurchaseRequest",
	listingTypeNameReturningRecreationRequest="ReturningRecreationRequest",
	listingTypeNameSingleRecreationRequest="SingleRecreationRequest";
	
	//The following Strings are the names of the sort-options you can sort listings with
	public static final String
	sortOptionId="id",
	sortOptionPrice_Desc="price_desc",
	sortOptionPrice_Asc="price_asc",
	sortOptionAlphabetical_Asc="alphabetical_asc",
	sortOptionAlphabetical_Desc="alphabetical_desc",
	sortOptionDate_Asc="date_asc",
	sortOptionDate_Desc="date_desc",
	sortOptionLocation_Asc="location_asc",
	sortOptionLoction_Desc="location_desc";
	
	//The following Strings are the names of the filter-options you can filter listings with
	public static final String
	listingSearchResultFieldListings="listings",
	listingSearchResultFieldPrice_Min="price_min",
	listingSearchResultFieldPrice_Max="price_max",
	listingSearchResultFieldCount="count",
	listingSearchResultFieldPages="pages",
	listingSearchResultFieldpageNumber="pageNumber";
	
	//The following Strings are the names of the fields in a JSONObject representing a user
	public static final String
	userFieldId="userId",
	userFieldFirstName="firstName",
	userFieldLastName="lastName",
	userFieldIsAdmin="isAdmin",
	userFieldEMail="eMail",
	userFieldPicture="userImage",
	userFieldPhone="phone",
	userFieldLocation="location",
	userFieldPosition="position";

	//The following Strings are the names of the fields in a JSONObject representing a comment
	public static final String 
	commentDataFieldDate="createDate",
	commentDataFieldCommentId="commentId",
	commentDataFieldUserId="userId",
	commentDataFieldMessage="message",
	commentDataFieldUserImage="userImage";

	//This int is the number of listings shown in a single page
	public static final int pageSize=50;

	//The following Strings are the names of the fields in a responseBody
	public static final String 
	responseFieldUsers="users",
	responseFieldFavouriteList="ids",
	responseFieldNewListingId="id";

	public static final int numberOfImagesPerGallery = 4;

	public static final String 
	globalStatisticsFieldLocationInformation = "locations",
	globalStatisticsFieldNumberOfListings = "numberOfListings",
	globalStatisticsFieldNumberOfActiveListings = "numberOfActiveListings",
	globalStatisticsFieldNumberOfInactiveListings = "numberOfInactiveListings",
	globalStatisticsFieldListingTypeInformation="listingTypes",
	globalStatisticsFieldNumberOfUsers="numberOfUsers",
	globalStatisticsFieldNumberOfAdmins="numberOfAdmins";

	public static final String 
	locationInformationFieldLocationName="locationName",
	locationInformationFieldNumberOfListings="numberOfListings";

	public static final String 
	listingTypeInformationFieldListingTypeName="listingTypeName",
	listingTypeInformationFieldNumberOfListings="numberOfListings";

	private static List<String> allItemConditions() {
		List<String> allOptions=new LinkedList<String>();
		allOptions.add("New");
		allOptions.add("Used");
		return allOptions;
	}

	private static List<String> allFreeTimeActivityCategories() {
		List<String> allOptions=new LinkedList<String>();
		allOptions.add("Sport");
		allOptions.add("Social");
		allOptions.add("Outdoors");
		allOptions.add("Events");
		return allOptions;
	}

	private static List<String> allRegularityOptions() {
		List<String> allOptions=new LinkedList<String>();
		allOptions.add("Daily");
		allOptions.add("Weeky");
		allOptions.add("Fortnightly");
		allOptions.add("Monthly");
		allOptions.add("Randomly");
		return allOptions;
	}
	
}
