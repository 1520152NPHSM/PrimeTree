package backendServer.listings.entities;

import java.sql.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.json.JSONObject;

import backendServer.exceptions.NoImageGallerySupportedException;
import backendServer.exceptions.WrongFormatException;
import backendServer.listings.Constants;
import backendServer.listings.SimpleMethods;

/**This class represents Listings with listingType SellItem*/
@Entity
@Table(name="BorrowRequest")
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class BorrowRequest extends RequestListing{
	private Date borrowFromDate;
	private Date borrowToDate;
	private String picture;
	
	public Date getBorrowFromDate() {
		return borrowFromDate;
	}

	public void setBorrowFromDate(Date borrowFromDate) {
		this.borrowFromDate = borrowFromDate;
	}

	public Date getBorrowToDate() {
		return borrowToDate;
	}

	public void setBorrowToDate(Date borrowToDate) {
		this.borrowToDate = borrowToDate;
	}

	public BorrowRequest(){
		this.setType(Constants.listingTypeNameBorrowRequest);
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
		if(!listingData.isNull(Constants.listingDataFieldBorrowFromDate)){
			this.setBorrowFromDate(new Date(listingData.getLong(Constants.listingDataFieldBorrowFromDate)));
		}else{
			this.setBorrowFromDate(null);
		}
		if(!listingData.isNull(Constants.listingDataFieldBorrowToDate)){
			this.setBorrowToDate(new Date(listingData.getLong(Constants.listingDataFieldBorrowToDate)));
		}else{
			setBorrowToDate(null);
		}
	}
	
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put(Constants.listingDataFieldPicture, this.getPicture());
		json.put(Constants.listingDataFieldBorrowFromDate, this.getBorrowFromDate());
		json.put(Constants.listingDataFieldBorrowToDate, this.getBorrowToDate());
		return json;
	}
	
}
