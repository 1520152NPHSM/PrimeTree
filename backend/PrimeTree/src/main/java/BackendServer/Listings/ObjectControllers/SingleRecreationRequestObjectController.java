package BackendServer.Listings.ObjectControllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import BackendServer.Exceptions.ListingNotFoundException;
import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.Entities.SingleRecreationRequest;
import BackendServer.Listings.Repositories.SingleRecreationRequestRepository;
/**This sub-class of RequestListingObjectController controlls all SingleRecreationRequest-Listings*/
@Component
public class SingleRecreationRequestObjectController extends RequestListingObjectController{
	
	@Override
	protected SingleRecreationRequest createNew(){
		return new SingleRecreationRequest();
	}
	
	SingleRecreationRequestRepository listingRepository;

	@Autowired
	public void setListingRepository(SingleRecreationRequestRepository listingRepository) {
		this.listingRepository=listingRepository;
		super.listingRepository=this.listingRepository;
	}
	
	public SingleRecreationRequestObjectController(){
		listingType=Constants.listingTypeNameSingleRecreationRequest;
	}

}
