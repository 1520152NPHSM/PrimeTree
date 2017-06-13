package backendServer.listings.objectControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backendServer.listings.Constants;
import backendServer.listings.entities.ServiceOffering;
import backendServer.listings.repositories.ServiceOfferingRepository;
/**This sub-class of OfferingObjectController controlls all ServiceOffering-Listings*/
@Component
public class ServiceOfferingObjectController extends OfferingObjectController{
	
	@Override
	protected ServiceOffering createNew() {
		return new ServiceOffering();
	}
	
	ServiceOfferingRepository listingRepository;

	@Autowired
	public void setListingRepository(ServiceOfferingRepository listingRepository) {
		this.listingRepository=listingRepository;
		super.listingRepository=this.listingRepository;
	}
	
	public ServiceOfferingObjectController(){
		listingType=Constants.listingTypeNameServiceOffering;
	}

}
