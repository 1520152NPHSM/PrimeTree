package backendServer.listings.objectControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backendServer.exceptions.ListingNotFoundException;
import backendServer.exceptions.NoImageGallerySupportedException;
import backendServer.listings.Constants;
import backendServer.listings.entities.RideShareRequest;
import backendServer.listings.repositories.RideShareRequestRepository;
/**This sub-class of RequestListingObjectController controlls all RideShareRequest-Listings*/
@Component
public class RideShareRequestObjectController extends RequestListingObjectController{
	
	@Override
	protected RideShareRequest createNew() {
		return new RideShareRequest();
	}
	
	RideShareRequestRepository listingRepository;

	@Autowired
	public void setListingRepository(RideShareRequestRepository listingRepository) {
		this.listingRepository=listingRepository;
		super.listingRepository=this.listingRepository;
	}
	
	public RideShareRequestObjectController(){
		listingType=Constants.listingTypeNameRideShareRequest;
	}

}
