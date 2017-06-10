package BackendServer.Exceptions;

/**This Exception is thrown if a invalid Value is set.
 * 
 * @author Mark Beckmann
 * */
public class InvalidValueException extends Exception{
	public InvalidValueException(String string){
		super(string);
	}

}
