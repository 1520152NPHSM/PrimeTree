package BackendServer.Listings.ObjectControllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import BackendServer.Exceptions.ListingNotFoundException;
import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.Entities.ReturningRecreationRequest;
import BackendServer.Listings.Repositories.ReturningRecreationRequestRepository;
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
