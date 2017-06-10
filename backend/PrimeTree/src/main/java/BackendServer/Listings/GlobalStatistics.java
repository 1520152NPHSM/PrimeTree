package BackendServer.Listings;

import org.json.JSONArray;
import org.json.JSONObject;

import BackendServer.Exceptions.ListingTypeNotRegisteredException;
import BackendServer.Exceptions.LocationNotRegisteredException;
import BackendServer.Listings.Entities.Listing;
import BackendServer.User.User;

public class GlobalStatistics {

	private int numberOfActiveListings;
	private int numberOfInactiveListings;
	private int numberOfListings;
	JSONArray locations;
	JSONArray listingTypes;
	
	private int numberOfUsers;
	private int numberOfAdmins;

	public GlobalStatistics(){
		this.setLocations(new JSONArray());
		this.setListingTypes(new JSONArray());
	}
	
	public void registerListing(Listing registeredListing){
		this.setNumberOfListings(this.getNumberOfListings() + 1);
		try{
			getLocationObjectWithTheName(registeredListing.getLocation()).put(Constants.locationInformationFieldNumberOfListings,
			getLocationObjectWithTheName(registeredListing.getLocation()).getInt(Constants.locationInformationFieldNumberOfListings)+1);
		}catch(LocationNotRegisteredException e){
			JSONObject location=new JSONObject();
			location.put(Constants.locationInformationFieldLocationName, registeredListing.getLocation());
			location.put(Constants.locationInformationFieldNumberOfListings, 1);
			this.getLocations().put(location);
		}
		if(registeredListing.isActive()){
			this.setNumberOfActiveListings(this.getNumberOfActiveListings() + 1);
		}else{
			this.setNumberOfInactiveListings(this.getNumberOfInactiveListings() + 1);
		}
		try{
			getListingTypeObjectWithTheName(registeredListing.getType()).put(Constants.locationInformationFieldNumberOfListings,
			getListingTypeObjectWithTheName(registeredListing.getType()).getInt(Constants.locationInformationFieldNumberOfListings)+1);
		}catch(ListingTypeNotRegisteredException e){
			JSONObject listingType=new JSONObject();
			listingType.put(Constants.listingTypeInformationFieldListingTypeName, registeredListing.getType());
			listingType.put(Constants.listingTypeInformationFieldNumberOfListings, 1);
			this.getListingTypes().put(listingType);
		}
	}
	
	public void registerUser(User registeredUser){
		this.numberOfUsers++;
		if(registeredUser.isAdmin()){
			this.numberOfAdmins++;
		}
	}
	
	private JSONObject getListingTypeObjectWithTheName(String listingType) throws ListingTypeNotRegisteredException {
		for(int index=0;index<locations.length();index++){
			if(locations.getJSONObject(index).getString(Constants.listingTypeInformationFieldListingTypeName).equals(listingType)){
				return listingTypes.getJSONObject(index);
			}
		}
		throw new ListingTypeNotRegisteredException();
	}

	private JSONObject getLocationObjectWithTheName(String location) throws LocationNotRegisteredException {
		for(int index=0;index<locations.length();index++){
			if(locations.getJSONObject(index).getString(Constants.locationInformationFieldLocationName).equals(location)){
				return locations.getJSONObject(index);
			}
		}
		throw new LocationNotRegisteredException();
	}

	public JSONObject toJSON() {
		JSONObject parsedStatistics=new JSONObject();
		parsedStatistics.put(Constants.globalStatisticsFieldNumberOfListings, this.getNumberOfListings());
		parsedStatistics.put(Constants.globalStatisticsFieldNumberOfActiveListings, this.getNumberOfActiveListings());
		parsedStatistics.put(Constants.globalStatisticsFieldNumberOfInactiveListings, this.getNumberOfInactiveListings());
		parsedStatistics.put(Constants.globalStatisticsFieldLocationInformation, this.getLocations());
		parsedStatistics.put(Constants.globalStatisticsFieldListingTypeInformation, this.getListingTypes());
		parsedStatistics.put(Constants.globalStatisticsFieldNumberOfUsers, this.getNumberOfUsers());
		parsedStatistics.put(Constants.globalStatisticsFieldNumberOfAdmins, this.getNumberOfAdmins());
		return parsedStatistics;
	}
	
	public JSONArray getLocations() {
		return locations;
	}

	public void setLocations(JSONArray locations) {
		this.locations = locations;
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

	public JSONArray getListingTypes() {
		return listingTypes;
	}

	public void setListingTypes(JSONArray listingTypes) {
		this.listingTypes = listingTypes;
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

}
