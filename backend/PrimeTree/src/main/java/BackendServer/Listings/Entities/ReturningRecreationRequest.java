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
@Table(name="ReturningRecreationRequest")
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class ReturningRecreationRequest extends RecreationRequest{
	
	private Date startDate;
	private Date endDate;
	private String regularity;
	
	public String getRegularity() {
		return regularity;
	}

	public void setRegularity(String regularity) {
		this.regularity = regularity;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public ReturningRecreationRequest(){
		this.setType(Constants.listingTypeNameReturningRecreationRequest);
	}
	
	@Override
	public void fillFields(JSONObject listingData, long creator) throws WrongFormatException {
		super.fillFields(listingData, creator);
		if(listingData.isNull(Constants.listingDataFieldRegularity)){
			throw new WrongFormatException("Missing required fields");
		}
		if(listingData.isNull(Constants.listingDataFieldRegularity) ||
				!Constants.allRegularityOptions.contains(listingData.getString(Constants.listingDataFieldRegularity))){
			throw new WrongFormatException("Bad Regularity");
		}
		if(!listingData.isNull(Constants.listingDataFieldStartDate)){
			this.setStartDate(new Date(listingData.getLong(Constants.listingDataFieldStartDate)));
		}else{
			this.setStartDate(null);
		}
		if(!listingData.isNull(Constants.listingDataFieldEndDate)){
			this.setEndDate(new Date(listingData.getLong(Constants.listingDataFieldEndDate)));
		}else{
			this.setEndDate(null);
		}
		this.setRegularity(listingData.getString(Constants.listingDataFieldRegularity));
	}

	private void setEndDate(Date date) {
		this.endDate=date;
	}

	private void setStartDate(Date date) {
		this.startDate=date;
	}

	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put(Constants.listingDataFieldPicture, this.getPicture());
		return json;
	}
	
}
