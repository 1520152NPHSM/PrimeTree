package BackendServer.Listings.Entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.json.JSONArray;
import org.json.JSONObject;

import BackendServer.Exceptions.NoImageGallerySupportedException;
import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.SimpleMethods;

/**This class represents Listings with listingType ServiceOffering*/
@Entity
@Table(name="ServiceOffering")
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class ServiceOffering extends Offering {

	@ElementCollection
	private List<String> imageGallery;
	private String picture;
	private double price;
	
	public ServiceOffering(){
		this.setType(Constants.listingTypeNameServiceOffering);
		if(this.getImageGallery()==null){
			this.setImageGallery(new ArrayList<String>());
			for(int counter=0;counter<Constants.numberOfImagesPerGallery;counter++){
				this.getImageGallery().add(null);
			}
		}
	}

	@Override
	public List<String> getImageGallery() {
		return imageGallery;
	}

	public void setImageGallery(ArrayList<String> imageGallery) {
		this.imageGallery =imageGallery;
		for(int imagesLeft=this.imageGallery.size();imagesLeft<Constants.numberOfImagesPerGallery;imagesLeft++){
			imageGallery.add(imageGallery.size(), null);
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

	public void setPrice(double d) {
		this.price = d;
	}
	
	/* (non-Javadoc)
	 * @see BackendServer.Listings.Entities.Offering#fillFields(org.json.JSONObject, long)
	 * listingData has one additional required field and two additional optional fields:
	 * price(required): The price for the service
	 * picture(optional): The public picture location
	 * imageGallery(optional): A JSONArray with strings of public image paths
	 */
	@Override
	public void fillFields(JSONObject listingData, long creator) throws WrongFormatException {
		super.fillFields(listingData, creator);
		if(listingData.isNull(Constants.listingDataFieldPrice)){
			throw new WrongFormatException("Missing required fields");
		}
		if(listingData.isNull(Constants.listingDataFieldPrice)){
			throw new WrongFormatException("Missing required field(s)");
		}
		this.setPrice(listingData.getDouble(Constants.listingDataFieldPrice));
	}

	/* (non-Javadoc)
	 * @see BackendServer.Listings.Entities.Listing#toJSON()
	 * listingData has three additional fields in the created JSONObject:
	 * price(required): The price for the service
	 * picture(optional): The public picture location
	 * imageGallery(optional): A JSONArray with strings of public image paths
	 */
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put(Constants.listingDataFieldPrice, this.getPrice());
		json.put(Constants.listingDataFieldImageGallery, new JSONArray(this.getImageGallery()));
		json.put(Constants.listingDataFieldPicture, this.getPicture());
		return json;
	}
	
}
