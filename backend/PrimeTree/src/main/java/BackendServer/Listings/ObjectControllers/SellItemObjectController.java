package BackendServer.Listings.ObjectControllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import BackendServer.Exceptions.ListingNotFoundException;
import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.Entities.SellItem;
import BackendServer.Listings.Repositories.SellItemRepository;
/**This sub-class of OfferingObjectController controlls all SellItem-Listings*/
@Component
public class SellItemObjectController extends OfferingObjectController{
	
	@Override
	protected SellItem createNew() {
		return new SellItem();
	}
	
	SellItemRepository listingRepository;

	@Autowired
	public void setListingRepository(SellItemRepository listingRepository) {
		this.listingRepository=listingRepository;
		super.listingRepository=this.listingRepository;
	}
	
	public SellItemObjectController(){
		listingType=Constants.listingTypeNameSellItem;
	}
	
}
