package backendServer.listings.objectControllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backendServer.exceptions.ListingNotFoundException;
import backendServer.exceptions.WrongFormatException;
import backendServer.listings.Constants;
import backendServer.listings.entities.SingleRecreationRequest;
import backendServer.listings.repositories.SingleRecreationRequestRepository;
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
