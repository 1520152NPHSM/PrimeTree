package backendServer.listings.objectControllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backendServer.exceptions.ListingNotFoundException;
import backendServer.exceptions.WrongFormatException;
import backendServer.listings.Constants;
import backendServer.listings.entities.ReturningRecreationRequest;
import backendServer.listings.repositories.ReturningRecreationRequestRepository;
/**This sub-class of RequestListingObjectController controlls all ReturningRecreationRequest-Listings*/
@Component
public class ReturningRecreationRequestObjectController extends RequestListingObjectController{
	
	@Override
	protected ReturningRecreationRequest createNew(){
		return new ReturningRecreationRequest();
	}
	
	ReturningRecreationRequestRepository listingRepository;

	@Autowired
	public void setListingRepository(ReturningRecreationRequestRepository listingRepository) {
		this.listingRepository=listingRepository;
		super.listingRepository=this.listingRepository;
	}
	
	public ReturningRecreationRequestObjectController(){
		listingType=Constants.listingTypeNameReturningRecreationRequest;
	}

}
