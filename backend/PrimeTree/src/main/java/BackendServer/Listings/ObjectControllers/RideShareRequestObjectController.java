package BackendServer.Listings.ObjectControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import BackendServer.Exceptions.ListingNotFoundException;
import BackendServer.Exceptions.NoImageGallerySupportedException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.Entities.RideShareRequest;
import BackendServer.Listings.Repositories.RideShareRequestRepository;
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
