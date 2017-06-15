package backendServer.listings.objectControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backendServer.exceptions.ListingNotFoundException;
import backendServer.exceptions.NoImageGallerySupportedException;
import backendServer.listings.Constants;
import backendServer.listings.entities.BorrowRequest;
import backendServer.listings.repositories.BorrowRequestRepository;
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
