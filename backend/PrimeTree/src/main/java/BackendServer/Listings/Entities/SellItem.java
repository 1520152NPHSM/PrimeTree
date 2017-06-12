package BackendServer.Listings.Entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.json.JSONArray;
import org.json.JSONObject;

import BackendServer.Exceptions.NoImageGallerySupportedException;
import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.SimpleMethods;

/**This class represents Listings with listingType SellItem*/
@Entity
@Table(name="SellItem")
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class SellItem extends Offering{
	private double price;
	private String picture;
	@ElementCollection
	public List<String> imageGallery;
	private String itemCondition;
	
	public SellItem(){
		this.setType(Constants.listingTypeNameSellItem);
		if(this.getImageGallery()==null){
			this.setImageGallery(new ArrayList<String>());
			for(int counter=0;counter<Constants.numberOfImagesPerGallery;counter++){
				this.getImageGallery().add(null);
			}
		}
	}
	
	public String getPicture() {
		return picture;
	}

	@Override
	public void setPicture(String picture) {
		this.picture = picture;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public List<String> getImageGallery() {
		return imageGallery;
	}

	public void setImageGallery(ArrayList<String> imageGallery) {
		this.imageGallery =imageGallery;
	}

	/* (non-Javadoc)
	 * @see BackendServer.Listings.Entities.Offering#fillFields(org.json.JSONObject, long)
	 * listingData has otwo additional required fields and two additional optional fields:
	 * price(required): The price for the service
	 * condition(required): The condition of the item
	 * picture(optional): The public picture location
	 * imageGallery(optional): A JSONArray with strings of public image paths
	 */
	@Override
	public void fillFields(JSONObject listingData, long creator) throws WrongFormatException{
		super.fillFields(listingData, creator);
		if(listingData.isNull(Constants.listingDataFieldPrice)){
			throw new WrongFormatException("Missing required fields");
		}
		if(!listingData.isNull(Constants.listingDataFieldCondition) &&
				!Constants.allItemConditions.contains(listingData.getString(Constants.listingDataFieldCondition))){
			throw new WrongFormatException("This condition does not exist");
		}
		if(listingData.getDouble(Constants.listingDataFieldPrice)<0){
			throw new WrongFormatException("Negative Price");
		}
		this.setPrice(listingData.getDouble(Constants.listingDataFieldPrice));
		this.setItemCondition(listingData.getString(Constants.listingDataFieldCondition));
	}
	
	/* (non-Javadoc)
	 * @see BackendServer.Listings.Entities.Listing#toJSON()
	 * listingData has four additional fields in the created JSONObject:
	 * price(required): The price for the service
	 * condition(required): The condition of the item
	 * picture(optional): The public picture location
	 * imageGallery(optional): A JSONArray with strings of public image paths
	 */
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put(Constants.listingDataFieldCondition, this.getItemCondition());
		json.put(Constants.listingDataFieldPrice, this.getPrice());
		json.put(Constants.listingDataFieldPicture, this.getPicture());
		json.put(Constants.listingDataFieldImageGallery, new JSONArray(this.getImageGallery()));
		return json;
	}

	public String getItemCondition() {
		return itemCondition;
	}

	public void setItemCondition(String itemCondition) {
		this.itemCondition = itemCondition;
	}
	
}
