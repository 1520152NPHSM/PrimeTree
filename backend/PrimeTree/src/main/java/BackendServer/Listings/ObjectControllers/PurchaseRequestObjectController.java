package BackendServer.Listings.ObjectControllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import BackendServer.Exceptions.ListingNotFoundException;
import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.Entities.PurchaseRequest;
import BackendServer.Listings.Repositories.PurchaseRequestRepository;
/**This sub-class of RequestListingObjectController controlls all PurchaseRequest-Listings*/
@Component
public class PurchaseRequestObjectController extends RequestListingObjectController{

	@Override
	protected PurchaseRequest createNew() {
		return new PurchaseRequest();
	}
	
	PurchaseRequestRepository listingRepository;

	@Autowired
	public void setListingRepository(PurchaseRequestRepository listingRepository) {
		this.listingRepository=listingRepository;
		super.listingRepository=this.listingRepository;
	}
	
	public PurchaseRequestObjectController(){
		listingType=Constants.listingTypeNamePurchaseRequest;
	}

}
