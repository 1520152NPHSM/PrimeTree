package backendServer.listings.objectControllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backendServer.exceptions.ListingNotFoundException;
import backendServer.exceptions.WrongFormatException;
import backendServer.listings.Constants;
import backendServer.listings.entities.PurchaseRequest;
import backendServer.listings.repositories.PurchaseRequestRepository;
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
