package BackendServer.Listings.Entities;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.json.JSONObject;

import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;

/**This abstract class represents the common properties of all offering-listings*/
@Entity
@Table(name="Offering")
@PrimaryKeyJoinColumn(referencedColumnName="id")
//@Inheritance( strategy = InheritanceType.JOINED )
public abstract class Offering extends Listing{
	
	@Override
	public void fillFields(JSONObject listingData, long creator) throws WrongFormatException {
		super.fillFields(listingData, creator);
	}
	
	public String toString(){
		return super.toString();
	}
	
	public Offering(){
		this.setKind(Constants.listingKindOffering);
	}
	
}