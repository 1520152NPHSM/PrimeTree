package BackendServer.Listings.ObjectControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import BackendServer.Exceptions.ListingNotFoundException;
import BackendServer.Exceptions.NoImageGallerySupportedException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.Entities.BorrowRequest;
import BackendServer.Listings.Repositories.BorrowRequestRepository;
/**This sub-class of RequestListingObjectController controlls all BorrowRequest-Listings*/
@Component
public class BorrowRequestObjectController extends RequestListingObjectController{
	
	@Override
	protected BorrowRequest createNew() {
		return new BorrowRequest();
	}
	
	BorrowRequestRepository listingRepository;

	@Autowired
	public void setListingRepository(BorrowRequestRepository listingRepository) {
		this.listingRepository=listingRepository;
		super.listingRepository=this.listingRepository;
	}
	
	public BorrowRequestObjectController(){
		listingType=Constants.listingTypeNameBorrowRequest;
	}

}
