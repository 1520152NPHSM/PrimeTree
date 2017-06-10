package BackendServer.Exceptions;


/**This exception is thrown if there's a try to delete a favourite from a favourite-list, which didn't exist 
 * in the favourite-list.
 * @author Florian Kutz
 *
 */
public class FavouriteNotFoundException extends Exception {

	public FavouriteNotFoundException(String string) {
		super(string);
	}
	
	public FavouriteNotFoundException() {}

}
