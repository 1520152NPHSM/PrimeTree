package BackendServer.Listings.Entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.json.JSONObject;

import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;

/**This class represents Listings with listingType ReturningRecreationRequest*/
@Entity
@Table(name="SingleRecreationRequest")
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class SingleRecreationRequest extends RecreationRequest{
	
	private Date happeningDate;

	public SingleRecreationRequest(){
		this.setType(Constants.listingTypeNameSingleRecreationRequest);
	}
	
	@Override
	public void fillFields(JSONObject listingData, long creator) throws WrongFormatException {
		super.fillFields(listingData, creator);
		if(!listingData.isNull(Constants.listingDataFieldHappeningDate)){
			this.setHappeningDate(new Date(listingData.getLong(Constants.listingDataFieldHappeningDate)));
		}else{
			this.setHappeningDate(null);
		}
	}

	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put(Constants.listingDataFieldPicture, this.getPicture());
		return json;
	}

	public Date getHappeningDate() {
		return happeningDate;
	}

	public void setHappeningDate(Date happeningDate) {
		this.happeningDate = happeningDate;
	}
	
}
