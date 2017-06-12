package BackendServer.Listings.Entities;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.json.JSONArray;
import org.json.JSONObject;

import BackendServer.Exceptions.MainImageNotSupportedException;
import BackendServer.Exceptions.NoImageGallerySupportedException;
import BackendServer.Exceptions.WrongFormatException;
import BackendServer.Listings.Constants;
import BackendServer.Listings.SimpleMethods;

/**This abstract class represents the common properties of all listings*/
@Entity
@Table(name="Listing")
@Inheritance( strategy = InheritanceType.JOINED )
public abstract class Listing {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	private boolean active;
	private Date createDate;
	private long creatorId;
	private String description;
	private Date expiryDate;
	private String location;
	private String title;
	@OneToMany(mappedBy="listing",targetEntity=Comment.class,
	fetch=FetchType.EAGER)
	private List<Comment> comments; 
	private String type;
	private String kind;
	
	public Listing(){
		comments=new LinkedList<Comment>();
	}

	public long getListingId() {
		return this.id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getOwner() {
		return creatorId;
	}

	public void setOwner(long creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate =expiryDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	/**This method returns a json-String of this object*/
	public String toString(){
		return this.toJSON().toString();
	}
	
	/**This method checks whether the expiryDate is in the past*/
	public boolean isExpired(){
		return expiryDate!=null&&expiryDate.before(new Date());
	}
	
	public void setImageOfGallery(String pathName, int galleryIndex) throws NoImageGallerySupportedException{
		this.getImageGallery().set(galleryIndex, pathName);
	}

	public Collection<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public void addComment(Comment comment){
		this.comments.add(comment);
		if(comment.getListing()==null){
			comment.setListing(this);
		}
	}

	/**
	 * @return 0 if the type of this listing has no price, else the price
	 */
	public double getPrice() {
		return 0;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}
	
	/**This method fills the fields of this Object with the data in a JSONObject and the creatorId
	 * @param listingData this JSONObject contains all data for this listing:
	 * description: Description of the new listing
	 * (Not required) expiryDate: You can make the listing expire automaticly on this date 
	 * (Not required) isActive: You can set the active-attribute with this field. If it's not set, it's per default true
	 * by setting it as UNIX timestamp
	 * location: The location for this listing
	 * title: The title of this listing
	 * createDate: The time of the listingCreation as UNIX timestamp
	 * @param creatorId id of the creator
	 * @throws WrongFormatException if the listingData is null or is missing of required fields
	 */
	public void fillFields(JSONObject listingData, long creatorId) throws WrongFormatException{
		if(listingData==null){
			throw new WrongFormatException("No data");
		}
		if(listingData.isNull(Constants.listingDataFieldCreateDate) ||	
			listingData.isNull(Constants.listingDataFieldDescription) || 
			listingData.isNull(Constants.listingDataFieldLocation) || 
			listingData.isNull(Constants.listingDataFieldTitle) || 
			listingData.isNull(Constants.listingDataFieldListingType)){
			throw new WrongFormatException("Missing required field(s)");
		}
		if(!this.getType().equals(listingData.get(Constants.listingDataFieldListingType))){
			throw new WrongFormatException("Wrong type");
		}
		if(listingData.isNull(Constants.listingDataFieldActive)){
			this.setActive(true);
		}else{
			this.setActive(listingData.getBoolean(Constants.listingDataFieldActive));
		}
		this.setCreateDate(new Date(listingData.getLong(Constants.listingDataFieldCreateDate)));
		this.setOwner(creatorId);
		this.setDescription(listingData.getString(Constants.listingDataFieldDescription));
		this.setLocation(listingData.getString(Constants.listingDataFieldLocation));
		this.setTitle(listingData.getString(Constants.listingDataFieldTitle));
		if(!listingData.isNull(Constants.listingDataFieldDeadLine)){
			this.setExpiryDate(new Date((long) listingData.getDouble(Constants.listingDataFieldDeadLine)));
		}else{
			this.setExpiryDate(null);
		}
	}
	
	
	/**This method creates a JSONObject with all fields of this class	
	 * This method does not have access to information about the users so if the JSONObject 
	 * should contain those they must be manually added
	 * @return a JSONObject with all fields of this class
	 */
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put(Constants.listingDataFieldId, this.getListingId());
		json.put(Constants.listingDataFieldActive, this.isActive());
		json.put(Constants.listingDataFieldCreateDate, this.getCreateDate().getTime());
		json.put(Constants.listingDataFieldCreator, this.getOwner());
		json.put(Constants.listingDataFieldDescription, this.getDescription());
		json.put(Constants.listingDataFieldDeadLine, this.getExpiryDate());
		json.put(Constants.listingDataFieldLocation, this.getLocation());
		json.put(Constants.listingDataFieldTitle, this.getTitle());
		json.put(Constants.listingDataFieldListingType, this.getType());
		json.put(Constants.listingDataFieldComments, this.commentsToJSONArray());
		if(this.getExpiryDate()!=null){
			json.put(Constants.listingDataFieldDeadLine, this.getExpiryDate().getTime());			
		}
		return json;
	}
	
	/**This method creates a JSONArray with all comments to this listing as JSONObject.
	 * This method does not have access to information about the users so if the JSONObjects 
	 * should contain those they must be manually added
	 * @return a JSONArray with all comments to this listing as JSONObject
	 */
	private JSONArray commentsToJSONArray() {
		JSONArray commentsAsJSONArray=new JSONArray(comments.toArray());
		Iterator<Comment> commentIterator=comments.iterator();
		for(int index=0;index<comments.size();index++){
			commentsAsJSONArray.put(index, commentIterator.next().toJSON());
		}
		return commentsAsJSONArray;
	}

	/** This method checks whether this listing matches the filter options defined in the parameters
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
	 * @return true, if this listing matches all filter options
	 */
	public boolean matchFilterOptions(String[] location, boolean shallBeActive, int price_min, int price_max, String[] type, String kind) {
		boolean stillAMatch=true;
		if(!(location==null) && 
			!SimpleMethods.parseStringArrayToStringList(location)
			.contains(this.getLocation())){
				stillAMatch=false;
		}
		if(shallBeActive!=this.isActive()){
			stillAMatch=false;
		}
		if(price_min>this.getPrice()){
			stillAMatch=false;
		}
		if(price_max<this.getPrice()){
			stillAMatch=false;
		}
		if(!(type==null) && 
				!SimpleMethods.parseStringArrayToStringList(type)
				.contains(this.getType())){
				stillAMatch=false;
			}
		if(!(kind==null) && 
				!kind.equals(this.getKind())){
				stillAMatch=false;
			}
		return stillAMatch;
	}
	
	/**This method gives the filenames with paths of the imageGallery of this listing if the type has one
	 * @return the filenames with paths of the imageGallery
	 * @throws NoImageGallerySupportedException if the type has no imageGallery
	 */
	public List<String> getImageGallery() throws NoImageGallerySupportedException {
		throw new NoImageGallerySupportedException();
	}

	/** This method checks whether this listing matches the filter options defined in the parameters
	 * @param query: The search-subject which is scanned in the title and description of all listings. 
	 * If a listing doesn't have this string in either of those it is filtered out.
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
	 * @return true, if this listing matches all filter options
	 */
	public boolean matchFilterOptions(String query, String[] location, boolean b, int price_min, int price_max,
			String[] type, String kind) {
		return this.matchFilterOptions(location, b, price_min, price_max, type, kind) 
				&& query!=null
				&& (this.getTitle().contains(query) || this.getDescription().contains(query))
				;
	}
	
	public void setPicture(String picture) throws MainImageNotSupportedException{
		throw new MainImageNotSupportedException();
	}

	public String getPicture() throws MainImageNotSupportedException{
		throw new MainImageNotSupportedException();
	}
}

