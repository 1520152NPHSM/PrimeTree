package BackendServer.Listings.Entities;

import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.json.JSONObject;

import BackendServer.Exceptions.NoImageGallerySupportedException;
import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.SimpleMethods;

/**This class represents Listings with listingType RecreationRequest*/
@Entity
@Table(name="RecreationRequest")
@PrimaryKeyJoinColumn(referencedColumnName="id")
public abstract class RecreationRequest extends RequestListing{

	private String picture;
	private String activityLocation;
	private String category;
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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
		if(listingData.isNull(Constants.listingDataFieldFreeTimeActivityCategory)
				|| listingData.isNull(Constants.listingDataFieldActivityLocation)){
			throw new WrongFormatException("Missing required fields");
		}
		if(!Constants.allFreeTimeActivityCategories.contains(listingData.getString(Constants.listingDataFieldFreeTimeActivityCategory))){
			throw new WrongFormatException("This Category does not exist");
		}
		this.setCategory(listingData.getString(Constants.listingDataFieldFreeTimeActivityCategory));
		this.setActivityLocation(listingData.getString(Constants.listingDataFieldActivityLocation));
	}
	
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put(Constants.listingDataFieldPicture, this.getPicture());
		return json;
	}

	public String getActivityLocation() {
		return activityLocation;
	}

	public void setActivityLocation(String activityLocation) {
		this.activityLocation = activityLocation;
	}
	
}
