package backendServer.listings;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import backendServer.exceptions.ListingTypeNotRegisteredException;
import backendServer.exceptions.LocationNotRegisteredException;
import backendServer.listings.entities.Listing;
import backendServer.user.User;

/**This class collects information about listings and users given to this class
 * and creates statistics about everything
 * @author Florian Kutz
 *
 */
public class GlobalStatistics {

	private int numberOfActiveListings;
	private int numberOfInactiveListings;
	private int numberOfListings;
	private List<NumberOfListingsWithACommonField> locations, listingTypes;

	private int numberOfUsers;
	private int numberOfAdmins;

	public GlobalStatistics(){
		this.setListingTypes(new LinkedList<NumberOfListingsWithACommonField>());
		this.setLocations(new LinkedList<NumberOfListingsWithACommonField>());
	}
	
	/**You can register one listing with this method. In order to get the correct global statistics you 
	 * have to call this method with every single listing exactly once
	 * @param registeredListing The registered listing
	 */
	public void registerListing(Listing registeredListing){
		this.setNumberOfListings(this.getNumberOfListings() + 1);
		try{
			getLocationObjectWithTheName(registeredListing.getLocation()).increaseNumberOfListings();;
		}catch(LocationNotRegisteredException e){
			this.getLocations().add(new NumberOfListingsWithACommonField(registeredListing.getLocation(), Constants.locationInformationFieldLocationName));
		}
		if(registeredListing.isActive()){
			this.setNumberOfActiveListings(this.getNumberOfActiveListings() + 1);
		}else{
			this.setNumberOfInactiveListings(this.getNumberOfInactiveListings() + 1);
		}
		try{
			getListingTypeObjectWithTheName(registeredListing.getType()).increaseNumberOfListings();
		}catch(ListingTypeNotRegisteredException e){
			this.listingTypes.add(new NumberOfListingsWithACommonField(registeredListing.getType(), Constants.listingTypeInformationFieldListingTypeName));
		}
	}
	
	private List<NumberOfListingsWithACommonField> getLocations() {
		return locations;
	}

	/**You can register one user with this method. In order to get the correct global statistics you 
	 * have to call this method with every single user exactly once
	 * @param registeredUser
	 */
	public void registerUser(User registeredUser){
		this.numberOfUsers++;
		if(registeredUser.isAdmin()){
			this.numberOfAdmins++;
		}
	}
	
	private NumberOfListingsWithACommonField getListingTypeObjectWithTheName(String listingType) throws ListingTypeNotRegisteredException {
		for(int index=0;index<listingTypes.size();index++){
			if(listingTypes.get(index).getFieldValue().equals(listingType)){
				return listingTypes.get(index);
			}
		}
		throw new ListingTypeNotRegisteredException();
	}

	private NumberOfListingsWithACommonField getLocationObjectWithTheName(String location) throws LocationNotRegisteredException {
		for(int index=0;index<locations.size();index++){
			if(locations.get(index).getFieldValue().equals(location)){
				return locations.get(index);
			}
		}
		throw new LocationNotRegisteredException();
	}

	/**This method creates a JSONObject with the collected data.
	 * @return a JSONObject with those fields:
	 * numberOfListings: int
	 * numberOfActiveListings: int
	 * numberOfInactiveListings: int
	 * locations: JSONArray of JSONObject with the fields locationName: string and numberOfListings: int
	 * listingTypes: JSONArray of JSONObject with the fields listingTypeName: string and numberOfListings: int
	 * numberOfUsers: int
	 * numberOfAdmins: int
	 */
	public JSONObject toJSON() {
		JSONObject parsedStatistics=new JSONObject();
		parsedStatistics.put(Constants.globalStatisticsFieldNumberOfListings, this.getNumberOfListings());
		parsedStatistics.put(Constants.globalStatisticsFieldNumberOfActiveListings, this.getNumberOfActiveListings());
		parsedStatistics.put(Constants.globalStatisticsFieldNumberOfInactiveListings, this.getNumberOfInactiveListings());
		parsedStatistics.put(Constants.globalStatisticsFieldLocationInformation, this.makeLocationsJSONArray());
		parsedStatistics.put(Constants.globalStatisticsFieldListingTypeInformation, this.makeListingTypesJSONArray());
		parsedStatistics.put(Constants.globalStatisticsFieldNumberOfUsers, this.getNumberOfUsers());
		parsedStatistics.put(Constants.globalStatisticsFieldNumberOfAdmins, this.getNumberOfAdmins());
		return parsedStatistics;
	}

	private JSONArray makeLocationsJSONArray() {
		JSONArray locationsAsJSONArray=new JSONArray();
		for(int index=0;index<locations.size();index++){
			locationsAsJSONArray.put(locations.get(index).toJSON());
		}
		return locationsAsJSONArray;
	}
	
	private JSONArray makeListingTypesJSONArray() {
		JSONArray listingTypesAsJSONArray=new JSONArray();
		for(int index=0;index<listingTypes.size();index++){
			listingTypesAsJSONArray.put(listingTypes.get(index).toJSON());
		}
		return listingTypesAsJSONArray;
	}

	public int getNumberOfActiveListings() {
		return numberOfActiveListings;
	}

	public void setNumberOfActiveListings(int numberOfActiveListings) {
		this.numberOfActiveListings = numberOfActiveListings;
	}

	public int getNumberOfInactiveListings() {
		return numberOfInactiveListings;
	}

	public void setNumberOfInactiveListings(int numberOfInactiveListings) {
		this.numberOfInactiveListings = numberOfInactiveListings;
	}

	public int getNumberOfListings() {
		return numberOfListings;
	}

	public void setNumberOfListings(int numberOfListings) {
		this.numberOfListings = numberOfListings;
	}

	public int getNumberOfUsers() {
		return numberOfUsers;
	}

	public void setNumberOfUsers(int numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}

	public int getNumberOfAdmins() {
		return numberOfAdmins;
	}

	public void setNumberOfAdmins(int numberOfAdmins) {
		this.numberOfAdmins = numberOfAdmins;
	}
	
	public List<NumberOfListingsWithACommonField> getListingTypes() {
		return listingTypes;
	}

	public void setListingTypes(List<NumberOfListingsWithACommonField> listingTypes) {
		this.listingTypes = listingTypes;
	}

	public void setLocations(List<NumberOfListingsWithACommonField> locations) {
		this.locations = locations;
	}
	
	private class NumberOfListingsWithACommonField{
		private String fieldValue;
		private String fieldName;
		private int numberOfListings;
		NumberOfListingsWithACommonField(String fieldValue, String fieldName){
			this.setFieldValue(fieldValue);
			this.setFieldName(fieldName);
			numberOfListings=1;
		}
		public JSONObject toJSON() {
			JSONObject asJSON=new JSONObject();
			asJSON.put(fieldName, fieldValue);
			asJSON.put(Constants.listingTypeOrLocationInformationFieldNumberOfListings, numberOfListings);
			return asJSON;
		}
		public int getNumberOfListings() {
			return numberOfListings;
		}
		public void increaseNumberOfListings() {
			this.numberOfListings++;
		}
		public String getFieldValue() {
			return fieldValue;
		}
		public void setFieldValue(String fieldValue) {
			this.fieldValue = fieldValue;
		}

		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
	}

}
