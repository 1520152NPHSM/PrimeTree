package BackendServer.Listings;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import BackendServer.Exceptions.CommentNotFoundException;
import BackendServer.Exceptions.GalleryIndexOutOfLimitException;
import BackendServer.Exceptions.ListingNotFoundException;
import BackendServer.Exceptions.MainImageNotSupportedException;
import BackendServer.Exceptions.NoImageGallerySupportedException;
import BackendServer.Exceptions.PathNotTemporaryException;
import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Entities.Listing;
import BackendServer.Listings.ObjectControllers.ListingObjectController;
import BackendServer.Listings.Repositories.CommentRepository;

/**This class implements PersistenceAdapter. It uses the bean getAnArrayOfAllTypesOfListingObjectController()
 * to write and read from and to the listingdb database as well as saving and publishing files.
 * 
 * 
 * @author Florian Kutz
 *
 */
public class PersistenceAdapterImpl implements PersistenceAdapter {
	
	//This is an Array containing Instances of all non-abstract classes extending ListingObjectController
	@Autowired
	ListingObjectController[] listingControllers;
	
	@Autowired 
	CommentRepository commentRepository;
	
	/**This class allows PersistenceAdapterImpl to give performActionOnAlllistingControllers(...) a function 
	 * parameter, which performs a custom method on one listingObjectController*/
	private abstract class ListingObjectControllerActionPerformer {
		
		protected ListingObjectController listingObjectController;
		
		/**This method), which is called by performActionOnAlllistingControllers(...),
		 * performs a custom method with listingId on listingObjectController
		 * @throws WrongFormatException If the overridden Method tries to create or edit a listing with a JSONObjectin the wrong format
		 * @throws NoImageGallerySupportedException If the overridden Method tries to make some stuff with the imageGallery of a listing but fails because the listing doesn't support an imageGallery
		 * @throws MainImageNotSupportedException If the overridden Method tries to make some stuff with the mainImage of a listing but fails because the listing doesn't support a mainImage*/
		public abstract Object performAction(long listingId) throws ListingNotFoundException, WrongFormatException, NoImageGallerySupportedException, MainImageNotSupportedException;
		
		public void setListingObjectController(ListingObjectController listingObjectController) {
			this.listingObjectController = listingObjectController;
		}
	}

	@Override
	public int persistNewListing(JSONObject newListingData, long creatorId) throws WrongFormatException{
		return (int) getListingControllerWithTheRightType(newListingData).
				createAndPersistNewInstance(newListingData, creatorId);
	}

	@Override
	public Listing getListingById(long listingId) throws ListingNotFoundException {
		try {
			return (Listing) performActionOnAlllistingControllers(listingId,
					new ListingObjectControllerActionPerformer(){

						@Override
						public Object performAction(long listingId) throws ListingNotFoundException {
							return listingObjectController.getListingById(listingId);
						}
				
			});
		} catch (WrongFormatException e) {
			System.out.println("A WrongFormatException in the get method appeared. Normally this does not happen.");
			return null;
		} catch (NoImageGallerySupportedException e) {
			System.out.println("A NoImageGalleryException in the get method appeared. Normally this does not happen.");
			return null;
		} catch (MainImageNotSupportedException e) {
			System.out.println("A MainImageNotSupportedException in the get method appeared. Normally this does not happen.");
			return null;
		}
	}
	
	/**This method returns a ListingObjectController-instance, whose listingType matches the one of the listingData.
	 * If listingData contains no field with the listingType a WrongFormatException is thrown.*/
	private ListingObjectController getListingControllerWithTheRightType(JSONObject listingData) throws WrongFormatException{
		if(listingData==null){
			throw new WrongFormatException("ListingData is null");
		}
		
		for(int controllerIndex=0;controllerIndex<listingControllers.length;controllerIndex++){
			if(listingControllers[controllerIndex].isThisListingType(listingData)){
				return listingControllers[controllerIndex];
			}
		}
		throw new WrongFormatException("ListingType " + listingData.getString(Constants.listingDataFieldListingType) + " does not exist.");
	}

//	@Override
//	public Listing[] getListingArrayByIdArray(int[] listingIds) throws ListingNotFoundException {
//		Collection<Listing> results=new LinkedList<Listing>();
//		for(int IdArrayIndex=0;IdArrayIndex<listingIds.length;IdArrayIndex++){
//			results.add(getListingById(listingIds[IdArrayIndex]));
//		}
//		return SimpleMethods.parseObjectArrayToListingArray(results.toArray());
//	}

	@Override
	public void deleteListingById(long listingId) throws ListingNotFoundException {
		try {
			performActionOnAlllistingControllers(listingId,
					new ListingObjectControllerActionPerformer(){

						@Override
						public Object performAction(long listingId) throws ListingNotFoundException {
							try{
								deleteAnyImage(listingObjectController.getListingById(listingId).getPicture());
							}catch(MainImageNotSupportedException noImageToDelete){
								//Do nothing and continue
							}
							try{
								for(String imageInGalleryPath : listingObjectController.getListingById(listingId).getImageGallery()){
									deleteAnyImage(imageInGalleryPath);
								}
							}catch(NoImageGallerySupportedException noGalleryToDelete){
								//Do nothing and continue
							}
							listingObjectController.deleteListing((long) listingId);
							return null;
						}
				
			});
		} catch (WrongFormatException e) {
			System.out.println("A WrongFormatException in the delete method appeared. Normally this does not happen.");
		} catch (NoImageGallerySupportedException e) {
			System.out.println("A NoImageGallerySupportedException in the delete method appeared. Normally this does not happen.");
		} catch (MainImageNotSupportedException e) {
			System.out.println("A (MainImageNotSupportedException in the get method appeared. Normally this does not happen.");
		}
	}
	
	/**This method tries action.performAction(listingId) on all listingControllers by 
	 * continuing if one listingController doesn't find the listing
	 * throws ListingNotFoundException if the listing with id listingId does not exist
	 * @throws WrongFormatException If the performAction Method tries to create or edit a listing with a JSONObjectin the wrong format
		 * @throws NoImageGallerySupportedException If the performAction Method tries to make some stuff with the imageGallery of a listing but fails because the listing doesn't support an imageGallery
		 * @throws MainImageNotSupportedException If the performAction Method tries to make some stuff with the mainImage of a listing but fails because the listing doesn't support a mainImage*/
	private Object performActionOnAlllistingControllers(long listingId, ListingObjectControllerActionPerformer action) throws ListingNotFoundException, WrongFormatException, NoImageGallerySupportedException, MainImageNotSupportedException{
		for(int controllerIndex=0;controllerIndex<listingControllers.length;controllerIndex++){
			try{
				action.setListingObjectController(listingControllers[controllerIndex]);
				return action.performAction(listingId);
			}catch(ListingNotFoundException e){
				//do nothing and go to next listingController
			}
		}
		throw new ListingNotFoundException("Listing with id " + listingId + " did not exist.");
	}

	@Override
	public boolean isOwnerOfListing(long listingId, long userId) throws ListingNotFoundException {
		return userId==this.getListingById(listingId).getOwner();
	}
	
	@Override
	public void edit(long listingId, final JSONObject listingData)
			throws ListingNotFoundException, WrongFormatException {
		try {
			this.performActionOnAlllistingControllers(listingId, new ListingObjectControllerActionPerformer(){
				
				private JSONObject listingDataInAnonymClass=listingData;

				@Override
				public Object performAction(long listingId) throws ListingNotFoundException, WrongFormatException {
					listingObjectController.edit(listingId, listingDataInAnonymClass);
					return null;
				}
				
			});
		} catch (NoImageGallerySupportedException e) {
			System.out.println("A WrongFormatException in the edit method appeared. Normally this does not happen.");
		} catch (MainImageNotSupportedException e) {
			System.out.println("A (MainImageNotSupportedException in the get method appeared. Normally this does not happen.");
		}
	}

	/**This method creates a Collection of all Listings in the database
	 * @return all existing listings unsorted in a Collection
	 */
	private Collection<Listing> getAllListings(){
		LinkedList<Listing> resultList=new LinkedList<Listing>();
		for(int controllerIndex=0;controllerIndex<listingControllers.length;controllerIndex++){
			resultList.addAll(listingControllers[controllerIndex].getAll());
		}
		return resultList;
	}

	@Override
	public void comment(final JSONObject commentData, final long authorId, long listingId) throws ListingNotFoundException {
		try {
			this.performActionOnAlllistingControllers(listingId, new ListingObjectControllerActionPerformer(){

				@Override
				public Object performAction(long listingId)
						throws ListingNotFoundException, WrongFormatException, NoImageGallerySupportedException {
					listingObjectController.comment(commentData, authorId, listingId);
					return null;
				}
				
			});
		} catch (WrongFormatException e) {
			System.out.println("A WrongFormatException in the comment method appeared. Normally this does not happen.");
		} catch (NoImageGallerySupportedException e) {
			System.out.println("A NoImageGallerySupportedException in the comment method appeared. Normally this does not happen.");
		} catch (MainImageNotSupportedException e) {
			System.out.println("A (MainImageNotSupportedException in the comment method appeared. Normally this does not happen.");
		}
	}

	@Override
	public void deleteComment(long commentId) throws CommentNotFoundException{
		if(commentRepository.findOne(commentId)==null){
			throw new CommentNotFoundException();
		}
		commentRepository.delete(commentId);
	}

	@Override
	public Listing[] getListingsFiltered(int page, final String[] location, final int price_min, final int price_max, final String[] type,
			final String kind, String sort, ListingSearchStatistics statistics) {
		return getAllListingsFiltered(statistics, page, sort, new ListingFilterer(){

			@Override
			public boolean checkIfListingMatches(Listing checkedListing) {
				return (checkedListing.matchFilterOptions(location, true, price_min, price_max, type, kind)||
						checkedListing.matchFilterOptions(location, false, price_min, price_max, type, kind));
			}
			
		});
	}

	@Override
	public Listing[] getListingsFiltered(int page, final String[] location, final boolean shallBeActive, final int price_min, final int price_max, final String[] type, final String kind,
			String sort, ListingSearchStatistics statistics) {
		return getAllListingsFiltered(statistics, page, sort, new ListingFilterer(){

			@Override
			public boolean checkIfListingMatches(Listing checkedListing) {
				return checkedListing.matchFilterOptions(location, shallBeActive, price_min, price_max, type, kind);
			}
			
		});
	}

	@Override
	public Listing[] getListingsBySearch(final String query, int page, final String[] location, boolean b, final int price_min,
			final int price_max, final String[] type, final String kind, String sort, ListingSearchStatistics statistics) {
		return getAllListingsFiltered(statistics, page, sort, new ListingFilterer(){

			@Override
			public boolean checkIfListingMatches(Listing checkedListing) {
				return checkedListing.matchFilterOptions(query, location, true, price_min, price_max, type, kind);
			}
			
		});
	}
	
	/**This interface is given to the private method getAllListingsFiltered so it can check for a single 
	 * listing if it matches an overridden matching-condition
	 * @author Florian Kutz
	 *
	 */
	private interface ListingFilterer{
		/**This method checks whether a single listing matches the filter conditions
		 * @param checkedListing the checked listing
		 * @return true, if checkedListing matches the filter-conditions
		 */
		public boolean checkIfListingMatches(Listing checkedListing);
	}
	
	/**This private method returns a page of listings that get a true from listingFilter.checkIfListingMatches 
	 * and writes the statistics into the statistics-parameter
	 * @param statistics A statistics Object which is filled in the method
	 * @param page: The number of the requested page
	 * @param sort This string defines the sort-criteria of all results before the page is pulled out.
	 * @param listingFilterer This instance decides whether a listing matches th filter options or not.
	 * @return an array of all listings that match in the requested page
	 */
	private Listing[] getAllListingsFiltered(ListingSearchStatistics statistics, int page, String sort, ListingFilterer listingFilterer){
		Collection<Listing> resultSet=getAllListings();
		Iterator<? extends Listing> listingIterator=resultSet.iterator();
		double lowestPriceFound=Integer.MAX_VALUE;
		double highestPriceFound=0;
		while(listingIterator.hasNext()){
			Listing checkedListing=listingIterator.next();
			if(!listingFilterer.checkIfListingMatches(checkedListing)){
				listingIterator.remove();
			}else if(checkedListing.getPrice()<lowestPriceFound){
				lowestPriceFound=checkedListing.getPrice();
			}else if(checkedListing.getPrice()>highestPriceFound){
				highestPriceFound=checkedListing.getPrice();
			}
		}
		Listing[] resultArray=SimpleMethods.parseObjectArrayToListingArray(resultSet.toArray());
		int pageBeginning=(page-1)*Constants.pageSize,
				pageEnd=Integer.min(page*Constants.pageSize, Integer.max(resultArray.length-1, 0)) ;
		Arrays.sort(resultArray, this.createListingComparator(sort));
		statistics.setPages((resultSet.size()-1)/50);
		statistics.setPrice_max(highestPriceFound);
		statistics.setPrice_min(lowestPriceFound);
		resultArray=Arrays.copyOfRange(resultArray, pageBeginning, pageEnd);
		statistics.setCount(resultArray.length);
		return resultArray;
	}

	/**This method creates a new Comparator<Listing> that compares two Listings with the sortOption-criteria
	 * @param sortOption the criteria by which the listings should be compared
	 * @return a new Comparator<Listing> that compares two Listings with the sortOption-criteria
	 */
	private Comparator<Listing> createListingComparator(String sortOption) {
		if(sortOption.equals(Constants.sortOptionPrice_Desc)){
			return new Comparator<Listing>(){

				@Override
				public int compare(Listing o1, Listing o2) {
					return (int) (o2.getPrice()-o1.getPrice());
				}
				
			};
		} else if(sortOption.equals(Constants.sortOptionPrice_Asc)){
			return new Comparator<Listing>(){

				@Override
				public int compare(Listing o1, Listing o2) {
					return (int) (o1.getPrice()-o2.getPrice());
				}
				
			};
		}  else if(sortOption.equals(Constants.sortOptionAlphabetical_Asc)){
			return new Comparator<Listing>(){

				@Override
				public int compare(Listing o1, Listing o2) {
					return o1.getTitle().compareTo(o2.getTitle());
				}
				
			};
		}  else if(sortOption.equals(Constants.sortOptionAlphabetical_Desc)){
			return new Comparator<Listing>(){

				@Override
				public int compare(Listing o1, Listing o2) {
					return o2.getTitle().compareTo(o1.getTitle());
				}
				
			};
		}  else if(sortOption.equals(Constants.sortOptionDate_Asc)){
			return new Comparator<Listing>(){

				@Override
				public int compare(Listing o1, Listing o2) {
					return o1.getCreateDate().compareTo(o2.getCreateDate());
				}
				
			};
		}  else if(sortOption.equals(Constants.sortOptionLocation_Asc)){
			return new Comparator<Listing>(){

				@Override
				public int compare(Listing o1, Listing o2) {
					return o1.getLocation().compareTo(o2.getLocation());
				}
				
			};
		}  else if(sortOption.equals(Constants.sortOptionPrice_Desc)){
			return new Comparator<Listing>(){

				@Override
				public int compare(Listing o1, Listing o2) {
					return o2.getLocation().compareTo(o1.getLocation());
				}
				
			};
		}  else if(sortOption.equals(Constants.sortOptionPrice_Desc)){
			return new Comparator<Listing>(){

				@Override
				public int compare(Listing o1, Listing o2) {
					return (int) (o2.getPrice()-o1.getPrice());
				}
				
			};
		} else{
			return new Comparator<Listing>(){

				@Override
				public int compare(Listing o1, Listing o2) {
					return (int) (o1.getListingId()-o2.getListingId());
				}
				
			};
		}
	}
	
	@Override
	public Listing[] getListingsFromUser(long id) {
		Collection<Listing> resultSet=getAllListings();
		Iterator<? extends Listing> listingIterator=resultSet.iterator();
		while(listingIterator.hasNext()){
			Listing checkedListing=listingIterator.next();
			if(checkedListing.getOwner()!=id){
				resultSet.remove(checkedListing);
			}
		}
		Listing[] resultArray=SimpleMethods.parseObjectArrayToListingArray(resultSet.toArray());
		Arrays.sort(resultArray, this.createListingComparator(Constants.sortOptionId));
		return resultArray;
	}

	@Override
	public void uploadMainImage(byte[] imageData, long listingId, String originalFilename) throws IOException, ListingNotFoundException, MainImageNotSupportedException{
		final String localPath="src/main/webapp/resources/assets/listings/" + listingId + "/";
		final String localFileName=makeLocalMainImagePath(listingId, originalFilename);
		uploadAnyImage(imageData, localPath, localFileName);
		this.getListingById(listingId).setPicture(getPublicFilePathFromLocal(localPath+localFileName));
		try {
			this.performActionOnAlllistingControllers(listingId, new ListingObjectControllerActionPerformer(){

				@Override
				public Object performAction(long listingId)
						throws ListingNotFoundException, WrongFormatException, NoImageGallerySupportedException, MainImageNotSupportedException {
					this.listingObjectController.setMainImageOnListing(listingId, localPath+localFileName);
					return null;
				}
				
			});
		} catch (WrongFormatException e) {
			System.out.println("A WrongFormatExceptionin the get method appeared. Normally this does not happen.");
		} catch (NoImageGallerySupportedException e) {
			System.out.println("A NoImageGallerySupportedException in the get method appeared. Normally this does not happen.");
		}
	}

	@Override
	public String uploadTemporaryImage(byte[] imageData, String originalFilename) throws IOException {
		String localPath="src/main/webapp/resources/temporary/";
		String localFileName=makeUnredundantTemporaryFileName(localPath,originalFilename);
		uploadAnyImage(imageData, localPath, localFileName);
		return getPublicFilePathFromLocal(localPath+localFileName);
	}
	
	private void uploadAnyImage(byte[] imageData, String localPath, String localFileName) throws IOException{
		try{
			Files.deleteIfExists(Paths.get(localPath+localFileName));
			Files.createDirectories(Paths.get(localPath));
			Files.createFile(Paths.get(localPath+localFileName));
		}catch(FileAlreadyExistsException e){
			//do nothing and continue
		}catch(AccessDeniedException shouldNotBeThrown){
			System.out.println(shouldNotBeThrown.getMessage());
		}
		FileOutputStream outputStream=new FileOutputStream(localPath+localFileName);
		outputStream.write(imageData);
		outputStream.close();
	}
	

	
	private void deleteAnyImage(String localFilePath){
		try {
			if(localFilePath!=null){
				Files.deleteIfExists(Paths.get(localFilePath));
			}
		} catch (IOException e) {
			System.out.println("Failed to delete image " + localFilePath);
			e.printStackTrace();
		}
	}

	@Override
	public void deleteTemporaryImage(String publicImagePath) throws PathNotTemporaryException {
		if(isFileTemporary(publicImagePath)){
			throw new PathNotTemporaryException();
		}
		deleteAnyImage(getLocalPathFromPublicPath(publicImagePath));
	}
	
	private boolean isFileTemporary(String publicImagePath) {
		return publicImagePath.startsWith("temporary/");
	}

	@Override
	public void putImageInGallery(byte[] imageData, int listingId,final int galleryIndex, String originalFilename) throws ListingNotFoundException, NoImageGallerySupportedException, IOException, GalleryIndexOutOfLimitException {
		if(galleryIndex<Constants.numberOfImagesPerGallery){
			final String localPath="/resources/assets/listings/" + listingId + "/gallery/";
			final String fileName=makeImageInGalleryPathName(galleryIndex, originalFilename);
			uploadAnyImage(imageData, localPath, fileName);
			try {
				this.performActionOnAlllistingControllers(listingId, new ListingObjectControllerActionPerformer(){

					String publicFilePath=getPublicFilePathFromLocal(localPath+fileName);
					int galleryIndexInPerformer=galleryIndex;
						
					@Override
					public Object performAction(long listingId) throws ListingNotFoundException, WrongFormatException, NoImageGallerySupportedException {
						this.listingObjectController.setImagePath(listingId, galleryIndexInPerformer, publicFilePath);
						return null;
					}
					
				});
			} catch (WrongFormatException e) {
				System.out.println("A WrongFormatException in the putImageInaGallery method appeared. Normally this does not happen.");
			} catch (MainImageNotSupportedException e) {
				System.out.println("A (MainImageNotSupportedException in the putImageInGallery method appeared. Normally this does not happen.");
			}
		}else{
			throw new GalleryIndexOutOfLimitException();
		}
	}

	/**This method creates a new unredundant public pathname for a new image for a gallery
	 * @param listingId id of the listing the gallery belongs to
	 * @param originalFilename the original filename of the image
	 * @return the new public pathname
	 * @throws IOException if the pathname shows that the file was no image
	 * @throws NoImageGallerySupportedException if the listing doesn't support a main image
	 * @throws ListingNotFoundException
	 */
	private String makeImageInGalleryPathName(int galleryIndex,  String originalFilename) throws IOException, NoImageGallerySupportedException, ListingNotFoundException {
		return galleryIndex + this.getImageFileTypeEnding(originalFilename);
	}

	@Override
	public void deleteImageInGallery(int listingId, final int galleryIndex) throws ListingNotFoundException, NoImageGallerySupportedException {
		try {
			String publicFilePath=(String)this.performActionOnAlllistingControllers(listingId, new ListingObjectControllerActionPerformer(){

				String publicFilePath;
				
				@Override
				public Object performAction(long listingId)
						throws ListingNotFoundException, WrongFormatException, NoImageGallerySupportedException {
					publicFilePath=listingObjectController.getListingById(listingId).getImageGallery().get(galleryIndex);
					listingObjectController.deleteGalleryImage(listingId, galleryIndex);
					return publicFilePath;
				}
				
			});
			deleteAnyImage(getLocalPathFromPublicPath(publicFilePath));
		} catch (WrongFormatException e) {
			System.out.println("A WrongFormatException in the deleteImageInGallery method appeared. Normally this does not happen.");
		} catch (MainImageNotSupportedException e) {
			System.out.println("A (MainImageNotSupportedException in the deleteImageInGallery method appeared. Normally this does not happen.");
		}
	}

	private String makeUnredundantTemporaryFileName(String localPath, String originalFilename) throws IOException {
		String fileName=makePotentiallyRedundantLocalTemporaryFilePath(originalFilename);
		while(Files.exists(Paths.get(localPath+fileName))){
			fileName=makePotentiallyRedundantLocalTemporaryFilePath(originalFilename);
		}
		return fileName;
	}

	private String makePotentiallyRedundantLocalTemporaryFilePath(String originalFilename) throws IOException {
		return (long) Math.random()*Long.MAX_VALUE + getImageFileTypeEnding(originalFilename);
	}

	/** This method creates the local filepath for file with the type of the originalFilename belonging to the 
	 *  listing with id listingId
	 *  
	 *  @param listingId: the id of the listing the file belongs to
	 *  @param imageData: the imageData represented in a byte-Array
	 *  @param originalFilename: the filename of the original file. The method can get the filetype (.png, jpeg or .jpg) from this String
	 *  
	 * @return local pathname
	 * 
	 * @throws IOException if the originalFilename hints, that the file is not an image file
	 * @throws ListingNotFoundException If no listing with the id listingId exists
	 * @throws MainImageNotSupportedException if the main image does not support a main-image*/
	private String makeLocalMainImagePath(long listingId, String originalFilename) throws IOException, MainImageNotSupportedException, ListingNotFoundException {
		return "main-image" + getImageFileTypeEnding(originalFilename);
	}
	
	/** This method creates the local directory-path for a file belonging to the listing with id listingId
	 * @param originalFilename: name of the original File
	 * @return local pathname
	 * @throws IOException if the originalFilename hints, that the file is not an image file*/
	private String getImageFileTypeEnding(String originalFilename) throws IOException{
		String ending=originalFilename.substring(originalFilename.length()-4).toLowerCase();
		if(".png".equals(ending)||".jpg".equals(ending)){
			return ending;
		}else{
			ending=originalFilename.substring(originalFilename.length()-5).toLowerCase();
			if(".jpeg".equals(ending)){
				return ending;
			}
			throw new IOException("The given file is neither a .png, .jpeg nor .jpg file.");
		}
	}
	
	private String getLocalPathFromPublicPath(String publicFilePath) {
		return "src/main/webapp/src/main/webapp/" + publicFilePath;
	}

	private String getPublicFilePathFromLocal(String localFilePath) {
		return localFilePath.substring(15);
	}

}