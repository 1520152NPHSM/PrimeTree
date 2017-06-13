package backendServer.listings;
import java.io.IOException;

import org.json.JSONObject;

import backendServer.exceptions.CommentNotFoundException;
import backendServer.exceptions.ListingNotFoundException;
import backendServer.exceptions.MainImageNotSupportedException;
import backendServer.exceptions.NoImageGallerySupportedException;
import backendServer.exceptions.PathNotTemporaryException;
import backendServer.exceptions.WrongFormatException;
import backendServer.listings.entities.Listing;

/**An implementation of this interface can read and write persistent data with the present data-structure
 * 
 * @author Florian Kutz
 *
 */
public interface PersistenceAdapter {
	
	/**This method takes the data for a new listing in a single object and creates a persisted entry in the ListingDatabase.
	 * 
	 * @param:
	 * newListingData: This object contains data for the new listing in the same format of the listingData-definition in the REST-Implementation
	 * 
	 * @return: the id of the new persisted listing 
	 * 
	 * @throws:
	 * WrongFormatException if newListingData is missing of required fields
	 * */
	public int persistNewListing(JSONObject newListingData, long userId) throws WrongFormatException;
	
	/**This method searches for the listing with the listingId and returns the Object
	 * 
	 * @param:
	 * listingId: Id of the required listing
	 * 
	 * @return: the listing-object
	 * 
	 * @throws:
	 * ListingNotFoundException if no listing with listingId listingId exists
	 * */
	public Listing getListingById(long listingId) throws ListingNotFoundException;

//	/**This method searches for all listings with a listingId present in the listingIds Array
//	 * 
//	 * @param:
//	 * listingIds: An array of all listingIds of the listings this method returns
//	 * 
//	 * @return: An array of all listingIds of all Listings
//	 * 
//	 * @throws 
//	 * ListingNotFoundException if at least one of the ids in the array does not exist as a listing
//	 * */
//	public Listing[] getListingArrayByIdArray(int[] listingIds) throws ListingNotFoundException;

	 /**This method deletes the listing with the listingId listingId
	 * 
	 * @param:
	 * listingId: the id of the listing
	 * @throws ListingNotFoundException
	 */
	public void deleteListingById(long newId) throws ListingNotFoundException;
	
	/**This method checks whether the user with id userId is the
	 *  creator/owner of the listing with listingId listingId.
	 * @param listingId: the id of the listing
	 * @param userId: the id of the userId
	 * @return true: if the user is the creator/owner of the listing
	 * @throws ListingNotFoundException if the listing with id listingId doesn't exist
	 */
	boolean isOwnerOfListing(long newId, long userId) throws ListingNotFoundException;
	
	/**This method creates a .png or jpeg or .jpg file for a listing
	 * DO NOT call this method with a pathname that doesn't belong to the imageData. 
	 *  
	 *  @param:
	 *  listingId: the id of the listing the file belongs to
	 *  imageData: the imageData represented in a byte-Array
	 *  originalFilename: the filename of the original file. The method can get the filetype (.png, .jpeg or 
	 *  .jpg) from this String
	 * @throws MainImageNotSupportedException if the main image does not support a main-image
	 * @throws IOException if originalFilename has no image Filetype
	 * @throws ListingNotFoundException if the listing with listingId listingId does not exist*/
	public void uploadMainImage(byte[] imageData, long newId, String originalFilename) throws IOException, ListingNotFoundException, MainImageNotSupportedException;

	/**This method edits the listing with id listingId by overriding all data except id, type and creator of 
	 * the listing with the data in listingData. Warning: All relevant fields
	 * 
	 * @param:
	 * listingId: the id of the listing the file belongs to
	 * listingData: A JSONObject with all new Data
	 * 
	 * @throws:
	 * ListingNotFoundException if the listing with listingId listingId does not exist
	 * */
	public void edit(long listingId, JSONObject listingData)
			throws ListingNotFoundException, WrongFormatException;
	
	/**This method adds a comment to the listing with id listingId
	 * @param commentData The data of the new comment as a JSONObject. It contains at least those two fields:
	 * message: The text of the comment
	 * createDate: The date of the creation of the comment.
	 * All additional fields are ignored.
	 * @param authorId The userId of the author.
	 * @param listingId The id of the commented listing
	 * @throws ListingNotFoundException if the listing with id listingId doesn't exist
	 */
	public void comment(JSONObject commentData, long authorId, long listingId) throws ListingNotFoundException;

	/**This method completely deletes the comment with id commentId from the database
	 * @param commentId id of the comment that should be deleted
	 */
	public void deleteComment(long commentId) throws CommentNotFoundException;
	
	/**This method returns a page of listings that match the filterOptions and writes the statistics 
	 * into the statistics-parameter
	 * @param page: The number of the requested page
	 * @param location: An array with all allowed locations. If one listing is not in this location, 
	 * it is filtered out. If this field is null, no listing is filtered out by its location.
	 * @param: shallBeActive: If this param is set false only inactive listings are returned and if this boolean 
	 * is true only active listings are returned
	 * @param price_min: The minimal requested price. All listings with a higher rice than this are sorted out. 
	 * All listings that don't have a price are valued as their price was 0.
	 * @param price_max: The maximal requested price. All listings with a lower rice than this are sorted out. 
	 * All listings that don't have a price are valued as their price was 0.
	 * @param type: An array with all allowed listingTypes. If one listing has a type which is not listed here 
	 * it is filtered out. If this field is null no listing is filtered out by its type.
	 * @param kind: A string of the allowed type. If one listing has a different kind
	 * it is filtered out. If this field is null no listing is filtered out by its kind.
	 * @param sort This string defines the sort-criteria of all results before the page is pulled out.
	 * @param statistics A statistics Object which is filled in the method
	 * @return an array of all listings in the requested page
	 */
	public Listing[] getListingsFiltered(int page, String[] location, boolean shallBeActive, int price_min, int price_max, String[] type, String kind,
			String sort, ListingSearchStatistics statistics) throws WrongFormatException;

	/**This method returns a page of listings that match the filterOptions and writes the statistics 
	 * into the statistics-parameter
	 * @param page: The number of the requested page
	 * @param location: An array with all allowed locations. If one listing is not in this location, 
	 * it is filtered out. If this field is null, no listing is filtered out by its location.
	 * @param price_min: The minimal requested price. All listings with a higher rice than this are sorted out. 
	 * All listings that don't have a price are valued as their price was 0.
	 * @param price_max: The maximal requested price. All listings with a lower rice than this are sorted out. 
	 * All listings that don't have a price are valued as their price was 0.
	 * @param type: An array with all allowed listingTypes. If one listing has a type which is not listed here 
	 * it is filtered out. If this field is null no listing is filtered out by its type.
	 * @param kind: A string of the allowed type. If one listing has a different kind
	 * it is filtered out. If this field is null no listing is filtered out by its kind.
	 * @param sort This string defines the sort-criteria of all results before the page is pulled out.
	 * @param statistics A statistics Object which is filled in the method
	 * @return an array of all listings in the requested page
	 */
	public Listing[] getListingsFiltered(int page, String[] location, int price_min, int price_max, String[] type, String kind,
			String sort, ListingSearchStatistics statistics) throws WrongFormatException;

	/**This method returns a page of listings that match the filterOptions and contains query in the title or the 
	 * description and writes the statistics into the statistics-parameter
	 * @param query: The search-subject which is scanned in the title and description of all listings. 
	 * If a listing doesn't have this string in either of those it is sorted out.
	 * @param page: The number of the requested page
	 * @param location: An array with all allowed locations. If one listing is not in this location, 
	 * it is filtered out. If this field is null, no listing is filtered out by its location.
	 * @param: shallBeActive: If this param is set false only inactive listings are returned and if this boolean 
	 * is true only active listings are returned
	 * @param price_min: The minimal requested price. All listings with a higher rice than this are sorted out. 
	 * All listings that don't have a price are valued as their price was 0.
	 * @param price_max: The maximal requested price. All listings with a lower rice than this are sorted out. 
	 * All listings that don't have a price are valued as their price was 0.
	 * @param type: An array with all allowed listingTypes. If one listing has a type which is not listed here 
	 * it is filtered out. If this field is null no listing is filtered out by its type.
	 * @param kind: A string of the allowed type. If one listing has a different kind
	 * it is filtered out. If this field is null no listing is filtered out by its kind.
	 * @param sort This string defines the sort-criteria of all results before the page is pulled out.
	 * @param statistics A statistics Object which is filled in the method
	 * @return an array of all listings in the requested page
	 * @throws WrongFormatException if price_min>price_max
	 */
	public Listing[] getListingsBySearch(String query, int page, String[] location, boolean b, int price_min, int price_max,
			String[] type, String kind, String sort, ListingSearchStatistics statistics) throws WrongFormatException;
	
	/**This method removes an image from the gallery in the listing with the id listingId and replaces it 
	 * with a new image
	 * @param imageData: The data of the new Image in a byteArray
	 * @param listingId: the id of the listing
	 * @param galleryIndex: The index of the image in the gallery that should be replaced
	 * @param originalFilename: The original Filename of the new image
	 * @throws ListingNotFoundException: If no listing with id listingId exists
	 * @throws NoImageGallerySupportedException if the listing doesn't support the imageGallery
	 * @throws IOException If the uploaded file isn't an image file
	 * @throws GalleryIndexOutOfLimitException 
	 */
	public void putImageInGallery(byte[] imageData, int listingId, int galleryIndex, String originalFilename) throws ListingNotFoundException, NoImageGallerySupportedException, IOException;

	/**This method deletes an image from the gallery in the listing with the id listingId
	 * @param listingId: the id of the listing
	 * @param galleryIndex: The index of the image in the gallery that should be replaced
	 * @throws ListingNotFoundException: If no listing with id listingId exists
	 * @throws NoImageGallerySupportedException if the listing doesn't support the imageGallery
	 */
	public void deleteImageInGallery(int listingId, int galleryIndex) throws ListingNotFoundException, NoImageGallerySupportedException;

	/**This method returns all listings whose owner is the user with the userId userId
	 * @param userId: id of the user
	 * @return an array with all Listings from this user
	 */
	public Listing[] getListingsFromUser(long userId);

	/**This method uploads a temporary Image
	 * @param imageData: The data of the new Image in a byteArray
	 * @param originalFilename: The original Filename of the new image
	 * @return the public imagePath
	 * @throws IOException If the uploaded file isn't an image file
	 */
	public String uploadTemporaryImage(byte[] imageData, String originalFilename) throws IOException;

	/**This method deletes a temporaryImage
	 * @param imagePath: the public imagePath
	 * @throws PathNotTemporaryException 
	 */
	public void deleteTemporaryImage(String imagePath) throws PathNotTemporaryException;

	public void registerAllListingsInGlobalStatistics(GlobalStatistics statistics);
}
