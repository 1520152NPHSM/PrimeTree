package backendServer.exceptions;

import org.json.JSONException;

/**This Exception is thrown if a JSONObject is missing of required attributes.
 * 
 * @author Florian Kutz
 * */
@SuppressWarnings("serial")
public class WrongFormatException extends JSONException {

	public WrongFormatException(String string) {
		super(string);
	}

}
