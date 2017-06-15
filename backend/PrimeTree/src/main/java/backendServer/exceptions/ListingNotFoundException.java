package backendServer.exceptions;

/**This Exception is thrown if someone tries to access a listing with an id that does not exist as a listing.
 * 
 * @author Florian Kutz
 * */
@SuppressWarnings("serial")
public class ListingNotFoundException extends Exception {

	public ListingNotFoundException(String string) {
		super(string);
	}

}
