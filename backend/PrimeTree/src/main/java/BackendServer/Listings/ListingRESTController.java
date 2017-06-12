package BackendServer.Listings;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import BackendServer.Exceptions.CommentNotFoundException;
import BackendServer.Exceptions.GalleryIndexOutOfLimitException;
import BackendServer.Exceptions.ListingNotFoundException;
import BackendServer.Exceptions.MainImageNotSupportedException;
import BackendServer.Exceptions.NoImageGallerySupportedException;
import BackendServer.Exceptions.PathNotTemporaryException;
import BackendServer.Exceptions.UserNotFoundException;
import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Entities.Listing;
import BackendServer.User.Service.UserManager;

/**This class defines all REST-methods beginning with /listing or /listings.
 * It uses a persistenceAdapter in every method to create, read, edit and delete listings and to save images.
 * It also uses a userManager to get the userId by his/her username and get the username by the userId.
 * 
 * @author Florian Kutz
 *
 */
@Controller
@RequestMapping(value = "")
public class ListingRESTController {
	
	//The persistenceAdapter is used in every method to create, read, edit and delete listings and to save images.
	@Autowired
	PersistenceAdapter persistenceAdapter;
	
	// The userManager is used to get the userId by his/her username and get the username by the userId.
	@Autowired
	UserManager userManager;
	
	/**This method creates a new listing. It is allowed for anyone who's logged in.
	 * @param body: This parsed JSONObject must have all datas required for the new listing-object.
	 * Those are:
	 * If type=SaleOffer or type=ServiceOffer: condition: Condition of the item
	 * In all cases: description: Description of the new listing
	 * In all cases: (Not required) expiryDate: You can make the listing expire automaticly on this date 
	 * by setting it as UNIX timestamp
	 * In all cases: location: The location for this listing
	 * If type=SaleOffer: price: The requested price for this item
	 * In all cases: title: The title of this listing
	 * In all cases: type: The listingtype
	 * In all cases: createDate: The time of the listingCreation as UNIX timestamp
	 * If type=RideShareOffer: fromLocation: The startLocation of the offered ride
	 * If type=RideShareOffer: journeyStops: All stops on the offered ride
	 * If type=RideShareOffer: toLocation: The destination of the offered ride
	 * If type=RideShareOffer: availableSeats: The number of available seats in the car
	 * If type=RideShareOffer: travelDateAndTime: The date and time of the start of the ride as UNIX timestamp
 	 * @param request
	 * @param response: The status stays at 200 if everything went ok and 400 if body is missing of required fields.
	 * If the user is not logged in the status is 403, but that's an issue for spring security, not this method
	 * 
	 * @return a stringified JSONObject with the new listing in the field with the name defined in
	 * Constants.responseFieldNewListingId
	 */
	@CrossOrigin
	@RequestMapping(value = "listing", method=RequestMethod.POST)
    public @ResponseBody String createListing(@RequestBody String body, HttpServletRequest request, HttpServletResponse response){
		System.out.println("create-Aufruf von " + getUsername());
		JSONObject newListingData = new JSONObject(body);
		System.out.println(newListingData);
		JSONObject result=new JSONObject();
		try {
			int newId = persistenceAdapter.persistNewListing(newListingData, getUserId());
			result.put(Constants.responseFieldNewListingId, newId);
			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch (WrongFormatException thrownException) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return result.toString();
    }

	/**This method returns the required listing as a stringified JSONObject. 
	 * It's allowed for anyone with read access to this listing
	 * @param listingId The id of the required listing
	 * @param request
	 * @param response The status stays at 200 if everything went ok and 404 if there's no listing with the required id.
	 * If the user is not logged in or has no access to this listing the status is 403.
	 * 
	 * @return a stringified JSONObject representing the listingData. In detail:
	 * type=SaleOffer or type=ServiceOffer: condition: Condition of the item
	 * In all cases: description: Description of the new listing
	 * In all cases: (Not required) expiryDate: You can make the listing expire automaticly on this date 
	 * by setting it as UNIX timestamp
	 * In all cases: location: The location for this listing
	 * If type=SaleOffer: price: The requested price for this item
	 * In all cases: title: The title of this listing
	 * In all cases: type: The listingtype
	 * In all cases: createDate: The time of the listingCreation as UNIX timestamp
	 * If type=RideShareOffer: fromLocation: The startLocation of the offered ride
	 * If type=RideShareOffer: journeyStops: All stops on the offered ride
	 * If type=RideShareOffer: toLocation: The destination of the offered ride
	 * If type=RideShareOffer: availableSeats: The number of available seats in the car
	 * If type=RideShareOffer: travelDateAndTime: The date and time of the start of the ride as UNIX timestamp
	 * In all cases: isActive: A boolean showing whether this listing is active
	 * creator: The username of the creator
	 * id: The id of this listing
	 * If the type supports an imageGallery: imageGallery: The paths to all gallery-images
	 * mainImage: The path to the main-Image resource
	 * In all cases: comments: an array of comment objects
	 * 
	 * Every comment: 
	 * commentID: the comment id
	 * userID: userid from the employee who wrote this comment
	 * createDate:	the unix time when this comment was written
	 * message:	the comment text
	 * userImage: path to user image
	 */
	@CrossOrigin
	@RequestMapping(value = "listing/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasPermission(#id, 'listing', 'reader') or hasAuthority('ADMIN')")
	public @ResponseBody String getListing(@PathVariable(value="id") int listingId, HttpServletRequest request, HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			Listing foundListing=persistenceAdapter.getListingById(listingId);
			result=foundListing.toJSON();
			this.addUserImagePropertyIntoAllComments(new JSONArray().put(result));
			response.setStatus(HttpServletResponse.SC_OK);
			return result.toString();
		} catch (ListingNotFoundException thrownException) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return result.toString();
		}
	}
	
	/**This methods edits an existing listing. This method cannot be used to add or 
	 * delete images from a listing or to add or delete comments
	 * @param body: all new data for the new listing
	 * @param listingId the id of the edited listing
	 * @param request
	 * @param response The status stays at 200 if everything went ok, 400 if required fields in body are missing,
	 *  404 if the listing didn't even exist and 403 if the user is not logged in or is not allowed to edit 
	 *  this resource.
	 */
	@CrossOrigin
	@RequestMapping(value = "listing/{id}", method = RequestMethod.POST)
	@PreAuthorize("hasPermission(#id, 'listing', 'owner') or hasAuthority('ADMIN')")
	public @ResponseBody void editListing(@RequestBody final String body, @PathVariable(value="id") final int listingId, HttpServletRequest request, HttpServletResponse response){
		try {
			persistenceAdapter.edit(listingId, new JSONObject(body));
			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch (ListingNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (WrongFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	/**This method deletes a listing completely
	 * @param listingId The id of the listing that should be deleted
	 * @param request
	 * @param response The status stays at 200 if everything went ok, 404 if the listing didn't even exist and 403
	 * if the user is not logged in or is not allowed to delete this resource.
	 */
	@CrossOrigin
	@RequestMapping(value = "listing/delete/{id}", method=RequestMethod.DELETE)
	@PreAuthorize("hasPermission(#id, 'listing', 'owner') or hasAuthority('ADMIN')")
    public @ResponseBody void delete(@PathVariable(value="id") final int listingId, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("delete() aufgerufen");
		try {
			persistenceAdapter.deleteListingById(listingId);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (ListingNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
    }
	
	/** This method activates a listing
	 * @param listingId the id of the listing
	 * @param request
	 * @param response The status stays at 200 if everything went ok, 404 if the listing didn't even exist and 403
	 * if the user is not logged in or is not allowed to activate this resource.
	 */
	@CrossOrigin
	@RequestMapping(value = "listing/{id}/activate", method=RequestMethod.POST)
	@PreAuthorize("hasPermission(#id, 'listing', 'owner') or hasAuthority('ADMIN')")
    public @ResponseBody void activateListing(@PathVariable(value="id") final int listingId, HttpServletRequest request, HttpServletResponse response){
		try {
			persistenceAdapter.edit(listingId, persistenceAdapter.getListingById(listingId).toJSON().put(Constants.listingDataFieldActive, true));
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (ListingNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (WrongFormatException e) {
			System.out.println("Uncommon error while activating a listing.");
		}
	}
	
	/** This method deactivates a listing
	 * @param listingId the id of the listing
	 * @param request
	 * @param response The status stays at 200 if everything went ok, 404 if the listing didn't even exist and 403
	 * if the user is not logged in or is not allowed to deactivate this resource.
	 */
	@CrossOrigin
	@RequestMapping(value = "listing/{id}/deactivate", method=RequestMethod.POST)
	@PreAuthorize("hasPermission(#id,'listing', 'owner') or hasAuthority('ADMIN')")
    public @ResponseBody void deactivateListing(@PathVariable(value="id") final int listingId, HttpServletRequest request, HttpServletResponse response){
		try {
			persistenceAdapter.edit(listingId, persistenceAdapter.getListingById(listingId).toJSON().put(Constants.listingDataFieldActive, false));
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (ListingNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (WrongFormatException e) {
			System.out.println("Uncommon error while deactivating a listing.");
		}
	}
	
	/**With this method you can post a comment to a listing
	 * @param listingId The id of the commented listing
	 * @param body A stringified JSONObject containing all data for the comment:
		createDate:	the unix time when this comment was written
		message:	the comment text
	 * @param request
	 * @param response The status stays at 200 if everything went ok, 404 if the listing doesn't exist, 400 if the 
	 * comment is missing of arguments and 403 if the user is not logged in or is not allowed to comment this 
	 * resource.
	 */
	@CrossOrigin
	@RequestMapping(value = "listing/{id}/comment", method = RequestMethod.POST)
	@PreAuthorize("hasPermission(#id, 'listing', 'reader') or hasAuthority('ADMIN')")
	public @ResponseBody void postComment(@PathVariable(value="id") int listingId, @RequestBody String body, HttpServletRequest request, HttpServletResponse response){
		try {
			persistenceAdapter.comment(new JSONObject(body), getUserId(), listingId);
			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch (ListingNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	/**This method deletes a comment
	 * @param commentId The id of the comment that should be deleted
	 * @param listingId The id of the commented listing
	 * @param request
	 * @param response The status stays at 200 if everything went ok, 404 if the listing or the comment didn't 
	 * even exist and 403 if the user is not logged in or is not allowed to delete this comment.
	 */
	@CrossOrigin
	@RequestMapping(value = "listing/{listingId}/comment/{commentId}", method = RequestMethod.DELETE)
	@PreAuthorize("hasPermission(#commentId, 'comment', 'author') or hasAuthority('ADMIN')")
	public @ResponseBody void deleteComment(@PathVariable(value="listingId") int commentId, @PathVariable(value="commentId") int listingId, HttpServletRequest request, HttpServletResponse response){
		try {
			persistenceAdapter.deleteComment(commentId);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (CommentNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	/**This method uploads a temporary image and returns the public path so the frontend can edit it
	 * @param request
	 * @param response The status stays at 200 if everything went ok, 415 if the file isn't a valid image file and 401 if the user is not logged in.
	 * @param file the file that should be uploaded
	 */
	@CrossOrigin
	@RequestMapping(value= "listing/upload/temporary", method=RequestMethod.PUT)
	public @ResponseBody String uploadTemporaryImage(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") final MultipartFile file){
		JSONObject result=new JSONObject();
		try {
			if(file==null){
				throw new IOException();
			}
			String publicPath=persistenceAdapter.uploadTemporaryImage(file.getBytes(), file.getOriginalFilename());
			result.put("imagePath", publicPath);
			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
		}
		return result.toString();
	}
	
	/**This method deletes a temporary image
	 * @param request
	 * @param response
	 * 201:
          description: image was created successfully
        401:
          description: user must be authenticated
        403:
          The path does not show to a temporary image
	 * @param imagePath the public path of the image that should be deleted
	 */
	@CrossOrigin
	@RequestMapping(value= "listing/upload/temporary", method=RequestMethod.DELETE)
	public @ResponseBody void deleteTemporaryImage(HttpServletRequest request, HttpServletResponse response, @RequestParam("imagePath") String imagePath){
		try {
			persistenceAdapter.deleteTemporaryImage(imagePath);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (PathNotTemporaryException e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
	}
	
	/**This method uploads an image for this listing and sets the picture-column in the listing to the public 
	 * resource path of the uploaded image.
	 * @param listingId The id of the listing
	 * @param request
	 * @param response The status stays at 200 if everything went ok, 404 if the listing doesn't exist, 400 if the 
	 * file isn't a valid image file and 403 if the user is not logged in or is not allowed to upload an image 
	 * for this resource.
	 * @param file The image-file; Must be .png, .jpg or .jpeg
	 */
	@CrossOrigin
	@RequestMapping(value= "listing/upload/main-image/{id}", method=RequestMethod.PUT)
	@PreAuthorize("hasPermission(#id, 'listing', 'owner') or hasAuthority('ADMIN')")
	public @ResponseBody void listingMainImageUpload(@PathVariable(value="id") final int listingId, 
			HttpServletRequest request, HttpServletResponse response, @RequestParam("file") final MultipartFile file){
				try {
					if(file==null){
						throw new IOException();
					}
					persistenceAdapter.uploadMainImage(file.getBytes(), listingId, file.getOriginalFilename());
					response.setStatus(HttpServletResponse.SC_CREATED);
				} catch (IOException e) {
					response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
				} catch (ListingNotFoundException e) {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				} catch (MainImageNotSupportedException e) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
	}
	
	/**This method replaces one image in the gallery with a new one and updates the filepath.
	 * @param listingId The id of the listing
	 * @param galleryIndex The index of the replaced image
	 * @param request
	 * @param response The status stays at 200 if everything went ok, 404 if the listing  or the image with the index 
	 * doesn't exist, 400 if the file isn't a valid image file or the type of the listing doesn't support the 
	 * imageGallery and 403 if the user is not logged in or is not allowed to upload an image for this resource.
	 * @param file  The new image-file; Must be .png, .jpg or .jpeg
	 */
	@CrossOrigin
	@RequestMapping(value= "listing/upload/gallery/{listingId}/{galleryIndex}", method=RequestMethod.PUT)
	@PreAuthorize("hasPermission(#id, 'listing', 'owner') or hasAuthority('ROLE_ADMIN')")
	public @ResponseBody void galleryImageUpload(@PathVariable(value="listingId") final int listingId, @PathVariable(value="galleryIndex") final int galleryIndex, 
	HttpServletRequest request, HttpServletResponse response, @RequestParam("file") final MultipartFile file){
		try {
			if(file==null){
				throw new IOException();
			}
			persistenceAdapter.putImageInGallery(file.getBytes(), listingId, galleryIndex, file.getOriginalFilename());
			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
		} catch (ListingNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (NoImageGallerySupportedException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (GalleryIndexOutOfLimitException e) {
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		}
	}
	
	/**This method deletes an Image from the gallery of a listing
	 * @param listingId The id of the listing
	 * @param galleryIndex The index of the deleted Imgage in the image Gallery
	 * @param request
	 * @param response The status stays at 200 if everything went ok, 404 if the listing  or the image with the index 
	 * doesn't exist, 400 if the listing doesn't support imageGallery and 403 if the user is not logged in or 
	 * is not allowed to delete an image from this resource.
	 */
	@CrossOrigin
	@RequestMapping(value= "listing/upload/gallery/{listingId}", method=RequestMethod.DELETE)
	@PreAuthorize("hasPermission(#id, 'listing', 'owner') or hasAuthority('ADMIN')")
	public @ResponseBody void listingGalleryDelete(@PathVariable(value="listingId") final int listingId,
	HttpServletRequest request, HttpServletResponse response){
		try {
			try{persistenceAdapter.deleteImageInGallery(listingId, 0);}catch(IndexOutOfBoundsException e){/*Do nothing*/}
			try{persistenceAdapter.deleteImageInGallery(listingId, 1);}catch(IndexOutOfBoundsException e){/*Do nothing*/}
			try{persistenceAdapter.deleteImageInGallery(listingId, 2);}catch(IndexOutOfBoundsException e){/*Do nothing*/}
			try{persistenceAdapter.deleteImageInGallery(listingId, 3);}catch(IndexOutOfBoundsException e){/*Do nothing*/}
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (ListingNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (NoImageGallerySupportedException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	/**This method implements a filtered search request over all ative listings by returning a page as well as 
	 * some statistics of the search-results
	 * @param query: The search-subject which is scanned in the title and description of all listings. 
	 * If a listing doesn't have this string in either of those it is filtered out.
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
	 * @param request 
	 * @param response The status stays at 200 if everything went ok and 401 if the user isn't logged in.
	 * @return A stringified JSONObject with all resultData:
	 * 
		price_min: minimum found  price of all listings that math the filter and search query
		price_max: maximal found price of all listings that math the filter and search query
		count: amount of all listings from all pages
		pages: amount of pages
	 *  listings: A JSONArray with all listings from this page. Every listing is sent like in the return 
	 *  string from getListing(...)
	 */
	@CrossOrigin
	@RequestMapping(value= "listings/search", method=RequestMethod.GET)
	public @ResponseBody String getListingsBySearch(@RequestParam("query")String query,@RequestParam("page") Optional<Integer> pageOptional, @RequestParam("location") Optional<String[]> locationOptional, @RequestParam("price_min") Optional<Integer> price_minOptional, @RequestParam("price_max") Optional<Integer> price_maxOptional, @RequestParam("type") Optional<String[]> typeOptional, @RequestParam("kind") Optional<String> kindOptional, @RequestParam("sort") Optional<String> sortOptional, HttpServletRequest request, HttpServletResponse response){
		if(query==null || query.length()<2){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "";
			}
		int page=pageOptional.orElse(1);
		int price_min=price_minOptional.orElse(Integer.MIN_VALUE);
		int price_max=price_maxOptional.orElse(Integer.MAX_VALUE);
		String[] location=locationOptional.orElse(null);
		String[] type=typeOptional.orElse(null);
		String kind=kindOptional.orElse(null);
		String sort=sortOptional.orElse(Constants.sortOptionId);
		ListingSearchStatistics statistics=new ListingSearchStatistics();
		Listing[] resultListings;
		try{
			resultListings=persistenceAdapter.getListingsBySearch(query, page, location, true, price_min, price_max, type, kind, sort, statistics);
			JSONObject result=this.createPage(resultListings, statistics, page);
			response.setStatus(HttpServletResponse.SC_OK);
			return result.toString();
		}catch(WrongFormatException e){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "";
		}
	}
	
	/**This method returns a page of all listings that match the filter-options including activated and deactivated listings. 
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
	 * @param request 
	 * @param response The status stays at 200 if everything went ok and 401 if the user isn't logged in or 
	 * not an admin.
	 * @return A stringified JSONObject with all resultData:
	 * 
		price_min: minimum found  price of all listings that math the filter and search query
		price_max: maximal found price of all listings that math the filter and search query
		count: amount of all listings from all pages
		pages: amount of pages
	 *  listings: A JSONArray with all listings from this page. Every listing is sent like in the return 
	 *  string from getListing(...)
	 */
	@CrossOrigin
	@RequestMapping(value = "/listings", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('ADMIN')")
	public @ResponseBody String getAllListings(@RequestParam("page") Optional<Integer> pageOptional, @RequestParam("location") Optional<String[]> locationOptional, @RequestParam("price_min") Optional<Integer> price_minOptional, @RequestParam("price_max") Optional<Integer> price_maxOptional, @RequestParam("type") Optional<String[]> typeOptional, @RequestParam("kind") Optional<String> kindOptional, @RequestParam("sort") Optional<String> sortOptional, HttpServletRequest request, HttpServletResponse response){
		try{
			ListingSearchStatistics statistics=new ListingSearchStatistics();
			int page=pageOptional.orElse(1);
			int price_min=price_minOptional.orElse(Integer.MIN_VALUE);
			int price_max=price_maxOptional.orElse(Integer.MAX_VALUE);
			String[] location=locationOptional.orElse(null);
			String[] type=typeOptional.orElse(null);
			String kind=kindOptional.orElse(null);
			String sort=sortOptional.orElse(Constants.sortOptionId);
			Listing[] resultListings;
			resultListings=persistenceAdapter.getListingsFiltered(page, location, price_min, price_max, type, kind, sort, statistics);
			JSONObject result=this.createPage(resultListings, statistics, page);
			response.setStatus(HttpServletResponse.SC_OK);
			return result.toString();
		}catch(WrongFormatException e){
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return "";
		}
	}
	
	/**This method returns a page of all active listings that match the filter-options. 
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
	 * @param request 
	 * @param response The status stays at 200 if everything went ok and 401 if the user isn't logged in.
	 * @return A stringified JSONObject with all resultData:
	 * 
		price_min: minimum found  price of all listings that math the filter and search query
		price_max: maximal found price of all listings that math the filter and search query
		count: amount of all listings from all pages
		pages: amount of pages
	 *  listings: A JSONArray with all listings from this page. Every listing is sent like in the return 
	 *  string from getListing(...)
	 */
	@CrossOrigin
	@RequestMapping(value = "listings/active", method = RequestMethod.GET)
	public @ResponseBody String getActiveListings(@RequestParam("page") Optional<Integer> pageOptional, @RequestParam("location") Optional<String[]> locationOptional, @RequestParam("price_min") Optional<Integer> price_minOptional, @RequestParam("price_max") Optional<Integer> price_maxOptional, @RequestParam("type") Optional<String[]> typeOptional, @RequestParam("kind") Optional<String> kindOptional, @RequestParam("sort") Optional<String> sortOptional, HttpServletRequest request, HttpServletResponse response){
		try{
			ListingSearchStatistics statistics=new ListingSearchStatistics();
			Listing[] resultListings;
			int page=pageOptional.orElse(1);
			int price_min=price_minOptional.orElse(Integer.MIN_VALUE);
			int price_max=price_maxOptional.orElse(Integer.MAX_VALUE);
			String[] location=locationOptional.orElse(null);
			String[] type=typeOptional.orElse(null);
			String kind=kindOptional.orElse(null);
			String sort=sortOptional.orElse(Constants.sortOptionId);
			resultListings=persistenceAdapter.getListingsFiltered(page, location, true, price_min, price_max, type, kind, sort, statistics);
			JSONObject result=this.createPage(resultListings, statistics, page);
			response.setStatus(HttpServletResponse.SC_OK);
			return result.toString();
		}catch(WrongFormatException e){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "";
		}
	}
	
	/**This method returns a page of all inactive listings that match the filter-options. 
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
	 * @param request 
	 * @param response The status stays at 200 if everything went ok and 401 if the user isn't logged in or the 
	 * user is not an admin.
	 * @return A stringified JSONObject with all resultData:
	 * 
		price_min: minimum found  price of all listings that math the filter and search query
		price_max: maximal found price of all listings that math the filter and search query
		count: amount of all listings from all pages
		pages: amount of pages
	 *  listings: A JSONArray with all listings from this page. Every listing is sent like in the return 
	 *  string from getListing(...)
	 */
	@CrossOrigin
	@RequestMapping(value = "listings/inactive", method = RequestMethod.GET)
	public @ResponseBody String getInactiveListings(@RequestParam("page") Optional<Integer> pageOptional, @RequestParam("location") Optional<String[]> locationOptional, @RequestParam("price_min") Optional<Integer> price_minOptional, @RequestParam("price_max") Optional<Integer> price_maxOptional, @RequestParam("type") Optional<String[]> typeOptional, @RequestParam("kind") Optional<String> kindOptional, @RequestParam("sort") Optional<String> sortOptional, HttpServletRequest request, HttpServletResponse response){
		try{
			ListingSearchStatistics statistics=new ListingSearchStatistics();
			Listing[] resultListings;
			int page=pageOptional.orElse(1);
			int price_min=price_minOptional.orElse(Integer.MIN_VALUE);
			int price_max=price_maxOptional.orElse(Integer.MAX_VALUE);
			String[] location=locationOptional.orElse(null);
			String[] type=typeOptional.orElse(null);
			String kind=kindOptional.orElse(null);
			String sort=sortOptional.orElse(Constants.sortOptionId);
			resultListings=persistenceAdapter.getListingsFiltered(page, location, false, price_min, price_max, type, kind, sort, statistics);
			JSONObject result=this.createPage(resultListings, statistics, page);
			response.setStatus(HttpServletResponse.SC_OK);
			return result.toString();
		}catch(WrongFormatException e){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "";
		}
	}
	
	/** This method allows users to see all own listings
	 * @param request
	 * @param response Status stays at 200 if everything went well and 401 if the user is not logged in
	 * @return A stringified JSONObject with an array called listings of all listings which are 
	 * shown as in the return value of GET /listing/{id}
	 */
	@CrossOrigin
	@RequestMapping(value = "listings/own", method = RequestMethod.GET)
	public @ResponseBody String getOwnListings(HttpServletRequest request, HttpServletResponse response){
		Listing[] resultListings=persistenceAdapter.getListingsFromUser(getUserId());
		JSONObject result=new JSONObject();
		result.put(Constants.listingSearchResultFieldListings, new JSONArray(resultListings));
		this.addUserImagePropertyIntoAllComments(result.getJSONArray("listings"));
		response.setStatus(HttpServletResponse.SC_OK);
		return result.toString();
	}
	
//	/** A old method that allows to get multiple listings by id at once.
//	 * @param listingIds An array with all ids of the required listings
//	 * @param request
//	 * @param response The status stays at 200 if everything went well, 404 if at least one of the listings does not exist, 
//	 * 401 if the user does not have access to at least one of these listings.
//	 * @return
//	 */
//	@CrossOrigin
//	@RequestMapping(value = "listings/getmultiple", method = RequestMethod.GET)
//	public @ResponseBody String getListingArrayByIdArray(@RequestParam int[] listingIds, HttpServletRequest request,HttpServletResponse response){
//		System.out.println("getmultiple- Aufruf");
//		JSONObject result=new JSONObject();
//		try {
//			result.put(Constants.listingSearchResultFieldListings, persistenceAdapter.getListingArrayByIdArray(listingIds));
//		} catch (ListingNotFoundException thrownException) {
//			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//		}
//		return result.toString();
//	}
	
	/**This method creates a JSONObject showing a page
	 * @param resultListings the listings shown in the page
	 * @param statistics the search result statistics
	 * @return a JSONObject representing the page
	 */
	private JSONObject createPage(Listing[] resultListings, ListingSearchStatistics statistics, int pageNumber) {
		JSONObject page=new JSONObject();
		JSONArray listingArray=new JSONArray();
		for(int jsonArrayIndex=0;jsonArrayIndex<resultListings.length;jsonArrayIndex++){
			listingArray.put(jsonArrayIndex, resultListings[jsonArrayIndex].toJSON());
		}
		this.addUserImagePropertyIntoAllComments(listingArray);
		page.put(Constants.listingSearchResultFieldListings, listingArray);
		page.put(Constants.listingSearchResultFieldPrice_Min, statistics.getPrice_min());
		page.put(Constants.listingSearchResultFieldPrice_Max, statistics.getPrice_max());
		page.put(Constants.listingSearchResultFieldCount, statistics.getCount());
		page.put(Constants.listingSearchResultFieldPages, statistics.getPages());
		page.put(Constants.listingSearchResultFieldpageNumber, pageNumber);
		return page;
	}

	/**This method adds the UserImageProperty into all Comments in the listings in listingArray
	 * @param listingArray An array with all listings as JSONObjects whose comments should get the UserImage
	 */
	private void addUserImagePropertyIntoAllComments(JSONArray listingArray) {
		for(int listingArrayIndex=0;listingArrayIndex<listingArray.length();listingArrayIndex++){
			JSONArray commentsArray=listingArray.getJSONObject(listingArrayIndex).getJSONArray(Constants.listingDataFieldComments);
			for(int commentsArrayIndex=0;commentsArrayIndex<commentsArray.length();commentsArrayIndex++){
				JSONObject editedComment=commentsArray.getJSONObject(commentsArrayIndex);
					try {
						editedComment.put(Constants.commentDataFieldUserImage, 
								this.userManager.loadUserById(editedComment.getInt(Constants.commentDataFieldUserId)).getPicture());
					} catch (UserNotFoundException e) {
						System.out.println("The user with id " + editedComment.getInt(Constants.commentDataFieldUserId) + " doesn' exist anymore");
					}
			}
		}
		
	}
	
	private String getUsername(){
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	private long getUserId(){
		return userManager.loadUserByUsername(getUsername()).getId();
	}
}
