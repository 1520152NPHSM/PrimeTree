package backendServer.listings;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;

import backendServer.listings.entities.Listing;
import backendServer.userData.entities.UserData;

/**This class has some simple methods, that allow parsing arrays.
 * 
 * @author Florian Kutz
 *
 */
public class SimpleMethods {
	
	/**This method parses an array of integer to an array of int
	 * @param integerArray The integerArray
	 * @return integerArray parsed to intArray
	 */
	public static int[] parseIntegerArrayToIntArray(Integer[] integerArray) {
		int[] intArray=new int[integerArray.length];
		for(int index=0;index<integerArray.length;index++){
			intArray[index]=integerArray[index];
		}
		return intArray;
	}

	/**This method parses an array of Objects to an array of Listing
	 * @param objectArray The array of Objects
	 * @return objectArray parsed to an array of Listings
	 */
	public static Listing[] parseObjectArrayToListingArray(Object[] objectArray) {
		Listing[] listingArray=new Listing[objectArray.length];
		for(int index=0;index<objectArray.length;index++){
			listingArray[index]=(Listing)objectArray[index];
		}
		return listingArray;
	}

	/**This method parses a JSONArray with Strings to a StingList
	 * @param jsonArray the unparsed JSONArray
	 * @return the parsed List of Strings
	 */
	public static List<String> parseJSONArrayToStringList(JSONArray jsonArray) {
		LinkedList<String> stringCollection=new LinkedList<String>();
		for(int index=0;index<jsonArray.length();index++){
			stringCollection.add(jsonArray.getString(index));
		}
		return stringCollection;
	}

	/**This method creates an array of all ids of the listings in a listingCollection
	 * @param listingCollection the collection of listing
	 * @return array of ids
	 */
	public static int[] parseListingCollectionToIntArrayOfIds(Collection<? extends Listing> listingCollection) {
		Iterator<? extends Listing>listingIterator=listingCollection.iterator();
		LinkedList<Integer> resultList=new LinkedList<Integer>();
		while(listingIterator.hasNext()){
			resultList.add((int) listingIterator.next().getListingId());
		}
		Integer[] resultArray=new Integer[resultList.size()];
		resultList.toArray(resultArray);
		return SimpleMethods.parseIntegerArrayToIntArray(resultArray);
	}
	
	/**This method creates an array of all ids of the listings in a listingArray
	 * @param listingArray the array of listing
	 * @return array of ids
	 */
	public static int[] parseListingArrayToIntArrayOfIds(Listing[] listingArray) {
		LinkedList<Integer> resultList=new LinkedList<Integer>();
		for(int index=0;index<listingArray.length;index++){
			resultList.add((int) listingArray[index].getListingId());
		}
		Integer[] resultArray=new Integer[resultList.size()];
		resultList.toArray(resultArray);
		return SimpleMethods.parseIntegerArrayToIntArray(resultArray);
	}

	/**This method parses an array of Strings to a List<String>
	 * @param stringArray The unparsed array of Strins
	 * @return The parsed List<String>
	 */
	public static Collection<String> parseStringArrayToStringList(String[] stringArray) {
		LinkedList<String> stringCollection=new LinkedList<String>();
		for(int index=0;index<stringArray.length;index++){
			stringCollection.add(stringArray[index]);
		}
		return stringCollection;
	}

	/**This method parses a String representing a number to an int
	 * @param substring the String
	 * @return the int
	 */
	public static int parseStringToInt(String substring) {
		int resultInt=0;
		for(int index=substring.length()-1;index>=0;index--){
			resultInt*=10;
			resultInt+=substring.charAt(index);
			resultInt-='0';
		}
		return resultInt;
	}

	/**This method parses an array of Object to an array of UserData
	 * @param objectArray the unparsed array of objects
	 * @return the parsed array of UserData
	 */
	public static UserData[] parseObjectArrayToUserDataArray(Object[] objectArray) {
		UserData[] listingArray=new UserData[objectArray.length];
		for(int index=0;index<objectArray.length;index++){
			listingArray[index]=(UserData)objectArray[index];
		}
		return listingArray;
	}

}
