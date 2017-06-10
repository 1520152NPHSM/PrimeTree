package BackendServer.Listings;

/**This non-persistent class is a container for search-statistics
 * @author Florian Kutz
 *
 */
public class ListingSearchStatistics {
	
	private double price_min;
	private double price_max;
	private int count;
	private int pages;
	
	public double getPrice_min() {
		return price_min;
	}
	public void setPrice_min(double lowestPriceFound) {
		this.price_min = lowestPriceFound;
	}
	public double getPrice_max() {
		return price_max;
	}
	public void setPrice_max(double highestPriceFound) {
		this.price_max = highestPriceFound;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}

}
