package BackendServer.Listings.Entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.json.JSONObject;

import BackendServer.Listings.Constants;

/**This class represents the entity Comment
 * @author Florian Kutz
 *
 */
@Entity
public class Comment {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	
	private long authorId;
	
	private Date createDate;
	
	private String message;
	
	@ManyToOne(optional=false)
    @JoinColumn(name="ListingId",referencedColumnName="id")
	private Listing listing;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getText() {
		return message;
	}

	public void setText(String text) {
		this.message = text;
	}

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}

	/**This method creates a JSONObject with all fields in this Object. 
	 * The names of the fields are defined in Constants. Because this 
	 * class has no access to userData those must be added from outside if required.
	 * @return The JSONObject representing this
	 */
	public JSONObject toJSON() {
		JSONObject commentAsJSON=new JSONObject();
		commentAsJSON.put(Constants.commentDataFieldCommentId, this.getId());
		commentAsJSON.put(Constants.commentDataFieldUserId, this.getAuthorId());
		commentAsJSON.put(Constants.commentDataFieldDate, this.getCreateDate());
		commentAsJSON.put(Constants.commentDataFieldMessage, this.getText());
		return commentAsJSON;
	}

}
