package backendServer.listings.entities;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.json.JSONObject;

import backendServer.clientDatabaseAccess.entities.Location;
import backendServer.exceptions.NoImageGallerySupportedException;
import backendServer.exceptions.WrongFormatException;
import backendServer.listings.Constants;
import backendServer.listings.SimpleMethods;

/**This class represents Listings with listingType SellItem*/
@Entity
@Table(name="PurchaseRequest")
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class PurchaseRequest extends RequestListing{
	private String picture;
	private String itemCondition;
	
	public PurchaseRequest(){
		this.setType(Constants.listingTypeNamePurchaseRequest);
	}
	
	public String getPicture() {
		return picture;
	}

	@Override
	public void setPicture(String picture) {
		this.picture = picture;
	}

	
	@Override
	public void fillFields(JSONObject listingData, long creator) throws WrongFormatException {
		super.fillFields(listingData, creator);
		if(listingData.isNull(Constants.listingDataFieldCondition)){
			throw new WrongFormatException("Missing required fields");
		}
		if(!Constants.allItemConditions.contains(listingData.getString(Constants.listingDataFieldCondition))){
			throw new WrongFormatException("This condition does not exist");
		}
		this.setItemCondition(listingData.getString(Constants.listingDataFieldCondition));
	}
	
	
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put(Constants.listingDataFieldCondition, this.getItemCondition());
		json.put(Constants.listingDataFieldListingType, Constants.listingTypeNameSellItem);
		json.put(Constants.listingDataFieldPicture, this.getPicture());
		return json;
	}

	public String getItemCondition() {
		return itemCondition;
	}

	public void setItemCondition(String itemCondition) {
		this.itemCondition = itemCondition;
	}
	
}
