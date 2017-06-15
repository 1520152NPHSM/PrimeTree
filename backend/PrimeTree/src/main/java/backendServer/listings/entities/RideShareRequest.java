package backendServer.listings.entities;

import java.sql.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.json.JSONObject;

import backendServer.exceptions.WrongFormatException;
import backendServer.listings.Constants;
import backendServer.listings.SimpleMethods;

/**This class represents Listings with listingType RideSharing*/
@Entity
@Table(name="RideShareRequest")
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class RideShareRequest extends RequestListing {
	
	private String fromLocation;
	private String toLocation;
	private Date travelDateAndTime;
	
	public RideShareRequest(){
		this.setType(Constants.listingTypeNameRideShareRequest);
	}
	
	public String getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(String fromLocation) {
		this.fromLocation = fromLocation;
	}

	public String getToLocation() {
		return toLocation;
	}

	public void setToLocation(String toLocation) {
		this.toLocation = toLocation;
	}

	public Date getTravelDateAndTime() {
		return travelDateAndTime;
	}

	public void setTravelDateAndTime(Date travelDateAndTime) {
		this.travelDateAndTime = travelDateAndTime;
	}
	
	
	@Override
	public void fillFields(JSONObject listingData, long creator) throws WrongFormatException {
		super.fillFields(listingData, creator);
		if(listingData.isNull(Constants.listingDataFieldFromLocation)
				|| listingData.isNull(Constants.listingDataFieldToLocation)){
			throw new WrongFormatException("Missing required fields");
		}
		this.setFromLocation(listingData.getString(Constants.listingDataFieldFromLocation));
		this.setToLocation(listingData.getString(Constants.listingDataFieldToLocation));
		if(!listingData.isNull(Constants.listingDataFieldTravelDateAndTime)){
			this.setTravelDateAndTime(new Date(listingData.getLong(Constants.listingDataFieldTravelDateAndTime)));
		}else{
			this.setTravelDateAndTime(null);
		}
	}
	
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put(Constants.listingDataFieldFromLocation, this.getFromLocation());
		json.put(Constants.listingDataFieldToLocation, this.getToLocation());
		json.put(Constants.listingDataFieldTravelDateAndTime, this.getTravelDateAndTime());
		return json;
	}


}
