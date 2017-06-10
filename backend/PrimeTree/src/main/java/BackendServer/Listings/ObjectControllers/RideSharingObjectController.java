package BackendServer.Listings.ObjectControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import BackendServer.Listings.Constants;
import BackendServer.Listings.Entities.RideSharing;
import BackendServer.Listings.Repositories.RideSharingRepository;
/**This sub-class of OfferingObjectController controlls all RideSharing-Listings*/
@Component
public class RideSharingObjectController extends OfferingObjectController{
	
	@Override
	protected RideSharing createNew() {
		return new RideSharing();
	}
	
	RideSharingRepository listingRepository;

	@Autowired
	public void setListingRepository(RideSharingRepository listingRepository) {
		this.listingRepository=listingRepository;
		super.listingRepository=this.listingRepository;
	}
	
	public RideSharingObjectController(){
		listingType=Constants.listingTypeNameRideSharing;
	}

}
