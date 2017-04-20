package hello;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.beans.factory.annotation.Autowired;

/**In this Class all Constants for the SQLAdapterimpl-Package are defined*/
public class Constants {
	
	//the listingRepository gives the package access to the ListingDatabase
//	@Autowired 
//	public static ListingRepository listingRepository;
	
//	public static EntityManager entitymanager = Persistence.
//		createEntityManagerFactory( "Eclipselink_JPA" ).createEntityManager( );
	
	//The following Strings are the names of the required fields in all JSONObjects representing listingData
	public static final String
	listingDataFieldNameTitle="title",
	listingDataFieldNameDescription="listingDescription",
	listingDataFieldNamePictures="pictures",
	listingDataFieldNameDeadLine="deadLine",
	listingDataFieldNameListingType="listingType";
	
}
