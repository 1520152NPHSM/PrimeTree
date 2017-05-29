package BackendServer.Listings.ObjectControllers;

import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import BackendServer.Exceptions.ListingNotFoundException;
import BackendServer.Exceptions.NoImageGallerySupportedException;
import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.Entities.Comment;
import BackendServer.Listings.Entities.Listing;
import BackendServer.Listings.Repositories.CommentRepository;

/**This abstract class is made so sub-classes can control the listings with their individual listingType.
 * Controlling means checking, whether a listingData-JSONObject matches the individual listingType as well 
 * as persisting and reading the listings with the individual type. */
@Service
@Transactional
public abstract class ListingObjectController<L extends Listing> {
	
	public String listingType;
	protected JpaRepository<L, Long> listingRepository;
	@Autowired
	protected CommentRepository commentRepository;
	
	/**This method checks whether the listingData-JSONObject contains data for the listingType of the sub-class.
	 * throws: WrongFormatException if the JSONObject contains no data about the listingType.*/
	public boolean isThisListingType(JSONObject listingData)throws WrongFormatException{
		try{
			return this.listingType.equals(listingData.getString(Constants.listingDataFieldListingType));
		}catch(JSONException e){
			throw new WrongFormatException("The listingType is missing.");
		}
	}
	
	/**This method creates and persists a new Listing-Object with the data of listingData and the creator.*/
	public long createAndPersistNewInstance(JSONObject listingData, long creatorId) throws WrongFormatException {
		try{
			
			L newInstance=createNew();
			newInstance.fillFields(listingData, creatorId);
		    
			listingRepository.save(newInstance);
			return newInstance.getListingId();
			
		}catch(JSONException e){
			throw new WrongFormatException(e.toString());
		}
	}

	/**This method returns a Listing with this class's listingType with the id id*/
	public L getListingById(long listingId) throws ListingNotFoundException {
		L foundItem = listingRepository.findOne(listingId);
		if(foundItem==null){
			throw new ListingNotFoundException("Listing with id " + listingId + " does not exist.");
		}else{
			return foundItem;
		}
	}

	/**This method reads out all listings with this objects listingType
	 * @return a List with all listings
	 */
	public List<? extends Listing> getAll() {
		return listingRepository.findAll();
	}

	/**This method deletes a listing
	 * @param listingId id of the listing that should be deleted
	 * @throws ListingNotFoundException if no listing with this listingType and the id listingId existed
	 */
	public void deleteListing(Long listingId) throws ListingNotFoundException {
		L foundItem = listingRepository.findOne(listingId);
		if(foundItem==null){
			throw new ListingNotFoundException("Listing with id " + listingId + " does not exist.");
		}else{
			listingRepository.delete(listingId);
		}
	}
	
	/**This method edits a listing by replacing all fields from listingData
	 * @param listingId id of the edited listing
	 * @param listingData the new data for the listing
	 * @throws ListingNotFoundException if no listing with this listingType and the id listingId exists
	 * @throws WrongFormatException if the listingData is missing of fields
	 */
	public void edit(long listingId, JSONObject listingData)
		throws ListingNotFoundException, WrongFormatException {
		L editedListing = this.getListingById(listingId);
		System.out.println("Before: " + editedListing.toString());
		editedListing.fillFields(listingData, editedListing.getOwner());
		System.out.println("After: " + editedListing.toString());
		listingRepository.save(editedListing);
	}

	/**@return a completely new listing with this listingType
	 */
	protected abstract L createNew();

	/** This method adds a public pathname to the imageGallery-field of the listing if the listingType
	 * @param listingId id of the listing
	 * @param filePath added pathname
	 * @throws NoImageGallerySupportedException if the type of the listing doesn't support an imageGallery
	 * @throws ListingNotFoundException if no listing with id listingId is found
	 */
	public void addImagePath(long listingId, String filePath) throws NoImageGallerySupportedException, ListingNotFoundException {
		L editedListing = this.getListingById(listingId);
		editedListing.addImageToGallery(filePath);
		listingRepository.save(editedListing);
	}
	
	/**This method adds a new comment to a listing
	 * @param commentData the data of the comment
	 * @param authorId the userId of the author of the comment
	 * @param listingId the id of the commented listing
	 * @throws ListingNotFoundException if no listing with id listingId exists
	 */
	public void comment(JSONObject commentData, long authorId, long listingId) throws ListingNotFoundException{
		L commentedListing=this.getListingById(listingId);
		Comment newComment=new Comment();
		newComment.setAuthorId(authorId);
		newComment.setCreateDate(new Date(commentData.getLong(Constants.commentDataFieldDate)));
		newComment.setText(commentData.getString(Constants.commentDataFieldMessage));
		commentedListing.addComment(newComment);
		this.listingRepository.save(commentedListing);
		this.commentRepository.save(newComment);
	}

	/**This method deletes an imagePath from the imageGallery-List in a listing
	 * @param listingId id of the listing with the imageGallery
	 * @param galleryIndex the index of the deleted image in the gallery
	 * @throws NoImageGallerySupportedException if the listing doesn't support an imageGallery
	 * @throws ListingNotFoundException if the lsting with the id listingId doesn't exist
	 */
	public void deleteGalleryImage(long listingId, int galleryIndex) throws NoImageGallerySupportedException, ListingNotFoundException {
		L editedListing=this.getListingById(listingId);
		editedListing.getImageGallery().remove(galleryIndex);
		this.listingRepository.save(editedListing);
	}
	
}
