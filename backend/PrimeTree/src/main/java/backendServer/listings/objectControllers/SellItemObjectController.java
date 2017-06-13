package backendServer.listings.objectControllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backendServer.exceptions.ListingNotFoundException;
import backendServer.exceptions.WrongFormatException;
import backendServer.listings.Constants;
import backendServer.listings.entities.SellItem;
import backendServer.listings.repositories.SellItemRepository;
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
